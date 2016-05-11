package app.androidprog.com.tutomusicplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

import app.androidprog.com.tutomusicplayer.Adapter.SongAdapter;
import app.androidprog.com.tutomusicplayer.Model.Song;
import app.androidprog.com.tutomusicplayer.Request.SoundcloudApiRequest;
import app.androidprog.com.tutomusicplayer.Utility.Utility;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "APP";
    private RecyclerView recycler;
    private SongAdapter mAdapter;
    private ArrayList<Song> songList;
    private int currentIndex;
    private TextView tb_title, tb_duration;
    private ImageView iv_play, iv_next, iv_previous;
    private ProgressBar pb_loader, pb_main_loader;
    private MediaPlayer mediaPlayer;
    private long currentSongLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialisation des vues
        initializeViews();
        //Requête récupérant les chansons
        getSongList();

        songList = new ArrayList<>();

        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new SongAdapter(getApplicationContext(), songList, new SongAdapter.RecyclerItemClickListener() {
            @Override
            public void onClickListener(Song song, int position) {
                Toast.makeText(MainActivity.this, song.getTitle(), Toast.LENGTH_SHORT).show();
                changeSelectedSong(position);
                prepareSong(song);
            }
        });
        recycler.setAdapter(mAdapter);

        //Initialisation du media player
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //Lancer la chanson
                togglePlay(mp);
            }
        });
    }

    private void prepareSong(Song song){

        currentSongLength = song.getDuration();
        pb_loader.setVisibility(View.VISIBLE);
        tb_title.setVisibility(View.GONE);
        iv_play.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.selector_play));
        tb_title.setText(song.getTitle());
        tb_duration.setText(Utility.convertDuration(song.getDuration()));
        String stream = song.getStreamUrl()+"?client_id="+Config.CLIENT_ID;
        mediaPlayer.reset();

        try {
            mediaPlayer.setDataSource(stream);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void togglePlay(MediaPlayer mp){

        if(mp.isPlaying()){
            mp.stop();
            mp.reset();
        }else{
            pb_loader.setVisibility(View.GONE);
            tb_title.setVisibility(View.VISIBLE);
            mp.start();
            iv_play.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.selector_pause));
        }

    }


    private void initializeViews(){

        tb_title = (TextView) findViewById(R.id.tb_title);
        tb_duration = (TextView) findViewById(R.id.tv_time);
        iv_play = (ImageView) findViewById(R.id.iv_play);
        iv_next = (ImageView) findViewById(R.id.iv_next);
        iv_previous = (ImageView) findViewById(R.id.iv_previous);
        pb_loader = (ProgressBar) findViewById(R.id.pb_loader);
        pb_main_loader = (ProgressBar) findViewById(R.id.pb_main_loader);

        recycler = (RecyclerView) findViewById(R.id.recycler);
    }

    public void getSongList(){
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        SoundcloudApiRequest request = new SoundcloudApiRequest(queue);

        request.getSongList(new SoundcloudApiRequest.SoundcloudInterface() {
            @Override
            public void onSuccess(ArrayList<Song> songs) {
                currentIndex = 0;
                songList.addAll(songs);
                mAdapter.notifyDataSetChanged();
                mAdapter.setSelectedPosition(0);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeSelectedSong(int index){
        mAdapter.notifyItemChanged(mAdapter.getSelectedPosition());
        currentIndex = index;
        mAdapter.setSelectedPosition(currentIndex);
        mAdapter.notifyItemChanged(currentIndex);
    }

}

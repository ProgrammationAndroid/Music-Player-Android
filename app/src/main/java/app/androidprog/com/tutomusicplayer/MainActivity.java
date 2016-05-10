package app.androidprog.com.tutomusicplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

import app.androidprog.com.tutomusicplayer.Adapter.SongAdapter;
import app.androidprog.com.tutomusicplayer.Model.Song;
import app.androidprog.com.tutomusicplayer.Request.SoundcloudApiRequest;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "APP";
    private RecyclerView recycler;
    private SongAdapter mAdapter;
    private ArrayList<Song> songList;
    private int currentIndex;

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
            }
        });
        recycler.setAdapter(mAdapter);




    }

    private void initializeViews(){

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

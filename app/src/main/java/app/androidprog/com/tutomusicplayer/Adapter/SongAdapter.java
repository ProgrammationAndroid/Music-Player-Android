package app.androidprog.com.tutomusicplayer.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.androidprog.com.tutomusicplayer.Model.Song;
import app.androidprog.com.tutomusicplayer.R;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private Context context;
    private ArrayList<Song> songList;
    private RecyclerItemClickListener listener;

    public SongAdapter(Context context, ArrayList<Song> songList, RecyclerItemClickListener listener){

        this.context = context;
        this.songList = songList;
        this.listener = listener;

    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_row, parent, false);

        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {

        Song song = songList.get(position);
        if(song != null){

            holder.tv_title.setText(song.getTitle());
            holder.tv_artist.setText(song.getArtist());
            holder.tv_duration.setText(String.valueOf(song.getDuration()));
            Picasso.with(context).load(song.getArtworkUrl()).placeholder(R.drawable.music_placeholder).into(holder.iv_artwork);

            holder.bind(song, listener);

        }

    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_title, tv_artist, tv_duration;
        private ImageView iv_artwork, iv_play_active;

        public SongViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_artist = (TextView) itemView.findViewById(R.id.tv_artist);
            tv_duration = (TextView) itemView.findViewById(R.id.tv_duration);
            iv_artwork = (ImageView) itemView.findViewById(R.id.iv_artwork);
            iv_play_active = (ImageView) itemView.findViewById(R.id.iv_play_active);

        }

        public void bind(final Song song, final RecyclerItemClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickListener(song, getLayoutPosition());
                }
            });
        }

    }

    public interface RecyclerItemClickListener{
        void onClickListener(Song song, int position);
    }

}

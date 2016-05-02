package app.androidprog.com.tutomusicplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

import app.androidprog.com.tutomusicplayer.Model.Song;
import app.androidprog.com.tutomusicplayer.Request.SoundcloudApiRequest;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "APP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSongList();
    }

    public void getSongList(){
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        SoundcloudApiRequest request = new SoundcloudApiRequest(queue);

        request.getSongList(new SoundcloudApiRequest.SoundcloudInterface() {
            @Override
            public void onSuccess(ArrayList<Song> songs) {
                Log.d(TAG, "onSuccess: " + songs);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

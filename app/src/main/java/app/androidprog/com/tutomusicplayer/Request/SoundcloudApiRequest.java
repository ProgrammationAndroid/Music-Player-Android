package app.androidprog.com.tutomusicplayer.Request;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import app.androidprog.com.tutomusicplayer.Config;
import app.androidprog.com.tutomusicplayer.Model.Song;

public class SoundcloudApiRequest {

    public interface SoundcloudInterface{
        void onSuccess(ArrayList<Song> songs);
        void onError(String message);
    }

    private RequestQueue queue;
    private static final String URL = "http://api.soundcloud.com/tracks?filter=public&limit=100&client_id="+ Config.CLIENT_ID;
    private static final String TAG = "APP";
    
    public SoundcloudApiRequest(RequestQueue queue) {
        this.queue = queue;
    }

    public void getSongList(String query, final SoundcloudInterface callback){

        String url = URL;
        if(query.length() > 0){
            try {
                query = URLEncoder.encode(query, "UTF-8");
                url = URL + "&q=" + query;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        Log.d(TAG, "getSongList: " + url);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, "onResponse: " + response);

                ArrayList<Song> songs = new ArrayList<>();
                if(response.length() > 0){
                    for (int i = 0; i < response.length() ; i++) {
                        try {
                            JSONObject songObject = response.getJSONObject(i);
                            long id = songObject.getLong("id");
                            String title = songObject.getString("title");
                            String artworkUrl = songObject.getString("artwork_url");
                            String streamUrl = songObject.getString("stream_url");
                            long duration = songObject.getLong("duration");
                            int playbackCount = songObject.has("playback_count") ? songObject.getInt("playback_count") : 0;
                            JSONObject user = songObject.getJSONObject("user");
                            String artist = user.getString("username");

                            Song song = new Song(id, title, artist, artworkUrl, duration, streamUrl, playbackCount);
                            songs.add(song);

                        } catch (JSONException e) {
                            Log.d(TAG, "onResponse: " + e.getMessage());
                            callback.onError("Une erreur s'est produite");
                            e.printStackTrace();
                        }
                    }

                    callback.onSuccess(songs);

                }else{
                    callback.onError("Aucune chanson trouvée");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onResponse: " + error.getMessage());
                callback.onError("Une erreur s'est produite");
            }
        });

        queue.add(request);

    }
}

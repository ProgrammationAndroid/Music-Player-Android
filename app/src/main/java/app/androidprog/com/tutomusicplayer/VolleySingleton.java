package app.androidprog.com.tutomusicplayer;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {

    private static VolleySingleton mInstance;
    private RequestQueue mRequetQueue;
    private static Context mCtx;

    private VolleySingleton(Context context){
        mCtx = context;
        mRequetQueue = getRequestQueue();
    }

    public static synchronized VolleySingleton getInstance(Context context){
        if(mInstance == null){
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        if(mRequetQueue == null){
            mRequetQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return  mRequetQueue;
    }

}

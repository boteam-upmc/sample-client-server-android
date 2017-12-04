package fr.upmc.boteam.annex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "MainActivity";

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.42.91:3000/");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(LOG_TAG, "Try to connect..");
        mSocket.connect();
        mSocket.emit("isAndroid", true);

        mSocket.on(Socket.EVENT_CONNECT, onConnect);

        //mSocket.emit(request, data);
    }

    public Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(LOG_TAG, "Server is connected.");
        }
    };

    @Override
    protected void onPause() {
        super.onPause();

        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.disconnect();
    }

}

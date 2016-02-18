package com.docdevelopers.hearttracker;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import io.socket.client.IO;
import io.socket.client.Socket;


/**
 * Created by Francisco on 9/21/2015.
 */
public class ListenerService extends WearableListenerService {

    private static final String HELLO_WORLD_WEAR_PATH = "/docdevelopers-hearttracker-wear";
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://40.122.208.13:85");
        } catch (URISyntaxException e) {
            Log.d("SOCKET", e.toString());
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Created","Again?");
        Toast.makeText(ListenerService.this, "SERVICE STARTED", Toast.LENGTH_SHORT).show();
        mSocket.connect();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        /*
         * Receive the message from wear
         */
        if (messageEvent.getPath().equals(HELLO_WORLD_WEAR_PATH)) {
            Log.d("Got", "IT");

            int rate = byteArrayToLeInt(messageEvent.getData());
            Toast.makeText(this,Integer.toString(rate),Toast.LENGTH_LONG).show();
            attemptSendRate(rate);
        }

    }

    public static int byteArrayToLeInt(byte[] b) {
        final ByteBuffer bb = ByteBuffer.wrap(b);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb.getInt();
    }

    private void attemptSendRate(int rate) {
        mSocket.emit("rate", Integer.toString(rate));
    }

}
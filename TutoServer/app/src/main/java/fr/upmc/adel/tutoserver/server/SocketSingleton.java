package fr.upmc.adel.tutoserver.server;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by adel on 08/10/17.
 */

public class SocketSingleton {

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.LOCAL_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }
}

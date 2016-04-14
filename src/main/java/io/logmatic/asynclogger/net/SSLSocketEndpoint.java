package io.logmatic.asynclogger.net;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;


public class SSLSocketEndpoint implements Endpoint {

    private SSLSocket sslSocket;
    private DataOutputStream stream;
    private final String hostname;
    private final Integer port;

    public SSLSocketEndpoint(String hostname, Integer port) {
        this.port = port;
        this.hostname = hostname;
        this.sslSocket = null;

        openConnection();
    }

    public SSLSocketEndpoint(SSLSocket socket) {
        this.port = null;
        this.hostname = null;
        this.sslSocket = socket;

        openConnection();
    }


    @Override
    public boolean send(String data) {

        if (!isConnected()) return false;

        try {
            stream.writeBytes(data + '\n');
            stream.flush();
            return true;

        } catch (IOException e) {
            Log.e(getClass().getName(), "Failed to send data to the endpoint", e);

        }
        return false;
    }

    @Override
    public boolean isConnected() {
        return sslSocket.isConnected();
    }


    @Override
    public void closeConnection() {

        try {
            stream.flush();
            sslSocket.close();
        } catch (IOException e) {
            Log.e(getClass().getName(), "Connection close failed", e);
        } catch (NullPointerException ne) {
            Log.v(getClass().getName(), "Connection close failed, no previous connection have been open");
        }

    }

    @Override
    public boolean openConnection() {


        try {

            if (sslSocket == null) {
                Socket socket = new Socket(hostname, port);
                SSLSocketFactory sslFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
                sslSocket = (SSLSocket) sslFactory.createSocket(socket, hostname, port, true);
            }

            stream = new DataOutputStream(sslSocket.getOutputStream());
            return true;

        } catch (IOException e) {

            e.printStackTrace();
            Log.e(getClass().getName(), "Failed to open socket", e);
        }
        return false;
    }

}

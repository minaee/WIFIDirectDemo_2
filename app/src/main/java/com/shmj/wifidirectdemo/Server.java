package com.shmj.wifidirectdemo;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static com.shmj.wifidirectdemo.Chat.updateMessagesfromClient;
import static com.shmj.wifidirectdemo.Chat.updateMessagesfromServer;


/**
 * Created by Shahriar on 3/21/2018.
 */

public class Server extends Thread {
    InetAddress address;
    public static  int PORT = 1234;
    String msgToSend;
    private InputStream iStream;
    private OutputStream oStream;
    private boolean startReceive = false;
    private Chat chatActivity;


    public Server(InetAddress groupOwnerAddress, Chat chatActivity){
        address = groupOwnerAddress;
        chatActivity = chatActivity;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT,5,address);
            Socket socket = null;
            while (true){
                socket = serverSocket.accept();
                System.out.println("Add connection："+socket.getInetAddress()+":"+socket.getPort());
                new HandlerThread(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Method to write a byte array (that can be a message) on the output stream.
     * @param buffer byte[] array that represents data to write. For example, a String converted in byte[] with ".getBytes();"
     */
    public void write(byte[] buffer) {
        try {
            oStream.write(buffer);
            Log.i("resid inja: ", "3");
            String sendMsg = new String(buffer, "UTF-8");
            updateMessagesfromServer(sendMsg);
        } catch (IOException e) {
            Log.e("Server", "Exception during write", e);
        }
    }

    private class HandlerThread implements Runnable {
        private Socket socket;
        public HandlerThread(Socket client) {
            socket = client;
            try {
                iStream = socket.getInputStream();
                oStream = socket.getOutputStream();
                if( iStream != null && oStream != null){
                    Log.i("Server i&o stream: ", "not null");
                }else {
                    Log.i("Server i&o stream: ", " null");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            new Thread(this).start();
        }



        public void run() {
            //Log.i("in ane doros shod",Chat.msgToSend );

            Log.i("resid inja: ", "1");

            try {
                // Read client data
                //DataInputStream input = new DataInputStream(socket.getInputStream());
                //This should pay attention to the write method of the client output stream,
                // otherwise it will throw EOFException
                //String clientInputStr = input.readUTF();
                // Processing client data
                //System.out.println("Client sent over the content:" + clientInputStr);

                //Chat.updateMessagesfromClient(clientInputStr);

                byte[] buffer = new byte[1024];
                int bytes;

                while( !startReceive ){
                    try{
                        if(iStream!=null) {
                            bytes = iStream.read(buffer);
                            Log.i("resid inja: ", "2");
                            if (bytes == -1) {
                                break;
                            }
                            String receivedMsg = new String(buffer, "UTF-8");
                            Log.i("client returns: " , receivedMsg);
                            updateMessagesfromClient(receivedMsg);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                //chat.messages.setText(messages.getText() + "\n" + "client: " + clientInputStr);

                // Reply to the client
                //DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                //System.out.print("please enter:\t");
//                // Send a line of keyboard input
//                String s = new BufferedReader(new InputStreamReader(System.in)).readLine();
                /*msgToSend = Chat.getMsgToSend();
                if( msgToSend != null) {
                    out.writeUTF(msgToSend);
                    Chat.updateMessagesfromServer(msgToSend);
                }
                msgToSend = "";*/


                //out.writeUTF("test back");

                //out.close();
                //input.close();
            } catch (Exception e) {
                System.out.println("server run abnormal: " + e.getMessage());
            } finally {
                if (socket != null) {
                    try {
                        iStream.close();
                        socket.close();
                    } catch (Exception e) {
                        socket = null;
                        System.out.println("server finally abnormal:" + e.getMessage());
                    }
                }
            }
        }
    }
}
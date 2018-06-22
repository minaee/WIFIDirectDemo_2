package com.shmj.wifidirectdemo;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

import static com.shmj.wifidirectdemo.Chat.updateMessagesfromClient;
import static com.shmj.wifidirectdemo.Chat.updateMessagesfromServer;
import static com.shmj.wifidirectdemo.Server.PORT;

/**
 * Created by Shahriar on 3/21/2018.
 */

public class Client extends Thread {
    InetAddress address;
    String msgToSend;
    private InputStream iStream;
    private OutputStream oStream;
    Socket socket;
    private boolean startReceive = false;
    private Chat chatActivity;
    String secretKeyString = "1111111111111111";   //16 digit secret key
    public EncryptionAES encryptionAES;
    public byte[] decrypted_msg = null;


    public Client(InetAddress address, Chat chatActivity){
        this.address = address;
        this.chatActivity = chatActivity;

    }
    @Override
    public void run() {
        try {
            encryptionAES = new EncryptionAES();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        communication();
        try {
            socket = new Socket(address, Server.PORT);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Error: ", e.getMessage().toString());
        }
    }

    private void communication(){
        Socket socket = null;


        try {
            socket = new Socket(address, PORT);
            iStream = socket.getInputStream();
            oStream = socket.getOutputStream();
            if(iStream != null && oStream != null){
                Log.i("Client i&o stream: ", "not null");
            }else {
                Log.i("Client i&o stream: ", "null");
            }
            //Create a stream socket and connect it to the
            //specified port number on the specified host
            //socket = new Socket(address, PORT);

            //socket = new Socket(address, Server.PORT);

            //Read server data
            //DataInputStream input = new DataInputStream(socket.getInputStream());

            //Send data to the server
            //DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            //iStream = socket.getInputStream();
            //oStream = socket.getOutputStream();
            byte[] buffer = new byte[1024];
            int bytes;
            Log.i("resid inja: ", "1");

            while( !startReceive ){
                try{
                    if(iStream!=null) {
                        Log.i("resid inja: ", "2");
                        bytes = iStream.read(buffer);
                        if (bytes == -1) {
                            break;
                        }

                        if(buffer != null) {
                            String encrypted_msg = new String(buffer, "UTF-8");
                            decrypted_msg = encryptionAES.decryptMSG(secretKeyString, buffer);
                            String decrypted_msg_string = new String(decrypted_msg, "UTF-8");
                            Log.i("server returns: ", decrypted_msg_string);
                            updateMessagesfromServer(encrypted_msg, decrypted_msg_string);
                        }
                    }/*else{
                        Log.i("istream: ", "is null.");
                        iStream = socket.getInputStream();
                    }*/
                        //Log.e("error: ", iStream.toString());

                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("error: ", e.getMessage());
                }
            }

            /*
            msgToSend = Chat.getMsgToSend();
            if(msgToSend != null ) {
                out.writeUTF(msgToSend);
                Chat.updateMessagesfromClient(msgToSend);
            }
            msgToSend = null;
            */
//            String str = new BufferedReader(new InputStreamReader(System.in)).readLine();
//            out.writeUTF(str);
            //out.writeUTF("test");



            /*
            String ret = input.readUTF();
            Log.i("server returns: " , ret);
            Log.i("in ane doros shod",Chat.msgToSend );
            Chat.updateMessagesfromServer(ret);
            */
            //messages.setText(messages.getText() + "\n" + "Server: " + ret);

            //out.close();
            //input.close();
        } catch (Exception e) {
            System.out.println("Client exception:" + e.getMessage());
        } finally {
            if (socket != null) {
                try {
                    iStream.close();
                    //socket.close();
                } catch (IOException e) {
                    socket = null;
                    System.out.println("Clients finally abnormal:" + e.getMessage());
                }
            }
        }
    }

    /**
     * Method to write a byte array (that can be a message) on the output stream.
     * @param buffer byte[] array that represents data to write. For example, a String converted in byte[] with ".getBytes();"
     */
    public void write(byte[] buffer) {
        try {
            Log.i("resid inja: ", "3");
            oStream.write(buffer);
            String encrypted_msg = new String(buffer, "UTF-8");
            byte[] decrypted_msg_byte = encryptionAES.decryptMSG(secretKeyString, buffer);
            String encrypted_msg_string = new String(decrypted_msg_byte, "UTF-8");
            updateMessagesfromClient(encrypted_msg, encrypted_msg_string);
        } catch (IOException e) {
            Log.e("Client", "Exception during write", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

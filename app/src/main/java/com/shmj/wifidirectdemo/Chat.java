package com.shmj.wifidirectdemo;

import android.net.wifi.p2p.WifiP2pInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import java.security.Key;


/**
 * Created by Shahriar on 3/5/2018.
 */

public class Chat extends AppCompatActivity {

    TextView otherDevicename, encyprionAlgo, key;
    Button sendButton;
    EditText textTosend;

    static String  msgToSend;
    private Server server;
    private Client client;
    InetAddress mygroupOwnerAddress;
    boolean serverOrClient;
    WifiP2pInfo wifiP2pInfo;
    public EncryptionAES encryptionAES;
    String secretKeyString = "1111111111111111";   //16 digit secret key

    public static ArrayList<String> encrypted_msg_content = new ArrayList<>();
    public static ArrayAdapter encrypted_msg_adapter;
    public static ArrayList<String> decrypted_msg_content = new ArrayList<>();
    public static ListView decrypted_messages;
    public static ListView encrypted_messages;
    public static ArrayAdapter decrypted_msg_adapter;
    String otherDeviceName;
    public String  encrypted_msg = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_page);
        //WifiP2pInfo wifiP2pInfo = (WifiP2pInfo)getIntent().getSerializableExtra("WifiP2pInfo");

        //Log.i("wifiP2pinfo Chat", String.valueOf(wifiP2pInfo));

        // receiving content from MainActivity
        serverOrClient = getIntent().getBooleanExtra("serverOrClient",true);
        wifiP2pInfo = getIntent().getExtras().getParcelable("WifiP2pInfo");
        otherDeviceName  = getIntent().getExtras().getParcelable("otherDeviceName");

        /*if(serverOrClient == false){
            Log.i("WPI chat client" , wifiP2pInfo.toString() );
        }else{
            Log.i("WPI chat server" , wifiP2pInfo.toString() );
        }*/

        // creating server o client instances
        if(wifiP2pInfo != null) {
            setSender(wifiP2pInfo, serverOrClient);
        }

        // initializing Views
        sendButton = (Button) findViewById(R.id.sendButton);
        textTosend = (EditText) findViewById(R.id.textToSend);
        decrypted_messages = (ListView) findViewById(R.id.decrypt_messages);
        encrypted_messages = (ListView) findViewById(R.id.encrypt_messages);
        encyprionAlgo = (TextView) findViewById(R.id.otherDeviceName);
        key = (TextView) findViewById(R.id.key);

        //setting adapter for listview(chat messages)
        decrypted_msg_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, decrypted_msg_content );
        decrypted_msg_content.add("decrypted messages:");
        decrypted_messages.setAdapter(decrypted_msg_adapter);
        encrypted_msg_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, encrypted_msg_content);
        encrypted_msg_content.add("encrypted messages:");
        encrypted_messages.setAdapter(encrypted_msg_adapter);

        // name of chat partner
        otherDevicename = (TextView) findViewById(R.id.otherDeviceName);
        otherDevicename.setText("this is a new chat with: " + otherDeviceName);
        encyprionAlgo.setText( encyprionAlgo.getText().toString() + "AES");
        key.setText(key.getText().toString() + secretKeyString);
        msgToSend = "thisShitIsNull";

        try {
            encryptionAES = new EncryptionAES(secretKeyString.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void showMsg(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public static String getMsgToSend() {
        return msgToSend;
    }

    public void fuckingSend(View view){
       EditText textTosend2 = (EditText) findViewById(R.id.textToSend);
        if(String.valueOf(textTosend2.getText()) != null){
            msgToSend = String.valueOf(textTosend2.getText());
            //showMsg(msgToSend+" from Fucking Send.");

            //encrypt msg before sending

            try {
                //encrypted_msg = encryptionAES.encryptMSG(secretKeyString, msgToSend);
                encrypted_msg = encryptionAES.encrypt(msgToSend);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(serverOrClient == true && encrypted_msg != null){ // for server
                /*updateMessagesfromServer("");
                server = new Server(  wifiP2pInfo.groupOwnerAddress );
                server.start();*/

                //server.write(msgToSend.getBytes());
                server.write(encrypted_msg.getBytes());
                //msg_content.add("server: " + msgToSend.toString());
                //msg_adapter.notifyDataSetChanged();
                Log.i("msg to send server: ",msgToSend);
                //server.sendFromServer(String.valueOf(textTosend));
                //server.sendFromServer(String.valueOf(msgToSend));
            }else if(serverOrClient == false && encrypted_msg != null){    // for client
                /*updateMessagesfromClient("");
                client = new Client(  wifiP2pInfo.groupOwnerAddress );
                client.start();*/
                client.write(encrypted_msg.getBytes());
                //msg_content.add("client: " + msgToSend.toString());
                //msg_adapter.notifyDataSetChanged();
                Log.i("msg to send client: ",msgToSend);
//            client.sendFromClient(String.valueOf(textTosend));
                //client.sendFromClient(String.valueOf(msgToSend));
            } else {
                Log.i("encrypted message: ", "is null.");
                showMsg("encrypted message is null.");
            }


        }


   }

    private void setSender(WifiP2pInfo wifiP2pInfo, boolean serverOrClient) {
        mygroupOwnerAddress = wifiP2pInfo.groupOwnerAddress;

        if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner && serverOrClient == true) {
            // Do whatever tasks are specific to the group owner.
            // One common case is creating a server thread and accepting
            // incoming connections.
            Log.i("server Sender: ", "criteria met.");
            server = new Server(mygroupOwnerAddress,this);
            //server.start();
            showMsg("server created from server.");
            //client = new Client(mygroupOwnerAddress, this);
            //client.start();
            //showMsg("client created from server");
//                chat.sendAsServer(groupOwnerAddress);
            server.start();
            Log.i("server setSender: ", server.toString());

        } else if (wifiP2pInfo.groupFormed && serverOrClient == false) {
            // The other device acts as the client. In this case,
            // you'll want to create a client thread that connects to the group
            // owner.
            Log.i("client Sender: ", "criteria met.");
            client = new Client(mygroupOwnerAddress, this);
            //client.start();
            showMsg("client created from client");
            client.start();
            Log.i("client setSender: ", client.toString());
            //server = new Server(mygroupOwnerAddress, this);
            //server.start();
            //showMsg("server created from client");
            //chat.sendAsClient(groupOwnerAddress);
        }
    }

    static void updateMessagesfromServer(String encrypted_msg, String decrypted_msg){
        if(encrypted_msg != null && decrypted_msg != null){
            //messages.setText(messages.getText() + "\n" + "client: " + msgs);
            decrypted_msg_content.add( "Server: " + decrypted_msg);
            decrypted_msg_adapter.notifyDataSetChanged();
            encrypted_msg_content.add( "Server: " + encrypted_msg);
            encrypted_msg_adapter.notifyDataSetChanged();

        }
    }

    public static void updateMessagesfromClient(String encrypted_msg, String decrypted_msg) {
        if(encrypted_msg != null && decrypted_msg != null){
            //messages.setText(messages.getText() + "\n" + "Server: " + ret);
            decrypted_msg_content.add("Client: " + decrypted_msg);
            decrypted_msg_adapter.notifyDataSetChanged();
            encrypted_msg_content.add( "Client: " + encrypted_msg);
            encrypted_msg_adapter.notifyDataSetChanged();
        }
    }
}

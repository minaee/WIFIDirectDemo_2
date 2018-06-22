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
import java.util.ArrayList;

/**
 * Created by Shahriar on 3/5/2018.
 */

public class Chat extends AppCompatActivity {

    TextView otherDevicename;
    Button sendButton;
    EditText textTosend;

    static String  msgToSend;
    private Server server;
    private Client client;
    InetAddress mygroupOwnerAddress;
    boolean serverOrClient;
    WifiP2pInfo wifiP2pInfo;

    public static ArrayList<String> msg_content = new ArrayList<>();
    public static ListView messages;
    public static ArrayAdapter msg_adapter;

    public static ArrayList<String> getMsg_content() {
        return msg_content;
    }

    public static ArrayAdapter getMsg_adapter() {
        return msg_adapter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_page);
        //WifiP2pInfo wifiP2pInfo = (WifiP2pInfo)getIntent().getSerializableExtra("WifiP2pInfo");

        //Log.i("wifiP2pinfo Chat", String.valueOf(wifiP2pInfo));

        serverOrClient = getIntent().getBooleanExtra("serverOrClient",true);

        wifiP2pInfo = getIntent().getExtras().getParcelable("WifiP2pInfo");


        if(serverOrClient == false){
            Log.i("WPI chat client" , wifiP2pInfo.toString() );
        }else{
            Log.i("WPI chat server" , wifiP2pInfo.toString() );
        }

        if(wifiP2pInfo != null) {
            setSender(wifiP2pInfo, serverOrClient);
        }

        sendButton = (Button) findViewById(R.id.sendButton);
        textTosend = (EditText) findViewById(R.id.textToSend);
        messages = (ListView) findViewById(R.id.messages);

        msg_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, msg_content );
        msg_content.add("The chat goes Below...");
        messages.setAdapter(msg_adapter);

        otherDevicename = (TextView) findViewById(R.id.otherDeviceName);
        otherDevicename.setText("this is a new chat");
        msgToSend = "thisShitIsNull";
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

            if(serverOrClient == true){ // for server
                /*updateMessagesfromServer("");
                server = new Server(  wifiP2pInfo.groupOwnerAddress );
                server.start();*/

                server.write(msgToSend.getBytes());
                //msg_content.add("server: " + msgToSend.toString());
                //msg_adapter.notifyDataSetChanged();
                Log.i("msg to send server: ",msgToSend);
                //server.sendFromServer(String.valueOf(textTosend));
                //server.sendFromServer(String.valueOf(msgToSend));
            }else{    // for client
                /*updateMessagesfromClient("");
                client = new Client(  wifiP2pInfo.groupOwnerAddress );
                client.start();*/
                client.write(msgToSend.getBytes());
                //msg_content.add("client: " + msgToSend.toString());
                //msg_adapter.notifyDataSetChanged();
                Log.i("msg to send client: ",msgToSend);
//            client.sendFromClient(String.valueOf(textTosend));
                //client.sendFromClient(String.valueOf(msgToSend));
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

    static void updateMessagesfromServer(String msgs){
        if(msgs != null){
            //messages.setText(messages.getText() + "\n" + "client: " + msgs);
            msg_content.add( "Server: " + msgs);
            msg_adapter.notifyDataSetChanged();

        }
    }

    public static void updateMessagesfromClient(String ret) {
        if(ret != null){
            //messages.setText(messages.getText() + "\n" + "Server: " + ret);
            msg_content.add("Client: " + ret);
            msg_adapter.notifyDataSetChanged();
        }
    }
}

package com.shmj.wifidirectdemo;

import android.util.Log;

import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Shahriar on 6/22/2018.
 */

public class EncryptionAES {

    String secretKeyString;
    byte[] encryptedMsg;
    String msgContentString;

    public EncryptionAES( ) throws NoSuchAlgorithmException {  }

    public static byte[] encryptMSG(String secretKeyString, String msgContentString) {

        try {
            byte[] returnArray;
            // generate AES secret key from user input
            Key key = generateKey(secretKeyString);
            // specify the cipher algorithm using AES
            Cipher c = Cipher.getInstance("AES");
            // specify the encryption mode
            c.init(Cipher.ENCRYPT_MODE, key);
            // encrypt
            returnArray = c.doFinal(msgContentString.getBytes());
            return returnArray;
        } catch (Exception e) {
            e.printStackTrace();
            byte[] returnArray = null;
            return returnArray;
        }
    }



    private static Key generateKey(String secretKeyString) throws Exception {
        // generate secret key from string
        Key key = new SecretKeySpec(secretKeyString.getBytes(), "AES");
        return key;
    }

    // decryption function
    public static byte[] decryptMSG(String secretKeyString, byte[] encryptedMsg)
            throws Exception {
        // generate AES secret key from the user input secret key
        Key key = generateKey(secretKeyString);
        // get the cipher algorithm for AES
        Cipher c = Cipher.getInstance("AES");
        // specify the decryption mode
        c.init(Cipher.DECRYPT_MODE, key);

        byte[] msg = hex2byte(encryptedMsg);
        // decrypt the message
        byte[] decValue = c.doFinal(msg);
        return decValue;
    }


    // utility function: convert hex array to byte array
    public static byte[] hex2byte(byte[] b)  {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException("hello");
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            if(item != null) {
                Log.i("item: ",String.valueOf(n) + " " + String.valueOf(b));
                b2[n / 2] = (byte) Integer.parseInt(item, 16);
            }else {

            }
            //b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }


}

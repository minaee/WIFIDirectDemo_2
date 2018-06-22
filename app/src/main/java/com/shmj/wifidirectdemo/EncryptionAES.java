package com.shmj.wifidirectdemo;

import android.util.Log;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
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

    public static byte[] encryptMSG(String secretKeyString, String msgContentString)
            throws NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, InvalidKeyException {

        try {
            byte[] returnArray;
            // generate AES secret key from user input
            Key key = generateKey(secretKeyString);
            // specify the cipher algorithm using AES
            Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
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
            throws NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, InvalidKeyException   {
        // generate AES secret key from the user input secret key
        Key key = null;
        try {
            key = generateKey(secretKeyString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // get the cipher algorithm for AES
        Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
        // specify the decryption mode
        c.init(Cipher.DECRYPT_MODE, key);

        //byte[] msg = hex2byte(encryptedMsg);
        // decrypt the message
        byte[] decValue = c.doFinal(encryptedMsg);
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
                Log.i("item: ",item + " " + String.valueOf(n) + " " + String.valueOf(b));
                b2[n / 2] = (byte) Integer.parseInt(item, 16);
            }else {

            }
            //b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }


}

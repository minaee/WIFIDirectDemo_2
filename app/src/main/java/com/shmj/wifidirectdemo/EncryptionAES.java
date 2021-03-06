package com.shmj.wifidirectdemo;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import junit.framework.Test;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static android.content.ContentValues.TAG;

/**
 * Created by Shahriar on 6/22/2018.
 */

public class EncryptionAES {

    private static byte[] keyValue;
    String secretKeyString;
    byte[] encryptedMsg;
    String msgContentString;
    public String encryptionKey;



    public EncryptionAES( byte[] keyValue) throws NoSuchAlgorithmException {
        this.keyValue = keyValue;
    }

   /*public static byte[] encryptMSG(String secretKeyString, String msgContentString)
            throws NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, InvalidKeyException {

        try {
            byte[] returnArray;
            // generate AES secret key from user input
            Key key = generateKey(secretKeyString);
            // specify the cipher algorithm using AES
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
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
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
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
    }*/

    //private static final byte[] keyValue = new byte[]{'c', 'o', 'd', 'i', 'n', 'g', 'a', 'f', 'f', 'a', 'i', 'r', 's', 'c', 'o', 'm'};


    public static String encrypt(String cleartext)
            throws Exception {
        byte[] rawKey = getRawKey();
        byte[] result = encrypt(rawKey, cleartext.getBytes());
        return toHex(result);
    }

    public static String decrypt(String encrypted)
            throws Exception {

        byte[] enc = toByte(encrypted);
        byte[] result = decrypt(enc);
        return new String(result);
    }

    private static byte[] getRawKey() throws Exception {
        SecretKey key = new SecretKeySpec(keyValue, "AES");
        byte[] raw = key.getEncoded();
        return raw;
    }

    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKey skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private static byte[] decrypt(byte[] encrypted)
            throws Exception {
        SecretKey skeySpec = new SecretKeySpec(keyValue, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static byte[] toByte(String hexString) {
        Log.i("hexString: ", hexString);
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            Log.i("i= ", i + " toByte: "+ hexString.substring(2 * i, 2 * i + 2));
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                    16).byteValue();
        }
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private final static String HEX = "0123456789ABCDEF";

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

}

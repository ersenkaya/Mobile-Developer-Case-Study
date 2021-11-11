package com.tohan.veriparkcasestudy.Helper;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.ArrayList;

import android.util.Base64;
import android.util.Log;

import com.tohan.veriparkcasestudy.Api.Models.StockModel;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoHepler {

    public String encrypt(String value, String aesKey , String aesIV) {
        String result = "";
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.decode(aesKey.getBytes(), Base64.DEFAULT), "AES");
            IvParameterSpec paramSpec = new IvParameterSpec(Base64.decode(aesIV.getBytes(), Base64.DEFAULT));
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, paramSpec);
            result = Base64.encodeToString(cipher.doFinal(value.getBytes("UTF-8")), Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("retrofittest",e.toString());

        }
        return result;

    }

    public String decrypt(String value, String aesKey , String aesIV) {
        String result = "";
        try {
            Log.i("retrofittest", aesKey + " " +aesIV);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.decode(aesKey.getBytes(), Base64.DEFAULT), "AES");
            IvParameterSpec paramSpec = new IvParameterSpec(Base64.decode(aesIV.getBytes(), Base64.DEFAULT));
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, paramSpec);
            byte[] decryptedText = cipher.doFinal(Base64.decode(value.getBytes(), Base64.DEFAULT));
            result =  new String(decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("retrofittest",e.toString());

        }
        return result;

    }

    public ArrayList<StockModel> getStockListArray(ArrayList<StockModel> list, String aesKey , String aesIV){
        ArrayList<StockModel> newStockList = list;
        for(StockModel item : newStockList) {
            item.setSymbol(decrypt(item.getSymbol(),aesKey,aesIV));
        }
        return newStockList;
    }


    public String[][] getStockList(ArrayList<StockModel> list, String aesKey , String aesIV){
        String[][] newList = new String[list.size()][7];
        ArrayList<StockModel> newStockList = getStockListArray(list, aesKey, aesIV);

        for(int i = 0 ; i < newStockList.size(); i++) {
            StockModel item = newStockList.get(i);
            String isUpDown="none";
            if(item.isUp())
                isUpDown = "up";
            else if(item.isDown())
                isUpDown = "down";
            else
                isUpDown = "none";
            String[] str = {item.getSymbol(), String.valueOf(item.getPrice()),
                    String.valueOf(item.getDifference()), String.valueOf(item.getVolume()),
                    String.valueOf(item.getBid()), String.valueOf(item.getOffer()), isUpDown};
            newList[i] = str;
        }
        return newList;
    }

}

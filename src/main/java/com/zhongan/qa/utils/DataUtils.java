package com.zhongan.qa.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class DataUtils {

    public static String getJSONFromFile(String path) {
        BufferedReader bufferReader = null;
        StringBuffer stringBuffer = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(path);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "utf-8");
            bufferReader = new BufferedReader(inputStreamReader);
            stringBuffer = new StringBuffer();
            String lineData = null;
            while ((lineData = bufferReader.readLine()) != null) {
                stringBuffer.append(lineData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != bufferReader) {
                try {
                    bufferReader.close();
                    fileInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return stringBuffer.toString();
            }
        }
        return null;
    }
}

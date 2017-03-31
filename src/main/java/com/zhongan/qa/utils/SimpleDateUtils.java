package com.zhongan.qa.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleDateUtils {

    public static String getCurrentDateTime(String dateFormat) {
        SimpleDateFormat sf = new SimpleDateFormat(dateFormat);
        return sf.format(new Date());
    }
}

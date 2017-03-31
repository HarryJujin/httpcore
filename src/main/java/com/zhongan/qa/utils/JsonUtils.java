package com.zhongan.qa.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JsonUtils {

    private static Logger logger       = LoggerFactory.getLogger(JsonUtils.class);
    private static Object objToFindOut = null;

    public static String getValueFromJsonStringByKey(String jsonString, String key) {
        JSONObject jsonObj = JSON.parseObject(jsonString);
        return recursiveJSONObject(jsonObj, key).toString();
    }

    public static String translateMap2JsonString(Map<String, Object> parameterMap) {
        String jsonString = JSON.toJSONString(parameterMap);
        logger.info("jsonStringTranslateResult:{}", jsonString);
        return jsonString;
    }

    public static List<String> getListFromJsonStringByKey(String jsonString, String keyWord) {
        try {
            JSONArray jsonArray = JSON.parseArray(getValueFromJsonStringByKey(jsonString, keyWord));
            Iterator<Object> iterator = jsonArray.iterator();
            List<String> target = new ArrayList<String>();
            while (iterator.hasNext()) {
                target.add(iterator.next().toString());
            }
            return target;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("GetListFromJsonStringByKeyError:{}", e);
            return null;
        }

    }

    private static Object recursiveJSONObject(JSONObject jsonObj, String keyWord) {
        try {
            for (String key : jsonObj.keySet()) {
                if (keyWord.equals(key)) {
                    logger.info("targetClass:{}", jsonObj.get(keyWord).getClass().toString());
                    logger.info("targetValue:{}", jsonObj.get(keyWord).toString());
                    objToFindOut = jsonObj.get(keyWord);
                } else {
                    if (jsonObj.get(key) instanceof JSONObject) {
                        recursiveJSONObject(jsonObj.getJSONObject(key), keyWord);
                    } else if (jsonObj.get(key) instanceof JSONArray) {
                        recursiveJSONArray(jsonObj.getJSONArray(key), keyWord);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("fuck:{}", e);
        }
        return objToFindOut;
    }

    private static void recursiveJSONArray(JSONArray jsonArray, String keyWord) {
        Iterator<Object> objs = jsonArray.iterator();
        while (objs.hasNext()) {
            Object obj = objs.next();
            if (obj instanceof JSONObject) {
                recursiveJSONObject((JSONObject) obj, keyWord);
            } else if (obj instanceof JSONArray) {
                recursiveJSONArray((JSONArray) obj, keyWord);
            }
        }
    }
}

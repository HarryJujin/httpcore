package com.zhongan.qa.http;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

public class HttpUrlConstructor {

    public static String urlBuild(String domain, String resourceNode, Map<String, String> urlParameterMap) {
        StringBuffer urlBuffer = new StringBuffer();
        urlBuffer.append(domain);
        urlBuffer.append(resourceNode);
        if (null != urlParameterMap) {
            urlBuffer.append(makeUpUrlParameter(urlParameterMap));
        }
        return urlBuffer.toString();
    }

    private static String makeUpUrlParameter(Map<String, String> properties) {
        StringBuffer parameter = new StringBuffer();
        int i = 0;
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            if (i == 0) {
                parameter.append("?");
            } else {
                parameter.append("&");
            }
            parameter.append(entry.getKey());
            parameter.append("=");
            if (null != entry.getValue()) {
                parameter.append(entry.getValue());
            }
            i++;
        }
        return parameter.toString();
    }

    public static String joinRequestParam(TreeMap<String, ? extends Object> params) {
        StringBuilder sb = new StringBuilder();
        try {
            Iterator<String> keyIterator = params.navigableKeySet().iterator();
            while (keyIterator.hasNext()) {
                String key = keyIterator.next();
                Object value = params.get(key);
                if (value == null || StringUtils.isEmpty(value.toString()))
                    continue;
                sb.append(key);
                sb.append('=');
                sb.append(value.toString());
                sb.append('&');
            }
        } catch (Exception e) {
            //never happen
        }

        if (sb.length() > 0)
            sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }
}

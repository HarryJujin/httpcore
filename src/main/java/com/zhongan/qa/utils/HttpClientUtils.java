package com.zhongan.qa.utils;

import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import com.zhongan.qa.dto.HttpFileUploadDTO;
import com.zhongan.qa.http.HttpClientBase;

/**
 * 类HttpClientUtils.java的实现描述：http接口测试工具类
 * 
 * @author linyun 2016年6月30日 下午3:49:06
 */
public class HttpClientUtils {

    /**
     * @param headerMap could be null
     * @param proxyMap could be null
     * @param url
     * @param method GET/POST/PUT/DELETE
     * @param parameterMap Request parameter
     * @return result of HttpRequest
     */
    public static CloseableHttpResponse testWithKeyValue(Map<String, String> headerMap, Map<String, Object> proxyMap,
                                                         String url, String method, Map<String, String> parameterMap) {
        HttpClientBase httpBase = new HttpClientBase();
        CloseableHttpClient client;
        if (url.startsWith("https://")) {
            client = httpBase.initHttpsClient(headerMap, proxyMap);
        } else {
            client = httpBase.initClient(headerMap, proxyMap);
        }
        CloseableHttpResponse response = HttpResponseUtils.getResponseByKeyValueParameter(client, url, method,
                headerMap, parameterMap);
        return response;
    }

    /**
     * 默认是用post方式，提交表单
     * 
     * @param headerMap could be null
     * @param proxyMap could be null
     * @param url
     * @param parameterMap Request parameter
     * @param fileList 待上传文件列表
     * @return
     */
    public static CloseableHttpResponse testFileUpload(Map<String, String> headerMap, Map<String, Object> proxyMap,
                                                       String url, Map<String, String> parameterMap, String method,
                                                       HttpFileUploadDTO httpFileUploadDTO) {
        HttpClientBase httpBase = new HttpClientBase();
        CloseableHttpClient client = httpBase.initClient(headerMap, proxyMap);
        CloseableHttpResponse response = HttpResponseUtils.getResponseByMultiParameter(client, url, headerMap,
                parameterMap, method, httpFileUploadDTO);
        return response;
    }

    /**
     * @param headerMap could be null
     * @param proxyMap could be null
     * @param url
     * @param method GET/POST/PUT/DELETE
     * @param jsonParameter JSON字符串作为参数
     * @return
     */
    public static CloseableHttpResponse testWithJson(Map<String, String> headerMap, Map<String, Object> proxyMap,
                                                     String url, String method, String jsonParameter) {
        HttpClientBase httpBase = new HttpClientBase();
        CloseableHttpClient client;
        if (url.startsWith("https://")) {
            client = httpBase.initHttpsClient(headerMap, proxyMap);
        } else {
            client = httpBase.initClient(headerMap, proxyMap);
        }
        CloseableHttpResponse response = HttpResponseUtils.getResponseByJsonParameter(client, url, method, headerMap,
                jsonParameter);
        return response;
    }
    
    public static CloseableHttpResponse testWithXml(Map<String, String> headerMap, Map<String, Object> proxyMap,
            String url, String method, String xmlParameter) {
        HttpClientBase httpBase = new HttpClientBase();
        CloseableHttpClient client = httpBase.initClient(headerMap, proxyMap);
        CloseableHttpResponse response = HttpResponseUtils.getResponseByXmlParameter(client, url, method, headerMap, xmlParameter);
         return response;
     }
    
    /**
     * @param cookieMap could be null
     * @param proxyMap could be null
     * @param url
     * @param method must POST
     * @param ArgTypes DTO类名全称
     * @param ArgsObjects 报文
     * @return
     */

    public static CloseableHttpResponse testHSF(Map<String, String> headerMap, Map<String, Object> proxyMap,
                                                String url, String method, String ArgTypes,String ArgsObjects) {
        HttpClientBase httpBase = new HttpClientBase();
        CloseableHttpClient client = httpBase.initClient(headerMap, proxyMap);
        CloseableHttpResponse response = HttpResponseUtils.getHSFResponse(client, url, method, headerMap, ArgTypes,ArgsObjects);
        return response;
    }

}

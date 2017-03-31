package com.zhongan.qa.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.zhongan.qa.dto.HttpFileUploadDTO;
import com.zhongan.qa.http.HttpConstant;
import com.zhongan.qa.http.HttpDeleteWithBody;

public class HttpResponseUtils {

    private static Logger           logger        = LoggerFactory.getLogger(HttpResponseUtils.class);

    public static final ContentType TEXT_PLAIN    = ContentType.create("text/plain", Consts.UTF_8);

    private static RequestConfig    requestConfig = RequestConfig.custom().setSocketTimeout(30000)
                                                          .setConnectTimeout(30000).build();

    public static CloseableHttpResponse getResponseByKeyValueParameter(CloseableHttpClient client, String url,
                                                                       String method, Map<String, String> headerMap,
                                                                       Map<String, String> parameterMap) {
        try {
            List<NameValuePair> nvps;
            switch (method) {
                case HttpConstant.HTTPGET:
                    HttpGet getMethod = new HttpGet(url);
                    if (null != headerMap) {
                        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                            getMethod.setHeader(entry.getKey(), entry.getValue());
                        }
                    }
                    return client.execute(getMethod);

                case HttpConstant.HTTPPOST:
                    HttpPost postMethod = new HttpPost(url);
                    if (null != parameterMap) {
                        nvps = generateNameValuePair(parameterMap);
                        postMethod.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
                    }
                    if (null != headerMap) {
                        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                            postMethod.setHeader(entry.getKey(), entry.getValue());
                        }
                    }
                    postMethod.setConfig(requestConfig);
                    return client.execute(postMethod);

                case HttpConstant.HTTPPUT:
                    HttpPut putMethod = new HttpPut(url);
                    nvps = generateNameValuePair(parameterMap);
                    putMethod.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
                    if (null != headerMap) {
                        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                            putMethod.setHeader(entry.getKey(), entry.getValue());
                        }
                    }
                    putMethod.setConfig(requestConfig);
                    return client.execute(putMethod);

                case HttpConstant.HTTPDELETE:
                    HttpDeleteWithBody deleteMethod = new HttpDeleteWithBody(url);
                    if (null != headerMap) {
                        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                            deleteMethod.setHeader(entry.getKey(), entry.getValue());
                        }
                    }
                    if (null != parameterMap) {
                        nvps = generateNameValuePair(parameterMap);
                        deleteMethod.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
                    }
                    deleteMethod.setConfig(requestConfig);
                    return client.execute(deleteMethod);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("clientExecuteError:{}", e);
        }
        return null;
    }

    public static CloseableHttpResponse getResponseByMultiParameter(CloseableHttpClient client, String url,
                                                                    Map<String, String> headerMap,
                                                                    Map<String, String> parameterMap, String method,
                                                                    HttpFileUploadDTO httpFileUploadDTO) {
        String filePkgName = "file";
        if (null != httpFileUploadDTO.getPkgName()) {
            filePkgName = httpFileUploadDTO.getPkgName();
        }
        MultipartEntityBuilder reqEntityBulider = MultipartEntityBuilder.create();
        Iterator<String> iterator = httpFileUploadDTO.getFileList().iterator();
        while (iterator.hasNext()) {
            FileBody bin = new FileBody(new File(iterator.next()));
            reqEntityBulider.addPart(filePkgName, bin);
        }
        for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
            String contentName = entry.getKey();
            StringBody contentBody = new StringBody(entry.getValue(), TEXT_PLAIN);
            reqEntityBulider.addPart(contentName, contentBody);
        }
        HttpEntity reqEntity = reqEntityBulider.build();
        try {
            switch (method) {
                case HttpConstant.HTTPPOST:
                    HttpPost postMethod = new HttpPost(url);
                    postMethod.setEntity(reqEntity);
                    if (null != headerMap) {
                        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                            postMethod.setHeader(entry.getKey(), entry.getValue());
                        }
                    }
                    postMethod.setConfig(requestConfig);
                    return client.execute(postMethod);
                case HttpConstant.HTTPPUT:
                    HttpPut putMethod = new HttpPut(url);
                    putMethod.setEntity(reqEntity);
                    if (null != headerMap) {
                        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                            putMethod.setHeader(entry.getKey(), entry.getValue());
                        }
                    }
                    putMethod.setConfig(requestConfig);
                    return client.execute(putMethod);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("clientExecuteError:{}", e);
        }
        return null;
    }

    public static CloseableHttpResponse getResponseByJsonParameter(CloseableHttpClient client, String url,
                                                                   String method, Map<String, String> headerMap,
                                                                   String jsonParameter) {
        try {
            switch (method) {
                case HttpConstant.HTTPGET:
                    HttpGet getMethod = new HttpGet(url);
                    getMethod.addHeader("Content-Type", "application/json; charset=utf-8");
                    if (null != headerMap) {
                        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                            getMethod.setHeader(entry.getKey(), entry.getValue());
                        }
                    }
                    return client.execute(getMethod);

                case HttpConstant.HTTPPOST:
                    HttpPost postMethod = new HttpPost(url);
                    postMethod.addHeader("Content-Type", "application/json; charset=utf-8");
                    if (null != jsonParameter) {
                        postMethod.setEntity(new StringEntity(jsonParameter, "UTF-8"));
                    }
                    if (null != headerMap) {
                        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                            postMethod.setHeader(entry.getKey(), entry.getValue());
                        }
                    }
                    postMethod.setConfig(requestConfig);
                    return client.execute(postMethod);

                case HttpConstant.HTTPPUT:
                    HttpPut putMethod = new HttpPut(url);
                    putMethod.addHeader("Content-Type", "application/json; charset=utf-8");
                    if (null != jsonParameter) {
                        putMethod.setEntity(new StringEntity(jsonParameter, "UTF-8"));
                    }
                    if (null != headerMap) {
                        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                            putMethod.setHeader(entry.getKey(), entry.getValue());
                        }
                    }
                    putMethod.setConfig(requestConfig);
                    return client.execute(putMethod);

                case HttpConstant.HTTPDELETE:
                    HttpDeleteWithBody deleteMethod = new HttpDeleteWithBody(url);
                    if (null != jsonParameter) {
                        deleteMethod.setEntity(new StringEntity(jsonParameter, "UTF-8"));
                    }
                    if (null != headerMap) {
                        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                            deleteMethod.setHeader(entry.getKey(), entry.getValue());
                        }
                    }
                    deleteMethod.setConfig(requestConfig);
                    return client.execute(deleteMethod);

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("clientExecuteError:{}", e);
        }
        return null;
    }
    
    public static CloseableHttpResponse getResponseByXmlParameter(CloseableHttpClient client, String url, String method,
			Map<String, String> cookieMap, String xmlParameter) {
		try {
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(30000).setConnectTimeout(30000).build();		
				HttpPost postMethod = new HttpPost(url);
				postMethod.addHeader("Content-Type","text/xml; charset=utf-8");
				ContentType TEXT_XML=ContentType.create("text/xml", Consts.UTF_8);
				if (null != xmlParameter) {
					postMethod.setEntity(new StringEntity(xmlParameter,TEXT_XML));
				}
				if (null != cookieMap) {
					for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
						postMethod.setHeader("Cookie", entry.getKey() + "="
								+ entry.getValue());
					}
				}
				postMethod.setConfig(requestConfig);
				return client.execute(postMethod);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("clientExecuteError:{}", e);
		}
		return null;
	}

    public static CloseableHttpResponse getHSFResponse(CloseableHttpClient client, String url, String method,
                                                       Map<String, String> cookieMap, String ArgTypes,
                                                       String ArgsObjects) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        String ArgsTypesreplace = ArgTypes.replace("\"", "").replace("[", "").replace("]", "");
        String ArgsObjectsreplace = ArgsObjects.substring(1, ArgsObjects.length() - 1);
        Object[] type = ArgsTypesreplace.split(",");
        String postDataStr1 = "[" + ArgsObjectsreplace + "]";
        postDataStr1 = postDataStr1.replaceAll("\\n", "");
        parameterMap.put("ArgsObjects", postDataStr1);
        parameterMap.put("ArgsTypes", JSONObject.toJSONString(type));
        try {
            switch (method) {
                case HttpConstant.HTTPGET:

                case HttpConstant.HTTPPOST:
                    HttpPost postMethod = new HttpPost(url);
                    postMethod.setHeader("Content-Type", "application/x-www-form-urlencoded");
                    if (null != parameterMap) {
                        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                        nvps.add(new BasicNameValuePair("ArgsTypes", parameterMap.get("ArgsTypes")));
                        nvps.add(new BasicNameValuePair("ArgsObjects", parameterMap.get("ArgsObjects")));
                        postMethod.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
                    }
                    if (null != cookieMap) {
                        for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
                            postMethod.setHeader("Cookie", entry.getKey() + "=" + entry.getValue());
                        }
                    }
                    postMethod.setConfig(requestConfig);
                    return client.execute(postMethod);

                case HttpConstant.HTTPPUT:

                case HttpConstant.HTTPDELETE:
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getResponseContent(CloseableHttpResponse response) {
        try {
            String result = IOUtils.toString(response.getEntity().getContent(), "UTF-8").toString();
            response.close();
            logger.info("httpReponse:{}", result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("getReponseError:{}", e);
        }
        return "getResponseError";
    }

    public static boolean downloadFile(CloseableHttpResponse response, String filePath) {
        try {
            FileUtils.createFile(filePath);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            OutputStream os = new FileOutputStream(new File(filePath));
            FileUtils.saveTo(is, os);
            response.close();
            logger.info("downloadSuccess:{}", filePath);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("downloadError:{}", e);
        }
        return false;
    }

    public static List<NameValuePair> generateNameValuePair(Map<String, String> properties) {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return nvps;
    }
}

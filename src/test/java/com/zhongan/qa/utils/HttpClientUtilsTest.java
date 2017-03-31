package com.zhongan.qa.utils; 

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;

/** 
 * @author jujinxin 
 * @version 创建时间：2017年3月20日 上午11:34:49 
 * 类说明 
 */
public class HttpClientUtilsTest {

	@Test
	public void test_post_json() {
		
		Map<String, String> headerMap = new HashMap<String, String> ();
		Map<String, String> parameterMap = new HashMap<String, String> ();
		headerMap.put("Content-Type", "application/json");
		headerMap.put("t", "ios");
		headerMap.put("v", "1.0.0");
		headerMap.put("sign", DigestUtils.md5Hex("app.zhongAn"+"15121024247"+"92993"+"app.zhongAn"));
		headerMap.put("deviceId", "92993");
		String str =Map2JsonString(headerMap);
		String url ="http://app.itest1.zhongan.com/za-clare/app/user/login";
		String method="POST";
		parameterMap.put("phoneNo", "15121024247");
		parameterMap.put("password", "Abcd1234");
		String jsonParameter="{\"phoneNo\":\"15121024247\",\"password\":\"Abcd1234\"}";
		//CloseableHttpResponse re =HttpClientUtils.testWithKeyValue(headerMap, null, url, method, parameterMap);
		CloseableHttpResponse re =HttpClientUtils.testWithJson(headerMap, null, url, method, jsonParameter);
		System.out.println(HttpResponseUtils.getResponseContent(re));
	}
	public static String Map2JsonString(Map map){
		JSONObject json=new JSONObject(map);
		return json.toJSONString();
	}
	//@Test
	public void test_post_hsf(){
		String url = "http://10.139.34.119:8086/com.zhongan.core.user.service.CusCompanyService:1.0.0/findListByCondition";
		String method = "POST";
		String ArgTypes="com.zhongan.core.user.dto.CusUserCompanyDTO";
		String ArgsObjects="[{\"userId\":4165025}]";
		CloseableHttpResponse response = HttpClientUtils.testHSF(null, null, url, method, ArgTypes, ArgsObjects);
		String result = HttpResponseUtils.getResponseContent(response);
		System.out.println(result);
	}
	//@Test
	public void test_path_parameter(){
		String url ="http://localhost:9090/loginbyget?name=admin&pwd=admin";
		String method="GET";
		CloseableHttpResponse response = HttpClientUtils.testWithKeyValue(null, null, url, method, null);
		String result = HttpResponseUtils.getResponseContent(response);
		System.out.println(result);
	}
	
	//@Test
	public void test_form_post(){
		String url = "http://localhost:9090/configuration/loginbypost";
		String method="POST";
		Map<String, String> parameterMap = new HashMap<String, String> ();
		parameterMap.put("name", "admin");
		parameterMap.put("pwd", "admin");
		CloseableHttpResponse response = HttpClientUtils.testWithKeyValue(null, null, url, method, parameterMap);
		String result = HttpResponseUtils.getResponseContent(response);
		System.out.println(result);
	}
	
	//@Test
	public void test_url_parameter_get(){
		String url = "http://localhost:9090/configuration/login/admin&admin1";
		String method="GET";		
		CloseableHttpResponse response = HttpClientUtils.testWithKeyValue(null, null, url, method, null);
		String result = HttpResponseUtils.getResponseContent(response);
		System.out.println(result);
	}
	
	//@Test
	public void test_xmlhttprequest(){
		Map<String, String> parameterMap = new HashMap<String, String> ();
		parameterMap.put("premium_params[EXPIRES][type]", "3");
		parameterMap.put("premium_params[INSURANCE][belong]", "2");
		parameterMap.put("premium_params[EXPIRES][key]", "EXPIRES");
		parameterMap.put("premium_params[AMOUNT][value]", "1000000");
		parameterMap.put("premium_params[INSURANCE][key]", "INSURANCE");
		parameterMap.put("premium_params[INSURANCE][type]", "3");
		parameterMap.put("premium_params[BIRTHDAY][type]", "3");
		parameterMap.put("premium_params[AMOUNT][key]", "AMOUNT");
		parameterMap.put("premium_params[EXPIRES][value]", "1年");
		parameterMap.put("premium_params[EXPIRES][belong]", "3");
		parameterMap.put("premium_params[BIRTHDAY][value]", "1935-03-22");
		parameterMap.put("assortmentId", "51252513");
		parameterMap.put("premium_params[BIRTHDAY][key]", "BIRTHDAY");
		parameterMap.put("premium_params[BIRTHDAY][belong]", "2");
		parameterMap.put("premium_params[INSURANCE][value]", "Y");
		parameterMap.put("premium_params[AMOUNT][belong]", "4");
		parameterMap.put("premium_params[AMOUNT][type]", "2");
		Map<String, String> headerMap = new HashMap<String, String> ();
		headerMap.put("X-Requested-With", "XMLHttpRequest");
		String str =Map2JsonString(headerMap);
		//headerMap.put("Content-Type", "text/x-www-form-urlencoded; charset=gbk");
		//headerMap.put("Content-Type", "text/data-form; charset=utf-8");
		String url = "http://mp.zhongan.com/health/iYunBao/common/apiGetPremium.html";
		String method="POST";
		CloseableHttpResponse response = HttpClientUtils.testWithKeyValue(headerMap, null, url, method, parameterMap);
		String result = HttpResponseUtils.getResponseContent(response);
		System.out.println(result);
	}

}
 
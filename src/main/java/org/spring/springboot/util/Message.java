package org.spring.springboot.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;


public class Message {
	
public  static String CheckMessage(String moblie) throws HttpException, IOException {
		JSONArray jsonArray = new JSONArray();
		String num = String.valueOf((int)((Math.random()*9+1)*100000));
		jsonArray.add(num);
		jsonArray.add("2");
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://api.zthysms.com/sendSms.do");
		post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");// 在头文件中设置转码
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
	    String str=sdf.format(new Date());
	    System.out.println(str);
		String password = MD5Util.getMd5(MD5Util.getMd5("h7wnDE")+str);
		System.out.println(password);
		NameValuePair[] data = { 
			new NameValuePair("username", "btxxhy"),
			new NameValuePair("tkey", str),
			new NameValuePair("password", password),
			new NameValuePair("mobile", moblie),
			new NameValuePair("content", "收到的验证为:"+num+"【上海睿旅】")
		};
		post.setRequestBody(data);

		client.executeMethod(post);
		Header[] headers = (Header[]) post.getResponseHeaders();
		int statusCode = post.getStatusCode();
		System.out.println("statusCode:" + statusCode);
		for (Header h : headers) {
			System.out.println(h.toString());
		}
		String result = new String(post.getResponseBodyAsString());
		System.out.println(result);
		post.releaseConnection();
		
      return num;
     
	}
	
	public static void main(String[] args) throws HttpException, IOException {
		//SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
	    //String str=sdf.format(new Date());
	    //System.out.println(MD5Utils.getMD5Str(MD5Utils.getMD5Str("h7wnDE")+str));
		CheckMessage("13962862067");
    }

}

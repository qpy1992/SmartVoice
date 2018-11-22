/*
 * File ： HttpPoster.java
 * 
 * Project: wcmn
 *
 * Modify Information:
 * =============================================================================
 *   Author          Date                      Description
 *   ------------ ---------- ---------------------------------------------------
 *   IBM          2008-10-8          Implement Programming Spec V1.0
 * ============================================================================= 
 */
package org.spring.springboot.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;




public class HttpPoster {
	public static String httpPostWithJSON(String url,String json) throws Exception {

        HttpPost httpPost = new HttpPost(url);
        CloseableHttpClient client = HttpClients.createDefault();
        String respContent = null;
        
        //json方式
        //StringEntity entity = new StringEntity(jsonParam.toString(),"utf-8");//解决中文乱码问题    
        httpPost.setHeader("Content-type", "application/json;charset=utf-8");
        httpPost.setEntity(new ByteArrayEntity("384f364e461b290cd2fcb7b0e393be9c".getBytes()));
        //httpPost.setHeader("X-AUTH-TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJzaGJ0a2oiLCJzdWIiOiJzaGJ0a2oiLCJpYXQiOjE1MzM4NjM5NTF9.ix1_rl9CP3m9tjhabAFMh-GTb81kOEnr3J6mtziqGc0");
        
    
//        表单方式
//      List<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>(); 
//      pairList.add(new BasicNameValuePair("name", "admin"));
//      pairList.add(new BasicNameValuePair("pass", "123456"));
//      httpPost.setEntity(new UrlEncodedFormEntity(pairList, "utf-8"));   
        
        
        HttpResponse resp = client.execute(httpPost);
        if(resp.getStatusLine().getStatusCode() == 200) {
            HttpEntity he = resp.getEntity();
            respContent = EntityUtils.toString(he,"UTF-8");
        }
        return respContent;
    }

	
	 public static boolean ping(String ipAddress) throws Exception {
	        int  timeOut =  3000 ;  //超时应该在3钞以上        
	        boolean status = InetAddress.getByName(ipAddress).isReachable(timeOut);    
	        // 当返回值是true时，说明host是可用的，false则不可。
	        return status;
	}
    
    public static void main(String[] args) throws Exception {
    	//String result = httpPostWithJSON("http://v2.api.guanyierp.com/rest/erp_open");
    	 //System.out.println(result);
        //String result = httpPostWithJSON("http://205.168.1.105:8080/smarthox/rest/zkRegisterController");
       // System.out.println(result);

        //Float f=1000f; 
        //System.out.println(Integer.toHexString(Float.floatToIntBits(f))); 
/*		String sequence_number16 = MathUtil.getHexString(1000);
		
		String start=sequence_number16.substring(0,2);
		String end=sequence_number16.substring(sequence_number16.length()-2);
		System.out.println(start+end);*/
    	//String payload = "1.e0200";
    	//payload = payload.substring(4,6);
    	//System.out.println(payload.replace(".", ""));


    	//boolean stat = isConnect("112.90.178.68", 8081);
    	//System.out.println(stat);



    }
    public static boolean isConnect(String host,int port){
		Socket socket = new Socket();
		try{
			socket.connect(new InetSocketAddress(host, port));
		}catch (IOException e) {
			System.out.println("fail");
			//e.printStackTrace();
			return false;
		}finally{
			try{
				socket.close();
			}catch (IOException e) {
				e.printStackTrace();
		  }
		}
		return true;
    }

}

package org.spring.springboot.util;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import net.sf.json.JSONObject;
 
/**
 * @author 马弦
 * @date 2017年10月23日 下午2:49
 * HttpClient工具类
 */
public class HttpUtil {
	
	private static Logger logger = Logger.getLogger(HttpUtil.class);
 
	/**
     * 发送 get请求
     */
    public static String doGet(String url,String token) {
    	token = replaceBlank(token);
		CloseableHttpClient client = HttpClients.createDefault();
		String html = "";

		HttpGet httpGet = new HttpGet(url);
		//设置了headers的参数，如果请求的对这里不要求的话，可以省略。
		httpGet.setHeader("X-AUTH-TOKEN", token);

		CloseableHttpResponse response = null;
		try {
			response = client.execute(httpGet);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		HttpEntity httpEntity = response.getEntity();
		if (httpEntity != null) {
			// 打印响应内容
			try {
				html = EntityUtils.toString(httpEntity, "UTF-8");
				client.close();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return html;
    
    }

    /**
     * 发送 get请求
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 * @throws ParseException 
     */
    public static String doGet(String url,String token,Map params) throws ParseException, UnsupportedEncodingException, IOException {
    	token = replaceBlank(token);
		CloseableHttpClient client = HttpClients.createDefault();
		String html = "";
		
        // 创建参数队列
        //设置参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>(); 
        for (Iterator iter = params.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String value = String.valueOf(params.get(name));
			nvps.add(new BasicNameValuePair(name, value));
			
			//System.out.println(name +"-"+value);
		}
		
        //参数转换为字符串
        String paramsStr = EntityUtils.toString(new UrlEncodedFormEntity(nvps, "UTF-8"));
        url = url + "?" + paramsStr;
        // 创建httpget.
        
		HttpGet httpGet = new HttpGet(url);
		//设置了headers的参数，如果请求的对这里不要求的话，可以省略。
		httpGet.setHeader("X-AUTH-TOKEN", token);

		CloseableHttpResponse response = null;
		try {
					
			response = client.execute(httpGet);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		HttpEntity httpEntity = response.getEntity();
		if (httpEntity != null) {
			// 打印响应内容
			try {
				html = EntityUtils.toString(httpEntity, "UTF-8");
				client.close();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return html;
    
    }

    
    
	public static String httpPostWithJSON(String url,JSONObject jsonParam,String token) throws Exception {
		token = replaceBlank(token);
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpClient client = HttpClients.createDefault();
        String respContent = null;
        
//        json方式

        StringEntity entity = new StringEntity(jsonParam.toString(),"utf-8");//解决中文乱码问题    
        entity.setContentEncoding("UTF-8");    
        entity.setContentType("application/json");    
        httpPost.setEntity(entity);
        httpPost.setHeader("X-AUTH-TOKEN", token);
        System.out.println();
        
    
//        表单方式
//        List<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>(); 
//        pairList.add(new BasicNameValuePair("name", "admin"));
//        pairList.add(new BasicNameValuePair("pass", "123456"));
//        httpPost.setEntity(new UrlEncodedFormEntity(pairList, "utf-8"));   
        
        
        HttpResponse resp = client.execute(httpPost);
        if(resp.getStatusLine().getStatusCode() == 200) {
            HttpEntity he = resp.getEntity();
            respContent = EntityUtils.toString(he,"UTF-8");
        }
        return respContent;
    }
	
	/**
	 * post请求(用于key-value格式的参数)
	 * @param url
	 * @param params
	 * @return
	 */
	public static String doPost(String url, Map params){
		
		BufferedReader in = null;  
        try {  
            // 定义HttpClient  
            HttpClient client = new DefaultHttpClient();  
            // 实例化HTTP方法  
            HttpPost request = new HttpPost();  
            request.setURI(new URI(url));

            //设置参数
            List<NameValuePair> nvps = new ArrayList<NameValuePair>(); 
            for (Iterator iter = params.keySet().iterator(); iter.hasNext();) {
    			String name = (String) iter.next();
    			String value = String.valueOf(params.get(name));
    			nvps.add(new BasicNameValuePair(name, value));
    			
    			//System.out.println(name +"-"+value);
    		}
            request.setEntity(new UrlEncodedFormEntity(nvps,HTTP.UTF_8));
            
            HttpResponse response = client.execute(request);  
            int code = response.getStatusLine().getStatusCode();
            if(code == 200){	//请求成功
            	in = new BufferedReader(new InputStreamReader(response.getEntity()  
                        .getContent(),"utf-8"));
                StringBuffer sb = new StringBuffer("");  
                String line = "";  
                String NL = System.getProperty("line.separator");  
                while ((line = in.readLine()) != null) {  
                    sb.append(line + NL);  
                }
                
                in.close();  
                
                return sb.toString();
            }
            else{	//
            	System.out.println("状态码：" + code);
            	return null;
            }
        }
        catch(Exception e){
        	e.printStackTrace();
        	
        	return null;
        }
	}
	
	
	public static String getToken(){
    	String urlToken = StringUtil.host+"rest/tokens";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("username", StringUtil.username);
		map.put("password", StringUtil.password);
    	String token = doPost(urlToken,map);
    	return token;
	}
	
	public static JSONObject getPublish(String topicPublish,String message) throws ParseException, UnsupportedEncodingException, IOException{
		
		String token = getToken();
		
    	String urlPublish = StringUtil.host+"rest/zkSecondControlDeviceController/publishMqttp";

    	Map<String, Object> mapPublish = new HashMap<String, Object>();
		mapPublish.put("topic", topicPublish);
		mapPublish.put("qos", 0);
		mapPublish.put("message", message);
		
    	String resultPublish = HttpUtil.doGet(urlPublish,token,mapPublish);
    	JSONObject jsonObjectPublish = JSONObject.fromObject(json2map(resultPublish));
    	return jsonObjectPublish;
	}
	
	public static JSONObject getSubscribe(String topicSubscribe) throws ParseException, UnsupportedEncodingException, IOException{
		
		String token = getToken();
		
    	String urlSubscribe = StringUtil.host+"rest/zkSecondControlDeviceController/subscribeMqttp";

		Map<String, Object> mapSubscribe = new HashMap<String, Object>();
		mapSubscribe.put("topic", topicSubscribe);
		
    	String resultSubscribe = HttpUtil.doGet(urlSubscribe,token,mapSubscribe);
    	JSONObject jsonObjectSubscribe = JSONObject.fromObject(json2map(resultSubscribe));
    	return jsonObjectSubscribe;
	}
	
	
	
	public static String getPayload(String topic,String sequence_number) throws ParseException, UnsupportedEncodingException, IOException{
		String token = getToken();
    	String urlConfig = StringUtil.host+"rest/zkiotTempController/payload";
    	
		Map<String, Object> mapConfig = new HashMap<String, Object>();
		mapConfig.put("topic", topic);			
		mapConfig.put("sequence_number", sequence_number);	
		
    	String result = HttpUtil.doGet(urlConfig,token,mapConfig);
    	return result;
	}
	
	
	public static void main(String[] args) throws Exception {

/*		String s = "41e3b16e";
		Float value = Float.intBitsToFloat(Integer.valueOf(s.trim(), 16));
		System.out.println(value);	
		
*///56, 53, -28, 65
/*		byte[] strings = {65,-28,53,56};
		//System.out.println(ByteArray2Float(bb));
		
		//String[] strings = { "ramer", "jelly", "bean", "cake" };

	    for (int start = 0, end = strings.length - 1; start < end; start++, end--) {
	    	byte temp = strings[end];
	        strings[end] = strings[start];
	        strings[start] = temp;
	    }*/
		
		
/*	  	  String url = StringUtil.host+"rest/zkHairIndicatorsController";
		  String token = HttpUtil.getToken();*/
			
		 
		  
/*	      JSONObject jsonParam = new JSONObject();  
	      
	      jsonParam.put("id", "string");
	      jsonParam.put("fdeviceid", "string");
	      jsonParam.put("faddtime", "string");
	      
	      jsonParam.put("temperature", 0);
	      jsonParam.put("humidity", 0);
	      jsonParam.put("pm25", 0);
	      jsonParam.put("pm100", 0);
	      jsonParam.put("formaldehyde", 0);
	      jsonParam.put("voc", 0);
	      jsonParam.put("co", 0);
	      jsonParam.put("co2", 0);
	
			
		  String result =  HttpUtil.httpPostWithJSON(url, jsonParam, token);
		  System.out.println(result);*/

	    //String s = "00123456";
		//System.out.println(reverse(s));
		
		String token = HttpUtil.getToken();
    	String url = StringUtil.host+"rest/zkRegisterController";
    	
    	JSONObject userInfo = new JSONObject();
    	userInfo.put("ftelephone", "1223232");
    	
      	Date now = new Date(); 
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	//可以方便地修改日期格式
    	String hehe = dateFormat.format( now ); 
    	userInfo.put("createDate", hehe);
		
    	HttpUtil.httpPostWithJSON(url,userInfo,token);

		
		
    }
	
	  public static byte[] reverse(byte[] parm){
			for (int start = 0, end = parm.length - 1; start < end; start++, end--) {
				byte temp = parm[end];
				parm[end] = parm[start];
				parm[start] = temp;
			}  
			return parm;
	}
	
	  public static String reverse(String str) {
		  String zStr = "";
		  for(int i=0;i<str.length()/2;i++)
		  {
		  	 String temp = str.substring(i*2,i*2+2);
		  	 zStr = temp + zStr;
		  }
		  return zStr;
	}
	
	/** 
     * 将byte[]转成float
     * @param data
     * @return float
     */ 
    public static float ByteArray2Float(byte[] data) {
        if (data == null || data.length < 4) {
            return -1234.0f;
        }
        return ByteBuffer.wrap(data).getFloat();
    }



	  public static String replaceBlank(String str) {
	      String dest = "";
	      if (str!=null) {
	          Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	          Matcher m = p.matcher(str);
	          dest = m.replaceAll("");
	      }
	      return dest;
	}
	  
		
	public static Map<String, Object> json2map(String str_json) {
	        Map<String, Object> res = null;
	        try {
	            Gson gson = new Gson();
	            res = gson.fromJson(str_json, new TypeToken<Map<String, Object>>() {
	            }.getType());
	        } catch (JsonSyntaxException e) {
	        }
	        return res;
	    }
	
}


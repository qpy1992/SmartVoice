package org.spring.springboot.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.spring.springboot.service.UserService;
import org.spring.springboot.util.HttpUtil;
import org.spring.springboot.util.MD5Util;
import org.spring.springboot.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSON;
import net.sf.json.JSONObject;

@Configuration
@Controller
@RequestMapping("/")
public class UserCtrl{


  //根据手机号注册信息

	@Autowired
	private UserService userService;	
	Map<String, Object> map = new HashMap<String, Object>();
	
	//@Autowired  
	//private PlatformTransactionManager transactionManager;  	

	
	
	@ResponseBody
	@ApiOperation(value="微信获取用户信息", notes="")
	@RequestMapping(value = "userInfo",method= RequestMethod.GET)
	@ApiImplicitParams({
		 @ApiImplicitParam(paramType="query", name = "open_id", value = "open_id", required = true, dataType = "String")
	})
	public JSON userInfo(@RequestParam String open_id) throws UnsupportedEncodingException {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject jsonMessage = new JSONObject();
		map.put("open_id", open_id);
		List<Map<String, Object>> userList = userService.list(map);
		//根据电话号码进行判断	
		if (userList.size()!= 0) {
			Map<String, Object> userInfo = userList.get(0);
			
			String nick_name = userInfo.get("wx_name").toString();
			nick_name = new String(Base64.decodeBase64(nick_name),"UTF-8");
			userInfo.put("wx_name", nick_name);
			
			jsonMessage.put("userInfo",userInfo);
			jsonMessage.put("message","用户查询成功");
			jsonMessage.put("code",1);
			
		} else {
			jsonMessage.put("message","用户还未绑定手机,自动跳转手机绑定页面");
			jsonMessage.put("code",0);
		}
		return jsonMessage;
	}
	
	
	@ResponseBody
	@ApiOperation(value="小程序端用户注册", notes="")
	@RequestMapping(value = "userInsert",method= RequestMethod.POST)
	@ApiImplicitParams({
		 @ApiImplicitParam(paramType="query", name = "nickname", value = "昵称", required = true, dataType = "String"),
		 @ApiImplicitParam(paramType="query", name = "head_img", value = "头像", required = true, dataType = "String"),
		 @ApiImplicitParam(paramType="query", name = "open_id", value = "open_id", required = true, dataType = "String"),	
		 @ApiImplicitParam(paramType="query", name = "mobile", value = "手机", required = true, dataType = "String")	

	})
	public JSON userInsert(@RequestParam String head_img,@RequestParam String open_id,@RequestParam String mobile,@RequestParam String nickname) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject jsonMessage = new JSONObject();
		int isUrl=0;
		map.put("open_id", open_id);
		nickname =Base64.encodeBase64String(nickname.getBytes("UTF-8"));
		map.put("nickname", nickname);
		map.put("head_img", head_img);
		map.put("mobile", mobile);
		//根据电话号码进行判断	
		if (userService.list(map).size()!= 0) {
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>" );
			System.out.println("用户存在，则update" );
			isUrl= userService.update(map);
			
			if(isUrl==0)
			{
				jsonMessage.put("message","用户信息更新失败");
				jsonMessage.put("code",0);
		    }
		    else
		    {
				jsonMessage.put("message","用户信息更新成功");
				jsonMessage.put("code",1);
		    }
			
		} else {
			int flag = userService.count(map);
			if(flag>0)
			{
				userService.updateWxInfo(map);
			}
			else
			{
				//System.out.println("用户不存在，则insert" );
				userService.insert(map);
				
				String token = HttpUtil.getToken();
		    	String url = StringUtil.host+"rest/zkRegisterController";
		    	
		    	JSONObject userInfo = new JSONObject();
		    	userInfo.put("ftelephone", mobile);
		    	
		      	Date now = new Date(); 
		    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    	//可以方便地修改日期格式
		    	String hehe = dateFormat.format( now ); 
		    	userInfo.put("createDate", hehe);
				
		    	HttpUtil.httpPostWithJSON(url,userInfo,token);
			}
			jsonMessage.put("message","用户信息录入成功");
			jsonMessage.put("code",1);
			jsonMessage.put("id",map.get("id").toString());

		}
			
		return jsonMessage;
	}
	
	
	@ResponseBody
	@ApiOperation(value="检测用户注册状态", notes="")
	@RequestMapping(value = "checkUserPC",method= RequestMethod.POST)
	@ApiImplicitParams({
		 @ApiImplicitParam(paramType="query", name = "mobile", value = "手机", required = true, dataType = "String")
	})
	public JSON checkUserPC(@RequestParam String mobile) throws UnsupportedEncodingException {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject jsonMessage = new JSONObject();
		map.put("mobile", mobile);
		//根据电话号码进行判断	
		if (userService.count(map)> 0) {
			Map<String, Object> memberInfo = userService.memberInfo(map);
			if(memberInfo.get("fpassword")==null)
			{
				jsonMessage.put("message","用户还小程序已经注册,但PC端还未注册跳转注册界面,调用backFpassword接口，重置密码 ");
				jsonMessage.put("code",2);
			}
			else
			{
				jsonMessage.put("message","检测通过，用户可以登录");
				jsonMessage.put("code",1);
			}
			
		} else {
			jsonMessage.put("message","用户还未注册，跳转注册界面");
			jsonMessage.put("code",0);
		}
			
		return jsonMessage;
	}
	
	@ResponseBody
	@ApiOperation(value="用户登录", notes="")
	@RequestMapping(value = "userLoginPC",method= RequestMethod.POST)
	@ApiImplicitParams({
		 @ApiImplicitParam(paramType="query", name = "mobile", value = "手机", required = true, dataType = "String"),
		 @ApiImplicitParam(paramType="query", name = "fpassword", value = "密码", required = true, dataType = "String")
	})
	public JSON userLoginPC(@RequestParam String mobile,@RequestParam String fpassword) throws UnsupportedEncodingException {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject jsonMessage = new JSONObject();
		map.put("mobile", mobile);
		fpassword = MD5Util.getMd5(fpassword);
		map.put("fpassword", fpassword);
		
		Map<String, Object> memberInfo = userService.login(map);
		if(memberInfo==null)
		{
			jsonMessage.put("message","手机或者密码错误");
			jsonMessage.put("code",0);
		}
		else
		{
			String fstatus = memberInfo.get("fstatus").toString();
			
			if(fstatus.equals("1"))
			{
				jsonMessage.put("message","用户禁用，无法登陆");
				jsonMessage.put("code",2);
				return jsonMessage;
			}
			jsonMessage.put("message","用户登录成功");
			jsonMessage.put("memberInfo",memberInfo);
			jsonMessage.put("code",1);
		}

		//根据电话号码进行判断	
		return jsonMessage;
	}
	
	
	@ResponseBody
	@ApiOperation(value="用户注册", notes="")
	@RequestMapping(value = "userInsertPC",method= RequestMethod.POST)
	@ApiImplicitParams({
		 @ApiImplicitParam(paramType="query", name = "mobile", value = "手机", required = true, dataType = "String"),
		 @ApiImplicitParam(paramType="query", name = "fpassword", value = "密码", required = true, dataType = "String")
	})
	public JSON userInsertPC(@RequestParam String mobile,@RequestParam String fpassword) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject jsonMessage = new JSONObject();
		map.put("mobile", mobile);
		fpassword = MD5Util.getMd5(fpassword);
		map.put("fpassword", fpassword);
		String head_pic = StringUtil.head_pic;
		map.put("head_pic", head_pic);
		//根据电话号码进行判断	
		if (userService.count(map)==0) {
			userService.insertPC(map);
			
			String token = HttpUtil.getToken();
	    	String url = StringUtil.host+"rest/zkRegisterController";
	    	
	    	JSONObject userInfo = new JSONObject();
	    	userInfo.put("ftelephone", mobile);
	    	
	      	Date now = new Date(); 
	    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	//可以方便地修改日期格式
	    	String hehe = dateFormat.format( now ); 
	    	userInfo.put("createDate", hehe);
			
	    	HttpUtil.httpPostWithJSON(url,userInfo,token);
			
			jsonMessage.put("message","用户信息录入成功");
			jsonMessage.put("code",1);
			jsonMessage.put("id",map.get("id").toString());
		}
		else{
			jsonMessage.put("message","用户已经注册过，不能重复注册");
			jsonMessage.put("code",0);
		}
		return jsonMessage;
	}
	
	@ResponseBody
	@ApiOperation(value="用户重置密码(记得原来的密码)", notes="")
	@RequestMapping(value = "backFpassword",method= RequestMethod.POST)
	@ApiImplicitParams({
		 @ApiImplicitParam(paramType="query", name = "mobile", value = "手机", required = true, dataType = "String"),
		 @ApiImplicitParam(paramType="query", name = "fpasswordOld", value = "旧密码", required = true, dataType = "String"),
		 @ApiImplicitParam(paramType="query", name = "fpasswordNew", value = "新密码", required = true, dataType = "String")
	})
	public JSON backFpassword(@RequestParam String mobile,@RequestParam String fpasswordOld,@RequestParam String fpasswordNew) throws UnsupportedEncodingException {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject jsonMessage = new JSONObject();
		map.put("mobile", mobile);
		fpasswordOld = MD5Util.getMd5(fpasswordOld);
		map.put("fpassword", fpasswordOld);
		Map<String, Object> memberInfo = userService.login(map);
		if(memberInfo==null)
		{
			jsonMessage.put("message","手机或者密码错误");
			jsonMessage.put("code",0);
			return jsonMessage;
		}
		
		fpasswordNew = MD5Util.getMd5(fpasswordNew);
		map.put("fpassword", fpasswordNew);
		//根据电话号码进行判断	

		userService.updateFpassword(map);
		jsonMessage.put("message","用户密码更新成功");
		jsonMessage.put("code",1);
		//jsonMessage.put("id",map.get("id").toString());

		return jsonMessage;
	}
	
	
	@ResponseBody
	@ApiOperation(value="用户重置密码(忘记原来的密码)", notes="")
	@RequestMapping(value = "backFpasswordByMobile",method= RequestMethod.POST)
	@ApiImplicitParams({
		 @ApiImplicitParam(paramType="query", name = "mobile", value = "手机", required = true, dataType = "String"),
		 @ApiImplicitParam(paramType="query", name = "fpasswordNew", value = "新密码", required = true, dataType = "String")
	})
	public JSON backFpasswordByMobile(@RequestParam String mobile,@RequestParam String fpasswordNew) throws UnsupportedEncodingException {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject jsonMessage = new JSONObject();
		map.put("mobile", mobile);
		
		fpasswordNew = MD5Util.getMd5(fpasswordNew);
		map.put("fpassword", fpasswordNew);
		//根据电话号码进行判断	

		userService.updateFpassword(map);
		jsonMessage.put("message","用户密码更新成功");
		jsonMessage.put("code",1);
		//jsonMessage.put("id",map.get("id").toString());

		return jsonMessage;
	}
	
	
	@ResponseBody
	@ApiOperation(value="更新用户PC", notes="")
	@RequestMapping(value = "updatePC",method= RequestMethod.POST)
	@ApiImplicitParams({
		 @ApiImplicitParam(paramType="query", name = "mobile", value = "手机", required = true, dataType = "String"),
		 @ApiImplicitParam(paramType="query", name = "head_pic", value = "头像", required = true, dataType = "String"),
		 @ApiImplicitParam(paramType="query", name = "fnickname", value = "昵称", required = true, dataType = "String")
	})
	public JSON updatePC(@RequestParam String mobile,@RequestParam String head_pic,@RequestParam String fnickname) throws UnsupportedEncodingException {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject jsonMessage = new JSONObject();
		map.put("mobile", mobile);
		map.put("head_pic", head_pic);
		map.put("fnickname", fnickname);
		//根据电话号码进行判断	

		int flag = userService.updatePC(map);
		if(flag>0)
		{
			jsonMessage.put("message","用户更新成功");
			jsonMessage.put("code",1);
		}
		else
		{
			jsonMessage.put("message","手机号码不存在,用户更新失败");
			jsonMessage.put("code",0);
		}
		//jsonMessage.put("id",map.get("id").toString());
		return jsonMessage;
	}	


	@ResponseBody
	@ApiOperation(value="手机验证码是否正确", notes="num:手机验证码")
    @ApiImplicitParam(paramType="query", name = "mobile", value = "手机号", required = true, dataType = "String")
	@RequestMapping(value = "checkMessage",method= RequestMethod.GET)
	public JSON checkMessage(@RequestParam String mobile) throws IOException {
		JSONObject jsonMessage = new JSONObject();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mobile", mobile);
/*		int flag = userService.count(map);
		if(flag>0)
		{
			jsonMessage.put("message","该号码已经注册过，不能重复注册");
			jsonMessage.put("code",0);
			return jsonMessage;
		}*/
		//String num = Message.CheckMessage(mobile);
		String num = "123456";
		jsonMessage.put("validateCode", num);
		jsonMessage.put("message","短信获取成功");
		jsonMessage.put("code",1);
		return jsonMessage;
	}
	
	@ResponseBody
	@ApiOperation(value="获取用户信息", notes="num:手机验证码")
    @ApiImplicitParam(paramType="query", name = "registerid", value = "用户id", required = true, dataType = "String")
	@RequestMapping(value = "registerInfo",method= RequestMethod.GET)
	public JSON registerInfo(@RequestParam String registerid) throws IOException {
		JSONObject jsonMessage = new JSONObject();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("registerid", registerid);
		Map<String, Object> registerInfo  = userService.registerInfo(map);

		jsonMessage.put("registerInfo", registerInfo);
		jsonMessage.put("message","用户信息获取成功");
		jsonMessage.put("code",1);
		return jsonMessage;
	}


	@ResponseBody
	@ApiOperation(value="省份列表", notes="省份列表")
	@RequestMapping(value = "provinceList",method= RequestMethod.GET)
	public JSON provinceList() {
		JSONObject jsonMessage = new JSONObject();
		List<Map<String,Object>> provinceList = userService.serProvinceList();
		jsonMessage.put("provinceList", provinceList);
		jsonMessage.put("message","省份查询成功");
		jsonMessage.put("code",1);
		return jsonMessage;
	}
	@ResponseBody
	@ApiOperation(value="市列表", notes="市列表")
	@RequestMapping(value = "cityList",method= RequestMethod.GET)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType="query", name = "provinceId", value = "省份id", required = true, dataType = "String")
	})
	public JSON cityList(@RequestParam String provinceId ) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject jsonMessage = new JSONObject();
		map.put("provinceId", provinceId);
		List<Map<String,Object>> cityList = userService.serCityList(map);
		jsonMessage.put("cityList", cityList);
		jsonMessage.put("message","市查询成功");
		jsonMessage.put("code",1);
		return jsonMessage;
	}


	@ResponseBody
	@ApiOperation(value="区列表", notes="区列表")
	@RequestMapping(value = "districtList",method= RequestMethod.GET)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType="query", name = "cityId", value = "市id", required = true, dataType = "String")
	})
	public JSON districtList(@RequestParam String cityId ) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject jsonMessage = new JSONObject();
		map.put("cityId", cityId);
		List<Map<String,Object>> districtList = userService.serDistrictList(map);
		jsonMessage.put("districtList", districtList);
		jsonMessage.put("message","地区查询成功");
		jsonMessage.put("code",1);
		return jsonMessage;
	}

	
	public String GetUtf8(String  str){     
	    try {
	    	str = new String(str.getBytes("iso8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	    return str;
	} 

}

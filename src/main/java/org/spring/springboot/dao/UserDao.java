package org.spring.springboot.dao;

import java.util.List;
import java.util.Map;






public interface UserDao {
	
	public int insert(Map<String,Object> map);
	public int insertPC(Map<String,Object> map);
	public int update(Map<String,Object> map);
	public int updateWxInfo(Map<String,Object> map);
	public int updatePC(Map<String,Object> map);
	public int updateFpassword(Map<String,Object> map);

	public List<Map<String,Object>> list(Map<String,Object> map); 
	
	public int count(Map<String,Object> map);
	
	public Map<String,Object> memberInfo(Map<String,Object> map);
	public Map<String,Object> login(Map<String,Object> map);

	public List<Map<String,Object>> serProvinceList();

	public List<Map<String,Object>> serCityList(Map<String,Object> map);

	public List<Map<String,Object>> serDistrictList(Map<String,Object> map);
	
	public Map<String,Object> registerInfo(Map<String,Object> map);
}

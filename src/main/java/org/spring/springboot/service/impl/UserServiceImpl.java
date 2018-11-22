package org.spring.springboot.service.impl;

import java.util.List;
import java.util.Map;

import org.spring.springboot.dao.UserDao;
import org.spring.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private UserDao userDao;

	@Override
	public int insert(Map<String,Object> map ){
		return userDao.insert(map);
	}
	@Override
	public int insertPC(Map<String,Object> map ){
		return userDao.insertPC(map);
	}
	

	@Override
	public int update(Map<String,Object> map ){
		return userDao.update(map);
	}
	@Override
	public int updatePC(Map<String,Object> map ){
		return userDao.updatePC(map);
	}
	@Override
	public int updateWxInfo(Map<String,Object> map ){
		return userDao.updateWxInfo(map);
	}
	@Override
	public int updateFpassword(Map<String,Object> map ){
		return userDao.updateFpassword(map);
	}
	@Override
	public List<Map<String,Object>> list(Map<String,Object> map){
		return userDao.list(map);
	}
	@Override
	public int count(Map<String,Object> map)
	{
		return userDao.count(map);
	}
	@Override
	public Map<String,Object>  memberInfo(Map<String,Object> map)
	{
		return userDao.memberInfo(map);
	}
	@Override
	public Map<String,Object>  login(Map<String,Object> map)
	{
		return userDao.login(map);
	}
	public List<Map<String,Object>> serProvinceList()
	{
		return userDao.serProvinceList();
	}

	public List<Map<String,Object>> serCityList(Map<String,Object> map)
	{
		return userDao.serCityList(map);
	}

	public List<Map<String,Object>> serDistrictList(Map<String,Object> map)
	{
		return userDao.serDistrictList(map);
	}

	public Map<String,Object> registerInfo(Map<String,Object> map)
	{
		return userDao.registerInfo(map);
	}
}

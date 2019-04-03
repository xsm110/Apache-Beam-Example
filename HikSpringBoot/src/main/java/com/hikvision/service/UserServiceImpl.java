package com.hikvision.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.hikvision.dao.UserDao;
import com.hikvision.entity.User;

@Component
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;

	@Override
	public User selectByPrimaryKey(int userId) {
		return userDao.selectByPrimaryKey(userId);
	}
	
	@Override
	public int  insert(User user)
	{
		return userDao.insert(user);
	}
    
	  /**
     * 不分页查询全部 数据
     * @return 查询到的列表数据 不分页
     */
    @Override
    public List<User> list()
    {
      return userDao.list();
    }
    
    /**
     * 根据主键删除 数据
     * @param id 主键id
     * @return 删除数据 影响行数
     */
    @Override
    public int delete()
    {
      return userDao.delete();
    }
}

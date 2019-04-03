package com.hikvision.service;

import java.util.List;

import com.hikvision.entity.User;

public interface UserService {
	public User selectByPrimaryKey(int userId);
	public int  insert(User user);
	  /**
     * 不分页查询全部 数据
     * @return 查询到的列表数据 不分页
     */
    public List<User> list();

    public int delete();
}

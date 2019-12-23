package com.framework.webClient.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.framework.webClient.entity.User;
@Mapper
public interface WebClientConfigMapper {
	 List<User>  selectByName(String name);
}

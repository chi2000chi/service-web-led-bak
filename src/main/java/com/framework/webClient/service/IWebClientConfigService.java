package com.framework.webClient.service;

import java.util.List;

import com.framework.webClient.entity.User;

public interface IWebClientConfigService {
	List<User> selectByName(String name);
}

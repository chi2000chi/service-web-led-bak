package com.framework.webClient.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.framework.webClient.dao.WebClientConfigMapper;
import com.framework.webClient.entity.User;

@Service
public class WebClientConfigServiceImpl implements IWebClientConfigService {
	@Autowired
	private WebClientConfigMapper webClientConfigDao;
	@Override
	public List<User> selectByName(String name) {
		return webClientConfigDao.selectByName(name);
	}
}



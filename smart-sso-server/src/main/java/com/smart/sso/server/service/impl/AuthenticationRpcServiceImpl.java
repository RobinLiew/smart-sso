package com.smart.sso.server.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.smart.sso.client.dto.RpcPermissionDto;
import com.smart.sso.client.dto.RpcUserDto;
import com.smart.sso.client.rpc.AuthenticationRpcService;
import com.smart.sso.server.common.TokenManager;
import com.smart.sso.server.dto.LoginUserDto;
import com.smart.sso.server.service.PermissionService;

@Service("authenticationRpcService")
public class AuthenticationRpcServiceImpl implements AuthenticationRpcService {

	@Autowired
	private PermissionService permissionService;
	@Autowired
	private TokenManager tokenManager;

	@Override
	public boolean validate(String token) {
		return tokenManager.validate(token) != null;
	}
	
	@Override
	public RpcUserDto selectUser(String token) {
		return tokenManager.validate(token);
	}
	
	@Override
	public List<RpcPermissionDto> selectPermissionList(String token, String appCode) {
		if (StringUtils.isEmpty(token)) {
			return permissionService.selectListByUserId(appCode, null);
		}
		else {
			LoginUserDto user = tokenManager.validate(token);
			if (user == null) {
				return Collections.emptyList();
			}
			return permissionService.selectListByUserId(appCode, user.getId());
		}
	}
}

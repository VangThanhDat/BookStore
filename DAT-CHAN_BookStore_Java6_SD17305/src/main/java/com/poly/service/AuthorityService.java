package com.poly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.bean.Authority;
import com.poly.bean.Role;
import com.poly.bean.User;
import com.poly.repository.AuthorityRepository;

@Service
public class AuthorityService {
	@Autowired
	AuthorityRepository dao;

	@Autowired
	RoleService roleService;

	public Authority create(Authority entity) {
		return dao.saveAndFlush(entity);
	}

	public Authority createUser(User user) {
		Role userRole = roleService.getRoleById("USER");
		Authority authority = new Authority();
		authority.setUser(user);
		authority.setRole(userRole);
		return dao.saveAndFlush(authority);
	}

	public Authority createAdmin(User user) {
		Role userRole = roleService.getRoleById("ADMIN");
		Authority authority = new Authority();
		authority.setUser(user);
		authority.setRole(userRole);
		return dao.saveAndFlush(authority);
	}

}

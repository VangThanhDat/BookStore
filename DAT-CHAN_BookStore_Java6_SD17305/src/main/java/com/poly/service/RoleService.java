package com.poly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.bean.Role;
import com.poly.repository.RoleRepository;

@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;

    public Role getRoleById(String ID) {
        return roleRepository.findById(ID).get();
    }
}

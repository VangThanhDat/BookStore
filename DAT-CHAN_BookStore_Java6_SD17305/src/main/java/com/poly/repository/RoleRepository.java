package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.bean.Role;

public interface RoleRepository extends JpaRepository<Role, String> {

}

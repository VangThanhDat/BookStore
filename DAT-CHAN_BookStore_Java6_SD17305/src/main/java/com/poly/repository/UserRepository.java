package com.poly.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.poly.bean.User;

public interface UserRepository extends JpaRepository<User, String> {

	@Query("SELECT u.email FROM User u")
	List<String> getAllEmails();

	@Query("SELECT u.phone FROM User u")
	List<String> getAllPhones();

	@Query("SELECT u FROM User u WHERE u.id LIKE ?1 OR u.phone LIKE ?1 OR u.fullname LIKE ?1 OR u.email LIKE ?1")
	Page<User> findUserByNamePage(String name, Pageable pageable);

	@Query("SELECT a.role.id FROM Authority a WHERE a.user.id = :id")
	List<String> findRoleIdsByUsername(String id);
}

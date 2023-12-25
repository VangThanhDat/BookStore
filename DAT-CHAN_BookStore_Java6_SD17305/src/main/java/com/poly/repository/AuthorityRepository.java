package com.poly.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.poly.bean.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

	@Query("SELECT a FROM Authority a WHERE a.user.id =?1")
	Page<Authority> findByUsername(Pageable pageable);

}

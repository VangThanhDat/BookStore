package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.poly.bean.Cart;

public interface  CartRepository extends JpaRepository<Cart, Long> {
	
	@Query("SELECT c FROM Cart c WHERE c.user.id =?1")
	List<Cart> findByUserId(String userId);
	
	@Query("SELECT c FROM Cart c WHERE c.user.id =?1 and c.product.id = ?2")
	Cart findByUserIdAndProductId(String userId, String productId);
	
	@Query("SELECT COUNT(c) FROM Cart c  WHERE c.user.id = ?1")
    Integer countProductsByUserId(String userId);
	
	@Query("SELECT c.product.price*c.quantityPurchased FROM Cart c  WHERE c.user.id = ?1")
    Integer totalMoneyPay(String userId);

	@Query("DELETE FROM Cart c WHERE c.product.id = ?1")
	@Modifying
	@Transactional
    Object deleteByProductId(String productId);
	
	@Query("DELETE FROM Cart c WHERE c.user.id = ?1")
	@Modifying
	@Transactional
    Object deleteAllByUserId(String userId);
}

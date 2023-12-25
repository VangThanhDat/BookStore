package com.poly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.bean.Cart;
import com.poly.repository.CartRepository;

@Service
public class CartService {

	@Autowired
	CartRepository dao;

	public Cart findById(Long id){
		return dao.getById(id);
	}
	public List<Cart> findByUsername(String username) {
		return dao.findByUserId(username);
	}

	public Cart addProduct(Cart entity) {
		return dao.saveAndFlush(entity);
	}

	public Object deleteByIdProduct(String productId) {
		return dao.deleteByProductId(productId);
	}

	public Cart findByUserIdAndProductId(String userId, String productId) {
		return dao.findByUserIdAndProductId(userId, productId);
	}

	public Object deleteAllByIdUser(String userId) {
		return dao.deleteAllByUserId(userId);
	}

	public Cart updateItem(Cart entity) {
		return dao.saveAndFlush(entity);
	}

	public Integer countProductCart(String username) {
		return dao.countProductsByUserId(username);
	}

	public Integer totalMoneyPay(String username) {
		return dao.totalMoneyPay(username);
	}

	public void deleteCart(Cart cart) {
		dao.delete(cart);
	}
}

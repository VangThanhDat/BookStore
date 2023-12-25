package com.poly.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.poly.bean.Category;
import com.poly.bean.Product;
import com.poly.repository.CategoryRepository;

@Service
public class CategorieService {
	@Autowired
	CategoryRepository cdao;

	public List<Category> findAll() {
		return cdao.findAll();
	}

	public List<Product> getProductsByCategory(String categoryId) {
		Optional<Category> category = cdao.findById(categoryId);
		if (category.isPresent()) {
			return category.get().getProducts();
		}
		return new ArrayList<>();
		// trả về null hoặc thông báo lỗi
	}

	// Begin (Dat) Category Test
	public Category save(Category category) {
		return cdao.save(category);
	}

	public Category update(Category category) {
		return cdao.saveAndFlush(category);
	}

	public void delete(Category category) {
		cdao.delete(category);
	}
	public Page<Category> findAll1(Pageable pageable){
		return cdao.findAll1(pageable);
	}
	// End (Dat) Category Test

}

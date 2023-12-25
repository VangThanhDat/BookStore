package com.poly.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.bean.Category;
import com.poly.bean.Product;
import com.poly.service.CategorieService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class CategoryRestController {
	@Autowired
	CategorieService categoryService;
	
	@GetMapping("/category")
	public ResponseEntity<List<Category>> category(){
		if (categoryService.findAll().size() > 0) {
			return ResponseEntity.ok(categoryService.findAll());
		} else {
			return ResponseEntity.noContent().build();
		}
	}
	
	@GetMapping("/category/{categoryId}/products")
	public ResponseEntity<List<Product>> productsByCategory(@PathVariable String categoryId){
		List<Product> product = categoryService.getProductsByCategory(categoryId);
		if(product != null) {
			return ResponseEntity.ok(product);
		}else {
			return ResponseEntity.noContent().build();
		}
	}
}

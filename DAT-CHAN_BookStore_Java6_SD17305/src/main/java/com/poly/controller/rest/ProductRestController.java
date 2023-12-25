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
import com.poly.service.ProductService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class ProductRestController {
	@Autowired
	ProductService productService;

	@GetMapping("/products")
	public ResponseEntity<List<Product>> products() {
		if (productService.findAll().size() > 0) {
			return ResponseEntity.ok(productService.findAll());
		} else {
			return ResponseEntity.noContent().build();
		}
	}

	@GetMapping("/products/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable String id) {
		Product product = productService.findById(id);
		if (product != null) {
			return ResponseEntity.ok(product);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	@GetMapping("/products/related/{categoryId}")
	public ResponseEntity<List<Product>> getRelatedProducts(@PathVariable String categoryId) {
	    List<Product> relatedProducts = productService.getRelatedProductsByCategory(categoryId);
	    if (!relatedProducts.isEmpty()) {
	        return ResponseEntity.ok(relatedProducts);
	    } else {
	        return ResponseEntity.noContent().build();
	    }
	}
	
	@GetMapping("/products/search/{productName}")
	public ResponseEntity<List<Product>> searchProductsByName(@PathVariable String productName){
		List<Product> products = productService.searchProductsByName(productName);
		if (!products.isEmpty()) {
	        return ResponseEntity.ok(products);
	    } else {
	        return ResponseEntity.noContent().build();
	    }
	}
	
}

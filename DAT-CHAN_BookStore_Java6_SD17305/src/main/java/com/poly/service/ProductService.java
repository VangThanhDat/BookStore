package com.poly.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.poly.bean.Category;
import com.poly.bean.Product;
import com.poly.bean.ProductTypeStatisticsDTO;
import com.poly.bean.SoldProductDTO;
import com.poly.repository.ProductRepository;

@Service
public class ProductService {
	@Autowired
	ProductRepository pdao;

	public List<Product> findAll() {
		return pdao.findAll();
	}

	public Product findByIdA(String productId) {
		return pdao.getById(productId);
	}

	public Boolean checkID(String id) {
		return pdao.existsById(id);
	}

	public Page<Product> findAll(Pageable pageable) {
		return pdao.findAll(pageable);
	}

	public Product findById(String id) {
		Optional<Product> optionalProduct = pdao.findById(id);
		return optionalProduct.orElse(null);
	}
	

	public Object entity(Product id) {
		return pdao.entity(id);
	}

	public Page<Product> findProductByNamePage(String name, Pageable pageable) {
		return pdao.findProductByNamePage(name, pageable);
	}

	public Page<Product> findByNamePage(String name, Pageable pageable) {
		return pdao.findByNamePage(name, pageable);
	}

	public Page<Product> findByCategory(Category category, Pageable pageable) {
		return pdao.findByCategory(category, pageable);
	}

	public Product create(Product entity) {
		return pdao.save(entity);
	}

	public Product update(Product entity) {
		return pdao.saveAndFlush(entity);
	}

	public void delete(Product entity) {
		pdao.delete(entity);
	}

	public List<Product> searchProductsByName(String productName) {
		return pdao.findByNameContainingIgnoreCase(productName);
	}

	public List<Product> getRelatedProductsByCategory(String categoryId) {
		return pdao.findByCategoryId(categoryId);
	}


	public List<SoldProductDTO> findSoldProducts() {
		return pdao.findSoldProducts();
	}
	
	public List<ProductTypeStatisticsDTO> getProductTypeStatistics() {
		return pdao.getProductTypeStatistics();
	}
	
	public Product findById2(String id) {
		Product product=new Product();
		Product prod = pdao.getById(id);
		product.setId(prod.getId());
		product.setName(prod.getName());
		product.setAuthor(prod.getAuthor());
		product.setCategory(prod.getCategory());
		product.setDescription(prod.getDescription());
		product.setPrice(prod.getPrice());
		product.setImage(prod.getImage());
		product.setQuantity(prod.getQuantity());
		product.setCategory(prod.getCategory());
	    return product;
	}
	public List<SoldProductDTO> findSoldProductsByMonthAndYear(Integer selectedMonth, Integer selectedYear) {
        return pdao.findSoldProductsByMonthAndYear(selectedMonth, selectedYear);
    }
	 public List<ProductTypeStatisticsDTO> getProductTypeStatisticsByMonthAndYear(Integer selectedMonth, Integer selectedYear) {
	        return pdao.getProductTypeStatisticsByMonthAndYear(selectedMonth, selectedYear);
	    }
}
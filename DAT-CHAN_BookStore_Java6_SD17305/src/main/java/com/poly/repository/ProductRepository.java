package com.poly.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poly.bean.Category;
import com.poly.bean.Product;
import com.poly.bean.ProductTypeStatisticsDTO;
import com.poly.bean.SoldProductDTO;

public interface ProductRepository extends JpaRepository<Product, String> {

	@Query("SELECT p FROM Product p WHERE p.quantity > 0")
	List<Product> findAll();

	@Query("SELECT p FROM Product p WHERE p.quantity > 0")
	Page<Product> findAll(Pageable pageable);

	@Query("UPDATE Product p SET p.quantity = 0 WHERE p.id =?1")
	Object entity(Product id);

	@Query("SELECT p FROM Product p WHERE p.name LIKE ?1")
	Page<Product> findProductByNamePage(String name, Pageable pageable);

	@Query("SELECT p FROM Product p WHERE p.name LIKE ?1")
	Page<Product> findByNamePage(String name, Pageable pageable);

	@Query("SELECT p FROM Product p WHERE p.category = ?1")
	Page<Product> findByCategory(Category category, Pageable pageable);

	@Query("SELECT new SoldProductDTO(p.id, p.name, p.price, p.quantity, sum(od.quantityPurchased), p.category.name) "
			+ "FROM OrderDetail od JOIN od.product p WHERE od.order.status = 1 GROUP BY p.id, p.name, p.price, p.quantity, p.category.name")
	List<SoldProductDTO> findSoldProducts();

	@Query("SELECT new ProductTypeStatisticsDTO(p.category.name, SUM(od.quantityPurchased)) "
			+ "FROM OrderDetail od JOIN od.product p WHERE od.order.status = 1 GROUP BY p.category.name")
	List<ProductTypeStatisticsDTO> getProductTypeStatistics();

	@Query("SELECT new com.poly.bean.SoldProductDTO(p.id, p.name, p.price, p.quantity, sum(od.quantityPurchased), p.category.name) "
			+ "FROM OrderDetail od JOIN od.product p " + "WHERE od.order.status = 1 "
			+ "AND (:selectedMonth IS NULL OR MONTH(od.order.date) = :selectedMonth) "
			+ "AND (:selectedYear IS NULL OR YEAR(od.order.date) = :selectedYear) "
			+ "GROUP BY p.id, p.name, p.price, p.quantity, p.category.name "
			+ "ORDER BY sum(od.quantityPurchased) DESC")
	List<SoldProductDTO> findSoldProductsByMonthAndYear(@Param("selectedMonth") Integer selectedMonth,
			@Param("selectedYear") Integer selectedYear);

	@Query("SELECT new ProductTypeStatisticsDTO(p.category.name, SUM(od.quantityPurchased)) "
			+ "FROM OrderDetail od JOIN od.product p " + "WHERE od.order.status = 1 "
			+ "AND (:selectedMonth IS NULL OR MONTH(od.order.date) = :selectedMonth) "
			+ "AND (:selectedYear IS NULL OR YEAR(od.order.date) = :selectedYear) " + "GROUP BY p.category.name")
	List<ProductTypeStatisticsDTO> getProductTypeStatisticsByMonthAndYear(@Param("selectedMonth") Integer selectedMonth,
			@Param("selectedYear") Integer selectedYear);

	List<Product> findByNameContainingIgnoreCase(String name);

	List<Product> findByCategoryId(String categoryId);
}

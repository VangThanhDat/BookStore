package com.poly.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.poly.bean.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {

	// @Query("SELECT o.product.id, o.product.name, COUNT(o.id) AS soLuong FROM
	// OrderDetail o GROUP BY o.product.id, o.product.name")
	// List<Object[]> getTop10SPBanChay(Pageable page);

	// @Query("SELECT o.product.name , SUM(o.order.total * o.product.price) AS
	// tongTien FROM OrderDetail o GROUP BY o.product.name")
	// List<Object[]> getDoanhThu(Pageable page);

	@Query("SELECT od FROM OrderDetail od WHERE od.order.id = :orderId")
	Page<OrderDetail> findOrderDetailsByOrderId(String orderId, Pageable pageable);

	@Query("SELECT MONTH(od.date) AS month, SUM(od.quantityPurchased * od.product.price) AS totalRevenue " +
			"FROM OrderDetail od WHERE od.order.status = 1 GROUP BY MONTH(od.date)")
	List<Object[]> getTotalRevenueByMonth();

	@Query("SELECT od FROM OrderDetail od WHERE od.order.id = :orderId")
	List<OrderDetail> findByOrderId(String orderId);

	@Query("SELECT od FROM OrderDetail od WHERE od.order.id like ?1")
	List<OrderDetail> findOrderId(String ID);
}

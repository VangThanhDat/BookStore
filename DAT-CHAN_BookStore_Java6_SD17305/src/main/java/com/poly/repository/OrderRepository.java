package com.poly.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.poly.bean.Order;

public interface OrderRepository extends JpaRepository<Order, String> {
    @Query("SELECT o FROM Order o WHERE o.status = 1")
    List<Order> findByStatusOne();

    @Query("SELECT o FROM Order o WHERE o.user.fullname LIKE ?1")
    Page<Order> findOderByNameUser(String name, Pageable pageable);
    
    @Query("SELECT o FROM Order o WHERE o.user.id LIKE ?1")
    List<Order> findbyUserId(String userid);
    
    long countByStatus(int status);

    //    HistoryStatus
    @Query("SELECT o FROM Order o WHERE o.user.id LIKE ?1 and o.status = 0")
    List<Order> findWaitbyUserId(String userid);
    
    @Query("SELECT o FROM Order o WHERE o.user.id LIKE ?1 and o.status = 1")
    List<Order> findAcceptbyUserId(String userid);
    
    @Query("SELECT o FROM Order o WHERE o.user.id LIKE ?1 and o.status = 3")
    List<Order> findShipbyUserId(String userid);
    
    @Query("SELECT o FROM Order o WHERE o.user.id LIKE ?1 and o.status = 2")
    List<Order> findCancelbyUserId(String userid);
    
    @Query("SELECT o FROM Order o WHERE o.user.id LIKE ?1 ORDER BY o.date desc,o.id desc")
    List<Order> findbyUserIdAll(String userid);
}
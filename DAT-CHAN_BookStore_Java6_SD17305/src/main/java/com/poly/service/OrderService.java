package com.poly.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.poly.bean.Order;
import com.poly.repository.OrderRepository;

@Service
public class OrderService {
    @Autowired
    OrderRepository oRepo;

    public Page<Order> findAllOrders(Pageable page) {
        return oRepo.findAll(page);
    }

    public List<Order> findAll(){
    	return oRepo.findAll();
    }
    
    public Order create(Order entity) {
    	return oRepo.saveAndFlush(entity);
    }

    public Page<Order> findOrderByName(String name, Pageable page) {
        return oRepo.findOderByNameUser(name, page);
    }

    public Optional<Order> findByID(String id) {
        return oRepo.findById(id);
    }

    public Order updateStatus(Order order, Integer status) {
        order.setStatus(status);
        oRepo.saveAndFlush(order);
        return order;
    }
    
    public List<Order> findbyUserID(String userid) {
        return oRepo.findbyUserId(userid);
    }
    
    public List<Order> findbyUserIDAll(String userid) {
        return oRepo.findbyUserIdAll(userid);
    }
    
    public long countUnconfirmedOrders() {
        return oRepo.countByStatus(0); 
    }
    
    public long countConfirmedOrders() {
        return oRepo.countByStatus(1);
    }

     // Begin HistoryStatus
     public List<Order> findWaitbyUserID(String userid) {
        return oRepo.findWaitbyUserId(userid);
    }

    public List<Order> findAcceptbyUserID(String userid) {
        return oRepo.findAcceptbyUserId(userid);
    }

    public List<Order> findShipbyUserID(String userid) {
        return oRepo.findShipbyUserId(userid);
    }

    public List<Order> findCancelbyUserID(String userid) {
        return oRepo.findCancelbyUserId(userid);
    }
    // End HistoryStatus

}

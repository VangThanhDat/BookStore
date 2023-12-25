package com.poly.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import com.poly.bean.OrderDetail;
import com.poly.repository.OrderDetailRepository;

@Service
public class OrderDetailService {
    @Autowired
    OrderDetailRepository odRepo;

    public Page<OrderDetail> findAllOrders(Pageable page) {
        return odRepo.findAll(page);
    }

    public Page<OrderDetail> findODByOrderID(String orderID, Pageable pageable) {
        return odRepo.findOrderDetailsByOrderId(orderID, pageable);
    }
    
    public OrderDetailService(OrderDetailRepository orderDetailRepository) {
        this.odRepo = orderDetailRepository;
    }
    
    public List<OrderDetail> findOrderID(String id){
    	return odRepo.findByOrderId(id);
    }
    
    public Map<Integer, Double> getTotalRevenueByMonth() {
        Map<Integer, Double> monthlyRevenues = new HashMap<>();

        // Lấy tổng doanh thu từng tháng từ repository
        List<Object[]> results = odRepo.getTotalRevenueByMonth();
        for (Object[] result : results) {
            int month = (int) result[0];
            double totalRevenue = (double) result[1];
            // Bỏ qua tháng đầu tiên (tháng 0)
            if (month > 0) {
                monthlyRevenues.put(month, totalRevenue);
            }
        }

        // Kiểm tra và bổ sung các tháng không có doanh thu (tổng doanh thu là 0) vào map
        for (int month = 1; month <= 12; month++) {
            monthlyRevenues.putIfAbsent(month, 0.0);
        }
        return monthlyRevenues;
    }

    public List<OrderDetail> findAll(){
        return odRepo.findAll();
    }

    public OrderDetail save(OrderDetail entity){
        return odRepo.saveAndFlush(entity);
    }
}

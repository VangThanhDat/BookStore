package com.poly.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.poly.bean.OrderDetail;
import com.poly.service.OrderDetailService;
import com.poly.service.OrderService;

@CrossOrigin("*")
@RestController
public class OrderDetailRController {
	@Autowired
	OrderDetailService odService;

	@Autowired
	OrderService oService;

	@GetMapping("/restApi/order/{orderId}")
	public ResponseEntity<List<OrderDetail>> getByOrderID(@PathVariable String orderId) {
		List<OrderDetail> odd = odService.findOrderID(orderId);
		if (!odd.isEmpty()) {
			return ResponseEntity.ok(odd);
		} else {
			return ResponseEntity.noContent().build();
		}
	}
}

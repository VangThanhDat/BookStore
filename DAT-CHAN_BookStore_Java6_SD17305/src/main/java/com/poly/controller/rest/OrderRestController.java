package com.poly.controller.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.poly.bean.Cart;
import com.poly.bean.Order;
import com.poly.bean.OrderDetail;
import com.poly.bean.Product;
import com.poly.bean.User;
import com.poly.service.CartService;
import com.poly.service.MailService;
import com.poly.service.OrderDetailService;
import com.poly.service.OrderService;
import com.poly.service.ProductService;
import com.poly.service.UserService;
import com.poly.util.JWTTokenUtil;

@CrossOrigin("*")
@RestController
public class OrderRestController {

	@Autowired
	OrderService oService;

	@Autowired
	OrderDetailService odetailService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	CartService cartService;

	@Autowired
	ProductService productService;

	@Autowired
	MailService mailService;
	
	@Autowired
	JWTTokenUtil jwtTokenUtil;
	// history
	@GetMapping("/restApi/allorder") //All Orders
	public ResponseEntity<?> order(@RequestHeader("Authorization") String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
		}
		String token = authorizationHeader.replace("Bearer ", "");
		System.out.println("(OrderAll)User token: "+token);
		String username = jwtTokenUtil.getUsernameFromToken(token);
		System.out.println("(OrderAll)UserID: "+username);
	
		return ResponseEntity.ok().body(oService.findbyUserIDAll(username));
	}

	@GetMapping("/restApi/orderwait") //Wait Order
	public ResponseEntity<?> orderWait(@RequestHeader("Authorization") String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
		}
		String token = authorizationHeader.replace("Bearer ", "");
		System.out.println("(OrderWait)User token: " + token);
		String username = jwtTokenUtil.getUsernameFromToken(token);
		System.out.println("(OrderWait)UserID: " + username);

		return ResponseEntity.ok().body(oService.findWaitbyUserID(username));
	}

	@GetMapping("/restApi/orderaccept")	//Accept Order
	public ResponseEntity<?> orderAccept(@RequestHeader("Authorization") String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
		}
		String token = authorizationHeader.replace("Bearer ", "");
		System.out.println("(OrderAccept)User token: " + token);
		String username = jwtTokenUtil.getUsernameFromToken(token);
		System.out.println("(OrderAccept)UserID: " + username);

		return ResponseEntity.ok().body(oService.findAcceptbyUserID(username));
	}

	@GetMapping("/restApi/ordership")	//Ship Order
	public ResponseEntity<?> orderShip(@RequestHeader("Authorization") String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
		}
		String token = authorizationHeader.replace("Bearer ", "");
		System.out.println("(OrderAccept)User token: " + token);
		String username = jwtTokenUtil.getUsernameFromToken(token);
		System.out.println("(OrderAccept)UserID: " + username);

		return ResponseEntity.ok().body(oService.findShipbyUserID(username));
	}

	@GetMapping("/restApi/ordercancel")	//Cancel Order
	public ResponseEntity<?> orderCancel(@RequestHeader("Authorization") String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
		}
		String token = authorizationHeader.replace("Bearer ", "");
		System.out.println("(Order)User token: " + token);
		String username = jwtTokenUtil.getUsernameFromToken(token);
		System.out.println("(Order)UserID: " + username);

		return ResponseEntity.ok().body(oService.findCancelbyUserID(username));
	}

	@PutMapping("/restApi/order/cancel/{id}")
	public ResponseEntity<?> removeItem(@RequestHeader("Authorization") String authorizationHeader,@PathVariable("id") String id) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
		}
		String token = authorizationHeader.replace("Bearer ", "");
		System.out.println("(Order)User token: " + token);
		String orid = jwtTokenUtil.getUsernameFromToken(token);
		System.out.println("(Order)UserID: " + orid);

		Order order = oService.findByID(id).get();
        oService.updateStatus(order, 2);
		
		return ResponseEntity.ok().body(oService.findAcceptbyUserID(orid));
	}
	// End History API
	
	@PostMapping("/rest/order/create")
	public ResponseEntity<?> create(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Order orderFrom) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
		}
		String token = authorizationHeader.replace("Bearer ", "");
		String username = jwtTokenUtil.getUsernameFromToken(token);

		Map<String, Object> response = new HashMap<>();
		//create OrderId Auto
		String orderId = this.createOrderId();
		// User Id
		User user = userService.findById(username);
		//Total
		List<Cart> listC = cartService.findByUsername(username);
		Order order = new Order();
		order.setUser(user);
		order.setId(orderId);
		order.setDate(orderFrom.getDate());
		order.setTotal(getTotalCart(username));
		order.setAddress(orderFrom.getAddress());
		order.setStatus(0);
		oService.create(order);

		for(Cart cart : listC){
			OrderDetail oDetail = new OrderDetail();
			Product product = cart.getProduct();
			Integer quantiy = product.getQuantity();
			product.setQuantity(quantiy - cart.getQuantityPurchased());
			productService.update(product);
			oDetail.setId(this.createOrderDetailID());
			oDetail.setOrder(order);
			oDetail.setProduct(cart.getProduct());
			oDetail.setQuantityPurchased(cart.getQuantityPurchased());
			odetailService.save(oDetail);
			cartService.deleteCart(cart);
		}
		mailService.sendMailPay(user.getEmail(), user.getFullname());
		return ResponseEntity.ok().body(response);
	}

	public String createOrderId(){
		List<Order> listO = oService.findAll();
		String orderID = listO.get(listO.size() - 1).getId();
		int numberOrder = Integer.parseInt(orderID.substring(orderID.length() - 3)) + 1;
		System.out.println("PayController.createOrderID() "+"DH" +String.format("%03d", numberOrder));
		return "DH" +String.format("%03d", numberOrder);
	}
	public String createOrderDetailID () {
		List<OrderDetail> listO = odetailService.findAll();
		String orderDetailID = listO.get(listO.size() - 1).getId();
		int numberOrder = Integer.parseInt(orderDetailID.substring(orderDetailID.length() - 3)) + 1;
		System.out.println("PayController.createOrderDetailID() "+"HD" +String.format("%03d", numberOrder));
		return "HD" +String.format("%03d", numberOrder);
	}

	public Double getTotalCart(String username) {
		Double total = 0.0;
		List<Cart> listC = cartService.findByUsername(username);
		for (Cart cart : listC) {
			total += cart.getProduct().getPrice() * cart.getQuantityPurchased();
		}
		return total;
	}
}

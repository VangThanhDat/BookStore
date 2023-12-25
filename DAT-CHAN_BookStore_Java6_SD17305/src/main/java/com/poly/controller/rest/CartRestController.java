package com.poly.controller.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poly.bean.Cart;
import com.poly.service.CartService;
import com.poly.service.ProductService;
import com.poly.service.UserService;
import com.poly.util.JWTTokenUtil;

@CrossOrigin("*")
@RestController
public class CartRestController {

	@Autowired
	CartService cartService;

	@Autowired
	UserService userService;

	@Autowired
	ProductService proService;

	@Autowired
	JWTTokenUtil jwtTokenUtil;

	@GetMapping("/rest/cart")
	// Kiểm tra xem header "Authorization" có giá trị không
	public ResponseEntity<?> cart(@RequestHeader("Authorization") String authorizationHeader) {
		// String username =
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			// Nếu không có token hoặc không bắt đầu bằng "Bearer ", trả về lỗi 401
			// (UNAUTHORIZED)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
		}

		// Lấy token từ header
		String token = authorizationHeader.replace("Bearer ", "");
		System.out.println(token);
		String username = jwtTokenUtil.getUsernameFromToken(token);
		System.out.println(username);

		return ResponseEntity.ok().body(cartService.findByUsername(username));
	}

	@GetMapping("/rest/username")
	public ResponseEntity<?> username(@RequestHeader("Authorization") String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			// Nếu không có token hoặc không bắt đầu bằng "Bearer ", trả về lỗi 401
			// (UNAUTHORIZED)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
		}
		// Lấy token từ header
		String token = authorizationHeader.replace("Bearer ", "");
		String username = jwtTokenUtil.getUsernameFromToken(token);
		System.out.println(username);
		return ResponseEntity.ok().body(userService.findById(username));
	}

	@GetMapping("/rest/addCart/{id}")
	public ResponseEntity<?> addCart(@RequestHeader("Authorization") String authorizationHeader,
			@PathVariable("id") String id) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			// Nếu không có token hoặc không bắt đầu bằng "Bearer ", trả về lỗi 401
			// (UNAUTHORIZED)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
		}
		// Lấy token từ header
		String token = authorizationHeader.replace("Bearer ", "");
		String username = jwtTokenUtil.getUsernameFromToken(token);

		List<Cart> listc = cartService.findByUsername(username);
		Boolean checkProduct = false;
		for (Cart cart : listc) {
			if (cart.getProduct().getId().equals(id)) {
				checkProduct = true;
				cart.setQuantityPurchased(cart.getQuantityPurchased() + 1);
				cartService.addProduct(cart);
				break;
			}
		}
		Cart entity = new Cart();
		if (!checkProduct) {
			entity.setProduct(proService.findByIdA(id));
			entity.setUser(userService.findById(username));
			entity.setQuantityPurchased(1);
			cartService.addProduct(entity);
		}
		List<Cart> listC = cartService.findByUsername(username);
		return ResponseEntity.ok().body(listC);
	}

	@GetMapping("/rest/cart/remove/{id}")
	public ResponseEntity<?> removeItem(@RequestHeader("Authorization") String authorizationHeader,
			@PathVariable("id") String id) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			// Nếu không có token hoặc không bắt đầu bằng "Bearer ", trả về lỗi 401
			// (UNAUTHORIZED)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
		}

		// Lấy token từ header
		String token = authorizationHeader.replace("Bearer ", "");
		String username = jwtTokenUtil.getUsernameFromToken(token);

		cartService.deleteByIdProduct(id);
		return ResponseEntity.ok().body(cartService.findByUsername(username));
	}

	@GetMapping("/rest/cart/removeAll")
	public ResponseEntity<?> removeAllItem(@RequestHeader("Authorization") String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			// Nếu không có token hoặc không bắt đầu bằng "Bearer ", trả về lỗi 401
			// (UNAUTHORIZED)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
		}

		// Lấy token từ header
		String token = authorizationHeader.replace("Bearer ", "");
		String username = jwtTokenUtil.getUsernameFromToken(token);

		cartService.deleteAllByIdUser(username);
		return ResponseEntity.ok().body(cartService.findByUsername(username));
	}

	@GetMapping("/rest/cart/increase/{id}")
	public ResponseEntity<?> increaseItem(@PathVariable("id") Long id) {

		Cart entiy = cartService.findById(id);
		entiy.setQuantityPurchased(entiy.getQuantityPurchased() + 1);
		cartService.updateItem(entiy);
		return ResponseEntity.ok().body(cartService.findById(id));
	}

	@GetMapping("/rest/cart/reduce/{id}")
	public ResponseEntity<?> reduceItem(@PathVariable("id") Long id) {
		Cart entiy = cartService.findById(id);
		entiy.setQuantityPurchased(entiy.getQuantityPurchased() - 1);
		cartService.updateItem(entiy);
		return ResponseEntity.ok().body(cartService.findById(id));
	}
}

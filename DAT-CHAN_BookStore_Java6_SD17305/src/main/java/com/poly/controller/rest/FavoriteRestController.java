package com.poly.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.poly.bean.Category;
import com.poly.bean.Favorite;
import com.poly.bean.Product;
import com.poly.service.FavoriteService;
import com.poly.service.ProductService;
import com.poly.util.JWTTokenUtil;



@CrossOrigin("*")
@RestController
public class FavoriteRestController {
	@Autowired
	FavoriteService fService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	JWTTokenUtil jwtTokenUtil;
	
	@GetMapping("/restApi/like")
	public ResponseEntity<?> list(@RequestHeader("Authorization") String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
		}
		String token = authorizationHeader.replace("Bearer ", "");
		String username = jwtTokenUtil.getUsernameFromToken(token);
		
		return ResponseEntity.ok().body(fService.findByUsername(username));
	}
	
	@PutMapping("/restApi/islike/{id}")
	public ResponseEntity<?> removeItem(@RequestHeader("Authorization") String authorizationHeader,
			@PathVariable("id") String id) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
		}

		// Lấy token từ header
		String token = authorizationHeader.replace("Bearer ", "");
		String username = jwtTokenUtil.getUsernameFromToken(token);

		fService.findByUsername(id);
		boolean check=false;
		List<Favorite> listf=fService.findByUsername(username);
		for(Favorite favorite:listf) {
			if(favorite.getId().equals(id)) {
				check= true;
				fService.update(favorite);
				break;
			}
		}
		if (check=false) {
			Favorite obj=new Favorite();
			fService.save(obj);
		}
		
		return ResponseEntity.ok().body(fService.save(null));
	}
	
	@GetMapping("/restApi/like/search/{productName}")
	public ResponseEntity<?> searchProductsByName(@RequestHeader("Authorization") String authorizationHeader,@PathVariable String productName){
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
		}
		String token = authorizationHeader.replace("Bearer ", "");
		String username = jwtTokenUtil.getUsernameFromToken(token);
		
		List<Favorite> favorites=fService.findByProductname(productName, username);
		if(favorites !=null) {
			return ResponseEntity.ok(favorites);
		}else {
			return ResponseEntity.noContent().build();
		}	
	}
	
	@GetMapping("/restApi/like/{categoryId}/products")
	public ResponseEntity<?> productsByCategory(@RequestHeader("Authorization") String authorizationHeader,@PathVariable String categoryId){
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
		}
		String token = authorizationHeader.replace("Bearer ", "");
		String username = jwtTokenUtil.getUsernameFromToken(token);
		
		List<Favorite> fList = fService.findByCategory(categoryId,username);
		if(fList != null) {
			return ResponseEntity.ok(fList);
		}else {
			return ResponseEntity.noContent().build();
		}
	}
}

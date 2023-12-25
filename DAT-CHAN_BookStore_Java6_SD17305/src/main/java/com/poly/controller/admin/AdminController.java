package com.poly.controller.admin;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.poly.bean.SoldProductDTO;
import com.poly.repository.ProductRepository;
import com.poly.service.OrderService;
import com.poly.service.ProductService;
import com.poly.service.UserService;

@Controller
public class AdminController {

	@Autowired
	UserService userService;

	@Autowired
	ProductService pdao;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	ProductRepository productRepo;
	
	@Autowired
	HttpSession session;

	@GetMapping("/admin/index")
	public String index(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = null;
		if (authentication != null && authentication.isAuthenticated()
				&& authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			username = userDetails.getUsername();

			// Bây giờ bạn có thể sử dụng thông tin của người dùng đã đăng nhập
		}
		System.out.println("username admin: " + username);
		if (username != null) {
			session.setAttribute("adminName", userService.findById(username).getFullname());
//			model.addAttribute("adminName", udao.findById(username).getFullname());
		}
		
		long totalUsers = userService.countTotalUsers();
        model.addAttribute("totalUsers", totalUsers);
        
        long unconfirmedOrders = orderService.countUnconfirmedOrders();
        model.addAttribute("unconfirmedOrders", unconfirmedOrders);
        
        long confirmedOrders = orderService.countConfirmedOrders();
        model.addAttribute("confirmedOrders", confirmedOrders);
        
        List<SoldProductDTO> soldProducts = pdao.findSoldProducts();
        model.addAttribute("soldProducts", soldProducts);

		return "admin/index";
	}

}

package com.poly.controller.admin;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poly.bean.*;
import com.poly.repository.OrderDetailRepository;
import com.poly.repository.ProductRepository;
import com.poly.service.OrderDetailService;
import com.poly.service.ProductService;


@Controller
public class StatisticalController {
	@Autowired
	ProductRepository pRep;
	
	@Autowired 
	ProductService productService;
	
	@Autowired
	OrderDetailRepository odao;
	
	
	@Autowired
	OrderDetailService orderDetailService;
		
	@RequestMapping("/admin/statictis/TKSP")
	public String TKSP(Model model,
            @RequestParam(required = false) Integer selectedMonth,
            @RequestParam(required = false) Integer selectedYear) {
		List<SoldProductDTO> soldProducts;
		List<ProductTypeStatisticsDTO> productTypeStatistics;
		
	    if (selectedMonth != null && selectedYear != null) {
	        soldProducts = productService.findSoldProductsByMonthAndYear(selectedMonth, selectedYear);
	        productTypeStatistics = productService.getProductTypeStatisticsByMonthAndYear(selectedMonth, selectedYear);
	    } else {
	        soldProducts = productService.findSoldProducts();
	        productTypeStatistics = productService.getProductTypeStatistics();
	    }

	    model.addAttribute("soldProducts", soldProducts);     
        model.addAttribute("productTypeStatistics", productTypeStatistics);
		return "admin/statistic/ThongKeSanPham";
	}
	
	@RequestMapping("/admin/statictis/TKDT")
	public String TKDT(Model model,
            @RequestParam(required = false) Integer selectedMonth,
            @RequestParam(required = false) Integer selectedYear) {
		List<SoldProductDTO> soldProducts;
		
	    if (selectedMonth != null && selectedYear != null) {
	        soldProducts = productService.findSoldProductsByMonthAndYear(selectedMonth, selectedYear);
	    } else {
	        soldProducts = productService.findSoldProducts();
	    }
        model.addAttribute("soldProducts", soldProducts);
       
		Map<Integer, Double> monthlyRevenues = orderDetailService.getTotalRevenueByMonth();
	    model.addAttribute("monthlyRevenues", monthlyRevenues);
	   
		return "admin/statistic/ThongKeDoanhThu";
	}
}

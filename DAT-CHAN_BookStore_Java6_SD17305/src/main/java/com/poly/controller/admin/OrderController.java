package com.poly.controller.admin;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poly.bean.Order;
import com.poly.bean.Product;
import com.poly.service.OrderService;

@Controller
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    HttpSession session;

    @GetMapping("/admin/manage/order")
    public String order(Model model, @RequestParam("p") Optional<Integer> page,
            @RequestParam("keyword") Optional<String> name) {

        String findName;
        if (session.getAttribute("keyword") == null) {
            findName = name.orElse("");
        } else {
            findName = name.orElse((String) session.getAttribute("keyword"));
        }

        if (page.isPresent()) {
            if (page.get() < 0) {
                page = Optional.of(1);
            } else {
                page = Optional.of(page.get());
            }

        }
        PageRequest pageable = PageRequest.of(page.orElse(1) - 1, 5);
        Page<Order> pageO = orderService.findOrderByName("%" + findName + "%", pageable);
        if (pageO.isEmpty()) {
            model.addAttribute("message", "Không tìm thấy đơn hàng với từ khóa: " + findName);
        } else {
            model.addAttribute("page", pageO);
        }

        return "admin/manage/order";
    }

    @PostMapping("/admin/manage/order")
    public String order2(Model model, @RequestParam("keyword") Optional<String> key) {
        String name = key.orElse("").toString();
        session.setAttribute("keyword", name);
        return "redirect:/admin/manage/order";
    }
}

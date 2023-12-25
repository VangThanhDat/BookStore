package com.poly.controller.admin;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poly.bean.Order;
import com.poly.bean.OrderDetail;
import com.poly.service.OrderDetailService;
import com.poly.service.OrderService;

@Controller
public class OrderDetailController {

    @Autowired
    OrderDetailService orderDetailService;

    @Autowired
    OrderService orderService;

    @GetMapping("/admin/manage/detail-order/{orderID}")
    public String odetail(@PathVariable("orderID") String orderID, Model model,
            @RequestParam("p") Optional<Integer> page) {
        if (page.isPresent()) {
            if (page.get() < 0) {
                page = Optional.of(1);
            } else {
                page = Optional.of(page.get());
            }

        }
        PageRequest pageable = PageRequest.of(page.orElse(1) - 1, 5);
        Page<OrderDetail> pageO = orderDetailService.findODByOrderID(orderID, pageable);
        if (pageO.isEmpty()) {
            model.addAttribute("message", "Không tìm thấy chi tiết hóa đơn nào");
        } else {
            model.addAttribute("page", pageO);
            model.addAttribute("order", orderService.findByID(orderID).get());
        }
        return "admin/manage/detail-order";
    }

    @PostMapping("/admin/manage/detail-order/confirm/{orderID}")
    public String confirm(Model model, @PathVariable("orderID") String orderID) {
        Order order = orderService.findByID(orderID).get();
        orderService.updateStatus(order, 1);
        return "redirect:/admin/manage/detail-order/" + orderID;
    }

    @PostMapping("/admin/manage/detail-order/cancel/{orderID}")
    public String cancel(Model model, @PathVariable("orderID") String orderID) {
        Order order = orderService.findByID(orderID).get();
        orderService.updateStatus(order, 2);
        return "redirect:/admin/manage/detail-order/" + orderID;
    }
}

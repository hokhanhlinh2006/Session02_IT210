package com.example.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class OrderController {

    @GetMapping("/orders")
    public String orders(HttpSession session,
                         ServletContext application,
                         Model model) {

        if (session.getAttribute("loggedUser") == null) {
            return "redirect:/login";
        }

        List<Map<String, Object>> orders = new ArrayList<>();

        orders.add(createOrder("OD01", "iPhone 15", 25000000));
        orders.add(createOrder("OD02", "Samsung S24", 22000000));
        orders.add(createOrder("OD03", "Xiaomi 13", 15000000));

        model.addAttribute("orders", orders);

        // FIX Race Condition
        synchronized (application) {
            Integer count = (Integer) application.getAttribute("totalViewCount");
            if (count == null) count = 0;
            count++;
            application.setAttribute("totalViewCount", count);
        }

        return "orders";
    }

    private Map<String, Object> createOrder(String id, String product, double price) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("product", product);
        map.put("price", price);
        map.put("date", new Date());
        return map;
    }
}
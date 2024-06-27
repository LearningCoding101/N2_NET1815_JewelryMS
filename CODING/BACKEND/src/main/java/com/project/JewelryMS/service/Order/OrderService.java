package com.project.JewelryMS.service.Order;

import com.project.JewelryMS.entity.PurchaseOrder;
import com.project.JewelryMS.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public PurchaseOrder saveOrder(PurchaseOrder purchaseOrder) {
        return orderRepository.save(purchaseOrder);
    }

    public List<PurchaseOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    public PurchaseOrder getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

}

package com.project.JewelryMS.service.Order;

import com.project.JewelryMS.entity.OrderBuyDetail;
import com.project.JewelryMS.entity.OrderDetail;
import com.project.JewelryMS.repository.OrderBuyDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrderBuyDetailService {
    @Autowired
    private OrderBuyDetailRepository orderBuyDetailRepository;

    public OrderBuyDetail saveOrderBuyDetail(OrderBuyDetail order) {
        return orderBuyDetailRepository.save(order);
    }

    public List<OrderBuyDetail> getAllOrders() {
        return orderBuyDetailRepository.findAll();
    }

    public OrderBuyDetail getOrderById(Long id) {
        return orderBuyDetailRepository.findById(id).orElse(null);
    }

    public void deleteOrder(Long id) {
        orderBuyDetailRepository.deleteById(id);
    }
}

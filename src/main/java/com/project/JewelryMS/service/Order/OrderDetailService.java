package com.project.JewelryMS.service.Order;

import com.project.JewelryMS.entity.Guarantee;
import com.project.JewelryMS.entity.OrderDetail;
import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.entity.Promotion;
import com.project.JewelryMS.model.Order.TotalOrderRequest;
import com.project.JewelryMS.model.Order.TotalOrderResponse;
import com.project.JewelryMS.model.OrderDetail.*;
import com.project.JewelryMS.repository.GuaranteeRepository;
import com.project.JewelryMS.repository.OrderDetailRepository;
import com.project.JewelryMS.repository.ProductSellRepository;
import com.project.JewelryMS.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import java.security.Timestamp;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductSellRepository productSellRepository;


    public OrderDetail saveOrderDetail(OrderDetail order) {
        return orderDetailRepository.save(order);
    }

    public List<OrderDetail> getAllOrders() {
        return orderDetailRepository.findAll();
    }

    public OrderDetail getOrderById(Long id) {
        return orderDetailRepository.findById(id).orElse(null);
    }

    public void deleteOrder(Long id) {
        orderDetailRepository.deleteById(id);
    }

    public List<OrderDetailDTO> getOrderDetailsByOrderId(Long orderId) {
        return orderDetailRepository.findOrderDetailsByOrderId(orderId);
    }
}


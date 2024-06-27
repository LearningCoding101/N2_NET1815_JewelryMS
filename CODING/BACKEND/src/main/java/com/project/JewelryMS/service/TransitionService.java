package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.PurchaseOrder;
import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.enumClass.OrderTypeEnum;
import com.project.JewelryMS.model.Transition.TransitionResponse;
import com.project.JewelryMS.repository.CustomerRepository;
import com.project.JewelryMS.repository.OrderRepository;
import com.project.JewelryMS.repository.StaffAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransitionService {
    @Autowired
    private OrderRepository purchaseOrderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StaffAccountRepository staffAccountRepository;

    public List<TransitionResponse> getPurchaseOrderHistory() {
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
        List<TransitionResponse> responses = new ArrayList<>();

        for (PurchaseOrder order : purchaseOrders) {
            TransitionResponse response = new TransitionResponse();
            response.setOrderID(order.getPK_OrderID());
            response.setPaymentType(order.getPaymentType());
            response.setOrderStatus(order.getStatus());
            response.setTotalAmount(order.getTotalAmount());

            if (order.getOrderBuyDetails() != null && !order.getOrderBuyDetails().isEmpty()) {
                response.setOrderType(OrderTypeEnum.OUTGOING);
            } else if (order.getOrderDetails() != null && !order.getOrderDetails().isEmpty()) {
                response.setOrderType(OrderTypeEnum.INGOING);
            }

            if (order.getStaffAccount() != null) {
                response.setStaffID(order.getStaffAccount().getStaffID());
                response.setStaffName(order.getStaffAccount().getAccount().getAccountName());

            }

            if (order.getCustomer() != null) {
                response.setCustomerID(order.getCustomer().getPK_CustomerID());
                response.setCusName(order.getCustomer().getCusName());
            }

            responses.add(response);
        }

        return responses;
    }
}

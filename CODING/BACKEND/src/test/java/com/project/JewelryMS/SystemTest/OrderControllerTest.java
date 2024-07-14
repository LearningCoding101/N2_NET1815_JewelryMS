package com.project.JewelryMS.SystemTest;

import com.project.JewelryMS.controller.OrderController;
import com.project.JewelryMS.model.Order.*;
import com.project.JewelryMS.service.Order.OrderHandlerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Mock
    private OrderHandlerService orderHandlerService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saleCreateOrder() {
        CreateOrderWrapper order = new CreateOrderWrapper();
        Long orderId = 1L;
        when(orderHandlerService.handleCreateOrderWithDetails(order.getOrderRequest(), order.getDetailList(), order.getEmail())).thenReturn(orderId);

        ResponseEntity<Long> result = orderController.saleCreateOrder(order);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(orderId, result.getBody());
    }

    @Test
    void getAllOrders() {
        List<OrderResponse> orders = Arrays.asList(new OrderResponse(), new OrderResponse());
        when(orderHandlerService.getAllOrder()).thenReturn(orders);

        ResponseEntity<List<OrderResponse>> result = orderController.getAllOrders();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(orders, result.getBody());
    }



    @Test
    void calculateOrderTotal() {
        List<TotalOrderRequest> requests = Arrays.asList(new TotalOrderRequest(), new TotalOrderRequest());
        TotalOrderResponse response = new TotalOrderResponse();
        when(orderHandlerService.totalOrder(requests)).thenReturn(response);

        ResponseEntity<TotalOrderResponse> result = orderController.calculateOrderTotal(requests);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void confirmCashPayment() {
        ConfirmCashPaymentRequest request = new ConfirmCashPaymentRequest();
        when(orderHandlerService.updateOrderStatusCash(request)).thenReturn(true);

        ResponseEntity<String> result = orderController.confirmCashPayment(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Đơn hàng thanh toán thành công", result.getBody());
    }
}
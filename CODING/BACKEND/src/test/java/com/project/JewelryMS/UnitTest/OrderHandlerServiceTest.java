package com.project.JewelryMS.UnitTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.project.JewelryMS.service.Order.OrderBuyDetailService;
import com.project.JewelryMS.service.Order.OrderDetailService;
import com.project.JewelryMS.service.Order.OrderHandlerService;
import com.project.JewelryMS.service.Order.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.project.JewelryMS.entity.*;
import com.project.JewelryMS.model.Order.*;
import com.project.JewelryMS.repository.*;
import com.project.JewelryMS.service.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

class OrderHandlerServiceTest {

    @InjectMocks
    private OrderHandlerService orderHandlerService;

    @Mock
    private OrderService orderService;

    @Mock
    private ProductSellService productSellService;

    @Mock
    OrderDetailService orderDetailService;

    @Mock
    ProductSellRepository productSellRepository;
    @Mock
    ProductBuyRepository productBuyRepository;
    @Mock
    ProductBuyService productBuyService;
    @Mock
    OrderBuyDetailService orderBuyDetailService;

    @Mock
    PromotionRepository promotionRepository;
    @Mock
    GuaranteeRepository guaranteeRepository;
    @Mock
    OrderDetailRepository orderDetailRepository;
    @Mock
    CustomerRepository customerRepository;
    @Mock
    StaffAccountRepository staffAccountRepository;
    @Mock
    OrderRepository orderRepository;
    @Mock
    ImageService imageService;
    @Mock
    private SimpMessagingTemplate messagingTemplate;
    @Mock
    private EmailService emailService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize ProductSellService with mocked dependencies
        ReflectionTestUtils.setField(orderHandlerService, "productSellService", productSellService);

    }


    @Test
    void testClaimOrder() {
        Long orderId = 1L;
        String userId = "user1";

        assertTrue(orderHandlerService.claimOrder(orderId, userId));
        assertFalse(orderHandlerService.claimOrder(orderId, "user2"));
    }

    @Test
    void testReleaseOrder() {
        Long orderId = 1L;
        String userId = "user1";

        orderHandlerService.claimOrder(orderId, userId);
        assertTrue(orderHandlerService.releaseOrder(orderId, userId));
        assertFalse(orderHandlerService.releaseOrder(orderId, userId));
    }

//    @Test
//    void testHandleCreateOrderWithDetails() {
//        // Prepare test data
//        CreateOrderRequest orderRequest = new CreateOrderRequest();
//        orderRequest.setStatus(1);
//        orderRequest.setPaymentType("Cash");
//        orderRequest.setTotalAmount(1000000.0F);
//        orderRequest.setStaff_ID(1);
//
//        List<CreateOrderDetailRequest> detailRequests = new ArrayList<>();
//        CreateOrderDetailRequest detailRequest = new CreateOrderDetailRequest();
//        detailRequest.setProductID(1L);
//        detailRequest.setQuantity(2);
//        detailRequests.add(detailRequest);
//
//        // Mock StaffAccount
//        StaffAccount staffAccount = new StaffAccount();
//        staffAccount.setStaffID(1);
//        when(staffAccountRepository.findById(1)).thenReturn(Optional.of(staffAccount));
//
//        // Mock ProductSell
//        ProductSell productSell = new ProductSell();
//        productSell.setProductID(1L);
//        productSell.setCost(500000.0F);
//        when(productSellService.getProductSellById(1L)).thenReturn(productSell);
//
//        // Mock OrderService
//        when(orderService.saveOrder(any(PurchaseOrder.class))).thenAnswer(invocation -> {
//            PurchaseOrder savedOrder = invocation.getArgument(0);
//            savedOrder.setPK_OrderID(1L); // Simulate database assigning an ID
//            return savedOrder;
//        });
//
//        // Execute the method
//        Long orderId = orderHandlerService.handleCreateOrderWithDetails(orderRequest, detailRequests, "test@example.com");
//
//        // Verify the results
//        assertNotNull(orderId);
//        assertEquals(1L, orderId);
//
//        // Verify interactions
//        verify(staffAccountRepository).findById(1);
//        verify(productSellService, atLeastOnce()).getProductSellById(1L); // Changed to atLeastOnce()
//        verify(orderService).saveOrder(argThat(order ->
//                order.getStatus() == 1 &&
//                        "Cash".equals(order.getPaymentType()) &&
//                        Math.abs(1000000.0F - order.getTotalAmount()) < 0.001 &&
//                        order.getStaffAccount().getStaffID() == 1 &&
//                        "test@example.com".equals(order.getEmail()) &&
//                        order.getOrderDetails().size() == 1 &&
//                        order.getOrderDetails().iterator().next().getQuantity() == 2 &&
//                        order.getOrderDetails().iterator().next().getProductSell().getProductID() == 1L
//        ));
//    }
    @Test
    void testUpdateOrderStatus() {
        String info = "Thanh toan 1";
        PurchaseOrder order = new PurchaseOrder();
        order.setPK_OrderID(1L);
        order.setEmail("test@example.com");

        when(orderService.getOrderById(1L)).thenReturn(order);
        when(orderService.saveOrder(any())).thenReturn(order);

        orderHandlerService.updateOrderStatus(info);

        verify(orderService, times(1)).saveOrder(argThat(updatedOrder ->
                updatedOrder.getStatus() == 3 && updatedOrder.getPK_OrderID() == 1L
        ));
    }

    @Test
    void testTotalOrder() {
        List<TotalOrderRequest> requests = new ArrayList<>();
        TotalOrderRequest request = new TotalOrderRequest();
        request.setProductSell_ID(1L);
        request.setQuantity(2);
        request.setPromotion_ID(1L);
        requests.add(request);

        ProductSell productSell = new ProductSell();
        productSell.setProductID(1L);
        productSell.setCost(50.0F);

        Promotion promotion = new Promotion();
        promotion.setDiscount(10);

        when(productSellRepository.findById(1L)).thenReturn(Optional.of(productSell));
        when(promotionRepository.findById(1L)).thenReturn(Optional.of(promotion));

        TotalOrderResponse response = orderHandlerService.totalOrder(requests);

        assertEquals(100.0F, response.getSubTotal());
        assertEquals(10.0F, response.getDiscount_Price());
        assertEquals(90.0F, response.getTotal());
    }
}
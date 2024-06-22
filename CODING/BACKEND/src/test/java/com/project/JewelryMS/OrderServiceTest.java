package com.project.JewelryMS;

import com.project.JewelryMS.entity.*;
import com.project.JewelryMS.model.EmailDetail;
import com.project.JewelryMS.model.Order.*;
import com.project.JewelryMS.model.OrderDetail.OrderDetailRequest;
import com.project.JewelryMS.model.OrderDetail.OrderDetailResponse;
import com.project.JewelryMS.model.OrderDetail.OrderPromotionRequest;
import com.project.JewelryMS.model.OrderDetail.OrderTotalRequest;
import com.project.JewelryMS.repository.*;
import com.project.JewelryMS.service.EmailService;
import com.project.JewelryMS.service.Order.OrderBuyDetailService;
import com.project.JewelryMS.service.Order.OrderDetailService;
import com.project.JewelryMS.service.Order.OrderHandlerService;
import com.project.JewelryMS.service.Order.OrderService;
import com.project.JewelryMS.service.ProductBuyService;
import com.project.JewelryMS.service.ProductSellService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderHandlerService orderHandlerService;

    @Mock
    private OrderDetailService orderDetailService;

    @Mock
    private OrderService orderService;

    @Mock
    private ProductSellService productSellService;

    @Mock
    private ProductSellRepository productSellRepository;

    @Mock
    private ProductBuyRepository productBuyRepository;

    @Mock
    private ProductBuyService productBuyService;

    @Mock
    private OrderBuyDetailService orderBuyDetailService;

    @Mock
    private EmailService emailService;

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private GuaranteeRepository guaranteeRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private StaffAccountRepository staffAccountRepository;

    private PurchaseOrder purchaseOrder;
    private OrderDetail orderDetail;
    private OrderBuyDetail orderBuyDetail;

    @BeforeEach
    public void setUp() {
        purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPK_OrderID(1L);
        purchaseOrder.setPurchaseDate(new Date());
        purchaseOrder.setStatus(1);
        purchaseOrder.setTotalAmount(100.0f);

        orderDetail = new OrderDetail();
        orderDetail.setPurchaseOrder(purchaseOrder);
        orderDetail.setQuantity(1);
        orderDetail.setPK_ODID(1L);

        orderBuyDetail = new OrderBuyDetail();
        orderBuyDetail.setPurchaseOrder(purchaseOrder);
    }

    @Test
    public void testCreateOrderWithDetails() {
        List<OrderDetail> orderDetailList = Collections.singletonList(orderDetail);
        when(orderService.saveOrder(any(PurchaseOrder.class))).thenReturn(purchaseOrder);
        Long orderId = orderHandlerService.createOrderWithDetails(purchaseOrder, orderDetailList);
        assertEquals(1L, orderId);
        verify(orderService, times(1)).saveOrder(any(PurchaseOrder.class));
    }

    @Test
    public void testHandleCreateOrderWithDetails() {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setStatus(1);
        createOrderRequest.setPaymentType("Card");
        createOrderRequest.setTotalAmount(100.0f);

        CreateOrderDetailRequest createOrderDetailRequest = new CreateOrderDetailRequest();
        createOrderDetailRequest.setQuantity(1);
        createOrderDetailRequest.setProductID(44L);

        when(productSellService.getProductSellById(anyLong())).thenReturn(new ProductSell());
        when(orderService.saveOrder(any(PurchaseOrder.class))).thenReturn(purchaseOrder);

        Long orderId = orderHandlerService.handleCreateOrderWithDetails(createOrderRequest,
                Collections.singletonList(createOrderDetailRequest), "test@example.com");

        assertEquals(1L, orderId);
        verify(orderService, times(1)).saveOrder(any(PurchaseOrder.class));
    }

    @Test
    public void testCreateOrderWithBuyDetails() {
        List<OrderBuyDetail> orderBuyDetailList = Collections.singletonList(orderBuyDetail);
        when(orderService.saveOrder(any(PurchaseOrder.class))).thenReturn(purchaseOrder);
        Long orderId = orderHandlerService.createOrderWithBuyDetails(purchaseOrder, orderBuyDetailList);
        assertEquals(1L, orderId);
        verify(orderService, times(1)).saveOrder(any(PurchaseOrder.class));
    }

//    @Test
//    public void testHandleCreateOrderBuyWithDetails() {
//        CreateProductBuyRequest createProductBuyRequest = new CreateProductBuyRequest();
//        when(productBuyService.createProductBuy(any(CreateProductBuyRequest.class))).thenReturn(new ProductBuy());
//        when(orderService.saveOrder(any(PurchaseOrder.class))).thenReturn(purchaseOrder);
//
//        Long orderId = orderHandlerService.handleCreateOrderBuyWithDetails(Collections.singletonList(createProductBuyRequest));
//        assertEquals(1L, orderId);
//        verify(orderService, times(1)).saveOrder(any(PurchaseOrder.class));
//    }

    @Test
    public void testAddOrderBuyDetail() {
        when(orderService.getOrderById(anyLong())).thenReturn(purchaseOrder);
        when(productBuyService.getProductBuyById2(anyLong())).thenReturn(new ProductBuy());

        orderHandlerService.addOrderBuyDetail(1L, 1L);
        verify(orderBuyDetailService, times(1)).saveOrderBuyDetail(any(OrderBuyDetail.class));
    }

//    @Test
//    public void testGetAllBuyOrder() {
//        when(orderService.getAllOrders()).thenReturn(Collections.singletonList(purchaseOrder));
//        List<OrderBuyResponse> result = orderHandlerService.getAllBuyOrder();
//        assertEquals(1, result.size());
//    }

    @Test
    public void testGetProductBuyByOrderId() {
        when(orderService.getOrderById(anyLong())).thenReturn(purchaseOrder);
        List<ProductBuyResponse> result = orderHandlerService.getProductBuyByOrderId(1L);
        assertNotNull(result);
    }

    @Test
    public void testAddOrderDetail() {
        when(orderService.getOrderById(anyLong())).thenReturn(purchaseOrder);
        when(productSellService.getProductSellById(anyLong())).thenReturn(new ProductSell());

        orderHandlerService.addOrderDetail(1L, 1L, 1);
        verify(orderDetailService, times(1)).saveOrderDetail(any(OrderDetail.class));
    }

    @Test
    public void testGetAllOrder() {
        when(orderService.getAllOrders()).thenReturn(Collections.singletonList(purchaseOrder));
        List<OrderResponse> result = orderHandlerService.getAllOrder();
        assertEquals(1, result.size());
    }

    @Test
    public void testGetProductByOrderId() {
        when(orderService.getOrderById(anyLong())).thenReturn(purchaseOrder);
        List<ProductResponse> result = orderHandlerService.getProductByOrderId(1L);
        assertNotNull(result);
    }

    @Test
    public void testGenerateEmailOrderTable() {
        when(orderService.getOrderById(anyLong())).thenReturn(purchaseOrder);
        List<ProductResponse> result = orderHandlerService.generateEmailOrderTable(1L);
        assertNotNull(result);
    }

    @Test
    public void testUpdateOrderStatus() {
        when(orderService.getOrderById(anyLong())).thenReturn(purchaseOrder);

        orderHandlerService.updateOrderStatus("Thanh toan 1");
        assertEquals(3, purchaseOrder.getStatus());
    }

    @Test
    public void testSendConfirmationEmail() {
        doNothing().when(emailService).sendConfirmEmail(anyLong(), any(EmailDetail.class));

        orderHandlerService.sendConfirmationEmail(1L, "test@example.com");
        verify(emailService, times(1)).sendConfirmEmail(anyLong(), any(EmailDetail.class));
    }

    @Test
    public void testUpdateOrderStatusCash() {
        ConfirmCashPaymentRequest request = new ConfirmCashPaymentRequest();
        request.setAmount(100.0f);
        request.setTotal(150.0f);
        request.setOrderID(84L);

        when(orderService.getOrderById(anyLong())).thenReturn(purchaseOrder);

        boolean result = orderHandlerService.updateOrderStatusCash(request);
        assertTrue(result);
        assertEquals(3, purchaseOrder.getStatus());
    }

    @Test
    public void testCalculateSubTotal() {
        OrderDetailRequest request = new OrderDetailRequest();
        request.setProductSell_ID(1L);
        request.setQuantity(2);

        ProductSell productSell = new ProductSell();
        productSell.setCost(50.0f);

        when(productSellRepository.findById(anyLong())).thenReturn(Optional.of(productSell));

        Float result = orderHandlerService.calculateSubTotal(request);
        assertEquals(100.0f, result);
    }

    @Test
    public void testCalculateDiscountProduct() {
        OrderPromotionRequest request = new OrderPromotionRequest();
        request.setPromotionID(1L);
        request.setProductSell_ID(1L);
        request.setQuantity(2);

        Promotion promotion = new Promotion();
        promotion.setDiscount(10);

        ProductSell productSell = new ProductSell();
        productSell.setCost(50.0f);

        when(promotionRepository.findById(anyLong())).thenReturn(Optional.of(promotion));
        when(productSellRepository.findById(anyLong())).thenReturn(Optional.of(productSell));

        Float result = orderHandlerService.calculateDiscountProduct(request);
        assertEquals(10.0f, result);
    }


}
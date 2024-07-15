package com.project.JewelryMS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.project.JewelryMS.model.Dashboard.RevenueDateRequest;
import com.project.JewelryMS.model.EmailDetail;
import com.project.JewelryMS.model.Order.*;
import com.project.JewelryMS.model.OrderDetail.*;
import com.project.JewelryMS.service.CustomerService;
import com.project.JewelryMS.service.EmailService;
import com.project.JewelryMS.service.Order.OrderDetailService;
import com.project.JewelryMS.service.Order.OrderHandlerService;
import com.project.JewelryMS.service.QRService;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/order")
//@SecurityRequirement(name = "api")
@CrossOrigin(origins = "*")

public class OrderController {
    @Autowired
    OrderHandlerService orderHandlerService;
    @Autowired
    EmailService emailService;
    @Autowired
    QRService qrService;
    @Autowired
    OrderDetailService orderDetailService;
    @Autowired
    CustomerService customerService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Create a new order
    @PostMapping
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<Long> saleCreateOrder(@RequestBody CreateOrderWrapper order) {
        Long ID = orderHandlerService.handleCreateOrderWithDetails(order.getOrderRequest(), order.getDetailList(), order.getEmail());
        return ResponseEntity.ok(ID);
    }
    @GetMapping("test/{orderId}")
    public List<OrderDetailDTO> getOrderDetails(@PathVariable Long orderId) {
        return orderDetailService.getOrderDetailsByOrderId(orderId);
    }
    //Product Buy Section//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @PostMapping("initialize-PB-order")
    public ResponseEntity<Long> saleCreateOrder(@RequestBody CreateOrderBuyWrapper order) {
        Long ID = orderHandlerService.handleCreateOrderBuyWithDetails(order);
        return ResponseEntity.ok( ID );
    }
    @PostMapping("/claim")
    public ResponseEntity<String> claimOrder(@RequestBody ClaimOrderRequest request) {
        boolean claimed = orderHandlerService.claimOrder(request.getOrderId(), request.getUserId());
        if (claimed) {
            messagingTemplate.convertAndSendToUser(
                    request.getUserId(),
                    "/queue/claim-responses",
                    new ClaimResponse(true, request.getOrderId(), request.getUserId())
            );
            return ResponseEntity.ok("Order claimed successfully");
        } else {
            messagingTemplate.convertAndSendToUser(
                    request.getUserId(),
                    "/queue/claim-responses",
                    new ClaimResponse(false, request.getOrderId(), request.getUserId(), "Order already claimed")
            );
            return ResponseEntity.badRequest().body("Failed to claim order");
        }
    }

    @PostMapping("/release")
    public ResponseEntity<String> releaseOrder(@RequestBody ReleaseOrderRequest request) {
        boolean released = orderHandlerService.releaseOrder(request.getOrderId(), request.getUserId());
        if (released) {
            messagingTemplate.convertAndSend("/topic/new-order", request.getOrderId());
            return ResponseEntity.ok("Order released successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to release order");
        }
    }
    @PostMapping("append-productBuy")
    public ResponseEntity<Void> addOrderBuyDetail(@RequestParam Long orderId, @RequestParam Long productBuyId) {
        orderHandlerService.addOrderBuyDetail(orderId, productBuyId);
        return ResponseEntity.ok().build();
    }

    // Create a new order and generate QR code

    @GetMapping("get-all-orderBuy")
    public ResponseEntity<List<OrderBuyResponse>> getAllBuyOrders() {
        List<OrderBuyResponse> buyOrders = orderHandlerService.getAllBuyOrder();
        return ResponseEntity.ok(buyOrders);
    }

    @GetMapping("get-orderBuy/{id}")
    public ResponseEntity<List<ProductBuyResponse>> getProductBuyByOrderId(@PathVariable(name = "id") Long orderId) {
        List<ProductBuyResponse> productBuys = orderHandlerService.getProductBuyByOrderId(orderId);
        return ResponseEntity.ok(productBuys);
    }

    //Product Buy Section//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @PostMapping(value = "initialize-qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> saleCreateOrderQR(@RequestBody CreateOrderWrapper order) {
        // Generate QR code value
        Long orderID = orderHandlerService.handleCreateOrderWithDetails(order.getOrderRequest(), order.getDetailList(), order.getEmail());
        String value = orderID.toString();
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(qrService.createQR(value));


        // Pass email details and QR code image data to email service
        if(!order.getEmail().trim().isEmpty()){
            EmailDetail emailDetail = new EmailDetail();
            emailDetail.setRecipient(order.getEmail());
            emailDetail.setSubject("Your Order QR Code");
            emailDetail.setMsgBody("Please find your order QR code attached.");

            emailService.sendMailWithEmbeddedImage(emailDetail,
                    qrImage,
                    orderHandlerService.generateEmailOrderTable(orderID));

        }
        System.out.println(orderID);
        messagingTemplate.convertAndSend("/topic/new-order", orderID);

        // Return the QR code image as the HTTP response
        return ResponseEntity.ok(qrImage);
    }
    @PutMapping("append-product")
    public ResponseEntity saleAppendProductToOrder(){
        return ResponseEntity.ok().build();
    }

    // Get a pending order by ID
    @GetMapping("/{id}")
  //  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<ProductResponse>> cashierGetPendingOrder(@PathVariable Long id) {
        List<ProductResponse> productResponses = orderHandlerService.getProductByOrderId(id);
        System.out.println(id);
        messagingTemplate.convertAndSend("/topic/order-claimed", id);

        return ResponseEntity.ok(productResponses);
    }

    // Get all orders
    @GetMapping
   // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderHandlerService.getAllOrder());
    }

    // Complete order payment
    @PutMapping("/payment")
  //  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<Void> cashierCompleteOrder() {
        return ResponseEntity.ok().build();
    }

//    // Calculate order subtotal
//    @PostMapping("/subtotal")
//  //  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
//    public ResponseEntity<Float> calculateTotalAmount(@RequestBody OrderDetailRequest orderDetailRequest) {
//        Float totalAmount = orderHandlerService.calculateSubTotal(orderDetailRequest);
//        return ResponseEntity.ok(totalAmount);
//    }
//
//    // Calculate product discount
//    @PostMapping("/discount")
//   // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
//    public ResponseEntity<Float> calculateDiscount(@RequestBody OrderPromotionRequest orderPromotionRequest) {
//        Float totalAmount = orderHandlerService.calculateDiscountProduct(orderPromotionRequest);
//        return ResponseEntity.ok(totalAmount);
//    }
//
//    // Calculate order details total
//    @PostMapping("/order-details-total")
//   // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
//    public ResponseEntity<Float> calculateOrderDetailTotal(@RequestBody OrderTotalRequest orderTotalRequest) {
//        Float totalAmount = orderHandlerService.TotalOrderDetails(orderTotalRequest);
//        return ResponseEntity.ok(totalAmount);
//    }

    // Calculate order total
    @PostMapping("/total")
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<TotalOrderResponse> calculateOrderTotal(@RequestBody List<TotalOrderRequest> totalOrderRequests) {
        TotalOrderResponse totalOrderResponse = orderHandlerService.totalOrder(totalOrderRequests);
        return ResponseEntity.ok(totalOrderResponse);
    }


    @PostMapping("calculate-customer-points")
    public ResponseEntity<Integer> calculateAndUpdatePoints(@RequestBody CalculatePointsRequest request) {
        return ResponseEntity.ok(customerService.calculateAndUpdatePoints(request));
    }

//    @PostMapping("/calculate-guarantee-end-date")
//    public ResponseEntity<List<OrderDetailResponse>> calculateGuaranteeEndDate(@RequestBody CalculateGuaranteeDateRequest request) {
//        List<OrderDetailResponse> responses =orderHandlerService.calculateAndSetGuaranteeEndDate(request);
//        return new ResponseEntity<>(responses, HttpStatus.OK);
//    }

    @PatchMapping("/cash-confirm")
    public ResponseEntity confirmCashPayment(@RequestBody ConfirmCashPaymentRequest request){
        boolean isUpdated = orderHandlerService.updateOrderStatusCash(request);

        if (isUpdated) {
            return ResponseEntity.ok("Đơn hàng thanh toán thành công");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Đơn hàng thanh toán thất bại");
        }

    }

    @PatchMapping("process-payment-PB")
    public ResponseEntity<String> confirmPaymentProductBuy(@RequestBody ConfirmPaymentPBRequest confirmPaymentPBRequest) {
        if (confirmPaymentPBRequest != null && confirmPaymentPBRequest.getOrder_ID() != null) {
            String response = orderHandlerService.updateOrderBuyStatus(confirmPaymentPBRequest);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Xác nhận quy trình thanh toán thất bại");
        }
    }

    @PutMapping("update-order")
    public ResponseEntity<String> updateOrderPB(@RequestBody UpdateOrderRequest updateOrderRequest){
        if(updateOrderRequest!=null){
            String response = orderHandlerService.updateOrder(updateOrderRequest);
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("updateOrderRequest not Found or Not Supported!!!");
        }
    }

    @GetMapping("search-customer-guarantee")
    public ResponseEntity<List<CustomerOrderGuaranteeResponse>> searchCustomerGuarantee(@RequestParam String search) {
        List<CustomerOrderGuaranteeResponse> response = orderHandlerService.searchCustomerGuarantee(search);
        return ResponseEntity.ok(response);
    }

    @GetMapping("search-order-date-guarantee")
    public ResponseEntity<List<CustomerOrderGuaranteeResponse>> searchCustomerGuarantee(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
        DateFilterOrderDate dateFilterOrderDate = new DateFilterOrderDate(LocalDate.parse(startTime), LocalDate.parse(endTime));
        List<CustomerOrderGuaranteeResponse> response = orderHandlerService.getOrdersByDateRange(dateFilterOrderDate);
        return ResponseEntity.ok(response);
    }
}

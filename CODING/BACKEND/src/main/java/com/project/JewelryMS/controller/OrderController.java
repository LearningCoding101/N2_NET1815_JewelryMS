package com.project.JewelryMS.controller;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.project.JewelryMS.entity.OrderDetail;
import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.model.EmailDetail;
import com.project.JewelryMS.model.Order.*;
import com.project.JewelryMS.model.OrderDetail.*;
import com.project.JewelryMS.service.CustomerService;
import com.project.JewelryMS.service.EmailService;
import com.project.JewelryMS.service.Order.OrderDetailService;
import com.project.JewelryMS.service.Order.OrderHandlerService;
import com.project.JewelryMS.service.QRService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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


    // Create a new order
    @PostMapping
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<String> saleCreateOrder(@RequestBody CreateOrderWrapper order) {
        orderHandlerService.handleCreateOrderWithDetails(order.getOrderRequest(), order.getDetailList(), order.getEmail());
        return ResponseEntity.ok("Create Order Successfully");
    }
    @GetMapping("test/{orderId}")
    public List<OrderDetailDTO> getOrderDetails(@PathVariable Long orderId) {
        return orderDetailService.getOrderDetailsByOrderId(orderId);
    }
    //Product Buy Section//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @PostMapping("initializePB")
    @CrossOrigin(origins = "*")

    public ResponseEntity saleCreateBuyOrder(@ModelAttribute CreateOrderBuyWrapper order){
        orderHandlerService.handleCreateOrderBuyWithDetails(order);
        return ResponseEntity.ok("Create Successfully");
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
        EmailDetail emailDetail = new EmailDetail();
        emailDetail.setRecipient(order.getEmail());
        emailDetail.setSubject("Your Order QR Code");
        emailDetail.setMsgBody("Please find your order QR code attached.");

        emailService.sendMailWithEmbeddedImage(emailDetail,
                qrImage,
                orderHandlerService.generateEmailOrderTable(orderID));

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

    // Calculate order subtotal
    @PostMapping("/subtotal")
  //  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<Float> calculateTotalAmount(@RequestBody OrderDetailRequest orderDetailRequest) {
        Float totalAmount = orderHandlerService.calculateSubTotal(orderDetailRequest);
        return ResponseEntity.ok(totalAmount);
    }

    // Calculate product discount
    @PostMapping("/discount")
   // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<Float> calculateDiscount(@RequestBody OrderPromotionRequest orderPromotionRequest) {
        Float totalAmount = orderHandlerService.calculateDiscountProduct(orderPromotionRequest);
        return ResponseEntity.ok(totalAmount);
    }

    // Calculate order details total
    @PostMapping("/order-details-total")
   // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<Float> calculateOrderDetailTotal(@RequestBody OrderTotalRequest orderTotalRequest) {
        Float totalAmount = orderHandlerService.TotalOrderDetails(orderTotalRequest);
        return ResponseEntity.ok(totalAmount);
    }

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


}

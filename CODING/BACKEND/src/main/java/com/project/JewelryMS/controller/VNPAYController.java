package com.project.JewelryMS.controller;

import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.model.Order.OrderData;
import com.project.JewelryMS.model.Staff.StaffAccountResponse;
import com.project.JewelryMS.service.Order.OrderHandlerService;
import com.project.JewelryMS.service.StaffAccountService;
import com.project.JewelryMS.service.VNPAYservice;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;

@RestController
@RequestMapping("vnpay")
@CrossOrigin(origins = "*")
public class VNPAYController {
    @Autowired
    private VNPAYservice vnPayService;
    @Autowired
    private OrderHandlerService orderHandlerService;
    @Autowired
    private StaffAccountService staffAccountService;
    @CrossOrigin
    @GetMapping({"", "/"})
    public String home(){
        return "createOrder";
    }

    // Chuyển hướng người dùng đến cổng thanh toán VNPAY
    @CrossOrigin
    @PostMapping("/submitOrder")
    public String submidOrder(@RequestParam("amount") int orderTotal,
                              @RequestParam("orderInfo") String orderInfo,
                              HttpServletRequest request){
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(request, orderTotal, orderInfo, baseUrl);
        return vnpayUrl;
    }

    // Sau khi hoàn tất thanh toán, VNPAY sẽ chuyển hướng trình duyệt về URL này
    @GetMapping("/vnpay-payment-return")
    @CrossOrigin
    public ResponseEntity paymentCompleted(HttpServletRequest request, Model model){
        int paymentStatus = vnPayService.orderReturn(request);

        String redirectUrl = paymentStatus == 1 ? "/ordersuccess" : "/orderfail";
        if(paymentStatus == 1){
            orderHandlerService.updateOrderStatus(request.getParameter("vnp_OrderInfo"));
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("http://jewelryms.xyz/staff" + redirectUrl)); // Removed the "/" before redirectUrl
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }
    @PostMapping("/queryqr")
    public ResponseEntity<String> queryTransaction(@RequestParam("orderId") String orderId,
                                                   @RequestParam("transactionDate") String transactionDate,
                                                   @RequestParam("orderInfo") String orderInfo) {
        try {
            String queryResponse = vnPayService.queryTransaction(orderId, transactionDate, orderInfo);
            return ResponseEntity.ok(queryResponse);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Query transaction failed: " + e.getMessage());
        }
    }
    @PostMapping("/refund")
    public ResponseEntity<String> refundOrder(@RequestParam("orderId") String orderId,
                                              @RequestParam("amount") long amount,
                                              @RequestParam("refundInfo") String refundInfo,
                                              @RequestParam("staffId") int staffId) {
        try {
            StaffAccountResponse staffAccount = staffAccountService.getStaffAccountById(staffId);
            if(staffAccount != null) {
                String refundResponse = vnPayService.refund(orderId, amount, refundInfo, staffAccount.getAccountName());
                return ResponseEntity.ok(refundResponse);
            } else {
                return ResponseEntity.badRequest().body("Staff account error");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Refund failed: " + e.getMessage());
        }
    }

}

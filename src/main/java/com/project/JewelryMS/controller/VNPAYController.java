package com.project.JewelryMS.controller;

import com.project.JewelryMS.model.Order.OrderData;
import com.project.JewelryMS.service.Order.OrderHandlerService;
import com.project.JewelryMS.service.VNPAYservice;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
}

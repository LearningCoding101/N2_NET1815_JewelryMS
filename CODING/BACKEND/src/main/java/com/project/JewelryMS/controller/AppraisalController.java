package com.project.JewelryMS.controller;

import com.project.JewelryMS.model.AppraiseProduct;
import com.project.JewelryMS.model.ProductBuy.CreateProductBuyRequest;
import com.project.JewelryMS.model.ProductBuy.ProductResponseBuy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


import com.project.JewelryMS.entity.ProductBuy;
import com.project.JewelryMS.model.ProductBuy.CalculatePBRequest;

import com.project.JewelryMS.service.ProductBuyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class AppraisalController {

    @Autowired
    private ProductBuyService productBuyService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/initialize-product")
    @SendTo("/topic/unappraisedProducts")
    public ProductBuy initializeProduct(CreateProductBuyRequest request) {
        System.out.println(request.toString());
        Long productBuyId = productBuyService.createProductBuy(request);
        return productBuyService.getProductBuyById2(productBuyId);
    }

    @MessageMapping("/submit-appraisal")
    public void submitAppraisal(CalculatePBRequest request) {
        ProductResponseBuy updatedProductBuy = productBuyService.updateProductBuy(request.getId().longValue(), request);
        System.out.println(request);
        // Send the updated ProductBuy to all clients
        messagingTemplate.convertAndSend("/topic/appraisedProducts", updatedProductBuy);
    }
}
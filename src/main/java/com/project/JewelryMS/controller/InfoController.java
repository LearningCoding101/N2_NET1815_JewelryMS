package com.project.JewelryMS.controller;


import com.project.JewelryMS.service.ApiService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Info")
@SecurityRequirement(name = "api")
@CrossOrigin(origins = "*")
public class InfoController {
    @Autowired
    private ApiService apiService;

    private final String goldUrl = "http://api.btmc.vn/api/BTMCAPI/getpricebtmc?key=3kd8ub1llcg9t45hnoh8hmn7t5kc2v";
    @GetMapping("/GoldPrice")
    @CrossOrigin(origins = "http://localhost:5173")
    public String getGoldPrice(){
        System.out.println("Pinged /GoldPrice");
        return apiService.getGoldPrice(goldUrl);
    }

    @GetMapping("/Test")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Float> getGoldPriceTest(){
        String price = apiService.getGoldPricecalculate(goldUrl);
        return ResponseEntity.ok(Float.parseFloat(price));
    }
}


package com.project.JewelryMS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.JewelryMS.model.Image.ImgbbResponse;
import com.project.JewelryMS.model.ImageRequest;
import com.project.JewelryMS.service.ImageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/images")
@SecurityRequirement(name = "api")
@CrossOrigin(origins = "*")
public class ImageController {
    private final String apiKey = "af69290db25a827d6f9ccd1adb503dbe";

    @Autowired
    private ImageService imageService;


    @PostMapping("/uploadByPath")
    public ResponseEntity<String> uploadImageByPath(@RequestBody ImageRequest imageRequest) {
        String url = imageService.uploadImageByPathService(imageRequest.getImage());
        return ResponseEntity.ok(url);
    }

}



package com.project.JewelryMS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.JewelryMS.entity.Category;
import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.model.Image.ImgbbResponse;
import com.project.JewelryMS.model.ImageTestRequest;
import com.project.JewelryMS.model.ProductSell.CreateProductSellRequest;
import com.project.JewelryMS.repository.CategoryRepository;
import com.project.JewelryMS.repository.ProductSellRepository;
import com.project.JewelryMS.service.ImageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
    public ResponseEntity<String> uploadImageByPath(@ModelAttribute ImageTestRequest imageTestRequest) {

        String base64EncodedFile;
        try {
            // Chuyển đổi MultipartFile thành base64
            byte[] fileBytes = imageTestRequest.getImage().getBytes();
            base64EncodedFile = Base64.getEncoder().encodeToString(fileBytes);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to read the file: " + e.getMessage());
        }

        String url = "https://api.imgbb.com/1/upload?key=" + apiKey;
        HttpHeaders headers = new HttpHeaders();
        //set Content type Header. The data sent in the HTTP request body will be URL encoded.
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //A MultiValueMap is created to hold the form data that will be sent in the request body.
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("image", base64EncodedFile);
        // HttpEntity object is created, which encapsulates both the headers and the body of the HTTP request.
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
        //Make Http Request
        RestTemplate restTemplate = new RestTemplate();
        //Send Http Request restTemplate.exchange
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                ImgbbResponse imgbbResponse = mapper.readValue(response.getBody(), ImgbbResponse.class);
                String imageUrl = imgbbResponse.getData().getUrl();
                return ResponseEntity.ok(imageUrl);
            } catch (IOException e) {
                return ResponseEntity.status(500).body("Failed to parse Imgbb response: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Failed to upload image");
        }
    }

}


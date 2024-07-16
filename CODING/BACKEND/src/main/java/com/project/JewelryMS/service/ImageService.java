package com.project.JewelryMS.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.JewelryMS.model.Image.ImgbbResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.URL;
import java.util.Base64;

@Service
public class ImageService {
    public String imgToBase64String(final RenderedImage img, final String formatName)
    {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        try
        {
            ImageIO.write(img, formatName, os);
            return Base64.getEncoder().encodeToString(os.toByteArray());
        }
        catch (final IOException ioe)
        {
            throw new UncheckedIOException(ioe);
        }
    }



    private String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }

    private final String apiKey = "af69290db25a827d6f9ccd1adb503dbe";

    public String uploadImageByPathService(MultipartFile file) {

        String base64EncodedFile;
        try {
            // Chuyển đổi MultipartFile thành base64
            byte[] fileBytes = file.getBytes();
            base64EncodedFile = Base64.getEncoder().encodeToString(fileBytes);
        } catch (IOException e) {
            return "Failed to read the file: ";
        }

        String url = "https://api.imgbb.com/1/upload?key=" + apiKey;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("image", base64EncodedFile);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                ImgbbResponse imgbbResponse = mapper.readValue(response.getBody(), ImgbbResponse.class);
                String imageUrl = imgbbResponse.getData().getUrl();
                return imageUrl;
            } catch (IOException e) {
                return "Failed to parse Imgbb response";
            }
        } else {
            return "Failed to upload image";
        }
    }

    public String uploadImageByPathService(String base64EncodedFile) {
        // Validate the base64 string
        if (base64EncodedFile == null || base64EncodedFile.isEmpty()) {
            return "Invalid base64 string: empty or null";
        }

        // Correct the base64 prefix if necessary
        if (base64EncodedFile.startsWith("data:image/")) {
            base64EncodedFile = base64EncodedFile.substring(base64EncodedFile.indexOf(",") + 1);
        }

        // Check if the base64 string is valid
        try {
            Base64.getDecoder().decode(base64EncodedFile);
        } catch (IllegalArgumentException e) {
            return "Invalid base64 string: unable to decode";
        }

        String url = "https://api.imgbb.com/1/upload?key=" + apiKey;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("image", base64EncodedFile);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                ImgbbResponse imgbbResponse = mapper.readValue(response.getBody(), ImgbbResponse.class);
                String imageUrl = imgbbResponse.getData().getUrl();
                return imageUrl;
            } catch (IOException e) {
                return "Failed to parse Imgbb response";
            }
        } else {
            return "Failed to upload image: " + response.getBody();
        }
    }

}

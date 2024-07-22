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

    // Khóa API để sử dụng dịch vụ imgbb
    private final String apiKey = "af69290db25a827d6f9ccd1adb503dbe";

    // Phương thức tải ảnh lên imgbb từ một tệp MultipartFile
    public String uploadImageByPathService(MultipartFile file) {

        String base64EncodedFile;
        try {
            // Chuyển đổi MultipartFile thành chuỗi base64
            byte[] fileBytes = file.getBytes();
            base64EncodedFile = Base64.getEncoder().encodeToString(fileBytes);
        } catch (IOException e) {
            return "Failed to read the file: "; // Thông báo lỗi nếu không đọc được tệp
        }

        String url = "https://api.imgbb.com/1/upload?key=" + apiKey; // URL API với khóa API
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // Đặt kiểu nội dung là URL encoded
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("image", base64EncodedFile); // Thêm chuỗi base64 vào body của request
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers); // Tạo đối tượng HttpEntity với body và headers
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class); // Gửi request POST

        if (response.getStatusCode().is2xxSuccessful()) { // Kiểm tra nếu request thành công
            try {
                ObjectMapper mapper = new ObjectMapper();
                ImgbbResponse imgbbResponse = mapper.readValue(response.getBody(), ImgbbResponse.class); // Chuyển đổi response JSON thành đối tượng ImgbbResponse
                String imageUrl = imgbbResponse.getData().getUrl(); // Lấy URL của ảnh đã tải lên
                return imageUrl;
            } catch (IOException e) {
                return "Failed to parse Imgbb response"; // Thông báo lỗi nếu không thể phân tích response
            }
        } else {
            return "Failed to upload image"; // Thông báo lỗi nếu request thất bại
        }
    }

    // Phương thức tải ảnh lên imgbb từ một chuỗi base64
    public String uploadImageByPathService(String base64EncodedFile) {
        if (base64EncodedFile == null || base64EncodedFile.isEmpty()) {
            return "Invalid base64 string: empty or null"; // Thông báo lỗi nếu chuỗi base64 trống hoặc null
        }

        // Xóa phần header "data:image/" nếu có
        if (base64EncodedFile.startsWith("data:image/")) {
            base64EncodedFile = base64EncodedFile.substring(base64EncodedFile.indexOf(",") + 1);
        }

        try {
            Base64.getDecoder().decode(base64EncodedFile); // Kiểm tra xem chuỗi base64 có hợp lệ không
        } catch (IllegalArgumentException e) {
            return "Invalid base64 string: unable to decode"; // Thông báo lỗi nếu chuỗi base64 không hợp lệ
        }

        String url = "https://api.imgbb.com/1/upload?key=" + apiKey; // URL API với khóa API
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // Đặt kiểu nội dung là URL encoded
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("image", base64EncodedFile); // Thêm chuỗi base64 vào body của request
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers); // Tạo đối tượng HttpEntity với body và headers
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response;

        try {
            response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class); // Gửi request POST
        } catch (Exception e) {
            return "Failed to upload image: " + e.getMessage(); // Thông báo lỗi nếu có ngoại lệ khi gửi request
        }

        if (response.getStatusCode().is2xxSuccessful()) { // Kiểm tra nếu request thành công
            try {
                ObjectMapper mapper = new ObjectMapper();
                ImgbbResponse imgbbResponse = mapper.readValue(response.getBody(), ImgbbResponse.class); // Chuyển đổi response JSON thành đối tượng ImgbbResponse
                String imageUrl = imgbbResponse.getData().getUrl(); // Lấy URL của ảnh đã tải lên
                return imageUrl;
            } catch (IOException e) {
                return "Failed to parse Imgbb response: " + e.getMessage(); // Thông báo lỗi nếu không thể phân tích response
            }
        } else {
            return "Failed to upload image: " + response.getBody(); // Thông báo lỗi nếu request thất bại
        }
    }

}

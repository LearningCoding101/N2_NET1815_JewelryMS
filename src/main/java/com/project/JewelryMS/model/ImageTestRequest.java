package com.project.JewelryMS.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
@Data
public class ImageTestRequest {
    String name;
    String password;
    MultipartFile image;
}

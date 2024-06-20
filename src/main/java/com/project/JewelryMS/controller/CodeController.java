package com.project.JewelryMS.controller;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.project.JewelryMS.service.EmailService;
import com.project.JewelryMS.service.QRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.image.BufferedImage;

@RestController
@RequestMapping("code")
@CrossOrigin(origins = "*")
public class CodeController {
    @Autowired
    QRService qrService;

    @GetMapping(value = "create", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> barcode(@RequestParam String value){


        return ResponseEntity.ok(MatrixToImageWriter.toBufferedImage(qrService.createQR(value)));
    }
}

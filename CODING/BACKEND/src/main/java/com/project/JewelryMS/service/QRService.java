package com.project.JewelryMS.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

@Service
public class QRService {

    public BitMatrix createQR(String value){
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = barcodeWriter.encode(value,
                    BarcodeFormat.QR_CODE,
                    200,
                    200);
        } catch (WriterException e) {
            System.out.println("Error occured at QRservice method createQR:" + e.getMessage());
        }
        return bitMatrix;
    }

}

package com.project.JewelryMS.model.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateOrderBuyWrapper {
    private List<CreateProductBuyRequest> order;

}

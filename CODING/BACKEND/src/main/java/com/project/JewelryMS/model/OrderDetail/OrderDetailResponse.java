package com.project.JewelryMS.model.OrderDetail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.entity.PurchaseOrder;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse {
    Long PK_ODID;
    Long productSell_ID;
    Long purchaseOrder_ID;
    Integer quantity;
    Timestamp guaranteeEndDate;
}

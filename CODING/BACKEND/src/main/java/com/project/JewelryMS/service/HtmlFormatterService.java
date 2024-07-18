package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Category;
import com.project.JewelryMS.entity.OrderDetail;
import com.project.JewelryMS.model.Order.ProductResponse;
import com.project.JewelryMS.model.OrderDetail.OrderDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HtmlFormatterService {
    @Autowired
    CategoryService categoryService;
    public String CreateTableRowOrder(String name,
                                      String cost,
                                      String chi,
                                      String carat,
                                      String manufacturer,
                                      String manufactureCost,
                                      String category,
                                      String quantity
                                      ){

        return "<tr>" +
                "<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>" + name + "</td>" +
                "<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>" + cost + "</td>" +
                "<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>" + chi + "</td>" +
                "<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>" + carat + "</td>" +
                "<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>" + manufacturer + "</td>" +
                "<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>" + manufactureCost + "</td>" +
                "<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>" + category + "</td>" +
                "<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>" + quantity + "</td>" +
                "</tr>";
    }
    public String DefaultOrderTable(List<ProductResponse> list){
        String tableStart = "<div style='overflow: auto; width: 100%;' role='region' tabindex='0'>" +
                "<table align='center' style='border: 2px solid #dededf; height: 100%; width: 100%; table-layout: fixed; border-collapse: collapse; border-spacing: 1px; text-align: center;'>" +
                "<caption style='caption-side: top; text-align: center;'>Thanh toán</caption>" +
                "<thead>" +
                "<tr>" +
                "<th style='border: 2px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;'>Tên</th>" +
                "<th style='border: 2px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;'>Giá</th>" +
                "<th style='border: 2px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;'>Chỉ</th>" +
                "<th style='border: 2px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;'>Carat</th>" +
                "<th style='border: 2px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;'>Nhà sản xuất</th>" +
                "<th style='border: 2px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;'>Phí Gia Công</th>" +
                "<th style='border: 2px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;'>Thể Loại</th>" +
                "<th style='border: 2px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;'>Số lượng</th>" +
                "</tr>" +
                "</thead>" +
                "<tbody>";

        //Content
        String categoryName = "";
        for(ProductResponse item : list){
            String categoryIfNull = "";
            Category category = categoryService.readCategoryById(item.getCategory_id());
            if (category != null) {
                categoryName = category.getName();
            }
            tableStart += CreateTableRowOrder(
                    item.getName(),
                    String.valueOf(item.getCost()),
                    String.valueOf(item.getChi()),
                    String.valueOf(item.getCarat()),
                    String.valueOf(item.getManufacturer()),
                    String.valueOf(item.getManufactureCost()),
                    categoryName, // categoryName will be empty string if category is null
                    String.valueOf(item.getQuantity())
            );
        }


        //endContent


        tableStart += " </tbody>\n" +
                "</table>\n" +
                "\n" +
                "</div>";


        return tableStart;
    }


    public String CreateTableRowOrderConfirm(String name,
                                             String cost,
                                             String chi,
                                             String carat,
                                             String manufacturer,
                                             String manufactureCost,
                                             String category,
                                             String quantity,
                                             String guaranteeTill,
                                             String coverageType,
                                             String period) {

        return "<tr>" +
                "<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>" + name + "</td>" +
                "<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>" + cost + "</td>" +
                "<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>" + chi + "</td>" +
                "<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>" + carat + "</td>" +
                "<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>" + manufacturer + "</td>" +
                "<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>" + manufactureCost + "</td>" +
                "<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>" + category + "</td>" +
                "<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>" + quantity + "</td>" +
                "<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>" + guaranteeTill + "</td>" +
                "<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>" + coverageType + "</td>" +
                "<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>" + period + "</td>" +
                "</tr>";
    }
    public String createRefundDetailTable(OrderDetailDTO orderDetail, float refundAmount) {
        StringBuilder htmlTable = new StringBuilder();

        // Start table structure
        htmlTable.append("<div style='overflow: auto; width: 100%;' role='region' tabindex='0'>")
                .append("<table align='center' style='border: 2px solid #dededf; width: 100%; table-layout: auto; border-collapse: collapse; border-spacing: 1px; text-align: center;'>")
                .append("<caption style='caption-side: top; text-align: center;'>Refund Details</caption>")
                .append("<thead>")
                .append("<tr>")
                .append("<th style='border: 2px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;'>Product Name</th>")
                .append("<th style='border: 2px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;'>Original Quantity</th>")
                .append("<th style='border: 2px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;'>Original Price</th>")
                .append("<th style='border: 2px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;'>Refund Amount</th>")
                .append("</tr>")
                .append("</thead>")
                .append("<tbody>");

        // Add row for the refund details
        htmlTable.append("<tr>")
                .append("<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>").append(orderDetail.getPName()).append("</td>")
                .append("<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>").append(orderDetail.getQuantity()).append("</td>")
                .append("<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>").append(String.format("%.2f", orderDetail.getCost())).append("</td>")
                .append("<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>").append(String.format("%.2f", refundAmount)).append("</td>")
                .append("</tr>");

        // End table structure
        htmlTable.append("</tbody>")
                .append("</table>")
                .append("</div>");

        return htmlTable.toString();
    }
    public String createOrderDetailTableConfirm(List<OrderDetailDTO> orderDetails) {
        StringBuilder htmlTable = new StringBuilder();

        // Start table structure
        htmlTable.append("<div style='overflow: auto; width: 100%;' role='region' tabindex='0'>")
                .append("<table align='center' style='border: 2px solid #dededf; width: 100%; table-layout: auto; border-collapse: collapse; border-spacing: 1px; text-align: center;'>")
                .append("<caption style='caption-side: top; text-align: center;'>Order Details</caption>")
                .append("<thead>")
                .append("<tr>")
                .append("<th style='border: 2px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;'>Product Name</th>")
                .append("<th style='border: 2px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;'>Quantity</th>")
                .append("<th style='border: 2px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;'>Guarantee End Date</th>")
                .append("<th style='border: 2px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;'>Coverage</th>")
                .append("<th style='border: 2px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;'>Policy Type</th>")
                .append("<th style='border: 2px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;'>Warranty Period (Months)</th>")
                .append("<th style='border: 2px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;'>Carat</th>")
                .append("<th style='border: 2px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;'>Cost</th>")
                .append("<th style='border: 2px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;'>Gemstone Type</th>")
                .append("<th style='border: 2px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;'>Image</th>")
                .append("<th style='border: 2px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;'>Metal Type</th>")
                .append("<th style='border: 2px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;'>Manufacturer</th>")
                .append("<th style='border: 2px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;'>ManufactureCost</th>")
                .append("</tr>")
                .append("</thead>")
                .append("<tbody>");

        // Add rows for each OrderDetailDTO
        for (OrderDetailDTO orderDetail : orderDetails) {
            htmlTable.append("<tr>")
                    .append("<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>").append(orderDetail.getPName()).append("</td>")
                    .append("<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>").append(orderDetail.getQuantity()).append("</td>")
                    .append("<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>").append(orderDetail.getGuaranteeEndDate()).append("</td>")
                    .append("<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>").append(orderDetail.getCoverage()).append("</td>")
                    .append("<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>").append(orderDetail.getPolicyType()).append("</td>")
                    .append("<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>").append(orderDetail.getWarrantyPeriodMonth()).append("</td>")
                    .append("<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>").append(orderDetail.getCarat()).append("</td>")
                    .append("<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>").append(orderDetail.getCost()).append("</td>")
                    .append("<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>").append(orderDetail.getGemstoneType()).append("</td>")
                    .append("<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>").append("<img src='").append(orderDetail.getImage()).append("' alt='Product Image' style='max-width: 100px;'>").append("</td>")
                    .append("<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>").append(orderDetail.getMetalType()).append("</td>")
                    .append("<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>").append(orderDetail.getManufacturer()).append("</td>")
                    .append("<td style='border: 2px solid #dededf; background-color: #ffffff; color: #000000; padding: 5px;'>").append(orderDetail.getManufactureCost()).append("</td>")
                    .append("</tr>");
        }

        // End table structure
        htmlTable.append("</tbody>")
                .append("</table>")
                .append("</div>");

        return htmlTable.toString();
    }

}

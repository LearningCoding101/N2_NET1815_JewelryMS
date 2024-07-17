package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.OrderDetail;
import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.entity.ProductSell_Promotion;
import com.project.JewelryMS.entity.Promotion;
import com.project.JewelryMS.model.Promotion.CreatePromotionRequest;
import com.project.JewelryMS.model.Promotion.PromotionRequest;
import com.project.JewelryMS.model.Promotion.PromotionResponse;
import com.project.JewelryMS.repository.OrderDetailRepository;
import com.project.JewelryMS.repository.ProductSellPromotionRepository;
import com.project.JewelryMS.repository.ProductSellRepository;
import com.project.JewelryMS.repository.PromotionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class PromotionService {
    @Autowired
    PromotionRepository promotionRepository;

    @Autowired
    ProductSellPromotionRepository productSellPromotionRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    ProductSellRepository productSellRepository;
    private String formatDate(Date date) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormatter.format(date);
    }

    private PromotionResponse toPromotionResponse(Promotion promotion) {
        PromotionResponse promotionResponse = new PromotionResponse();
        promotionResponse.setPromotionID(promotion.getPK_promotionID());
        promotionResponse.setCode(promotion.getCode());
        promotionResponse.setDescription(promotion.getDescription());

        // Check if discount is null before accessing intValue()
        if (promotion.getDiscount() != null) {
            promotionResponse.setDiscount(promotion.getDiscount());
        } else {
            // Handle null case (optional based on your business logic)
            promotionResponse.setDiscount(0); // Or set a default value
        }

        // Set default values for startDate and endDate if they are null
        if (promotion.getStartDate() != null) {
            promotionResponse.setStartDate(formatDate(promotion.getStartDate()));
        } else {
            promotionResponse.setStartDate(null); // Or set a default date
        }

        if (promotion.getEndDate() != null) {
            promotionResponse.setEndDate(formatDate(promotion.getEndDate()));
        } else {
            promotionResponse.setEndDate(null); // Or set a default date
        }

        promotionResponse.setStatus(promotion.isStatus());
        promotionResponse.setStatus(promotion.isStatus());

        List<Long> listPromotion = promotionRepository.findProductSellIdsByPromotionId(promotion.getPK_promotionID());
        List<String> promotionIds = listPromotion.stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
        promotionResponse.setProductSell_id(promotionIds);
        return promotionResponse;
    }

    public List<PromotionResponse> ReadAllPromotionWithProductID(){
        List<Promotion> promotionList = promotionRepository.findAllPromotion();
        return promotionList.stream()
                .map(this::toPromotionResponse)
                .collect(Collectors.toList());
    }


    public Promotion createPromotion(CreatePromotionRequest createPromotionRequest) {
        Promotion promotion = new Promotion();
        promotion.setCode(createPromotionRequest.getCode());
        promotion.setDescription(createPromotionRequest.getDescription());

        Date startDate = java.sql.Date.valueOf(createPromotionRequest.getStartDate());
        Date endDate = java.sql.Date.valueOf(createPromotionRequest.getEndDate());

        promotion.setStartDate(startDate);
        promotion.setEndDate(endDate);

        if (createPromotionRequest.getDiscount() >= 0 && createPromotionRequest.getDiscount() <= 100) {
            promotion.setDiscount(createPromotionRequest.getDiscount());
        } else {
            throw new RuntimeException("Can't Set Promotion <0 and >100");
        }
        validateDateOrder(promotion.getStartDate(), promotion.getEndDate());

        // Get the current date
        Date currentDate = new Date();

        // Format the date as "YYYY-MM-DD"
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(currentDate);
        Date parsedDate = new Date();
        try {
            parsedDate = dateFormat.parse(formattedDate);
        } catch (ParseException e) {
            System.err.println("Error parsing date: " + e.getMessage());
        }
        promotion.setStatus(promotion.getEndDate().equals(parsedDate) || promotion.getEndDate().after(parsedDate));

        Promotion savedPromotion = promotionRepository.save(promotion);

        List<ProductSell> productSells = productSellRepository.findAllById(createPromotionRequest.getProductSell_IDs());
        List<ProductSell_Promotion> productSellPromotions = productSells.stream()
                .map(productSell -> {
                    ProductSell_Promotion psp = new ProductSell_Promotion();
                    psp.setProductSell(productSell);
                    psp.setPromotion(savedPromotion);
                    return psp;
                })
                .collect(Collectors.toList());

        productSellPromotionRepository.saveAll(productSellPromotions);

        return savedPromotion;
    }


    public PromotionResponse getPromotionById(long PK_promotionID) {
        Promotion promotion = promotionRepository.findById(PK_promotionID).orElse(null);
        return promotion != null ? toPromotionResponse(promotion) : null;
    }

    public List<PromotionResponse> getPromotionByCode(String code) {
        List<Promotion> promotions = promotionRepository.findByCode(code);
        return promotions.stream()
                .map(this::toPromotionResponse)
                .collect(Collectors.toList());
    }

    public List<PromotionResponse> readAllPromotion() {
        List<Promotion> allPromotions = promotionRepository.findAll();
        return allPromotions.stream()
                .map(this::toPromotionResponse)
                .collect(Collectors.toList());
    }

    public List<PromotionResponse> readAllActivePromotion() {
        List<Promotion> activePromotions = promotionRepository.findByStatus(true);
        return activePromotions.stream()
                .filter(promotion -> promotion.getStartDate() != null && promotion.getEndDate() != null) // Filter out promotions with null dates
                .map(this::toPromotionResponse)
                .collect(Collectors.toList());
    }

    public List<PromotionResponse> getPromotionsByDate(Date targetDate) {
        List<Promotion> allPromotions = promotionRepository.findAll();
        updatePromotionStatusBasedOnEndDate(allPromotions);

        return allPromotions.stream()
                .filter(promotion -> !promotion.getStartDate().after(targetDate) && !promotion.getEndDate().before(targetDate))
                .map(this::toPromotionResponse)
                .collect(Collectors.toList());
    }

    private void updatePromotionStatusBasedOnEndDate(List<Promotion> promotions) {
        Date currentTime = new Date();

        for (Promotion promotion : promotions) {
            if (promotion.getEndDate().before(currentTime)) {
                promotion.setStatus(false);
                promotionRepository.save(promotion);
            }
        }
    }

    public void updatePromotionDetails(PromotionRequest promotionRequest) {
        Optional<Promotion> promotionUpdate = promotionRepository.findById(promotionRequest.getPK_promotionID());
        if (promotionUpdate.isPresent()) {
            Promotion promotion = promotionUpdate.get();

            Date startDate = java.sql.Date.valueOf(promotionRequest.getStartDate());
            Date endDate = java.sql.Date.valueOf(promotionRequest.getEndDate());

            validateEndDate(promotion, endDate); // Ensure this validation happens before setting endDate
            validateStartDate(promotion, startDate);
            validateDateOrder(startDate, endDate);
            validateDateDifference(startDate, endDate);

            promotion.setCode(promotionRequest.getCode());
            promotion.setDescription(promotionRequest.getDescription());
            promotion.setStartDate(startDate);
            promotion.setEndDate(endDate);

            if (promotionRequest.getDiscount() >= 0 && promotionRequest.getDiscount() <= 100) {
                promotion.setDiscount(promotionRequest.getDiscount());
                promotionRepository.save(promotion);
            } else {
                throw new RuntimeException("Discount must be between 0 and 100");
            }
        }
    }

    private void validateEndDate(Promotion promotion, Date newEndDate) {
        if (promotion.getEndDate() != null && promotion.getEndDate().after(newEndDate)) {
            throw new IllegalArgumentException("End date cannot be earlier than the current end date");
        }
    }

    private void validateStartDate(Promotion promotion, Date newStartDate) {
        Date currentTime = new Date();

        if (newStartDate.before(currentTime)) {
            throw new IllegalArgumentException("Start date cannot be earlier than the current time");
        }
    }

    private void validateDateOrder(Date startDate, Date endDate) {
        if (endDate.before(startDate)) {
            throw new IllegalArgumentException("End date cannot be before the start date");
        }
    }

    private void validateDateDifference(Date startDate, Date endDate) {
        long diffInDays = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);
        if (diffInDays < 3) {
            throw new IllegalArgumentException("End date should be apart from the start date for at least 3 days");
        }
    }

    //Now possible to disable promotions in order details.
    @Transactional
    public void deletePromotionById(Long promotionId) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new IllegalArgumentException("Promotion not found with ID: " + promotionId));

        // Check if order details associated with the promotion are not null
        if (promotion.getOrderDetails() != null) {
            for (OrderDetail orderDetail : promotion.getOrderDetails()) {
                orderDetail.setPromotion(null); // Remove the association
                orderDetailRepository.save(orderDetail); // Update OrderDetail entity
            }
        }

        promotion.setStatus(false);
        promotionRepository.save(promotion);
    }

}
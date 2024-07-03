package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Promotion;
import com.project.JewelryMS.model.Promotion.CreatePromotionRequest;
import com.project.JewelryMS.model.Promotion.PromotionRequest;
import com.project.JewelryMS.model.Promotion.PromotionResponse;
import com.project.JewelryMS.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private String formatDate(Date date) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormatter.format(date);
    }

    private PromotionResponse toPromotionResponse(Promotion promotion) {
        PromotionResponse promotionResponse = new PromotionResponse();
        promotionResponse.setPromotionID(promotion.getPK_promotionID());
        promotionResponse.setCode(promotion.getCode());
        promotionResponse.setDescription(promotion.getDescription());
        promotionResponse.setDiscount(promotion.getDiscount());
        promotionResponse.setStartDate(formatDate(promotion.getStartDate()));
        promotionResponse.setEndDate(formatDate(promotion.getEndDate()));
        promotionResponse.setStatus(promotion.isStatus());
        List<Long> listPromotion = promotionRepository.findProductSellIdsByPromotionId(promotion.getPK_promotionID());
        List<String> promotionIds = new ArrayList<>();
        for (Long promotionId : listPromotion) {
            promotionIds.add(String.valueOf(promotionId));
        }
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

        Date currentDate = new Date();
        promotion.setStatus(!(promotion.getEndDate().before(currentDate) || promotion.getEndDate().equals(currentDate)));

        return promotionRepository.save(promotion);
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

            validateEndDate(promotion, endDate);
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
                throw new RuntimeException("Can't Set Promotion <0 and >100");
            }
        }
    }

    private void validateEndDate(Promotion promotion, Date newEndDate) {
        if (promotion.getEndDate().after(newEndDate)) {
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

    public void deletePromotionById(long id) {
        Optional<Promotion> promotionUpdate = promotionRepository.findById(id);
        promotionUpdate.ifPresent(promotion -> {
            promotion.setStatus(false);
            promotionRepository.save(promotion);
        });
    }
}



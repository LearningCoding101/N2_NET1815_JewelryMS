package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Promotion;
import com.project.JewelryMS.model.Promotion.CreatePromotionRequest;
import com.project.JewelryMS.model.Promotion.PromotionRequest;
import com.project.JewelryMS.model.Promotion.PromotionResponse;
import com.project.JewelryMS.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    // Helper method to format the date
    private String formatDate(LocalDateTime date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH");
        String formattedDate = date.toLocalDate().format(dateFormatter);
        String formattedTime = date.toLocalTime().format(timeFormatter);
        String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        return formattedDate;
//                + " " + formattedTime + " (" + dayOfWeek + ")";
        //Date formatted changed by demand from the Front-end team
    }

    // Helper method to convert a Promotion entity to a PromotionResponse DTO
    private PromotionResponse toPromotionResponse(Promotion promotion) {
        PromotionResponse promotionResponse = new PromotionResponse();
        promotionResponse.setPromotionID(promotion.getPK_promotionID());
        promotionResponse.setCode(promotion.getCode());
        promotionResponse.setDescription(promotion.getDescription());
        promotionResponse.setDiscount(promotion.getDiscount());
        promotionResponse.setStartDate(formatDate(promotion.getStartDate()));
        promotionResponse.setEndDate(formatDate(promotion.getEndDate()));
        promotionResponse.setStatus(promotion.isStatus());
        // Assuming you have a method in your repository to get productSellIds by promotionId
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
        LocalDateTime startDate = LocalDateTime.parse(createPromotionRequest.getStartDate(), formatter);
        LocalDateTime endDate = LocalDateTime.parse(createPromotionRequest.getEndDate(), formatter);

        promotion.setCode(createPromotionRequest.getCode());
        promotion.setDescription(createPromotionRequest.getDescription());
        promotion.setStartDate(startDate);
        promotion.setEndDate(endDate);
        if(createPromotionRequest.getDiscount()>=0 && createPromotionRequest.getDiscount()<=100) {
            promotion.setDiscount(createPromotionRequest.getDiscount());
        }else{
            throw new RuntimeException("Can't Set Promotion <0 and >100");
        }
        validateDateOrder(promotion.getStartDate(), promotion.getEndDate());

        LocalDateTime currentDate = LocalDateTime.now();
        promotion.setStatus(!promotion.getEndDate().isBefore(currentDate));

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

    public List<PromotionResponse> getPromotionsByDate(LocalDateTime targetDate) {
        List<Promotion> allPromotions = promotionRepository.findAll();
        updatePromotionStatusBasedOnEndDate(allPromotions);

        return allPromotions.stream()
                .filter(promotion -> (promotion.getStartDate().isBefore(targetDate) || promotion.getStartDate().isEqual(targetDate))
                        && (promotion.getEndDate().isAfter(targetDate) || promotion.getEndDate().isEqual(targetDate)))
                .map(this::toPromotionResponse)
                .collect(Collectors.toList());
    }

    private void updatePromotionStatusBasedOnEndDate(List<Promotion> promotions) {
        LocalDateTime currentTime = LocalDateTime.now();

        for (Promotion promotion : promotions) {
            if (promotion.getEndDate().isBefore(currentTime)) {
                promotion.setStatus(false);
                promotionRepository.save(promotion);
            }
        }
    }

    public void updatePromotionDetails(PromotionRequest promotionRequest) {
        Optional<Promotion> promotionUpdate = promotionRepository.findById(promotionRequest.getPK_promotionID());
        if (promotionUpdate.isPresent()) {
            Promotion promotion = promotionUpdate.get();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
            LocalDateTime startDate = LocalDateTime.parse(promotionRequest.getStartDate(), formatter);
            LocalDateTime endDate = LocalDateTime.parse(promotionRequest.getEndDate(), formatter);

            validateEndDate(promotion, endDate);
            validateStartDate(promotion, startDate);
            validateDateOrder(startDate, endDate);
            validateDateDifference(startDate, endDate);

            promotion.setCode(promotionRequest.getCode());
            promotion.setDescription(promotionRequest.getDescription());
            promotion.setStartDate(startDate);
            promotion.setEndDate(endDate);
            if(promotionRequest.getDiscount()>=0 && promotionRequest.getDiscount()<=100) {
                promotion.setDiscount(promotionRequest.getDiscount());
                promotionRepository.save(promotion);
            }else{
                throw new RuntimeException("Can't Set Promotion <0 and >100");
            }
        }
    }

    private void validateEndDate(Promotion promotion, LocalDateTime newEndDate) {
        if (promotion.getEndDate().isAfter(newEndDate)) {
            throw new IllegalArgumentException("End date cannot be earlier than the current end date");
        }
    }

    private void validateStartDate(Promotion promotion, LocalDateTime newStartDate) {
        LocalDateTime currentTime = LocalDateTime.now();

        if (newStartDate.isBefore(currentTime)) {
            throw new IllegalArgumentException("Start date cannot be earlier than the current time");
        }
    }

    private void validateDateOrder(LocalDateTime startDate, LocalDateTime endDate) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before the start date");
        }
    }

//    private void validateDateDifference(LocalDateTime startDate, LocalDateTime endDate) {
//        long diffInMilliseconds = Math.abs(endDate.getDayOfYear() - startDate.getDayOfYear());
//        long diffInDays = TimeUnit.DAYS.convert(diffInMilliseconds, TimeUnit.MILLISECONDS);
//        if (diffInDays < 3) {
//            throw new IllegalArgumentException("End date should be apart from the start date for at least 3 days");
//        }
//    }//This method is incorrectly performed

    private void validateDateDifference(LocalDateTime startDate, LocalDateTime endDate) {
        long diffInDays = ChronoUnit.DAYS.between(startDate, endDate);
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



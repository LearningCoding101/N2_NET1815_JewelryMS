package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Performance;
import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.model.Performance.CreatePerformanceRequest;
import com.project.JewelryMS.model.Performance.DeletePerformanceRequest;
import com.project.JewelryMS.model.Performance.PerformanceRequest;
import com.project.JewelryMS.model.Performance.PerformanceResponse;

import com.project.JewelryMS.repository.PerformanceRepository;
import com.project.JewelryMS.repository.StaffAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PerformanceService {
    @Autowired
    PerformanceRepository performanceRepository;

    @Autowired
    private StaffAccountRepository staffAccountRepository;

    // Helper method to convert a Performance entity to a PerformanceResponse DTO
    private PerformanceResponse toPerformanceResponse(Performance performance) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH");
        String formattedDate = performance.getDate().toLocalDate().format(dateFormatter);
        String formattedTime = performance.getDate().toLocalTime().format(timeFormatter);
        String dayOfWeek = performance.getDate().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        return new PerformanceResponse(
                performance.getPK_performanceID(),
                performance.getStaffAccount().getStaffID(),
                formattedDate + " " + formattedTime + " (" + dayOfWeek + ")",
                performance.getSalesMade(),
                performance.getRevenueGenerated(),
                performance.getCustomerSignUp(),
                performance.isStatus()
        );
    }

    public PerformanceResponse createPerformanceReport(CreatePerformanceRequest createPerformanceRequest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
        LocalDateTime date = LocalDateTime.parse(createPerformanceRequest.getDate(), formatter);

        Optional<StaffAccount> staffOptional = staffAccountRepository.findById(Math.toIntExact((long) createPerformanceRequest.getStaffID()));
        if(staffOptional.isPresent()) {
            StaffAccount account = staffOptional.get();
            if (account != null) {
                Performance performance = new Performance();

                performance.setDate(date);
                performance.setSalesMade(createPerformanceRequest.getSalesMade());
                performance.setRevenueGenerated(createPerformanceRequest.getRevenueGenerated());
                performance.setCustomerSignUp(createPerformanceRequest.getCustomerSignUp());

                performance.setStaffAccount(account);

                Performance newPerformance = performanceRepository.save(performance);
                return toPerformanceResponse(newPerformance);
            }else{
                throw new RuntimeException("...");
            }
        }else{
            throw new RuntimeException("Staff account ID not exist");
        }
    }

    public PerformanceResponse getPerformanceById(long id) {
        Performance performance = performanceRepository.findByPerformanceId(id);
        return performance != null ? toPerformanceResponse(performance) : null;
    }

    public PerformanceResponse getPerformanceByStaffID(long id) {
        Performance performance = performanceRepository.findByStaffId(id);
        return performance != null ? toPerformanceResponse(performance) : null;
    }

    public List<PerformanceResponse> getPerformanceByDate(LocalDateTime targetDate) {
        LocalDateTime startOfDay = targetDate.with(LocalTime.MIN);
        LocalDateTime endOfDay = targetDate.with(LocalTime.MAX);

        List<Performance> performances = performanceRepository.findByDateBetween(startOfDay, endOfDay);

        return performances.stream()
                .map(this::toPerformanceResponse)
                .collect(Collectors.toList());
    }



    public List<PerformanceResponse> readAllPerformanceReport() {
        List<Performance> performances = performanceRepository.listAll();

        return performances.stream()
                .map(this::toPerformanceResponse)
                .collect(Collectors.toList());
    }

    public Performance updatePerformanceReportDetails(PerformanceRequest performanceRequest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
        LocalDateTime date = LocalDateTime.parse(performanceRequest.getDate(), formatter);

        Optional<Performance> performanceUpdate = performanceRepository.findById(performanceRequest.getPK_performanceID());

        if(performanceUpdate.isPresent()){
            Performance performance = performanceUpdate.get();

            performance.setDate(date);
            performance.setSalesMade(performanceRequest.getSalesMade());
            performance.setRevenueGenerated(performanceRequest.getRevenueGenerated());
            performance.setCustomerSignUp(performanceRequest.getCustomerSignUp());

            return performanceRepository.save(performance);
        }else {
            throw new RuntimeException("performance report with ID " + performanceRequest.getPK_performanceID() + " not found");
        }
    }

    public void deletePerformanceById(Long id) {
        Optional<Performance> performanceOptional = performanceRepository.findById(id);
        if (performanceOptional.isPresent()) {
            Performance performance = performanceOptional.get();
            if (performance != null) {
                boolean status=false;
                performance.setStatus(status);
                performanceRepository.save(performance);
            } else {
                throw new RuntimeException("Performance Report with ID:  " + id + " not found");
            }
        } else {
            throw new RuntimeException("Performance ID" + id+ " not found");
        }
    }

    public List<PerformanceResponse> readAllConfirmedPerformanceReport() {
        List<Performance> performances = performanceRepository.findByStatus(true);

        return performances.stream()
                .map(this::toPerformanceResponse)
                .collect(Collectors.toList());
    }
}


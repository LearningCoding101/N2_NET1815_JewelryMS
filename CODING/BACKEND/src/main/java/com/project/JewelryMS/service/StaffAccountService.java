package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.entity.Shift;
import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.model.Staff.StaffAccountRequest;
import com.project.JewelryMS.model.Staff.StaffAccountResponse;
import com.project.JewelryMS.repository.AuthenticationRepository;
import com.project.JewelryMS.repository.ShiftRepository;
import com.project.JewelryMS.repository.StaffAccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StaffAccountService {

    @Autowired
    private StaffAccountRepository staffAccountRepository;

    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    // Create

    // Read all
    public List<StaffAccountResponse> readAllStaffAccounts() {
        List<StaffAccount> staffAccounts = staffAccountRepository.findAllStaffAccountsByRoleStaff();
        return staffAccounts.stream()
                .map(this::mapToStaffAccountResponse)
                .collect(Collectors.toList());
    }

    // Read staff account by ID
    public StaffAccountResponse getStaffAccountById(Integer id) {
        Optional<StaffAccount> staffAccountOptional = staffAccountRepository.findById(id);
        return staffAccountOptional.map(this::mapToStaffAccountResponse).orElse(null);
    }

    // Method to map StaffAccount to StaffAccountResponse
    private StaffAccountResponse mapToStaffAccountResponse(StaffAccount staffAccount) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        StaffAccountResponse response = new StaffAccountResponse();
        response.setStaffID(staffAccount.getStaffID());
        response.setPhoneNumber(staffAccount.getPhoneNumber());
        response.setSalary(staffAccount.getSalary());
        response.setStartDate(staffAccount.getStartDate().format(formatter)); // Format date to string

        response.setAccountName(staffAccount.getAccount().getAccountName());
        response.setRole(staffAccount.getAccount().getRole());
        response.setStatus(staffAccount.getAccount().getStatus());
        response.setEmail(staffAccount.getAccount().getEmail());
        response.setUsername(staffAccount.getAccount().getUsername());

        if (staffAccount.getStaffShifts() != null && !staffAccount.getStaffShifts().isEmpty()) {
            List<StaffAccountResponse.ShiftResponse> shifts = staffAccount.getStaffShifts().stream()
                    .map(staffShift -> mapToShiftResponse(staffShift.getShift()))
                    .collect(Collectors.toList());
            response.setShift(shifts);
        } else {
            response.setShift(Collections.emptyList());
        }

        return response;
    }
    // Method to map Shift to ShiftResponse
    private StaffAccountResponse.ShiftResponse mapToShiftResponse(Shift shift) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        StaffAccountResponse.ShiftResponse shiftResponse = new StaffAccountResponse.ShiftResponse();
        shiftResponse.setShiftID(shift.getShiftID());
        shiftResponse.setEndTime(shift.getEndTime().format(formatter)); // Format date to string
        shiftResponse.setRegister(shift.getRegister());
        shiftResponse.setShiftType(shift.getShiftType());
        shiftResponse.setStartTime(shift.getStartTime().format(formatter)); // Format date to string
        shiftResponse.setStatus(shift.getStatus());
        shiftResponse.setWorkArea(shift.getWorkArea());
        return shiftResponse;
    }

    // Update staff account
    @Transactional
    public String updateStaffAccount(Integer id, StaffAccountRequest staffAccountRequest) {
        Optional<StaffAccount> existingStaffAccountOpt = staffAccountRepository.findById(id);
        if (existingStaffAccountOpt.isEmpty()) {
            throw new RuntimeException("StaffAccount with ID " + id + " not found");
        }

        StaffAccount existingStaffAccount = existingStaffAccountOpt.get();

        // Update fields from StaffAccountRequest
        existingStaffAccount.setPhoneNumber(staffAccountRequest.getPhoneNumber());
        existingStaffAccount.setSalary(staffAccountRequest.getSalary());
        existingStaffAccount.setStartDate(staffAccountRequest.getStartDate()); // No conversion needed

        // Update account information
        Account account = existingStaffAccount.getAccount();
        if (account != null) {
            account.setEmail(staffAccountRequest.getEmail());
            account.setAUsername(staffAccountRequest.getUsername());
            account.setAPassword(passwordEncoder.encode(staffAccountRequest.getPassword()));
            account.setAccountName(staffAccountRequest.getAccountName());
            account.setRole(staffAccountRequest.getRole()); // Update role from request

            authenticationRepository.save(account);
        } else {
            throw new RuntimeException("Account associated with StaffAccount ID " + id + " not found");
        }

        // Save the updated staff account
        staffAccountRepository.save(existingStaffAccount);

        // Prepare and return the response
        return "Update Staff Successfully";
    }

    // "Delete" a StaffAccount by deactivating the associated Account
    @Transactional
    public boolean deactivateStaffAccount(Integer id) {
        Optional<StaffAccount> staffAccountOpt = staffAccountRepository.findById(id);
        if (staffAccountOpt.isPresent()) {
            StaffAccount staffAccount = staffAccountOpt.get();
            Account account = staffAccount.getAccount();
            if (account != null) {
                account.setStatus(0);
                authenticationRepository.save(account);
                return true;
            } else {
                throw new RuntimeException("Account associated with StaffAccount ID " + id + " not found");
            }
        } else {
            throw new RuntimeException("StaffAccount with ID " + id + " not found");
        }
    }
}

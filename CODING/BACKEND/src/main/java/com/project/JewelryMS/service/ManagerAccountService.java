package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.enumClass.RoleEnum;
import com.project.JewelryMS.exception.DuplicateEmailException;
import com.project.JewelryMS.exception.DuplicateUsernameException;
import com.project.JewelryMS.model.Manager.CreateManagerAccountRequest;
import com.project.JewelryMS.model.Manager.ManagerAccountRequest;
import com.project.JewelryMS.model.Manager.ManagerAccountResponse;
import com.project.JewelryMS.repository.AuthenticationRepository;
import com.project.JewelryMS.repository.ManagerAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ManagerAccountService {

    @Autowired
    ManagerAccountRepository managerAccountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationRepository authenticationRepository;

    // Get all manager accounts
    public List<ManagerAccountResponse> getAllManagerAccounts() {
        List<Account> accounts = managerAccountRepository.findAllManagerAccounts();
        return accounts.stream()
                .map(this::mapToManagerAccountResponse)
                .collect(Collectors.toList());
    }

    // Get manager account by ID
    public ManagerAccountResponse getManagerAccountById(Integer id) {
        Optional<Account> accountOptional = managerAccountRepository.findManagerAccountById(id);
        return accountOptional.map(this::mapToManagerAccountResponse).orElse(null);
    }

    // Mapper method to convert Account to ManagerAccountResponse
    private ManagerAccountResponse mapToManagerAccountResponse(Account account) {
        return new ManagerAccountResponse(
                account.getPK_userID(),
                account.getEmail(),
                account.getUsername(),
                account.getAccountName(),
                account.getRole(),
                account.getStatus()
        );
    }

    public ManagerAccountResponse createManagerAccount(CreateManagerAccountRequest createManagerAccountRequest){
        Account account = new Account();
        if(authenticationRepository.existsByAUsername(createManagerAccountRequest.getUsername())){
            throw new DuplicateUsernameException("Username đã được sử dụng.");
        }
        if(authenticationRepository.existsByEmail(createManagerAccountRequest.getUsername())){
            throw new DuplicateEmailException("Email đã được sử dụng.");
        }
        account.setAccountName(createManagerAccountRequest.getAccountName());
        account.setEmail(createManagerAccountRequest.getEmail());
        account.setAPassword(passwordEncoder.encode(createManagerAccountRequest.getAPassword()));
        account.setAUsername(createManagerAccountRequest.getUsername());
        account.setRole(RoleEnum.ROLE_MANAGER);
        account.setAImage("https://i.ibb.co/Gxz9md6/avatar-trang-4.jpg");
        account.setStatus(1);
        Account account1 =  authenticationRepository.save(account);
        return getManagerAccountById(account1.getPK_userID());
    }

    // Update
    public ManagerAccountResponse updateManagerAccount(Integer id, ManagerAccountRequest managerAccountRequest) {
        if(getManagerAccountById(id)!=null) {
            Optional<Account> existingAccountOpt = managerAccountRepository.findById(id);
            if (existingAccountOpt.isPresent()) {
                Account account = existingAccountOpt.get();
                if (authenticationRepository.existsByAUsernameAndPkUserIDNot(account.getAUsername(), id)) {
                    throw new DuplicateUsernameException("Username đã được sử dụng.");
                }
                if (authenticationRepository.existsByEmailAndPkUserIDNot(account.getEmail(), id)) {
                    throw new DuplicateEmailException("Email đã được sử dụng.");
                }
                account.setAccountName(managerAccountRequest.getAccountName());
                account.setAUsername(managerAccountRequest.getUsername());
                account.setEmail(managerAccountRequest.getEmail());
                account.setRole(managerAccountRequest.getRole());
                Account account1 = authenticationRepository.save(account);
                return getManagerAccountById(account.getPK_userID());
            } else {
                throw new RuntimeException("ManagerAccount with ID " + id + " not found");
            }
        }
        throw new RuntimeException("ManagerAccount with ID " + id + " not found");
    }

    // Method to "delete" a StaffAccount by updating the Account status
    public String deactivateManagerAccount(Integer id) {
        if (getManagerAccountById(id) != null) {
            Optional<Account> managerAccountOpt = managerAccountRepository.findById(id);
            if (managerAccountOpt.isPresent()) {
                Account account = managerAccountOpt.get();
                account.setStatus(0);
                authenticationRepository.save(account);
                return "Delete Successfully";
            } else {
                throw new RuntimeException("ManagerAccount with ID " + id + " not found");
            }
        }else {
            throw new RuntimeException("ManagerAccount with ID " + id + " not found");
        }
    }


}



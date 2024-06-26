package com.project.JewelryMS.service;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.entity.RoleEnum;
import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.model.*;
import com.project.JewelryMS.model.Staff.CreateStaffAccountRequest;
import com.project.JewelryMS.repository.AuthenticationRepository;
import com.project.JewelryMS.repository.StaffAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthenticationService implements UserDetailsService {
    @Autowired
    EmailService emailService;
    @Autowired
    JWTservice jwTservice;

    @Autowired
    AuthenticationRepository authenticationRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    StaffAccountRepository staffAccountRepository;

    public CreateStaffAccountRequest register(CreateStaffAccountRequest createStaffAccountRequest){
        Account account = new Account();
        account.setAccountName(createStaffAccountRequest.getAccountName());
        account.setEmail(createStaffAccountRequest.getEmail());
        account.setAPassword(passwordEncoder.encode(createStaffAccountRequest.getPassword()));
        account.setAUsername(createStaffAccountRequest.getUsername());
        account.setRole(RoleEnum.ROLE_STAFF);
        StaffAccount staffAccount = new StaffAccount();
        staffAccount.setSalary(createStaffAccountRequest.getSalary());
        staffAccount.setPhoneNumber(createStaffAccountRequest.getPhoneNumber());
        staffAccount.setStartDate(createStaffAccountRequest.getStartDate());
        staffAccount.setAccount(account);
        account.setStatus(1);
        staffAccountRepository.save(staffAccount);
        authenticationRepository.save(account);
        return createStaffAccountRequest;
    }
    public Boolean handleRegisterCheckEmail(CreateStaffAccountRequest registerRequest){
        String emailCheck = registerRequest.getEmail();
        if(emailService.validEmail(emailCheck)){
            return true;
        }
        return false;
    }


    public List<Account> getAllAccount(){
        return authenticationRepository.findAll();
    }

    public Account getAccount(String email){
        return authenticationRepository.findAccountByemail(email);
    }
    public Account getAccountById(Long id){
        return authenticationRepository.findAccountById(id);
    }
    public String sendForgotPasswordEmail(String email) {
        Optional<Account> accountOptional = Optional.ofNullable(authenticationRepository.findAccountByemail(email.replace("\"", "")));
        if (accountOptional.isPresent()) {

            Account account = accountOptional.get();
            String tempPassword = emailService.sendTempPassword(account);
            account.setAPassword(passwordEncoder.encode(tempPassword));
            authenticationRepository.save(account);
            return "Successfully sent new temporary password";
        } else {
            return "Is your email correct?";
        }
    }
    public AccountResponse login(LoginRequest loginRequest){
        authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        ));
                Account account = authenticationRepository.findAccountByUsername(loginRequest.getUsername());
                    String token = jwTservice.generateToken(account);
                    AccountResponse accountResponse = new AccountResponse();
                    accountResponse.setUsername(account.getUsername());
                    accountResponse.setToken(token);
                    accountResponse.setRole(account.getRole());
                    accountResponse.setId((long)account.getPK_userID());
                    return accountResponse;
    }
    public ResponseEntity<String> changePassword(ChangePasswordRequest request ,Long id) {
        Account account = authenticationRepository.findById(id).orElse(null);

        if (account == null) {
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }

        if (!passwordEncoder.matches(request.getOldPassword(), account.getAPassword())) {
            return new ResponseEntity<>("Old password does not match", HttpStatus.BAD_REQUEST);
        }

        account.setAPassword(passwordEncoder.encode(request.getNewPassword()));
        authenticationRepository.save(account);

        return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
    }
@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = authenticationRepository.findAccountByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return account;
    }

    public List<Account> getAllManagerAccount() {
        return authenticationRepository.findByRole(RoleEnum.ROLE_MANAGER);
    }


    public AccountResponseGG loginGoogle(LoginGoogleRequest loginGoogleRequest) {
        AccountResponseGG accountResponseGG = new AccountResponseGG();
        try{
            FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(loginGoogleRequest.getToken());
            String email = firebaseToken.getEmail();
            Account account = authenticationRepository.findAccountByemail(email);
            if(account == null){
                 account = new Account();
                 account.setEmail(email);
                 account.setAUsername(firebaseToken.getName());
                 account.setRole(RoleEnum.ROLE_ADMIN);
                 account = authenticationRepository.save(account);
            }
             accountResponseGG.setEmail(account.getEmail());
             accountResponseGG.setAUsername(account.getAUsername());
             accountResponseGG.setPK_userID(account.getPK_userID());
             String token = jwTservice.generateToken(account);
             accountResponseGG.setToken(token);
             accountResponseGG.setRole(account.getRole());

        }catch(FirebaseAuthException e){
            e.printStackTrace();
        }
        return accountResponseGG;
    }
}

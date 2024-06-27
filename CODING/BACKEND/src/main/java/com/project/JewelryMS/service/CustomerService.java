package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Customer;
import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.model.Customer.CreateCustomerRequest;
import com.project.JewelryMS.model.Customer.CustomerRequest;
import com.project.JewelryMS.model.OrderDetail.CalculatePointsRequest;
import com.project.JewelryMS.model.Customer.ViewCustomerPointRequest;
import com.project.JewelryMS.repository.CustomerRepository;
import com.project.JewelryMS.repository.OrderDetailRepository;
import com.project.JewelryMS.repository.ProductSellRepository;
import com.project.JewelryMS.repository.StaffAccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    private ProductSellRepository productSellRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private StaffAccountRepository staffAccountRepository;

    @Transactional
    public Integer calculateAndUpdatePoints(CalculatePointsRequest request) {
        Float total = request.getTotal();
        Float point = total / 1000000;
        Integer points = Math.round(point);
        return points;
    }

    public String getCustomerRank(Long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if(customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            int totalPoints = customer.getPointAmount();
            if (totalPoints >= 0) {
                if (totalPoints >= 0 && totalPoints <= 99) {
                    return "Connect";
                } else if (totalPoints >= 100 && totalPoints <= 399) {
                    return "Member";
                } else if (totalPoints >= 400 && totalPoints <= 999) {
                    return "Companion";
                } else {
                    return "Intimate";
                }
            }
            return "Negative Point Error ???";
        }
        return "Not Found Customer ID";
    }

    public Customer createCustomer(CreateCustomerRequest createCustomerRequest) {
        Customer customer = new Customer();

        customer.setEmail(createCustomerRequest.getEmail());
        customer.setPhoneNumber(createCustomerRequest.getPhoneNumber());
        customer.setGender(createCustomerRequest.getGender());
        customer.setCreateDate(new Date());
        customer.setCusName(createCustomerRequest.getCusName());
        Optional<StaffAccount> staffAccountOptional = staffAccountRepository.findById(createCustomerRequest.getStaff_ID());
        if(staffAccountOptional.isPresent()) {
            StaffAccount staffAccount = staffAccountOptional.get();
            customer.setStaffAccount(staffAccount);
        }
        return customerRepository.save(customer);
    }

    public Customer getCustomerById(long id) {
        return customerRepository.findById(id).orElse(null);
    }
    public Customer getCustomerByPhoneNumber(String id) {
        return customerRepository.findByPhoneNumber(id).orElse(null);
    }


    public List<Customer> readAllCustomer() {
        return customerRepository.findAll();
    }

    public void updateCustomerDetails(CustomerRequest customerRequest) {
        Optional<Customer> customerUpdate = customerRepository.findById(customerRequest.getPK_CustomerID());
        if(customerUpdate.isPresent()){
            Customer customer = customerUpdate.get();
            customer.setEmail(customerRequest.getEmail());
            customer.setPhoneNumber(customerRequest.getPhoneNumber());
            customer.setGender(customerRequest.getGender());
            customer.setCusName(customerRequest.getCusName());
            customerRepository.save(customer);
        }
    }

    public void deleteCustomerById(Long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            if (customer != null) {
                boolean status=false;
                customer.setStatus(status);
                customerRepository.save(customer);
            } else {
                throw new RuntimeException("Customer with ID:  " + id+ " not found");
            }
        } else {
            throw new RuntimeException("Customer ID" + id+ " not found");
        }
    }


    public void updateCustomerPoints(ViewCustomerPointRequest customerRequest) {
        Optional<Customer> customerUpdate = customerRepository.findById(customerRequest.getPK_CustomerID());
        customerUpdate.ifPresent(customer -> {
            customer.setPointAmount(customerRequest.getPointAmount());
            customerRepository.save(customer);
        });
    }

    public List<Customer> readAllActiveCustomers() {
        // Retrieve only active customers (status = true)
        return customerRepository.findByStatus(true);
    }


//        long customerId = request.getCustomerId();
//        List<OrderDetail> orderDetails = request.getOrderDetails();
//
//        Customer customer = customerRepository.findById(customerId)
//                .orElseThrow(() -> new RuntimeException("Customer not found"));
//
//        float totalPoints = 0;
//
//        for (OrderDetail orderDetail : orderDetails) {
//            ProductSell productSell = productSellRepository.findById(orderDetail.getProductSell().getProductID())
//                    .orElseThrow(() -> new RuntimeException("Product not found"));
//
//            float cost = productSell.getCost();
//            int quantity = orderDetail.getQuantity();
//            String gemstoneType = productSell.getGemstoneType();
//            String metalType = productSell.getMetalType();
//            if (gemstoneType != null && gemstoneType.equals("Diamond")) {
//                totalPoints +=  ((cost * quantity) / 2000000);
//            } else if (metalType != null) {
//                if (metalType.equals("Jewelry Gold 24k")) {
//                    totalPoints +=  ((cost * quantity) / 2000000);
//                } else if (metalType.equals("Gold Bar")) {
//                    totalPoints +=  ((cost * quantity) / 6000000);
//                } else {
//                    totalPoints +=  ((cost * quantity) / 1000000);
//                }
//            }
//        }
//        int totalPoint = Math.round(totalPoints);
//        customer.setPointAmount(customer.getPointAmount() + totalPoint);

    public Map<String, Long> getGenderDemographics() {
        List<Customer> customers = customerRepository.findAll();

        Map<String, Long> genderCount = customers.stream()
                .collect(Collectors.groupingBy(Customer::getGender, Collectors.counting()));

        return genderCount;
    }
}

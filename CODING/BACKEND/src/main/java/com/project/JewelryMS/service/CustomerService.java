package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Customer;
import com.project.JewelryMS.model.Customer.CreateCustomerRequest;
import com.project.JewelryMS.model.Customer.CustomerRequest;
import com.project.JewelryMS.model.Customer.CustomerResponse;
import com.project.JewelryMS.model.OrderDetail.CalculatePointsRequest;
import com.project.JewelryMS.model.Customer.ViewCustomerPointRequest;
import com.project.JewelryMS.repository.CustomerRepository;
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

    @Transactional
    public Integer calculateAndUpdatePoints(CalculatePointsRequest request) {
        float total = request.getTotal();
        float point = total / 1000000;
        return Math.round(point);
    }

    public String getCustomerRank(Long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if(customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            int totalPoints = customer.getPointAmount();
            if (totalPoints >= 0) {
                if (totalPoints <= 99) {
                    return "Connect";
                } else if (totalPoints <= 399) {
                    return "Member";
                } else if (totalPoints <= 999) {
                    return "Companion";
                } else {
                    return "Intimate";
                }
            }
            return "Negative Point Error ???";
        }
        return "Not Found Customer ID";
    }

    public CustomerResponse createCustomer(CreateCustomerRequest createCustomerRequest) {
        Customer customer = new Customer();

        customer.setEmail(createCustomerRequest.getEmail());
        customer.setPhoneNumber(createCustomerRequest.getPhoneNumber());
        customer.setGender(createCustomerRequest.getGender());
        customer.setCreateDate(new Date());
        customer.setCusName(createCustomerRequest.getCusName());

        // Optionally set other default values if needed
        customer.setPointAmount(0); // Default pointAmount
        customer.setStatus(true); // Default status

        customer = customerRepository.save(customer);

        // Convert Customer entity to CustomerResponse DTO
        return convertToCustomerResponse(customer);
    }

    // Helper method to convert Customer entity to CustomerResponse
    private CustomerResponse convertToCustomerResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setPK_CustomerID(customer.getPK_CustomerID());
        response.setEmail(customer.getEmail());
        response.setPhoneNumber(customer.getPhoneNumber());
        response.setGender(customer.getGender());
        response.setCreateDate(customer.getCreateDate());
        response.setPointAmount(customer.getPointAmount());
        response.setStatus(customer.isStatus());

        return response;
    }

    public CustomerResponse getCustomerById(long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        return customerOptional.map(this::convertToCustomerResponse).orElse(null);
    }

    public CustomerResponse getCustomerByPhoneNumber(String phoneNumber) {
        Optional<Customer> customerOptional = customerRepository.findByPhoneNumber(phoneNumber);
        return customerOptional.map(this::convertToCustomerResponse).orElse(null);
    }

    public List<CustomerResponse> readAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(this::convertToCustomerResponse)
                .collect(Collectors.toList());
    }

    public void updateCustomerDetails(CustomerRequest customerRequest) {
        Optional<Customer> customerUpdate = customerRepository.findById(customerRequest.getPK_CustomerID());
        customerUpdate.ifPresent(customer -> {
            customer.setEmail(customerRequest.getEmail());
            customer.setPhoneNumber(customerRequest.getPhoneNumber());
            customer.setGender(customerRequest.getGender());
            customer.setCusName(customerRequest.getCusName());
            customerRepository.save(customer);
        });
    }

    public void deleteCustomerById(Long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        customerOptional.ifPresent(customer -> {
            customer.setStatus(false);
            customerRepository.save(customer);
        });
    }

    public List<CustomerResponse> readAllActiveCustomers() {
        List<Customer> activeCustomers = customerRepository.findByStatus(true);
        return activeCustomers.stream()
                .map(this::convertToCustomerResponse)
                .collect(Collectors.toList());
    }

    public void updateCustomerPoints(ViewCustomerPointRequest customerRequest) {
        Optional<Customer> customerUpdate = customerRepository.findById(customerRequest.getPK_CustomerID());
        customerUpdate.ifPresent(customer -> {
            customer.setPointAmount(customerRequest.getPointAmount());
            customerRepository.save(customer);
        });
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

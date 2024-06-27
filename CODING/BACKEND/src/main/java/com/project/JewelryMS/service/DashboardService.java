package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.*;
import com.project.JewelryMS.model.Dashboard.*;
import com.project.JewelryMS.model.Dashboard.Customer.*;
import com.project.JewelryMS.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
public class DashboardService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CustomerService customerService;
    @Autowired
    private OrderDetailRepository orderDetailRepository;



    public List<CategoryResponse> RevenueCategory(){
        Optional<List<Category>> optionalCategoryList = Optional.ofNullable(categoryRepository.findAllCategories());
        List<CategoryResponse> categoryResponseList = new ArrayList<>();
        if(optionalCategoryList.isPresent()){
            List<Category> categoryList = optionalCategoryList.get();
            for(Category category: categoryList){
                CategoryResponse categoryResponse = new CategoryResponse();
                categoryResponse.setCategory_Name(category.getName());
                categoryResponse.setCategory_Total(CalculateCategoryTotal(category.getId()));
                categoryResponseList.add(categoryResponse);
            }
        }
//        categoryResponseList.sort(Comparator.comparing(CategoryResponse::getCategory_Total).reversed());
            return categoryResponseList;
    }

    public Float CalculateCategoryTotal(Long Category_ID){
        List<PurchaseOrder> orders = orderRepository.findAll();

        // Tính tổng doanh thu cho danh mục
        float totalRevenue = 0.0F;

        for(PurchaseOrder order : orders){
            Set<OrderDetail> orderDetails = order.getOrderDetails();
            for(OrderDetail orderDetail : orderDetails){
                if(Long.valueOf(orderDetail.getProductSell().getCategory().getId()).equals(Category_ID)){
                    float orderDetailTotal = orderDetail.getQuantity() * orderDetail.getProductSell().getCost();
                    totalRevenue += orderDetailTotal;
                }
            }
        }
        return totalRevenue;
    }

    public List<TopSellProductResponse> getTopSellingProducts() {
        List<PurchaseOrder> orders = orderRepository.findAll();

        // Sử dụng Map để lưu trữ số lượng và doanh thu bán hàng cho từng sản phẩm
        Map<Long, TopSellProductResponse> productSalesMap = new HashMap<>();

        for (PurchaseOrder order : orders) {
            Set<OrderDetail> orderDetails = order.getOrderDetails();
            for (OrderDetail orderDetail : orderDetails) {
                ProductSell product = orderDetail.getProductSell();
                Long productId = product.getProductID();
                //Return value topSellProductResponse if it finds specific key mapping in productSalesMap otherwise return null
                TopSellProductResponse topSellProductResponse = productSalesMap.getOrDefault(productId, new TopSellProductResponse());
                topSellProductResponse.setProduct_Name(product.getPName());
                topSellProductResponse.setUnitSold(topSellProductResponse.getUnitSold() != null ? topSellProductResponse.getUnitSold() + orderDetail.getQuantity() : orderDetail.getQuantity());
                topSellProductResponse.setRevenueSold(topSellProductResponse.getRevenueSold() != null ? topSellProductResponse.getRevenueSold() + (orderDetail.getQuantity() * product.getCost()) : orderDetail.getQuantity() * product.getCost());
                productSalesMap.put(productId, topSellProductResponse);
            }
        }

        // Chuyển Map sang List và sắp xếp theo số lượng bán ra hoặc doanh thu
        List<TopSellProductResponse> topSellProductResponses = new ArrayList<>(productSalesMap.values());
        topSellProductResponses.sort(Comparator.comparing(TopSellProductResponse::getUnitSold).reversed());
        return topSellProductResponses;
    }


    public List<CategoryResponse> RevenueCategory(RevenueDateRequest revenueDateRequest){
        Optional<List<Category>> optionalCategoryList = Optional.ofNullable(categoryRepository.findAllCategories());
        List<CategoryResponse> categoryResponseList = new ArrayList<>();
        if(optionalCategoryList.isPresent()){
            List<Category> categoryList = optionalCategoryList.get();
            for(Category category: categoryList){
                CategoryResponse categoryResponse = new CategoryResponse();
                categoryResponse.setCategory_Name(category.getName());
                categoryResponse.setCategory_Total(CalculateCategoryTotal(category.getId(), revenueDateRequest));
                categoryResponseList.add(categoryResponse);
            }
        }
//       categoryResponseList.sort(Comparator.comparing(CategoryResponse::getCategory_Total).reversed());
        return categoryResponseList;
    }

    public Float CalculateCategoryTotal(Long Category_ID, RevenueDateRequest revenueDateRequest){
        // Lấy start và end dates từ request và chuyển đổi thành LocalDateTime
        LocalDate startDate = revenueDateRequest.getStartTime();
        LocalDate endDate = revenueDateRequest.getEndTime();

        LocalDateTime startDateTime = startDate.atStartOfDay(); // 00:00:00
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX); // 23:59:59.999999999

        // Truy vấn các đơn hàng trong khoảng thời gian đã chỉ định
        List<PurchaseOrder> orders = orderRepository.findOrdersByDateRange(startDateTime, endDateTime);

        // Tính tổng doanh thu cho danh mục
        float totalRevenue = 0.0F;

        for(PurchaseOrder order : orders){
            Set<OrderDetail> orderDetails = order.getOrderDetails();
            for(OrderDetail orderDetail : orderDetails){
                if(Long.valueOf(orderDetail.getProductSell().getCategory().getId()).equals(Category_ID)){
                    float orderDetailTotal = orderDetail.getQuantity() * orderDetail.getProductSell().getCost();
                    totalRevenue += orderDetailTotal;
                }
            }
        }
        return totalRevenue;
    }

    public List<TopSellProductResponse>  getTopSellingProducts(RevenueDateRequest revenueDateRequest) {
        // Lấy start và end dates từ request và chuyển đổi thành LocalDateTime
        LocalDate startDate = revenueDateRequest.getStartTime();
        LocalDate endDate = revenueDateRequest.getEndTime();

        LocalDateTime startDateTime = startDate.atStartOfDay(); // 00:00:00
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX); // 23:59:59.999999999

        // Truy vấn các đơn hàng trong khoảng thời gian đã chỉ định
        List<PurchaseOrder> orders = orderRepository.findOrdersByDateRange(startDateTime, endDateTime);

        // Sử dụng Map để lưu trữ số lượng và doanh thu bán hàng cho từng sản phẩm
        Map<Long, TopSellProductResponse> productSalesMap = new HashMap<>();

        for (PurchaseOrder order : orders) {
            Set<OrderDetail> orderDetails = order.getOrderDetails();
            for (OrderDetail orderDetail : orderDetails) {
                ProductSell product = orderDetail.getProductSell();
                Long productId = product.getProductID();
                //Return value topSellProductResponse if it finds specific key mapping in productSalesMap otherwise return null
                TopSellProductResponse topSellProductResponse = productSalesMap.getOrDefault(productId, new TopSellProductResponse());
                topSellProductResponse.setProduct_Name(product.getPName());
                topSellProductResponse.setUnitSold(topSellProductResponse.getUnitSold() != null ? topSellProductResponse.getUnitSold() + orderDetail.getQuantity() : orderDetail.getQuantity());
                topSellProductResponse.setRevenueSold(topSellProductResponse.getRevenueSold() != null ? topSellProductResponse.getRevenueSold() + (orderDetail.getQuantity() * product.getCost()) : orderDetail.getQuantity() * product.getCost());
                productSalesMap.put(productId, topSellProductResponse);
            }
        }

        // Chuyển Map sang List và sắp xếp theo số lượng bán ra hoặc doanh thu
        List<TopSellProductResponse> topSellProductResponses = new ArrayList<>(productSalesMap.values());
        topSellProductResponses.sort(Comparator.comparing(TopSellProductResponse::getUnitSold).reversed());
        return topSellProductResponses;
    }

    public List<CustomerLoyalty> getCustomerLoyaltyStatistics(RevenueDateRequest revenueDateRequest) {
        // Convert LocalDate to LocalDateTime to include the whole day
        LocalDate startDate = revenueDateRequest.getStartTime();
        LocalDate endDate = revenueDateRequest.getEndTime();

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        // Fetch customers created within the date range
        List<Customer> customers = customerRepository.findCustomersByCreateDateRange(startDateTime, endDateTime);

        // Build the list of CustomerLoyalty objects
        List<CustomerLoyalty> customerLoyalties = new ArrayList<>();
        for (Customer customer : customers) {
            CustomerLoyalty customerLoyalty = new CustomerLoyalty();
            customerLoyalty.setEmail(customer.getEmail());
            customerLoyalty.setPhoneNumber(customer.getPhoneNumber());
            customerLoyalty.setPointAmount(customer.getPointAmount());
            customerLoyalty.setRank(customerService.getCustomerRank(customer.getPK_CustomerID()));
            customerLoyalties.add(customerLoyalty);
        }

        return customerLoyalties;
    }

    public List<CustomerDemographics> getCustomerDemoGraphicResponse(RevenueDateRequest revenueDateRequest){
        // Convert LocalDate to LocalDateTime to include the whole day
        LocalDate startDate = revenueDateRequest.getStartTime();
        LocalDate endDate = revenueDateRequest.getEndTime();

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        // Fetch customers created within the date range
        List<Customer> customers = customerRepository.findCustomersByCreateDateRange(startDateTime, endDateTime);
        List<CustomerDemographics> customerDemoGraphics = new ArrayList<>();
        for(Customer customer: customers){
            CustomerDemographics customerDemoGraphic = new CustomerDemographics();
            customerDemoGraphic.setGender(customer.getGender());
            customerDemoGraphics.add(customerDemoGraphic);
        }
        return customerDemoGraphics;
    }

    public List<CustomerSignUp> getCustomerSignUpsByStaff(RevenueDateRequest revenueDateRequest) {
        LocalDate startDate = revenueDateRequest.getStartTime();
        LocalDate endDate = revenueDateRequest.getEndTime();

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<Object[]> results = customerRepository.findCustomerSignUpsByStaff(startDateTime, endDateTime);
        List<CustomerSignUp> customerSignUps = new ArrayList<>();

        for (Object[] result : results) {
            String staffName = (String) result[0];
            Long signUpCount = (Long) result[1];

            CustomerSignUp customerSignUp = new CustomerSignUp();
            customerSignUp.setStaffName(staffName);
            customerSignUp.setNumberSignUp(signUpCount.intValue());
            customerSignUps.add(customerSignUp);
        }

        return customerSignUps;
    }

    public static Date convertLocalDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public ComparisonResponse compareDay(DayComparisonRequest request) {
        Date Date1 =  convertLocalDateToDate(request.getDate1());
        Date Date2= convertLocalDateToDate(request.getDate2());
        OrderDetailProjection result1 = orderDetailRepository.findTotalQuantityAndRevenueOnDate(Date1);
        OrderDetailProjection result2 = orderDetailRepository.findTotalQuantityAndRevenueOnDate(Date2);

        long totalQuantity1 = (result1 != null && result1.getTotalQuantity() != null) ? result1.getTotalQuantity() : 0;
        float totalRevenue1 = (result1 != null && result1.getTotalRevenue() != null) ? result1.getTotalRevenue() : 0;

        long totalQuantity2 = (result2 != null && result2.getTotalQuantity() != null) ? result2.getTotalQuantity() : 0;
        float totalRevenue2 = (result2 != null && result2.getTotalRevenue() != null) ? result2.getTotalRevenue() : 0;

        long customerCount1 = customerRepository.countCustomersOnDate(Date1);
        long customerCount2 = customerRepository.countCustomersOnDate(Date2);

        return calculateDifference(totalQuantity1, totalRevenue1, customerCount1, totalQuantity2, totalRevenue2, customerCount2);
    }

    public ComparisonResponse compareMonth(MonthComparisonRequest request) {
        String month1 = request.getMonth1();
        String month2 = request.getMonth2();

        OrderDetailProjection result1 = orderDetailRepository.findTotalQuantityAndRevenueInMonth(month1);
        OrderDetailProjection result2 = orderDetailRepository.findTotalQuantityAndRevenueInMonth(month2);

        long totalQuantity1 = (result1 != null && result1.getTotalQuantity() != null) ? result1.getTotalQuantity() : 0;
        float totalRevenue1 = (result1 != null && result1.getTotalRevenue() != null) ? result1.getTotalRevenue() : 0;

        long totalQuantity2 = (result2 != null && result2.getTotalQuantity() != null) ? result2.getTotalQuantity() : 0;
        float totalRevenue2 = (result2 != null && result2.getTotalRevenue() != null) ? result2.getTotalRevenue() : 0;

        long customerCount1 = customerRepository.countCustomersInMonth(month1);
        long customerCount2 = customerRepository.countCustomersInMonth(month2);

        return calculateDifference(totalQuantity1, totalRevenue1, customerCount1, totalQuantity2, totalRevenue2, customerCount2);
    }

    public ComparisonResponse compareYear(YearComparisonRequest request) {
        Year year1 = Year.parse(request.getYear1());
        Year year2 = Year.parse(request.getYear2());

        OrderDetailProjection result1 = orderDetailRepository.findTotalQuantityAndRevenueInYear(year1);
        OrderDetailProjection result2 = orderDetailRepository.findTotalQuantityAndRevenueInYear(year2);

        long totalQuantity1 = (result1 != null && result1.getTotalQuantity() != null) ? result1.getTotalQuantity() : 0;
        float totalRevenue1 = (result1 != null && result1.getTotalRevenue() != null) ? result1.getTotalRevenue() : 0;

        long totalQuantity2 = (result2 != null && result2.getTotalQuantity() != null) ? result2.getTotalQuantity() : 0;
        float totalRevenue2 = (result2 != null && result2.getTotalRevenue() != null) ? result2.getTotalRevenue() : 0;

        long customerCount1 = customerRepository.countCustomersInYear(year1);
        long customerCount2 = customerRepository.countCustomersInYear(year2);

        return calculateDifference(totalQuantity1, totalRevenue1, customerCount1, totalQuantity2, totalRevenue2, customerCount2);
    }

    private ComparisonResponse calculateDifference(long totalQuantity1, float totalRevenue1, long customerCount1,
                                                   long totalQuantity2, float totalRevenue2, long customerCount2) {
        ComparisonResponse response = new ComparisonResponse();
        response.setTotalQuantityDifference(totalQuantity2 - totalQuantity1);
        response.setTotalRevenueDifference(totalRevenue2 - totalRevenue1);
        response.setTotalCustomerAccountsDifference(customerCount2 - customerCount1);
        return response;
    }


}
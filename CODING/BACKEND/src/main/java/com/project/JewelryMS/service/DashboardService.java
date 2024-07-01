package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.*;
import com.project.JewelryMS.model.Dashboard.*;
import com.project.JewelryMS.model.Dashboard.Customer.*;
import com.project.JewelryMS.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    StaffAccountRepository staffAccountRepository;

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

    public List<CustomerLoyalty> getCustomerLoyaltyStatistics(RevenueDateRequest request) {
        LocalDateTime startDateTime = request.getStartTime().atStartOfDay();
        LocalDateTime endDateTime = request.getEndTime().atTime(LocalTime.MAX);
        List<Customer> customers = customerRepository.findAllByCreateDateBetween(startDateTime, endDateTime);

        int connect = 0, member = 0, companion = 0, intimate = 0;
        for (Customer customer : customers) {
            String rank = getCustomerRank(customer);
            switch (rank) {
                case "Connect":
                    connect++;
                    break;
                case "Member":
                    member++;
                    break;
                case "Companion":
                    companion++;
                    break;
                case "Intimate":
                    intimate++;
                    break;
            }
        }

        CustomerLoyalty loyalty = new CustomerLoyalty(connect, member, companion, intimate);
        return Collections.singletonList(loyalty);
    }

    public List<CustomerDemographics> getCustomerDemoGraphicResponse(RevenueDateRequest request) {
        LocalDateTime startDateTime = request.getStartTime().atStartOfDay();
        LocalDateTime endDateTime = request.getEndTime().atTime(LocalTime.MAX);
        List<Customer> customers = customerRepository.findAllByCreateDateBetween(startDateTime, endDateTime);

        int male = 0, female = 0, other = 0;
        for (Customer customer : customers) {
            switch (customer.getGender().toLowerCase()) {
                case "male":
                    male++;
                    break;
                case "female":
                    female++;
                    break;
                default:
                    other++;
                    break;
            }
        }

        CustomerDemographics demographics = new CustomerDemographics(male, female, other);
        return Collections.singletonList(demographics);
    }

    private String getCustomerRank(Customer customer) {
        int totalPoints = customer.getPointAmount();
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

    //Get Revenue Generated by a Staff in a single day
    public List<StaffRevenueResponse> getRevenueGeneratedByStaff(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<PurchaseOrder> orders = orderRepository.findOrdersByDateRange(startDateTime, endDateTime);
        Map<Integer, Double> revenueByStaff = new HashMap<>();


        for (PurchaseOrder order : orders) {
            StaffAccount staffAccount = order.getStaffAccount();
            if (staffAccount != null) {
                int staffId = staffAccount.getStaffID();
                double orderRevenue = order.getOrderDetails().stream()
                        .mapToDouble(od -> od.getQuantity() * od.getProductSell().getCost())
                        .sum();
                revenueByStaff.merge(staffId, orderRevenue, Double::sum);
            }
        }

        List<StaffRevenueResponse> responseList = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : revenueByStaff.entrySet()) {
            staffAccountRepository.findById(entry.getKey()).ifPresent(staff -> responseList.add(new StaffRevenueResponse(staff.getAccount().getUsername(), entry.getValue())));
        }

        return responseList;
    }

    //Sales made by a staff in a day(amount of products)

    public List<StaffSalesResponse> getSalesMadeByStaff(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<PurchaseOrder> orders = orderRepository.findOrdersByDateRange(startDateTime, endDateTime);
        Map<Integer, Integer> salesByStaff = new HashMap<>();

        for (PurchaseOrder order : orders) {
            StaffAccount staffAccount = order.getStaffAccount();
            if (staffAccount != null) {
                int staffId = staffAccount.getStaffID();
                int totalSales = order.getOrderDetails().stream()
                        .mapToInt(OrderDetail::getQuantity)
                        .sum();
                salesByStaff.merge(staffId, totalSales, Integer::sum);
            }
        }

        List<StaffSalesResponse> responseList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : salesByStaff.entrySet()) {
            staffAccountRepository.findById(entry.getKey()).ifPresent(staff -> responseList.add(new StaffSalesResponse(staff.getAccount().getUsername(), entry.getValue())));
        }

        return responseList;
    }
    public List<DiscountEffectivenessResponse> getDiscountCodeEffectiveness() {
        List<OrderDetail> orderDetails = orderDetailRepository.findAll();

        Map<String, Long> discountCodeCount = orderDetails.stream()
                .filter(orderDetail -> orderDetail.getPromotion() != null && orderDetail.getPromotion().getCode() != null)
                .collect(Collectors.groupingBy(orderDetail -> orderDetail.getPromotion().getCode(), Collectors.counting()));

        return discountCodeCount.entrySet().stream()
                .map(entry -> {
                    DiscountEffectivenessResponse response = new DiscountEffectivenessResponse();
                    response.setDiscountCode(entry.getKey());
                    response.setNumberUse(Math.toIntExact(entry.getValue()));
                    return response;
                })
                .sorted(Comparator.comparingInt(DiscountEffectivenessResponse::getNumberUse).reversed())
                .collect(Collectors.toList());
    }
}
package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.*;
import com.project.JewelryMS.model.Dashboard.*;
import com.project.JewelryMS.model.Dashboard.Customer.*;
import com.project.JewelryMS.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private StaffAccountRepository staffAccountRepository;
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;
    @Autowired
    private ShiftRepository shiftRepository;
    @Autowired
    private WorkAreaRepository workAreaRepository;
    @Autowired
    private StaffShiftRepository staffShiftRepository;

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
        List<PurchaseOrder> orders = orderRepository.findAllCompleteOrder();

        // Tính tổng doanh thu cho category
        float totalRevenue = 0.0F;

        for(PurchaseOrder order : orders){
            Set<OrderDetail> orderDetails = order.getOrderDetails();
            for(OrderDetail orderDetail : orderDetails){
                if(Long.valueOf(orderDetail.getProductSell().getCategory().getId()).equals(Category_ID)){
                    float orderDetailTotal = orderDetail.getQuantity() * orderDetail.getProductSell().getCost();
                    // Apply discount if promotion is available
                    if (orderDetail.getPromotion() != null) {
                        float discount = orderDetail.getPromotion().getDiscount() / 100.0F;
                        orderDetailTotal -= orderDetailTotal * discount;
                    }
                    totalRevenue += orderDetailTotal;
                }
            }
        }
        return totalRevenue;
    }

    public List<TopSellProductResponse> getTopSellingProducts() {
        List<PurchaseOrder> orders = orderRepository.findAllCompleteOrder();

        // Sử dụng Map để lưu trữ amount và doanh thu bán hàng cho từng product
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

                float orderDetailRevenue = orderDetail.getQuantity() * product.getCost();
                // Apply discount if promotion is available
                if (orderDetail.getPromotion() != null) {
                    float discount = orderDetail.getPromotion().getDiscount() / 100.0F;
                    orderDetailRevenue -= orderDetailRevenue * discount;
                }
                topSellProductResponse.setRevenueSold(topSellProductResponse.getRevenueSold() != null ? topSellProductResponse.getRevenueSold() + orderDetailRevenue : orderDetailRevenue);

                productSalesMap.put(productId, topSellProductResponse);
            }
        }

        // convert Map sang List và sắp xếp theo sales amount or doanh thu
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
                    // Apply discount if promotion is available
                    if (orderDetail.getPromotion() != null) {
                        float discount = orderDetail.getPromotion().getDiscount() / 100.0F;
                        orderDetailTotal -= orderDetailTotal * discount;
                    }
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

                float orderDetailRevenue = orderDetail.getQuantity() * product.getCost();
                // Apply discount if promotion is available
                if (orderDetail.getPromotion() != null) {
                    float discount = orderDetail.getPromotion().getDiscount() / 100.0F;
                    orderDetailRevenue -= orderDetailRevenue * discount;
                }
                topSellProductResponse.setRevenueSold(topSellProductResponse.getRevenueSold() != null ? topSellProductResponse.getRevenueSold() + orderDetailRevenue : orderDetailRevenue);

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



    public YearComparisonResponse compareYear(YearComparisonRequest request) {
        int startYear = Integer.parseInt(request.getYear1());
        int endYear = Integer.parseInt(request.getYear2());

        List<String> years = new ArrayList<>();
        for (int year = startYear; year <= endYear; year++) {
            years.add(String.valueOf(year));
        }

        Map<String, Double> revenue = getRevenueByYear(years);
        Map<String, Long> quantity = getQuantityByYear(years);
        Map<String, Long> customerSignup = getCustomerSignupsByYear(years);

        YearComparisonResponse response = new YearComparisonResponse();
        response.setRevenue(revenue);
        response.setQuantity(quantity);
        response.setCustomerSignup(customerSignup);

        return response;
    }

    private Map<String, Double> getRevenueByYear(List<String> years) {
        Map<String, Double> result = new HashMap<>();
        for (String year : years) {
            Float totalRevenue = orderRepository.findTotalRevenueByYear(year);
            result.put(year, totalRevenue != null ? totalRevenue : 0.0);
        }
        return result;
    }

    private Map<String, Long> getQuantityByYear(List<String> years) {
        Map<String, Long> result = new HashMap<>();
        for (String year : years) {
            Long totalQuantity = orderRepository.findTotalQuantityByYear(year);
            result.put(year, totalQuantity != null ? totalQuantity : 0L);
        }
        return result;
    }

    private Map<String, Long> getCustomerSignupsByYear(List<String> years) {
        Map<String, Long> result = new HashMap<>();
        for (String year : years) {
            Long signups = customerRepository.findCustomerSignupsByYear(year);
            result.put(year, signups != null ? signups : 0L);
        }
        return result;
    }

    public Map<String, Float> getDailyAverageRevenuePerMonth(String startMonthYear, String endMonthYear) {
        Map<String, Float> dailyAverageRevenueMap = new HashMap<>();

        try {
            // Parse input months and years
            SimpleDateFormat monthYearFormat = new SimpleDateFormat("yyyy-MM");
            Date startDate = monthYearFormat.parse(startMonthYear);
            Date endDate = monthYearFormat.parse(endMonthYear);

            // Adjust end date to include the whole month
            Calendar cal = Calendar.getInstance();
            cal.setTime(endDate);
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            endDate = cal.getTime();

            List<DailyRevenueProjection> dailyRevenues = orderDetailRepository.findDailyRevenueBetweenDates(startDate, endDate);

            // Use a map to aggregate total revenue per month
            Map<String, Float> monthlyRevenueMap = new HashMap<>();
            Map<String, Integer> dailyCountMap = new HashMap<>();

            for (DailyRevenueProjection dailyRevenue : dailyRevenues) {
                String month = new SimpleDateFormat("yyyy-MM").format(dailyRevenue.getPurchaseDate());

                monthlyRevenueMap.put(month, monthlyRevenueMap.getOrDefault(month, 0.0f) + dailyRevenue.getTotalRevenue());
                dailyCountMap.put(month, dailyCountMap.getOrDefault(month, 0) + 1);
            }

            // Calculate daily average revenue for each month
            for (Map.Entry<String, Float> entry : monthlyRevenueMap.entrySet()) {
                String month = entry.getKey();
                Float totalRevenue = entry.getValue();
                Integer dayCount = dailyCountMap.get(month);

                dailyAverageRevenueMap.put(month, totalRevenue / dayCount);
            }
        } catch (ParseException e) {
            // Log the error or handle it as needed
            e.printStackTrace();
        }

        return dailyAverageRevenueMap;
    }

    public Map<String, Double> getDailyRevenue(String startDateStr, String endDateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(startDateStr);
        Date endDate = sdf.parse(endDateStr);

        List<DailyRevenueProjection> dailyRevenues = orderDetailRepository.findDailyRevenue(startDate, endDate);

        Map<String, Double> dailyRevenueMap = new HashMap<>();

        for (DailyRevenueProjection revenue : dailyRevenues) {
            String date = sdf.format(revenue.getPurchaseDate());
            double totalRevenue = (double) revenue.getTotalRevenue();

            if (dailyRevenueMap.containsKey(date)) {
                totalRevenue += dailyRevenueMap.get(date);
            }

            dailyRevenueMap.put(date, totalRevenue);
        }

        return dailyRevenueMap;
    }


    public List<ProductSellTrendResponse> getTopProductsForAllCustomers() {
        List<OrderDetail> orderDetails = orderDetailRepository.findAll();

        Map<Long, List<OrderDetail>> groupedResults = orderDetails.stream()
                .filter(od -> od.getPurchaseOrder() != null && od.getPurchaseOrder().getCustomer() != null && od.getPurchaseOrder().getStatus() == 3)
                .collect(Collectors.groupingBy(od -> od.getPurchaseOrder().getCustomer().getPK_CustomerID()));

        List<ProductSellTrendResponse> responses = new ArrayList<>();
        groupedResults.forEach((customerId, productList) -> {
            Map<String, Integer> productCountMap = new HashMap<>();
            productList.forEach(orderDetail -> {
                String productName = orderDetail.getProductSell().getPName();
                productCountMap.put(productName, productCountMap.getOrDefault(productName, 0) + orderDetail.getQuantity());
            });

            List<Map.Entry<String, Integer>> sortedProducts = productCountMap.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .toList();

            ProductSellTrendResponse response = new ProductSellTrendResponse();
            response.setCustomerName(customerService.getCustomerNameById(customerId));

            if (!sortedProducts.isEmpty()) response.setProductTop1(sortedProducts.get(0).getKey());
            if (sortedProducts.size() > 1) response.setProductTop2(sortedProducts.get(1).getKey());
            if (sortedProducts.size() > 2) response.setProductTop3(sortedProducts.get(2).getKey());

            responses.add(response);
        });

        return responses;
    }

    public List<CustomerPurchaseHistoryResponse> getAllCustomerPurchaseHistories() {
        List<PurchaseOrder> purchaseOrders = orderRepository.findAllWithDetails();
        Map<Long, List<PurchaseOrder>> ordersByCustomer = purchaseOrders.stream()
                .filter(po -> po.getCustomer() != null)
                .collect(Collectors.groupingBy(po -> po.getCustomer().getPK_CustomerID()));

        List<CustomerPurchaseHistoryResponse> responses = new ArrayList<>();

        for (Map.Entry<Long, List<PurchaseOrder>> entry : ordersByCustomer.entrySet()) {
            Long customerId = entry.getKey();
            List<PurchaseOrder> customerOrders = entry.getValue();

            if (customerOrders.isEmpty()) {
                continue;
            }

            for (PurchaseOrder po : customerOrders) {
                CustomerPurchaseHistoryResponse response = new CustomerPurchaseHistoryResponse();
                response.setCustomerName(po.getCustomer().getCusName());
                response.setOrderID(po.getPK_OrderID());
                response.setPaymentType(po.getPaymentType());
                response.setPurchaseDate(po.getPurchaseDate());
                response.setStatus(po.getStatus());
                response.setTotal(po.getTotalAmount());
                if (po.getStaffAccount() != null) {
                    response.setStaffID(po.getStaffAccount().getStaffID());
                    response.setStaffName(po.getStaffAccount().getAccount().getAccountName());
                } else {
                    response.setStaffID(null);
                    response.setStaffName(null);
                }
                responses.add(response);
            }
        }

        return responses;
    }

    public StaffStatisticsResponse getStaffStats(long staffId) {
        // Fetch the email of the staff using the staff ID
        String email = staffAccountRepository.findEmailByStaffId(staffId);

        // Fetch the statistics using the email
        long customerSignUps = customerRepository.countCustomerSignUpsByStaffEmail(email);
        Double revenueGenerated = purchaseOrderRepository.getTotalRevenueByStaffEmail(email);
        long salesCount = purchaseOrderRepository.getSalesCountByStaffEmail(email);
        long shiftsCount = shiftRepository.countShiftsByStaffEmail(email);

        return mapToStaffStatisticsResponse(email,staffId, customerSignUps, revenueGenerated, salesCount, shiftsCount);
    }


    public StaffStatisticsResponse getStaffStatsInRange(String email, LocalDate startDate, LocalDate endDate) {
        StaffAccount staffAccount = staffAccountRepository.findByEmail(email);
        if (staffAccount == null) {
            throw new EntityNotFoundException("Staff not found with email: " + email);
        }

        long staffId = staffAccount.getStaffID();
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

        long customerSignUps = customerRepository.countCustomerSignUpsByStaffEmailAndDateRange(
                email,
                Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant())
        );
        Double revenueGenerated = purchaseOrderRepository.getTotalRevenueByStaffEmailAndDateRange(email, startDateTime, endDateTime);
        if (revenueGenerated == null) {
            revenueGenerated = 0.0;
        }
        long salesCount = purchaseOrderRepository.getSalesCountByStaffEmailAndDateRange(email, startDateTime, endDateTime);
        long shiftsCount = shiftRepository.countShiftsByStaffEmailAndDateRange(email, startDateTime, endDateTime);

        return mapToStaffStatisticsResponse(email, staffId, customerSignUps, revenueGenerated, salesCount, shiftsCount);
    }


    // Mapping function
    private StaffStatisticsResponse mapToStaffStatisticsResponse(String email, long staffId, long customerSignUps, Double revenueGenerated, long salesCount, long shiftsCount) {
        StaffStatisticsResponse response = new StaffStatisticsResponse();
        response.setStaffId(staffId); // Set the staff ID
        response.setEmail(email); // Set the email
        response.setCustomerSignUps(customerSignUps);
        response.setRevenueGenerated(revenueGenerated);
        response.setSalesCount(salesCount);
        response.setShiftsCount(shiftsCount);
        return response;
    }



    //DashBoard workArea statistics Section

    public List<WorkAreaRevenueResponse> getWorkAreaRevenueResponsesByYear() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        List<WorkArea> workAreas = workAreaRepository.findAll();
        return workAreas.stream().map(workArea -> createWorkAreaRevenueResponseByYear(workArea, currentYear)).collect(Collectors.toList());
    }

    public List<WorkAreaRevenueResponse> getWorkAreaRevenueResponsesByMonth() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        List<WorkArea> workAreas = workAreaRepository.findAll();
        return workAreas.stream().map(workArea -> createWorkAreaRevenueResponseByMonth(workArea, currentYear, currentMonth)).collect(Collectors.toList());
    }

    public List<WorkAreaRevenueResponse> getWorkAreaRevenueResponsesByDay() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        List<WorkArea> workAreas = workAreaRepository.findAll();
        return workAreas.stream().map(workArea -> createWorkAreaRevenueResponseByDay(workArea, currentYear, currentMonth, currentDay)).collect(Collectors.toList());
    }

    private WorkAreaRevenueResponse createWorkAreaRevenueResponseByYear(WorkArea workArea, int year) {
        WorkAreaRevenueResponse response = new WorkAreaRevenueResponse();
        response.setWorkAreaID(workArea.getId());
        response.setWorkAreaCode(workArea.getWorkAreaCode());
        response.setDescription(workArea.getDescription());
        response.setStatus(workArea.getStatus());

        List<Staff_Shift> staffShifts = staffShiftRepository.findByWorkArea(workArea);
        List<Integer> staffIDs = staffShifts.stream()
                .map(staffShift -> staffShift.getStaffAccount().getStaffID())
                .collect(Collectors.toList());

        List<PurchaseOrder> orders;
        if (workArea.getWorkAreaCode().startsWith("SALE")) {
            orders = orderRepository.findByStaffSaleIDsAndYear(staffIDs, year);
        } else if (workArea.getWorkAreaCode().startsWith("CASH")) {
            orders = orderRepository.findByStaffIDsAndYear(staffIDs, year);
        } else {
            orders = List.of();
        }

        Float totalRevenue = calculateTotalRevenue(orders);
        response.setTotalRevenueAmount(totalRevenue);
        response.setNumberOrder(orders.size());
        response.setStaffWorkAreaRevenues(calculateStaffRevenues(staffIDs, orders, workArea.getWorkAreaCode()));

        return response;
    }

    private WorkAreaRevenueResponse createWorkAreaRevenueResponseByMonth(WorkArea workArea, int year, int month) {
        WorkAreaRevenueResponse response = new WorkAreaRevenueResponse();
        response.setWorkAreaID(workArea.getId());
        response.setWorkAreaCode(workArea.getWorkAreaCode());
        response.setDescription(workArea.getDescription());
        response.setStatus(workArea.getStatus());

        List<Staff_Shift> staffShifts = staffShiftRepository.findByWorkArea(workArea);
        List<Integer> staffIDs = staffShifts.stream()
                .map(staffShift -> staffShift.getStaffAccount().getStaffID())
                .collect(Collectors.toList());

        List<PurchaseOrder> orders;
        if (workArea.getWorkAreaCode().startsWith("SALE")) {
            orders = orderRepository.findByStaffSaleIDsAndMonth(staffIDs, year, month);
        } else if (workArea.getWorkAreaCode().startsWith("CASH")) {
            orders = orderRepository.findByStaffIDsAndMonth(staffIDs, year, month);
        } else {
            orders = List.of();
        }

        Float totalRevenue = calculateTotalRevenue(orders);
        response.setTotalRevenueAmount(totalRevenue);
        response.setNumberOrder(orders.size());
        response.setStaffWorkAreaRevenues(calculateStaffRevenues(staffIDs, orders, workArea.getWorkAreaCode()));

        return response;
    }

    private WorkAreaRevenueResponse createWorkAreaRevenueResponseByDay(WorkArea workArea, int year, int month, int day) {
        WorkAreaRevenueResponse response = new WorkAreaRevenueResponse();
        response.setWorkAreaID(workArea.getId());
        response.setWorkAreaCode(workArea.getWorkAreaCode());
        response.setDescription(workArea.getDescription());
        response.setStatus(workArea.getStatus());

        List<Staff_Shift> staffShifts = staffShiftRepository.findByWorkArea(workArea);
        List<Integer> staffIDs = staffShifts.stream()
                .map(staffShift -> staffShift.getStaffAccount().getStaffID())
                .collect(Collectors.toList());

        List<PurchaseOrder> orders;
        if (workArea.getWorkAreaCode().startsWith("SALE")) {
            orders = orderRepository.findByStaffSaleIDsAndDay(staffIDs, year, month, day);
        } else if (workArea.getWorkAreaCode().startsWith("CASH")) {
            orders = orderRepository.findByStaffIDsAndDay(staffIDs, year, month, day);
        } else {
            orders = List.of();
        }

        Float totalRevenue = calculateTotalRevenue(orders);
        response.setTotalRevenueAmount(totalRevenue);
        response.setNumberOrder(orders.size());
        response.setStaffWorkAreaRevenues(calculateStaffRevenues(staffIDs, orders, workArea.getWorkAreaCode()));

        return response;
    }

    private Float calculateTotalRevenue(List<PurchaseOrder> orders) {
        if (orders == null) {
            return 0f;
        }
        return orders.stream()
                .filter(Objects::nonNull) // Check for non-null orders
                .map(order -> {
                    Float totalAmount = order.getTotalAmount();
                    return totalAmount != null ? totalAmount : 0f; // Handle null totalAmount
                })
                .reduce(0f, Float::sum);
    }

    private List<StaffWorkAreaRevenue> calculateStaffRevenues(List<Integer> staffIDs, List<PurchaseOrder> orders, String workAreaCode) {
        if (orders == null) {
            return List.of();
        }

        Map<Integer, Float> staffRevenueMap = orders.stream()
                .filter(order -> order != null && (workAreaCode.startsWith("SALE")
                        ? staffIDs.contains(order.getStaffAccountSale().getStaffID())
                        : staffIDs.contains(order.getStaffAccount().getStaffID())))
                .collect(Collectors.groupingBy(
                        order -> workAreaCode.startsWith("SALE")
                                ? order.getStaffAccountSale().getStaffID()
                                : order.getStaffAccount().getStaffID(),
                        Collectors.reducing(0f, order -> {
                            Float totalAmount = order.getTotalAmount();
                            return totalAmount != null ? totalAmount : 0f;
                        }, Float::sum)
                ));

        return staffRevenueMap.entrySet().stream()
                .map(entry -> {
                    Integer staffID = entry.getKey();
                    Float revenueAmount = entry.getValue();
                    StaffAccount staff = staffShiftRepository.findStaffAccountById(staffID);
                    if (staff != null) {
                        StaffWorkAreaRevenue staffRevenue = new StaffWorkAreaRevenue();
                        staffRevenue.setStaffID(staffID);
                        staffRevenue.setStaffName(staff.getAccount().getUsername());
                        staffRevenue.setRevenueAmount(revenueAmount);
                        return staffRevenue;
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }




}
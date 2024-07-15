package com.project.JewelryMS.service.Order;

import com.project.JewelryMS.entity.*;
import com.project.JewelryMS.model.EmailDetail;
import com.project.JewelryMS.model.Order.*;
import com.project.JewelryMS.model.OrderDetail.OrderDetailResponse;
import com.project.JewelryMS.repository.*;
import com.project.JewelryMS.service.EmailService;
import com.project.JewelryMS.service.ImageService;
import com.project.JewelryMS.service.ProductBuyService;
import com.project.JewelryMS.service.ProductSellService;
import jakarta.annotation.PreDestroy;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderHandlerService {
    @Autowired
    OrderDetailService orderDetailService;
    @Autowired
    OrderService orderService;
    @Autowired
    ProductSellService productSellService;
    @Autowired
    ProductSellRepository productSellRepository;
    @Autowired
    ProductBuyRepository productBuyRepository;
    @Autowired
    ProductBuyService productBuyService;
    @Autowired
    OrderBuyDetailService orderBuyDetailService;
    @Autowired
    EmailService emailService;
    @Autowired
    PromotionRepository promotionRepository;
    @Autowired
    GuaranteeRepository guaranteeRepository;
    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    StaffAccountRepository staffAccountRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ImageService imageService;
    @PreDestroy
    public void cleanup() {
        scheduler.shutdown();
    }
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private final ConcurrentHashMap<Long, String> claimedOrders = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    public boolean claimOrder(Long orderId, String userId) {
        if (claimedOrders.putIfAbsent(orderId, userId) == null) {
            // Order was successfully claimed
            scheduler.schedule(() -> releaseOrder(orderId, userId), 3, TimeUnit.MINUTES);
            return true;
        }
        return false; // Order was already claimed
    }
    public boolean releaseOrder(Long orderId, String userId) {
        if (claimedOrders.remove(orderId, userId)) {
            // Order was successfully released
            messagingTemplate.convertAndSend("/topic/new-order", orderId);
            return true;
        }
        return false; // Order was not claimed by this user or already released
    }
    @Transactional
    public Long createOrderWithDetails(PurchaseOrder purchaseOrder, List<OrderDetail> list){
        Set<OrderDetail> detailSet = new HashSet<>();
        for(OrderDetail detail : list){
            detail.setPurchaseOrder(purchaseOrder);
            detailSet.add(detail);
        }
        purchaseOrder.setOrderDetails(detailSet);
        orderService.saveOrder(purchaseOrder);

        return purchaseOrder.getPK_OrderID();
    }
    public Long handleCreateOrderWithDetails(CreateOrderRequest orderRequest, List<CreateOrderDetailRequest> detailRequest, String email) {
        PurchaseOrder order = new PurchaseOrder();
        order.setStatus(orderRequest.getStatus());
        order.setPurchaseDate(new Date());
        Long id = -1L;
        if(orderRequest.getPaymentType()!=null) {
            order.setPaymentType(orderRequest.getPaymentType());
        }else{
            order.setPaymentType(null);
        }
        order.setTotalAmount(orderRequest.getTotalAmount());
        if (orderRequest.getCustomer_ID() != null) {
            customerRepository.findById(orderRequest.getCustomer_ID()).ifPresent(order::setCustomer);
        } else {
            order.setCustomer(null);
        }

        if (orderRequest.getStaff_ID() != null) {
            staffAccountRepository.findById(orderRequest.getStaff_ID()).ifPresent(order::setStaffAccount);
        } else {
            order.setStaffAccount(null);
        }
        order.setEmail(email);
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CreateOrderDetailRequest detail : detailRequest) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setQuantity(detail.getQuantity());
                orderDetail.setProductSell(productSellService.getProductSellById(detail.getProductID()));
                System.out.println(productSellService.getProductSellById(detail.getProductID()));
                orderDetail.setPurchaseOrder(order);

            orderDetails.add(orderDetail);
            }
        System.out.println(orderDetails.toString());
        if (!orderDetails.isEmpty()) {
                id = createOrderWithDetails(order, orderDetails);
                System.out.println(id);
        }
        return id;
    }


    //Product Buy Section///////////////////////////////////////////////////////////////////////////////////////////////
    @Transactional
    public Long createOrderWithBuyDetails(PurchaseOrder purchaseOrder, List<OrderBuyDetail> list){
        Set<OrderBuyDetail> detailSet = new HashSet<>();
        for(OrderBuyDetail detail : list){
            detail.setPurchaseOrder(purchaseOrder);
            detailSet.add(detail);
        }
        purchaseOrder.setOrderBuyDetails(detailSet);
        System.out.println(purchaseOrder.getPK_OrderID());
        orderService.saveOrder(purchaseOrder);
        return purchaseOrder.getPK_OrderID();
    }

    public Long handleCreateOrderBuyWithDetails(CreateOrderBuyWrapper createOrderBuyWrapper ){
        PurchaseOrder order = new PurchaseOrder();
        Long id = -1L;
        // Check if the input list is null
        if (createOrderBuyWrapper.getProductBuyLists() == null) {
            throw new IllegalArgumentException("createProductBuyRequests cannot be null");
        }
        List<Long> ProductBuyIDList = createOrderBuyWrapper.getProductBuyLists();
        CreatePBOrderRequest orderRequest = createOrderBuyWrapper.getOrderRequest();

        order.setPurchaseDate(new Date());
        order.setStatus(orderRequest.getStatus());
        order.setPaymentType(orderRequest.getPaymentType());
        order.setTotalAmount(orderRequest.getTotalAmount());
        if (orderRequest.getStaff_ID() != null) {
            staffAccountRepository.findById(orderRequest.getStaff_ID()).ifPresent(order::setStaffAccount);
        } else {
            order.setStaffAccount(null);
        }

        List<OrderBuyDetail> orderBuyDetails = new ArrayList<>();
        for(Long ProductBuyIDs : ProductBuyIDList){
            OrderBuyDetail orderBuyDetail = new OrderBuyDetail();
            orderBuyDetail.setProductBuy(productBuyService.getProductBuyById2(ProductBuyIDs));
            orderBuyDetail.setPurchaseOrder(order);
            orderBuyDetails.add(orderBuyDetail);
        }

        if(!orderBuyDetails.isEmpty()){
            id = createOrderWithBuyDetails(order, orderBuyDetails);
        }
        return id;
    }
    public void addOrderBuyDetail(Long orderId, Long productBuyId) {
        PurchaseOrder order = orderService.getOrderById(orderId);
        ProductBuy productBuy= productBuyService.getProductBuyById2(productBuyId);

        OrderBuyDetail orderBuyDetail = new OrderBuyDetail();
        orderBuyDetail.setProductBuy(productBuy);
        orderBuyDetail.setPurchaseOrder(order);
        orderBuyDetailService.saveOrderBuyDetail(orderBuyDetail);
    }

    public List<OrderBuyResponse> getAllBuyOrder(){
        List<OrderBuyResponse> result = new ArrayList<>();
        List<PurchaseOrder> orderList = orderService.getAllOrders();
        System.out.println(orderList.toString());
        for(PurchaseOrder order : orderList) {
            if (order.getOrderBuyDetails() != null && !order.getOrderBuyDetails().isEmpty()) {
                OrderBuyResponse orderToGet = new OrderBuyResponse();
                orderToGet.setStatus(order.getStatus());
                orderToGet.setPaymentType(order.getPaymentType());
                orderToGet.setTotalAmount(order.getTotalAmount());
                orderToGet.setPurchaseDate(order.getPurchaseDate());
                Set<ProductBuyResponse> productBuyResponses = new HashSet<>();
                List<OrderBuyDetail> iterateList = order.getOrderBuyDetails().stream().toList();
                for (OrderBuyDetail item : iterateList) {
                    ProductBuy product = item.getProductBuy();
                    ProductBuyResponse response = new ProductBuyResponse();
                    response.setProductBuyID(product.getPK_ProductBuyID());
                    response.setCategoryName(product.getCategory().getName());
                    response.setMetalType(product.getMetalType());
                    response.setGemstoneType(product.getGemstoneType());
                    response.setPbName(product.getPbName());
                    response.setCost(product.getPbCost());
                    productBuyResponses.add(response);
                }
                orderToGet.setProductBuyDetail(productBuyResponses);
                result.add(orderToGet);
            }
        }
        return result;
    }

    public List<ProductBuyResponse> getProductBuyByOrderId(Long orderID) {
        PurchaseOrder order = orderService.getOrderById(orderID);
        Set<ProductBuyResponse> productBuyResponses = new HashSet<>();
        System.out.println("reached " + order.getPurchaseDate());
        if (order.getOrderBuyDetails() != null && !order.getOrderBuyDetails().isEmpty()) {
            for (OrderBuyDetail item : order.getOrderBuyDetails()) {

                ProductBuy product = item.getProductBuy();
                ProductBuyResponse response = new ProductBuyResponse();
                response.setProductBuyID(product.getPK_ProductBuyID());
                response.setCategoryName(product.getCategory().getName());
                response.setMetalType(product.getMetalType());
                response.setGemstoneType(product.getGemstoneType());
                response.setPbName(product.getPbName());
                response.setCost(product.getPbCost());
                productBuyResponses.add(response);
            }
        }
        return new ArrayList<>(productBuyResponses);
    }
    //Product Buy Section///////////////////////////////////////////////////////////////////////////////////////////////

    public void addOrderDetail(Long orderId, Long productId, Integer quantity) {
        PurchaseOrder order = orderService.getOrderById(orderId);
        ProductSell productSell = productSellService.getProductSellById(productId);

        OrderDetail orderDetailId = new OrderDetail();
        orderDetailId.setProductSell(productSell);
        orderDetailId.setPurchaseOrder(order);
        orderDetailId.setQuantity(quantity);
        orderDetailService.saveOrderDetail(orderDetailId);
    }
    public List<OrderResponse> getAllOrder(){
        List<OrderResponse> result = new ArrayList<>();
        List<PurchaseOrder> orderList = orderService.getAllOrders();
        System.out.println(orderList.toString());
        for(PurchaseOrder order : orderList){
            OrderResponse orderToGet = new OrderResponse();
            orderToGet.setStatus(order.getStatus());
            orderToGet.setPaymentType(order.getPaymentType());
            orderToGet.setTotalAmount(order.getTotalAmount());
            orderToGet.setPurchaseDate(order.getPurchaseDate());
            if (order.getCustomer() != null) {
                Long customerID = order.getCustomer().getPK_CustomerID();
                orderToGet.setCustomer_ID(customerID);
            } else {
                orderToGet.setCustomer_ID(null);
            }
            if (order.getStaffAccount() != null) {
                Integer staffID = order.getStaffAccount().getStaffID();
                orderToGet.setStaff_ID(staffID);
            } else {
                // Handle the case where the customer is null, if necessary
                orderToGet.setStaff_ID(null); // or some default value
            }
            Set<ProductResponse> productResponses = new HashSet<>();
            List<OrderDetail> iterateList = order.getOrderDetails().stream().toList();
            for(OrderDetail item : iterateList){
                ProductSell product = item.getProductSell();
                ProductResponse response = new ProductResponse();
                response.setQuantity(item.getQuantity());
                response.setProductID(product.getProductID());
                response.setName(product.getPName());
                response.setCarat(product.getCarat());
                response.setChi(product.getChi());
                response.setCost(product.getCost());
                response.setDescription(product.getPDescription());
                response.setGemstoneType(product.getGemstoneType());
                response.setImage(product.getImage());
                response.setManufacturer(product.getManufacturer());
                response.setManufactureCost(product.getManufactureCost());
                response.setStatus(product.isPStatus());
                response.setCategory_id(product.getProductID());
                List<Long> listPromotion = productSellRepository.findPromotionIdsByProductSellId((product.getProductID()));
                List<String> promotionIds = new ArrayList<>();

                for (Long promotionId : listPromotion) {
                    promotionIds.add(String.valueOf(promotionId));
                }

                response.setPromotion_id(promotionIds);


                productResponses.add(response);
            }
            orderToGet.setProductDetail(productResponses);
            result.add(orderToGet);

        }
        return result;
    }
    public List<ProductResponse> getProductByOrderId(Long orderID) {
        PurchaseOrder order = orderService.getOrderById(orderID);
        Set<ProductResponse> productResponses = new HashSet<>();
        System.out.println("reached " + order.getPurchaseDate());

        for (OrderDetail item : order.getOrderDetails()) {

            ProductSell product = item.getProductSell();
            ProductResponse response = new ProductResponse();
            response.setOrderDetail_ID(item.getPK_ODID());
            response.setQuantity(item.getQuantity());
            response.setProductID(product.getProductID());
            response.setName(product.getPName());
            response.setCarat(product.getCarat());
            response.setChi(product.getChi());
            response.setCost(product.getCost());
            response.setDescription(product.getPDescription());
            response.setGemstoneType(product.getGemstoneType());
            response.setImage(product.getImage());
            response.setManufacturer(product.getManufacturer());
            response.setManufactureCost(product.getManufactureCost());
            response.setStatus(product.isPStatus());
            response.setCategory_id(product.getProductID());

            List<String> promotionIds = productSellRepository.findPromotionIdsByProductSellId(product.getProductID())
                    .stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList());
            response.setPromotion_id(promotionIds);

            productResponses.add(response);
        }

        return new ArrayList<>(productResponses);
    }

    public List<ProductResponse> generateEmailOrderTable(Long orderID){
        PurchaseOrder order = orderService.getOrderById(orderID);
        OrderResponse orderToGet = new OrderResponse();
        orderToGet.setStatus(order.getStatus());
        orderToGet.setPaymentType(order.getPaymentType());
        orderToGet.setTotalAmount(order.getTotalAmount());
        orderToGet.setPurchaseDate(order.getPurchaseDate());
//        if(order.getCustomer()!=null) {
//            Optional<Customer> customerOptional = customerRepository.findById(order.getCustomer().getPK_CustomerID());
//            if (customerOptional.isPresent()) {
//                Customer customer = customerOptional.get();
//                orderToGet.setCustomer_ID(customer.getPK_CustomerID());
//            }else{
//                orderToGet.setC;
//            }
//        }else{
//            order.setCustomer(null);
//        }
//        if(orderRequest.getStaff_ID()!=null) {
//            Optional<StaffAccount> staffAccountOptional = staffAccountRepository.findById(orderRequest.getStaff_ID());
//            if(staffAccountOptional.isPresent()){
//                StaffAccount staffAccount = staffAccountOptional.get();
//                order.setStaffAccount(staffAccount);
//            }else{
//                order.setStaffAccount(null);
//            }
//        }else{
//            order.setStaffAccount(null);
//        }
        Set<ProductResponse> productResponses = new HashSet<>();
        List<OrderDetail> iterateList = order.getOrderDetails().stream().toList();
        for(OrderDetail item : iterateList){
            ProductSell product = item.getProductSell();
            ProductResponse response = new ProductResponse();
            response.setQuantity(item.getQuantity());
            response.setProductID(product.getProductID());
            response.setName(product.getPName());
            response.setCarat(product.getCarat());
            response.setChi(product.getChi());
            response.setCost(product.getCost());
            response.setDescription(product.getPDescription());
            response.setGemstoneType(product.getGemstoneType());
            response.setImage(product.getImage());
            response.setManufacturer(product.getManufacturer());
            response.setManufactureCost(product.getManufactureCost());
            response.setStatus(product.isPStatus());
            response.setCategory_id(product.getProductID());
            List<Long> listPromotion = productSellRepository.findPromotionIdsByProductSellId((product.getProductID()));
            List<String> promotionIds = new ArrayList<>();

            for (Long promotionId : listPromotion) {
                promotionIds.add(String.valueOf(promotionId));
            }

            response.setPromotion_id(promotionIds);


            productResponses.add(response);
        }
        return productResponses.stream().toList();

    }

    public void updateOrderStatus(String info){
        int orderID = Integer.parseInt(info.replace("Thanh toan ", "").trim());

        PurchaseOrder orderToUpdate = orderService.getOrderById((long) orderID);
        System.out.println(orderToUpdate.toString());
        orderToUpdate.setStatus(3);
        orderToUpdate.setPaymentType("VNPAY");
        calculateAndSetGuaranteeEndDate((long) orderID);
        sendConfirmationEmail((long) orderID, orderToUpdate.getEmail());
        System.out.println(orderToUpdate);
        orderService.saveOrder(orderToUpdate);
    }

    public String updateOrderBuyStatus(ConfirmPaymentPBRequest confirmPaymentPBRequest) {
        Long orderId = confirmPaymentPBRequest.getOrder_ID();
        if (orderId == null) {
            return "Xác nhận qui trình thanh toán thất bại: Order ID is null";
        }

        Optional<PurchaseOrder> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            PurchaseOrder purchaseOrder = orderOptional.get();

            // Assuming imageService.uploadImageByPathService can handle base64 encoded string
            String image = imageService.uploadImageByPathService(confirmPaymentPBRequest.getImage());
            purchaseOrder.setImage(image);
            purchaseOrder.setStatus(3);
            List<ProductBuy> productBuyList = new ArrayList<>();
            for(OrderBuyDetail orderBuyDetail: purchaseOrder.getOrderBuyDetails()){
                productBuyList.add(orderBuyDetail.getProductBuy());
            }
            for(ProductBuy productBuy: productBuyList){
                ProductSell productSell = new ProductSell();
                productSell.setPStatus(true);
                productSell.setChi(productBuy.getChi());
                productSell.setCarat(productBuy.getCarat());
                productSell.setMetalType(productBuy.getMetalType());
                productSell.setGemstoneType(productBuy.getGemstoneType());
                productSell.setPName(productBuy.getPbName());
                productSell.setImage(productBuy.getImage());
                productSell.setCategory(productBuy.getCategory());
                productSell.setPDescription(productBuy.getPbName());
                productSell.setManufactureCost(0.0F);
                productSell.setManufacturer("N/A");
                Float cost = productSellService.calculateProductSellCost(productBuy.getChi(), productBuy.getCarat(), productBuy.getGemstoneType(), productBuy.getMetalType(), 0.0F);
                productSell.setCost(cost);
                String categoryCode = getCategoryCode(productBuy.getCategory().getName());
                String nextCode = getNextProductCode(categoryCode);
                productSell.setProductCode(nextCode);
                productSellRepository.save(productSell);
            }
            orderRepository.save(purchaseOrder);
            return "Xác nhận qui trình thanh toán thành công";
        }
        return "Xác nhận qui trình thanh toán thất bại: Order not found";
    }

    private String getCategoryCode(String categoryName) {
        // Take the first three letters of the category name
        return categoryName.substring(0, 3).toUpperCase();
    }

    private String getNextProductCode(String categoryCode) {
        String maxCode = productSellRepository.findMaxProductCodeByPrefix(categoryCode + "%");
        if (maxCode == null) {
            return categoryCode + "001";
        }
        int nextNumber = Integer.parseInt(maxCode.substring(3)) + 1;
        return categoryCode + String.format("%03d", nextNumber);
    }

    public void sendConfirmationEmail(Long orderId, String recipientEmail) {
        // Prepare EmailDetail object
        EmailDetail emailDetail = new EmailDetail();
        emailDetail.setRecipient(recipientEmail);
        emailDetail.setSubject("Confirmation Email for Order #" + orderId);
        emailDetail.setMsgBody("This is to confirm your order with ID #" + orderId + ". Thank you for your purchase.");

        // Call the service method to send confirmation email
        emailService.sendConfirmEmail(orderId, emailDetail);
    }
    public boolean updateOrderStatusCash(ConfirmCashPaymentRequest request){
        float paidAmount = request.getAmount();
        float askPrice = request.getTotal();
        if(paidAmount < askPrice){
            return false;
        } else {
            PurchaseOrder orderToUpdate = orderService.getOrderById(request.getOrderID());
            if(orderToUpdate != null){
                System.out.println(orderToUpdate);
                orderToUpdate.setStatus(3);
                orderToUpdate.setPaymentType("Tiền mặt");
                calculateAndSetGuaranteeEndDate(request.getOrderID());
                System.out.println(orderToUpdate);
                sendConfirmationEmail(request.getOrderID(), orderToUpdate.getEmail());
                return orderService.saveOrder(orderToUpdate) != null;
            } else{
                return false;
            }
        }

    }


    //Thai Dang fix may thang order detail bo len day, t lamf wrapper tam thoi thoi
    //Calculate SubTotal, Discount product and Total////////////////////////////////////////////////////////////////////////////////////////////////
//    public Float calculateSubTotal(OrderDetailRequest orderDetailRequest) {
//        float totalAmount = 0;
//        Optional<ProductSell> productSellOptional = productSellRepository.findById(orderDetailRequest.getProductSell_ID());
//        if (productSellOptional.isPresent()) {
//            ProductSell productSell = productSellOptional.get();
//            float productCost = productSell.getCost();
//            int quantity = orderDetailRequest.getQuantity();
//            totalAmount = productCost * quantity;
//        }
//        return totalAmount;
//    }
//
//    public Float calculateDiscountProduct(OrderPromotionRequest orderPromotionRequest) {
//        Promotion promotion = promotionRepository.findById(orderPromotionRequest.getPromotionID()).orElseThrow(() -> new IllegalArgumentException("Promotion ID not found"));
//        int discount = promotion.getDiscount();
//        float percentage = discount / 100.0F;
//        float totalAmount = 0.0F;
//        Optional<ProductSell> productSellOptional = productSellRepository.findById(orderPromotionRequest.getProductSell_ID());
//        if (productSellOptional.isPresent()) {
//            ProductSell productSell = productSellOptional.get();
//            float productCost = productSell.getCost();
//            int quantity = orderPromotionRequest.getQuantity();
//            totalAmount = productCost * quantity;
//        }
//        return totalAmount * percentage;
//    }
//
//    public Float TotalOrderDetails(OrderTotalRequest orderTotalRequest) {
//        float subtotal = orderTotalRequest.getSubTotal();
//        float discountProduct = orderTotalRequest.getDiscountProduct();
//        return subtotal - discountProduct;//total
//    }

    public TotalOrderResponse totalOrder(List<TotalOrderRequest> totalOrderRequests) {
        float subTotalResponse = 0.0F;
        float discount_priceResponse = 0.0F;
        float totalResponse = 0.0F;
        for (TotalOrderRequest request : totalOrderRequests) {
            // Fetch product details
            Optional<ProductSell> productSellOpt = productSellRepository.findById(request.getProductSell_ID());
            if (productSellOpt.isPresent()) {
                ProductSell productSell = productSellOpt.get();
                float cost = productSell.getCost();
                float subtotal = cost * request.getQuantity();
                subTotalResponse += subtotal;
                // Fetch promotion details if provided
                float discountAmount = 0.0F;
                if (request.getPromotion_ID() != null) {
                    Optional<Promotion> promotionOptional = promotionRepository.findById(request.getPromotion_ID());
                    if (promotionOptional.isPresent()) {
                        int discount = promotionOptional.get().getDiscount();
                        Optional<OrderDetail> orderDetailOptional = orderDetailRepository.findById(request.getOrderDetail_ID());
                        if(orderDetailOptional.isPresent()){
                            OrderDetail orderDetail = orderDetailOptional.get();
                            orderDetail.setPromotion(promotionOptional.get());
                            orderDetailRepository.save(orderDetail);
                        }
                        float percentage = discount / 100.0F;
                        discountAmount = subtotal * percentage;
                        discount_priceResponse +=discountAmount;
                    }
                }

                // Calculate the total after discount
                float totalDetails = subtotal - discountAmount;
                totalResponse += totalDetails;
            } else {
                throw new IllegalArgumentException("ProductSell ID not found: " + request.getProductSell_ID());
            }
        }
        TotalOrderResponse totalOrderResponse = new TotalOrderResponse();
        totalOrderResponse.setSubTotal(subTotalResponse);
        totalOrderResponse.setDiscount_Price(discount_priceResponse);
        totalOrderResponse.setTotal(totalResponse);
        return totalOrderResponse;
    }

    public String updateOrder(UpdateOrderRequest updateOrderRequest){
        Optional<PurchaseOrder> orderOptional = orderRepository.findById(updateOrderRequest.getOrder_ID());
        if(orderOptional.isPresent()){
            PurchaseOrder order = orderOptional.get();
            if (updateOrderRequest.getCustomer_ID() != null) {
                customerRepository.findById(updateOrderRequest.getCustomer_ID()).ifPresent(order::setCustomer);
            } else {
                order.setCustomer(null);
            }

            if (updateOrderRequest.getStaff_ID() != null) {
                staffAccountRepository.findById(updateOrderRequest.getStaff_ID()).ifPresent(order::setStaffAccount);
            } else {
                order.setStaffAccount(null);
            }
            if(updateOrderRequest.getPaymentType()!=null) {
                order.setPaymentType(updateOrderRequest.getPaymentType());
            }else{
                order.setPaymentType(null);
            }
            orderRepository.save(order);
            return "Update Order Successfully";
        }else{
            return "Order Not Found!!!";
        }
    }
    //Calculate and Set Guarantee End Date////////////////////////////////////////////////////////////////////////////////////////
    public List<OrderDetailResponse> calculateAndSetGuaranteeEndDate(Long orderId) {
        List<OrderDetail> orderDetails = orderDetailRepository.findAll();

        return orderDetails.stream()
                .filter(orderDetail -> orderDetail.getPurchaseOrder().getPK_OrderID().equals(orderId))
                .map(this::processOrderDetail)
                .toList();
    }

    private OrderDetailResponse processOrderDetail(OrderDetail orderDetail) {
        ProductSell productSell = orderDetail.getProductSell();
        if (productSell != null) {
            Optional<Guarantee> guaranteeOpt = guaranteeRepository.findByProductSell(productSell);

            if (guaranteeOpt.isPresent()) {
                Guarantee guarantee = guaranteeOpt.get();
                Integer warrantyPeriodMonth = guarantee.getWarrantyPeriodMonth();

                // Calculate guaranteeEndDate
                Timestamp now = new Timestamp(System.currentTimeMillis());
                Timestamp guaranteeEndDate = calculateGuaranteeEndDate(now, warrantyPeriodMonth);
                orderDetail.setGuaranteeEndDate(guaranteeEndDate);

                orderDetailRepository.save(orderDetail);

                // Create response
                return mapToOrderDetailResponse(orderDetail);
            }
        }
        return null;
    }
    private Timestamp calculateGuaranteeEndDate(Timestamp startDate, Integer warrantyPeriodMonth) {
        LocalDateTime startDateTime = startDate.toLocalDateTime();
        startDateTime = startDateTime.plusMonths(warrantyPeriodMonth);
        return Timestamp.valueOf(startDateTime);
    }

    private OrderDetailResponse mapToOrderDetailResponse(OrderDetail orderDetail) {
        OrderDetailResponse response = new OrderDetailResponse();
        response.setPK_ODID(orderDetail.getPK_ODID());
        response.setProductSell_ID(orderDetail.getProductSell().getProductID());
        response.setPurchaseOrder_ID(orderDetail.getPurchaseOrder().getPK_OrderID());
        response.setQuantity(orderDetail.getQuantity());
        response.setGuaranteeEndDate(orderDetail.getGuaranteeEndDate());
        return response;
    }

    //Calculate and Set Guarantee End Date///////////////////////////////////////////////////////////////////////////////////////////////////

    //Search Customer Name or Email or PhoneNumber ==> Customer ID
    //What if customerName duplicate

    public List<CustomerOrderGuaranteeResponse> searchCustomerGuarantee(String search) {
        List<Customer> customers = customerRepository.findByCusNameContainingIgnoreCase(search);
        if (customers.isEmpty()) {
            customers = customerRepository.findByEmailContainingIgnoreCase(search);
        }
        if (customers.isEmpty()) {
            customers = customerRepository.findByPhoneNumberContainingIgnoreCase(search);
        }

        return customers.stream()
                .map(customer -> new CustomerOrderGuaranteeResponse(
                        customer.getPK_CustomerID(),
                        customer.getCusName(),
                        orderRepository.findByCustomerID(customer.getPK_CustomerID()).stream()
                                .map(OrderHandlerService::mapToOrderGuaranteeResponse)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    public List<CustomerOrderGuaranteeResponse> getOrdersByDateRange(DateFilterOrderDate dateFilter) {

        LocalDate startDate = dateFilter.getStartTime();
        LocalDate endDate = dateFilter.getEndTime();

        LocalDateTime startDateTime = startDate.atStartOfDay(); // 00:00:00
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX); // 23:59:59.999999999

        List<PurchaseOrder> orders = orderRepository.findOrdersByDateRange(startDateTime, endDateTime);

        return orders.stream()
                .filter(order -> order.getCustomer() != null)
                .map(order -> new CustomerOrderGuaranteeResponse(
                        order.getCustomer().getPK_CustomerID(),
                        order.getCustomer().getCusName(),
                        orderRepository.findByCustomerID(order.getCustomer().getPK_CustomerID()).stream()
                                .map(OrderHandlerService::mapToOrderGuaranteeResponse)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    public static OrderGuaranteeResponse mapToOrderGuaranteeResponse(PurchaseOrder order) {
        return new OrderGuaranteeResponse(
                order.getPK_OrderID(),
                order.getPaymentType(),
                order.getPurchaseDate(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getStaffAccount().getStaffID(),
                order.getStaffAccount().getAccount().getAccountName(),
                order.getOrderDetails().stream().map(OrderHandlerService::mapToOrderDetailGuaranteeResponse).collect(Collectors.toList())
        );
    }

    public static OrderDetailGuaranteeResponse mapToOrderDetailGuaranteeResponse(OrderDetail orderDetail) {
        // Implement the mapping logic here
        // For simplicity, assuming constructor and fields
        ProductSell productSell = orderDetail.getProductSell();
        Guarantee guarantee = orderDetail.getProductSell().getGuarantee();
        return new OrderDetailGuaranteeResponse(
                orderDetail.getPK_ODID(),
                orderDetail.getQuantity(),
                orderDetail.getGuaranteeEndDate(),
                productSell.getProductID(),
                productSell.getChi(),
                productSell.getCarat(),
                productSell.getPName(),
                productSell.getPDescription(),
                productSell.getImage(),
                productSell.getGemstoneType(),
                productSell.getMetalType(),
                productSell.getCost(),
                productSell.getProductCode(),
                productSell.isPStatus(),
                productSell.getManufacturer(),
                productSell.getManufactureCost(),
                guarantee.getPK_guaranteeID(),
                guarantee.getCoverage(),
                guarantee.getPolicyType(),
                guarantee.isStatus(),
                guarantee.getWarrantyPeriodMonth()
        );
    }



}

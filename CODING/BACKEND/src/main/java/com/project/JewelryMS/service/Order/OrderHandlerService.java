package com.project.JewelryMS.service.Order;

import com.project.JewelryMS.entity.*;
import com.project.JewelryMS.model.EmailDetail;
import com.project.JewelryMS.model.Order.*;
import com.project.JewelryMS.model.OrderDetail.OrderDetailRequest;
import com.project.JewelryMS.model.OrderDetail.OrderDetailResponse;
import com.project.JewelryMS.model.OrderDetail.OrderPromotionRequest;
import com.project.JewelryMS.model.OrderDetail.OrderTotalRequest;
import com.project.JewelryMS.repository.*;
import com.project.JewelryMS.service.EmailService;
import com.project.JewelryMS.service.ProductBuyService;
import com.project.JewelryMS.service.ProductSellService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        order.setPaymentType(orderRequest.getPaymentType());
        order.setTotalAmount(orderRequest.getTotalAmount());
        if (orderRequest.getCustomer_ID() != null) {
            Optional<Customer> customerOptional = customerRepository.findById(orderRequest.getCustomer_ID());
            if (customerOptional.isPresent()) {
                Customer customer = customerOptional.get();
                order.setCustomer(customer);
            } else {
                order.setCustomer(null);
            }
        } else {
            order.setCustomer(null);
        }

        if (orderRequest.getStaff_ID() != null) {
            Optional<StaffAccount> staffAccountOptional = staffAccountRepository.findById(orderRequest.getStaff_ID());
            if (staffAccountOptional.isPresent()) {
                StaffAccount staffAccount = staffAccountOptional.get();
                order.setStaffAccount(staffAccount);
            } else {
                order.setStaffAccount(null);
            }
        } else {
            order.setStaffAccount(null);
            Long customerID = orderRequest.getCustomer_ID(); // This could be null
            Optional<Long> optionalCustomerId = Optional.ofNullable(customerID);
            if (optionalCustomerId.isPresent()) {
                Optional<Customer> customerOptional = customerRepository.findById(customerID);
                if (customerOptional.isPresent()) {
                    Customer customer = customerOptional.get();
                    order.setCustomer(customer);
                }
            }

            order.setEmail(email);
            List<OrderDetail> orderDetails = new ArrayList<>();
            for (CreateOrderDetailRequest detail : detailRequest) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setQuantity(detail.getQuantity());
                orderDetail.setProductSell(productSellService.getProductSellById(detail.getProductID()));
                orderDetail.setPurchaseOrder(order);
                orderDetails.add(orderDetail);
            }

            if (!orderDetails.isEmpty()) {
                id = createOrderWithDetails(order, orderDetails);
            }

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
        orderService.saveOrder(purchaseOrder);
        return purchaseOrder.getPK_OrderID();
    }

    public Long handleCreateOrderBuyWithDetails(List<CreateProductBuyRequest> List ){
        PurchaseOrder order = new PurchaseOrder();
        // Check if the input list is null
        if (List == null) {
            throw new IllegalArgumentException("createProductBuyRequests cannot be null");
        }
        List<CreateProductBuyRequest> createProductBuyRequests = List;

        Long id = -1L;
//        order.setStatus(null);
        order.setPurchaseDate(new Date());
//        order.setPaymentType(null);
//        order.setTotalAmount(null);
        List<Long> ProductBuyIDList = new ArrayList<>();
        for(CreateProductBuyRequest productBuyRequest: createProductBuyRequests){
            ProductBuy productBuy = new ProductBuy();
            productBuy = productBuyService.createProductBuy(productBuyRequest);
            ProductBuyIDList.add(productBuy.getPK_ProductBuyID());
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
        calculateAndSetGuaranteeEndDate((long) orderID);
        sendConfirmationEmail((long) orderID, orderToUpdate.getEmail());
        System.out.println(orderToUpdate.toString());
        orderService.saveOrder(orderToUpdate);


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
                System.out.println(orderToUpdate.toString());
                orderToUpdate.setStatus(3);
                calculateAndSetGuaranteeEndDate((long) request.getOrderID());
                System.out.println(orderToUpdate.toString());
                sendConfirmationEmail((long) request.getOrderID(), orderToUpdate.getEmail());
                return orderService.saveOrder(orderToUpdate) != null;
            } else{
                return false;
            }
        }

    }


    //Thai Dang fix may thang order detail bo len day, t lamf wrapper tam thoi thoi
    //Calculate SubTotal, Discount product and Total////////////////////////////////////////////////////////////////////////////////////////////////
    public Float calculateSubTotal(OrderDetailRequest orderDetailRequest) {
        float totalAmount = 0;
        Optional<ProductSell> productSellOptional = productSellRepository.findById(orderDetailRequest.getProductSell_ID());
        if (productSellOptional.isPresent()) {
            ProductSell productSell = productSellOptional.get();
            float productCost = productSell.getCost();
            int quantity = orderDetailRequest.getQuantity();
            totalAmount = productCost * quantity;
        }
        return totalAmount;
    }

    public Float calculateDiscountProduct(OrderPromotionRequest orderPromotionRequest) {
        Promotion promotion = promotionRepository.findById(orderPromotionRequest.getPromotionID()).orElseThrow(() -> new IllegalArgumentException("Promotion ID not found"));
        Integer discount = promotion.getDiscount();
        Float percentage = discount / 100.0F;
        Float totalAmount = 0.0F;
        Optional<ProductSell> productSellOptional = productSellRepository.findById(orderPromotionRequest.getProductSell_ID());
        if (productSellOptional.isPresent()) {
            ProductSell productSell = productSellOptional.get();
            float productCost = productSell.getCost();
            int quantity = orderPromotionRequest.getQuantity();
            totalAmount = productCost * quantity;
        }
        return totalAmount * percentage;
    }

    public Float TotalOrderDetails(OrderTotalRequest orderTotalRequest) {
        Float subtotal = orderTotalRequest.getSubTotal();
        Float discountProuduct = orderTotalRequest.getDiscountProduct();
        Float total = subtotal - discountProuduct;
        return total;
    }

    public TotalOrderResponse totalOrder(List<TotalOrderRequest> totalOrderRequests) {
        Float subTotalResponse = 0.0F;
        Float discount_priceResponse = 0.0F;
        Float totalResponse = 0.0F;
        for (TotalOrderRequest request : totalOrderRequests) {
            // Fetch product details
            Optional<ProductSell> productSellOpt = productSellRepository.findById(request.getProductSell_ID());
            if (productSellOpt.isPresent()) {
                ProductSell productSell = productSellOpt.get();
                Float cost = productSell.getCost();
                Float subtotal = cost * request.getQuantity();
                subTotalResponse += subtotal;
                // Fetch promotion details if provided
                Float discountAmount = 0.0F;
                if (request.getPromotion_ID() != null) {
                    Optional<Promotion> promotionOptional = promotionRepository.findById(request.getPromotion_ID());
                    if (promotionOptional.isPresent()) {
                        Integer discount = promotionOptional.get().getDiscount();
                        Float percentage = discount / 100.0F;
                        discountAmount = subtotal * percentage;
                        discount_priceResponse +=discountAmount;
                    }
                }

                // Calculate the total after discount
                Float totalDetails = subtotal - discountAmount;
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
    //Calculate and Set Guarantee End Date////////////////////////////////////////////////////////////////////////////////////////
    public List<OrderDetailResponse> calculateAndSetGuaranteeEndDate(Long orderId) {
        List<OrderDetail> orderDetails = orderDetailRepository.findAll();

        List<OrderDetailResponse> responses = orderDetails.stream()
                .filter(orderDetail -> orderDetail.getPurchaseOrder().getPK_OrderID().equals(orderId))
                .map(this::processOrderDetail)
                .toList();

        return responses;
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

}

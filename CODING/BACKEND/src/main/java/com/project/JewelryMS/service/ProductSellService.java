package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Category;
import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.entity.Promotion;
import com.project.JewelryMS.model.ProductSell.*;
import com.project.JewelryMS.repository.CategoryRepository;
import com.project.JewelryMS.repository.ProductSellRepository;
import com.project.JewelryMS.repository.PromotionRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductSellService {
    private static final Logger logger = LoggerFactory.getLogger(ProductSellService.class);

    private Float pricingRatio = 1.20F;

    @Autowired
    ProductSellRepository productSellRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PromotionRepository promotionRepository;

    @Autowired
    ApiService apiService;

    @Autowired
    ImageService imageService;

    public ProductSell removePromotionsFromProductSell(RemovePromotionRequest request) {
        Optional<ProductSell> productSellOpt = productSellRepository.findById(request.getProductSellId());
        if (!productSellOpt.isPresent()) {
            throw new IllegalArgumentException("ProductSell ID not found");
        }

        ProductSell productSell = productSellOpt.get();
        List<Promotion> existingPromotions = productSell.getPromotion();
        for (Long promotionId : request.getPromotionIds()) {
            existingPromotions.removeIf(promotion -> promotion.getPK_promotionID() == promotionId);
        }

        productSell.setPromotion(existingPromotions);
        return productSellRepository.save(productSell);
    }

    public ProductSell addPromotionsToProductSell(AddPromotionsRequest request) {
        Optional<ProductSell> productSellOpt = productSellRepository.findById(request.getProductSellId());
        if (!productSellOpt.isPresent()) {
            throw new IllegalArgumentException("ProductSell ID not found");
        }

        ProductSell productSell = productSellOpt.get();
        List<Promotion> existingPromotions = productSell.getPromotion();
        for (Long promotionId : request.getPromotionIds()) {
            Optional<Promotion> promotionOpt = promotionRepository.findById(promotionId);
            if (promotionOpt.isPresent() && !existingPromotions.contains(promotionOpt.get())) {
                existingPromotions.add(promotionOpt.get());
            }
        }

        productSell.setPromotion(existingPromotions);
        return productSellRepository.save(productSell);
    }

    public List<ProductSellResponse> getAllProductSellResponses() {
        List<ProductSellResponse> responses = new ArrayList<>();

        // Fetch all products with category and promotion details in one go
        List<ProductSell> productSells = productSellRepository.findAllWithCategoryAndPromotion();

        // Map product ID to a list of promotion IDs
        Map<Long, List<Long>> productPromotionMap = new HashMap<>();
        List<Object[]> promotionData = productSellRepository.findAllPromotionIds();
        for (Object[] entry : promotionData) {
            Long productId = (Long) entry[0];
            Long promotionId = (Long) entry[1];
            productPromotionMap.computeIfAbsent(productId, k -> new ArrayList<>()).add(promotionId);
        }

        for (ProductSell productSell : productSells) {
            ProductSellResponse response = new ProductSellResponse();
            response.setProductID(productSell.getProductID());
            response.setCarat(productSell.getCarat());
            response.setChi(productSell.getChi());
            response.setCost(productSell.getCost());
            response.setPDescription(productSell.getPDescription());
            response.setGemstoneType(productSell.getGemstoneType());
            response.setImage(productSell.getImage());
            response.setManufacturer(productSell.getManufacturer());
            response.setManufactureCost(productSell.getManufactureCost());
            response.setMetalType(productSell.getMetalType());
            response.setPName(productSell.getPName());
            response.setProductCode(productSell.getProductCode());
            response.setStatus(productSell.isPStatus());

            if (productSell.getCategory() != null) {
                response.setCategory_id(productSell.getCategory().getId());
                response.setCategory_name(productSell.getCategory().getName());
            }

            List<Long> promotionIds = productPromotionMap.getOrDefault(productSell.getProductID(), new ArrayList<>());
            List<String> promotionIdStrings = promotionIds.stream().map(String::valueOf).collect(Collectors.toList());
            response.setPromotion_id(promotionIdStrings);
            responses.add(response);

        }
        return responses;
    }

    private ProductSellResponse mapProductSellToResponse(ProductSell productSell) {
        ProductSellResponse response = new ProductSellResponse();

        response.setProductID(productSell.getProductID());
        response.setCarat(productSell.getCarat());
        response.setChi(productSell.getChi());
        response.setCost(productSell.getCost());
        response.setPDescription(productSell.getPDescription());
        response.setGemstoneType(productSell.getGemstoneType());
        response.setImage(productSell.getImage());
        response.setManufacturer(productSell.getManufacturer());
        response.setManufactureCost(productSell.getManufactureCost());
        response.setMetalType(productSell.getMetalType());
        response.setPName(productSell.getPName());
        response.setProductCode(productSell.getProductCode());
        response.setStatus(productSell.isPStatus());

        Category category = productSell.getCategory();
        if (category != null) {
            response.setCategory_id(category.getId());
            response.setCategory_name(category.getName());
        }

        List<String> promotionIds = productSellRepository.findPromotionIdsByProductSellId(productSell.getProductID())
                .stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
        response.setPromotion_id(promotionIds);

        return response;
    }



    public ProductSellResponse createProductSell(CreateProductSellRequest request) {
        ProductSell productSell = new ProductSell();
        productSell.setCarat(request.getCarat());
        // Set Category
        Optional<Category> categoryOpt = categoryRepository.findById(request.getCategory_id());
        if (categoryOpt.isPresent()) {
            productSell.setCategory(categoryOpt.get());
        } else {
            throw new IllegalArgumentException("Category ID not found");
        }
        productSell.setChi(request.getChi());
        productSell.setCost(calculateProductSellCost(request.getChi(),request.getCarat(),request.getGemstoneType(),request.getMetalType(),request.getManufactureCost()));
        productSell.setPDescription(request.getPdescription());
        productSell.setPName(request.getPname());
        productSell.setGemstoneType(request.getGemstoneType());
        // Gọi phương thức uploadImageByPath và send MultipartFile file image
        String imageUrl = imageService.uploadImageByPathService(request.getImage());
        productSell.setImage(imageUrl);
        productSell.setManufacturer(request.getManufacturer());
        productSell.setManufactureCost(request.getManufactureCost());
        productSell.setMetalType(request.getMetalType());
        productSell.setProductCode(request.getProductCode());
        productSell.setPStatus(true);
        // Save ProductSell
        ProductSell productSell1 = productSellRepository.save(productSell);
        return getProductSellById2(productSell1.getProductID());
    }

    public Float calculateProductSellCost(Integer chi, Float carat, String gemstoneType, String metalType, Float manufacturerCost){
        Float totalGemPrice = 0.0F;
        Float totalPrice = 0.0F;

        if (gemstoneType != null && carat != null) {
            Float gemStonePrice = 100000000.0F; // Price per carat
            totalGemPrice = (gemStonePrice * carat);
        }

        Float totalGoldPrice = 0.0F;
        if (metalType != null && chi != null) {
            Float goldPrice = Float.parseFloat(apiService.getGoldBuyPricecalculate("http://api.btmc.vn/api/BTMCAPI/getpricebtmc?key=3kd8ub1llcg9t45hnoh8hmn7t5kc2v"));
            totalGoldPrice = (goldPrice / 10) * chi;
        }

        totalPrice = (totalGemPrice + totalGoldPrice + manufacturerCost) * 1.2F;

        return totalPrice / 1000.0F;
    }

    public Float GetPricingRatio() {
        return pricingRatio;
    }

    public Float updatePricingRatio(Float newRatio) {
        this.pricingRatio = newRatio;
        return pricingRatio;
    }

    // Read all ProductSell entries
    public List<ProductSell> getAllProductSells() {
        return productSellRepository.findAll();
    }

    public ProductSell getProductSellById(long id) {
        Optional<ProductSell> productSellOptional = productSellRepository.findById(id);
        if(productSellOptional.isPresent()){
            ProductSell productSell = productSellOptional.get();
            return productSell;
        }else{
            return new ProductSell();
        }
    }

    public ProductSellResponse getProductSellById2(long id) {
        ProductSell productSell = productSellRepository.findByIdWithCategoryAndPromotion(id)
                .orElseThrow(() -> new IllegalArgumentException("ProductSell ID not found"));

        return mapToProductSellResponse(productSell);
    }

    private ProductSellResponse mapToProductSellResponse(ProductSell productSell) {
        ProductSellResponse response = new ProductSellResponse();
        response.setProductID(productSell.getProductID());
        response.setCarat(productSell.getCarat());
        response.setChi(productSell.getChi());
        response.setCost(productSell.getCost());
        response.setPDescription(productSell.getPDescription());
        response.setGemstoneType(productSell.getGemstoneType());
        response.setImage(productSell.getImage());
        response.setManufacturer(productSell.getManufacturer());
        response.setManufactureCost(productSell.getManufactureCost());
        response.setMetalType(productSell.getMetalType());
        response.setPName(productSell.getPName());
        response.setProductCode(productSell.getProductCode());
        response.setStatus(productSell.isPStatus());

        if (productSell.getCategory() != null) {
            response.setCategory_id(productSell.getCategory().getId());
            response.setCategory_name(productSell.getCategory().getName());
        }

        List<Long> listPromotion = productSellRepository.findPromotionIdsByProductSellId(productSell.getProductID());
        List<String> promotionIds = listPromotion.stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
        response.setPromotion_id(promotionIds);

        return response;
    }

    public ProductSellResponse updateProductSell(Long id, ProductSellRequest productSellRequest) {
        // Fetch the existing ProductSell entity
        ProductSell existingProductSell = productSellRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ProductSell ID not found"));

        // Update fields from the request
        existingProductSell.setCarat(productSellRequest.getCarat());
        existingProductSell.setChi(productSellRequest.getChi());
        existingProductSell.setCost(productSellRequest.getCost());
        existingProductSell.setPDescription(productSellRequest.getPdescription());
        existingProductSell.setGemstoneType(productSellRequest.getGemstoneType());
        String imageUrl = imageService.uploadImageByPathService(productSellRequest.getImage());
        existingProductSell.setImage(imageUrl);
        existingProductSell.setManufacturer(productSellRequest.getManufacturer());
        existingProductSell.setManufactureCost(productSellRequest.getManufactureCost());
        existingProductSell.setMetalType(productSellRequest.getMetalType());
        existingProductSell.setPName(productSellRequest.getPname());
        existingProductSell.setProductCode(productSellRequest.getProductCode());
        // Update category
        Category category = categoryRepository.findById(productSellRequest.getCategory_id())
                .orElseThrow(() -> new IllegalArgumentException("Category ID not found"));
        existingProductSell.setCategory(category);


        // Save the updated entity
        productSellRepository.save(existingProductSell);

        // Return the updated ProductSellResponse
        return mapToProductSellResponse(existingProductSell);
    }

    public void deleteProduct(long id){
        Optional<ProductSell> productSellOptional = productSellRepository.findById(id);
        if(productSellOptional.isPresent()){
            ProductSell productSell = productSellOptional.get();
            productSell.setPStatus(false);
            productSellRepository.save(productSell);
        }else {
            throw new RuntimeException("Product Sell with ID " + id + " not found");
        }
    }




}

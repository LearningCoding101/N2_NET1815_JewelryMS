package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Category;
import com.project.JewelryMS.entity.ProductBuy;
import com.project.JewelryMS.model.Order.CreateProductBuyRequest;
import com.project.JewelryMS.model.ProductBuy.CalculatePBRequest;
import com.project.JewelryMS.model.ProductBuy.CreateProductBuyResponse;
import com.project.JewelryMS.model.ProductBuy.ProductBuyResponse;
import com.project.JewelryMS.repository.CategoryRepository;
import com.project.JewelryMS.repository.ProductBuyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductBuyService {
    @Autowired
    private ProductBuyRepository productBuyRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ApiService apiService;
    @Autowired
    private ImageService imageService;
    public ProductBuy createProductBuy(CreateProductBuyRequest request) {
        ProductBuy productBuy = new ProductBuy();
        productBuy.setPbName(request.getName());

        // Find the category by name
        Optional<Category> categoryOpt = categoryRepository.findCategoryById(request.getCategory_id());
        if (categoryOpt.isPresent()) {
            productBuy.setCategory(categoryOpt.get());
        } else {
            throw new IllegalArgumentException("Category name not found");
        }

        productBuy.setMetalType(request.getMetalType());
        productBuy.setGemstoneType(request.getGemstoneType());
        if(request.getImage()!=null) {
            String imageUrl = imageService.uploadImageByPathService(request.getImage());
            productBuy.setImage(imageUrl);
        }else{
            productBuy.setImage(null);
        }
        productBuy.setChi(request.getMetalWeight());
        productBuy.setCarat(request.getGemstoneWeight());
        productBuy.setPbCost(request.getCost());
        productBuy.setPbStatus(true);
        return productBuyRepository.save(productBuy);
    }


    public Float calculateProductBuyCost(CalculatePBRequest createProductBuyRequest) {
        Float totalGemPrice = 0.0F;
        Float totalPrice = 0.0F;
        String gemstoneType = createProductBuyRequest.getGemstoneType();
        String metalType = createProductBuyRequest.getMetalType();
        Float carat = createProductBuyRequest.getGemstoneWeight();
        Integer chi = createProductBuyRequest.getMetalWeight();
        if (gemstoneType != null && carat != null) {
            Float gemStonePrice = 100000000.0F; // Price per carat
            totalGemPrice = ((gemStonePrice * carat) * 0.8F);
        }

        Float totalGoldPrice = 0.0F;
        if (metalType != null && chi != null) {
            Float goldPrice = Float.parseFloat(apiService.getGoldBuyPricecalculate("http://api.btmc.vn/api/BTMCAPI/getpricebtmc?key=3kd8ub1llcg9t45hnoh8hmn7t5kc2v"));
            totalGoldPrice = (goldPrice / 10) * chi;
        }

        totalPrice = (totalGemPrice + totalGoldPrice ); // Applying the markup

        return totalPrice / 1000.0F;
    }

    public CreateProductBuyResponse mapToCreateProductBuyResponse(ProductBuy productBuy) {
        CreateProductBuyResponse response = new CreateProductBuyResponse();
        response.setProductBuyID(productBuy.getPK_ProductBuyID());
        response.setCategoryName(productBuy.getCategory().getName());
        response.setPbName(productBuy.getPbName());
        response.setMetalType(productBuy.getMetalType());
        response.setGemstoneType(productBuy.getGemstoneType());
        response.setCost(productBuy.getPbCost());
        response.setImage(productBuy.getImage());
        return response;
    }

    public List<ProductBuyResponse> getAllProductBuys() {
        List<ProductBuy> productBuys = productBuyRepository.findAll();
        return productBuys.stream().map(this::mapToProductBuyResponse).collect(Collectors.toList());
    }

    public ProductBuyResponse getProductBuyById(Long id) {
        Optional<ProductBuy> productBuyOpt = productBuyRepository.findById(id);
        if (productBuyOpt.isPresent()) {
            return mapToProductBuyResponse(productBuyOpt.get());
        } else {
            throw new IllegalArgumentException("ProductBuy ID not found");
        }
    }

    private ProductBuyResponse mapToProductBuyResponse(ProductBuy productBuy) {
        ProductBuyResponse response = new ProductBuyResponse();
        response.setProductBuyID(productBuy.getPK_ProductBuyID());
        response.setCategoryName(productBuy.getCategory().getName());
        response.setPbName(productBuy.getPbName());
        response.setMetalType(productBuy.getMetalType());
        response.setGemstoneType(productBuy.getGemstoneType());
        response.setCost(productBuy.getPbCost());
        return response;
    }


    public ProductBuy getProductBuyById2(long id) {
        Optional<ProductBuy> productBuyOptional = productBuyRepository.findById(id);
        if(productBuyOptional.isPresent()){
            ProductBuy productBuy = productBuyOptional.get();
            return productBuy;
        }
        return productBuyOptional.get();
    }


    public String deleteProductBuy(Long id){
        Optional<ProductBuy> productBuyOptional = productBuyRepository.findById(id);
        if(productBuyOptional.isPresent()){
            ProductBuy productBuy = productBuyOptional.get();
            productBuy.setPbStatus(false);
            productBuyRepository.save(productBuy);
            return "Product Buy" + id + "delete sucessfully!";
        }
        return "Product Buy ID Not Found!!!";
    }

}

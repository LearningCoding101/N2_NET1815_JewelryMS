package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Category;
import com.project.JewelryMS.entity.PricingRatio;
import com.project.JewelryMS.entity.ProductBuy;
import com.project.JewelryMS.model.Order.CreateProductBuyRequest;
import com.project.JewelryMS.model.ProductBuy.CalculatePBRequest;
import com.project.JewelryMS.model.ProductBuy.CreateProductBuyResponse;
import com.project.JewelryMS.model.ProductBuy.ProductBuyResponse;
import com.project.JewelryMS.repository.CategoryRepository;
import com.project.JewelryMS.repository.PricingRatioRepository;
import com.project.JewelryMS.repository.ProductBuyRepository;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;



import java.io.*;
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
    @Autowired
    private PricingRatioRepository pricingRatioRepository;

    public void initializeGoldPrice() {
        String goldPriceStr = apiService.getGoldBuyPricecalculate("http://api.btmc.vn/api/BTMCAPI/getpricebtmc?key=3kd8ub1llcg9t45hnoh8hmn7t5kc2v");
        if (goldPriceStr != null) {
            this.goldPrice = Float.parseFloat(goldPriceStr);
        } else {
            throw new IllegalArgumentException("Gold price cannot be null");
        }
    }

    public Long createProductBuy(CreateProductBuyRequest request) {
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
            String imageUrl = imageService.uploadImageByPathService(
                    base64ToMultipartFile(request.getImage())
            );
            productBuy.setImage(imageUrl);
        }else{
            productBuy.setImage(null);
        }
        productBuy.setChi(request.getMetalWeight());
        productBuy.setCarat(request.getGemstoneWeight());
        productBuy.setPbCost(request.getCost());
        productBuy.setPbStatus(true);
        ProductBuy productBuy1 = productBuyRepository.save(productBuy);
        Long ProductBuy_ID =  productBuy1.getPK_ProductBuyID();
        return ProductBuy_ID;
    }
    public static MultipartFile base64ToMultipartFile(String base64String) {
        String base64Data = base64String.split(",")[1];

        // Decode base64 string to byte array
        byte[] fileBytes = Base64.decodeBase64(base64Data);
        return new MultipartFile() {

            private final String fileName = "filename.jpeg"; // Provide a suitable name if needed

            @Override
            public String getName() {
                return fileName;
            }

            @Override
            public String getOriginalFilename() {
                return fileName;
            }

            @Override
            public String getContentType() {
                // You may need to determine the content type based on the file content
                return "image/jpeg";
            }

            @Override
            public boolean isEmpty() {
                return fileBytes == null || fileBytes.length == 0;
            }

            @Override
            public long getSize() {
                return fileBytes.length;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return fileBytes;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(fileBytes);
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {
                // You can implement this if needed for file transfer
            }
        };
    }

    private static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.'));
    }

    public Float calculateProductBuyCost(CalculatePBRequest createProductBuyRequest) {
        Float totalGemPrice = 0.0F;
        Float totalPrice = 0.0F;
        String gemstoneType = createProductBuyRequest.getGemstoneType();
        String metalType = createProductBuyRequest.getMetalType();
        Float carat = createProductBuyRequest.getGemstoneWeight();
        Float chi = createProductBuyRequest.getMetalWeight();
        if (gemstoneType != null && carat != null) {
            Float gemStonePrice = 10000000.0F; // Price per carat
            totalGemPrice = ((gemStonePrice * carat) * 0.8F);
        }

        Float totalGoldPrice = 0.0F;
        if (metalType != null && chi != null) {
            Float goldPrice = Float.parseFloat(apiService.getGoldBuyPricecalculate("http://api.btmc.vn/api/BTMCAPI/getpricebtmc?key=3kd8ub1llcg9t45hnoh8hmn7t5kc2v"));
            totalGoldPrice = goldPrice * chi;
        }

        totalPrice = (totalGemPrice + totalGoldPrice ); // Applying the markup

        return totalPrice ;
    }
    private Float goldPrice;

    public Float getPricingRatioPB(){
        Optional<PricingRatio> pricingRatioOptional = pricingRatioRepository.findById(1L);
        if(pricingRatioOptional.isPresent()){
            PricingRatio pricingRatio1 = pricingRatioOptional.get();
            return pricingRatio1.getPricingRatioPB();
        }
        return 0.0F;
    }

    public Float updatePricingRatioPB(Float newRatio) {
        Optional<PricingRatio> pricingRatioOptional = pricingRatioRepository.findById(1L);
        if(pricingRatioOptional.isPresent()) {
            PricingRatio pricingRatio = pricingRatioOptional.get();
            pricingRatio.setPricingRatioPB(newRatio);
            pricingRatioRepository.save(pricingRatio);
        }
        return newRatio;
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

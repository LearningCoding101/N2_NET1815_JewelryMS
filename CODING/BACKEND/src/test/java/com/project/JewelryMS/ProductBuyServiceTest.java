package com.project.JewelryMS;

import com.project.JewelryMS.entity.Category;
import com.project.JewelryMS.entity.ProductBuy;
import com.project.JewelryMS.model.Order.CreateProductBuyRequest;
import com.project.JewelryMS.model.ProductBuy.CalculatePBRequest;
import com.project.JewelryMS.model.ProductBuy.CreateProductBuyResponse;
import com.project.JewelryMS.model.ProductBuy.ProductBuyResponse;
import com.project.JewelryMS.repository.CategoryRepository;
import com.project.JewelryMS.repository.ProductBuyRepository;
import com.project.JewelryMS.service.ApiService;
import com.project.JewelryMS.service.ImageService;
import com.project.JewelryMS.service.ProductBuyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductBuyServiceTest {

    @InjectMocks
    private ProductBuyService productBuyService;

    @Mock
    private ProductBuyRepository productBuyRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ApiService apiService;

    @Mock
    private ImageService imageService;

    private ProductBuy productBuy;
    private Category category;
    private MultipartFile imageFile;

    @BeforeEach
    public void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Gold");

        productBuy = new ProductBuy();
        productBuy.setPK_ProductBuyID(1L);
        productBuy.setCategory(category);
        productBuy.setPbName("Gold Ring");
        productBuy.setMetalType("Gold");
        productBuy.setGemstoneType("Diamond");
        productBuy.setImage("image_url");
        productBuy.setChi(3);
        productBuy.setCarat(1F);
        productBuy.setPbCost(10000000.0F);
        productBuy.setPbStatus(true);

        imageFile = mock(MultipartFile.class);
        when(imageFile.getOriginalFilename()).thenReturn("image.jpg");
    }

    @Test
    public void testCreateProductBuy() {
        CreateProductBuyRequest request = new CreateProductBuyRequest();
        request.setName("Gold Ring");
        request.setCategory_id(1L);
        request.setMetalType("Gold");
        request.setGemstoneType("Diamond");
        request.setImage(imageFile); // Set MultipartFile
        request.setMetalWeight(3);
        request.setGemstoneWeight(1F);
        request.setCost(10000000.0F);

        when(categoryRepository.findCategoryById(anyLong())).thenReturn(Optional.of(category));
        when(imageService.uploadImageByPathService(any(MultipartFile.class))).thenReturn("image_url");
        when(productBuyRepository.save(any(ProductBuy.class))).thenReturn(productBuy);

        ProductBuy createdProductBuy = productBuyService.createProductBuy(request);

        assertNotNull(createdProductBuy);
        assertEquals("Gold Ring", createdProductBuy.getPbName());
        assertEquals("image_url", createdProductBuy.getImage());
        verify(categoryRepository, times(1)).findCategoryById(anyLong());
        verify(imageService, times(1)).uploadImageByPathService(any(MultipartFile.class));
        verify(productBuyRepository, times(1)).save(any(ProductBuy.class));
    }

    @Test
    public void testCalculateProductBuyCost() {
        CalculatePBRequest request = new CalculatePBRequest();
        request.setGemstoneType("Diamond");
        request.setMetalType("Gold");
        request.setGemstoneWeight(1.0F);
        request.setMetalWeight(3);

        when(apiService.getGoldBuyPricecalculate(anyString())).thenReturn("1000000");

        Float totalCost = productBuyService.calculateProductBuyCost(request);

        assertNotNull(totalCost);
        assertEquals(80300.0F, totalCost); // Adjust the expected value based on the logic
        verify(apiService, times(1)).getGoldBuyPricecalculate(anyString());
    }

    @Test
    public void testMapToCreateProductBuyResponse() {
        CreateProductBuyResponse response = productBuyService.mapToCreateProductBuyResponse(productBuy);

        assertNotNull(response);
        assertEquals(1L, response.getProductBuyID());
        assertEquals("Gold Ring", response.getPbName());
        assertEquals("Gold", response.getMetalType());
        assertEquals("Diamond", response.getGemstoneType());
    }

    @Test
    public void testGetAllProductBuys() {
        when(productBuyRepository.findAll()).thenReturn(Collections.singletonList(productBuy));

        List<ProductBuyResponse> responses = productBuyService.getAllProductBuys();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        verify(productBuyRepository, times(1)).findAll();
    }

    @Test
    public void testGetProductBuyById() {
        when(productBuyRepository.findById(anyLong())).thenReturn(Optional.of(productBuy));

        ProductBuyResponse response = productBuyService.getProductBuyById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getProductBuyID());
        verify(productBuyRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testGetProductBuyById2() {
        when(productBuyRepository.findById(anyLong())).thenReturn(Optional.of(productBuy));

        ProductBuy foundProductBuy = productBuyService.getProductBuyById2(1L);

        assertNotNull(foundProductBuy);
        assertEquals(1L, foundProductBuy.getPK_ProductBuyID());
        verify(productBuyRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testDeleteProductBuy() {
        when(productBuyRepository.findById(anyLong())).thenReturn(Optional.of(productBuy));

        String response = productBuyService.deleteProductBuy(1L);

        assertNotNull(response);
        assertEquals("Product Buy1delete sucessfully!", response);
        verify(productBuyRepository, times(1)).findById(anyLong());
        verify(productBuyRepository, times(1)).save(any(ProductBuy.class));
    }
}

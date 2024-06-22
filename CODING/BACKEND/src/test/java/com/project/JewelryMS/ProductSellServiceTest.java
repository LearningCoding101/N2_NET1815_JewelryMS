package com.project.JewelryMS;

import com.project.JewelryMS.entity.Category;
import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.entity.Promotion;
import com.project.JewelryMS.model.ProductSell.*;
import com.project.JewelryMS.repository.CategoryRepository;
import com.project.JewelryMS.repository.ProductSellRepository;
import com.project.JewelryMS.repository.PromotionRepository;
import com.project.JewelryMS.service.ApiService;
import com.project.JewelryMS.service.ImageService;
import com.project.JewelryMS.service.ProductSellService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductSellServiceTest {

    @InjectMocks
    private ProductSellService productSellService;

    @Mock
    private ProductSellRepository productSellRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private ApiService apiService;

    @Mock
    private ImageService imageService;

    private ProductSell productSell;
    private Category category;
    private Promotion promotion;
    private MultipartFile imageFile;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Rings");

        promotion = new Promotion();
        promotion.setPK_promotionID(1L);
        promotion.setDescription("Summer Sale");

        productSell = new ProductSell();
        productSell.setProductID(1L);
        productSell.setPName("Diamond Ring");
        productSell.setCategory(category);
        productSell.setPromotion(Arrays.asList(promotion));

        imageFile = mock(MultipartFile.class); // Mocking MultipartFile
        when(imageFile.getOriginalFilename()).thenReturn("image.jpg");
    }

    @Test
    void testRemovePromotionsFromProductSell() {
        RemovePromotionRequest request = new RemovePromotionRequest();
        request.setProductSellId(1L);
        request.setPromotionIds(Arrays.asList(1L));

        when(productSellRepository.findById(1L)).thenReturn(Optional.of(productSell));

        ProductSell updatedProductSell = productSellService.removePromotionsFromProductSell(request);

        assertTrue(updatedProductSell.getPromotion().isEmpty());
        verify(productSellRepository, times(1)).save(productSell);
    }

    @Test
    void testAddPromotionsToProductSell() {
        AddPromotionsRequest request = new AddPromotionsRequest();
        request.setProductSellId(1L);
        request.setPromotionIds(Arrays.asList(2L));

        Promotion newPromotion = new Promotion();
        newPromotion.setPK_promotionID(2L);
        newPromotion.setDescription("Winter Sale");

        when(productSellRepository.findById(1L)).thenReturn(Optional.of(productSell));
        when(promotionRepository.findById(2L)).thenReturn(Optional.of(newPromotion));

        ProductSell updatedProductSell = productSellService.addPromotionsToProductSell(request);

        assertEquals(2, updatedProductSell.getPromotion().size());
        verify(productSellRepository, times(1)).save(productSell);
    }

    @Test
    void testCreateProductSell() {
        CreateProductSellRequest request = new CreateProductSellRequest();
        request.setCarat(1.5F);
        request.setCategory_id(1L);
        request.setChi(5);
        request.setGemstoneType("Diamond");
        request.setMetalType("Gold");
        request.setManufactureCost(1000F);
        request.setPdescription("Beautiful Diamond Ring");
        request.setPname("Diamond Ring");
        request.setImage(imageFile); // Set the MultipartFile
        request.setManufacturer("Best Manufacturer");
        request.setProductCode("DR001");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(imageService.uploadImageByPathService(imageFile)).thenReturn("image_url");
        when(productSellRepository.save(any(ProductSell.class))).thenReturn(productSell);

        ProductSellResponse response = productSellService.createProductSell(request);

        assertNotNull(response);
        assertEquals("Diamond Ring", response.getPName());
        verify(productSellRepository, times(1)).save(any(ProductSell.class));
    }
}
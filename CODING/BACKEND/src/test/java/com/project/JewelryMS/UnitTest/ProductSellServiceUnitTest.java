package com.project.JewelryMS.UnitTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import com.project.JewelryMS.entity.*;
import com.project.JewelryMS.model.ProductSell.*;
import com.project.JewelryMS.repository.*;
import com.project.JewelryMS.service.ApiService;
import com.project.JewelryMS.service.ImageService;
import com.project.JewelryMS.service.ProductSellService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ProductSellServiceUnitTest {

    @Mock
    private PricingRatioRepository pricingRatioRepository;

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

    @Mock
    private ProductSellPromotionRepository productSellPromotionRepository;

    @InjectMocks
    private ProductSellService productSellService;

    private ProductSell productSell;
    private Category category;
    private Promotion promotion;
    private ProductSell_Promotion productSellPromotion;

    @BeforeEach
    public void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Jewelry");

        promotion = new Promotion();
        promotion.setPK_promotionID(1L);
        promotion.setCode("PROMO1");

        productSell = new ProductSell();
        productSell.setProductID(1L);
        productSell.setPName("Gold Ring");
        productSell.setCategory(category);
        productSell.setProductSellPromotions(new ArrayList<>());
        productSell.setOrderDetails(new HashSet<>());
        productSell.setGuarantee(new Guarantee());

        productSellPromotion = new ProductSell_Promotion();
        productSellPromotion.setPK_PPID(1L);
        productSellPromotion.setProductSell(productSell);
        productSellPromotion.setPromotion(promotion);
    }

//    @Test
//    public void testAddPromotionsToProductSell() {
//        AddPromotionsRequest request = new AddPromotionsRequest();
//        request.setProductSellId(1L);
//        request.setPromotionIds(Arrays.asList(1L));
//
//        when(productSellRepository.findById(1L)).thenReturn(Optional.of(productSell));
//        when(promotionRepository.findById(1L)).thenReturn(Optional.of(promotion));
//        when(productSellPromotionRepository.save(any(ProductSell_Promotion.class))).thenReturn(productSellPromotion);
//
//        ProductSell result = productSellService.addPromotionsToProductSell(request);
//
//        assertNotNull(result);
//        assertEquals(1, result.getProductSellPromotions().size());
//    }
//
//    @Test
//    public void testRemovePromotionsFromProductSell() {
//        RemovePromotionRequest request = new RemovePromotionRequest();
//        request.setProductSellId(1L);
//        request.setPromotionIds(Arrays.asList(1L));
//
//        productSell.getProductSellPromotions().add(productSellPromotion);
//
//        when(productSellRepository.findById(1L)).thenReturn(Optional.of(productSell));
//        when(productSellPromotionRepository.findByProductSell(productSell))
//                .thenReturn(Arrays.asList(productSellPromotion));
//
//        ProductSell result = productSellService.removePromotionsFromProductSell(request);
//
//        assertNotNull(result);
//        assertEquals(0, result.getProductSellPromotions().size());
//    }

//    @Test
//    public void testCreateProductSell() {
//        CreateProductSellRequest request = new CreateProductSellRequest();
//        request.setPname("Gold Necklace");
//        request.setCategory_id(1L);
//
//        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
//        when(apiService.getGoldBuyPricecalculate(anyString())).thenReturn("50000000");
//        when(pricingRatioRepository.findById(1L)).thenReturn(Optional.of(new PricingRatio(1L, 1.5F)));
//
//        ProductSellResponse response = productSellService.createProductSell(request);
//
//        assertNotNull(response);
//        assertEquals("Gold Necklace", response.getPName());
//    }

//    @Test
//    public void testUpdateProductSell() {
//        ProductSellRequest request = new ProductSellRequest();
//        request.setPname("Updated Gold Ring");
//
//        when(productSellRepository.findById(1L)).thenReturn(Optional.of(productSell));
//
//        ProductSellResponse response = productSellService.updateProductSell(1L, request);
//
//        assertNotNull(response);
//        assertEquals("Updated Gold Ring", response.getPName());
//    }

    @Test
    public void testDeleteProductSell() {
        when(productSellRepository.findById(1L)).thenReturn(Optional.of(productSell));

        productSellService.deleteProduct(1L);

        assertFalse(productSell.isPStatus());
        verify(productSellRepository, times(1)).save(productSell);
    }

    @Test
    public void testGetAllProductSellResponses() {
        when(productSellRepository.findAllWithCategoryAndPromotion()).thenReturn(Arrays.asList(productSell));

        List<ProductSellResponse> responses = productSellService.getAllProductSellResponses();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Gold Ring", responses.get(0).getPName());
    }


}

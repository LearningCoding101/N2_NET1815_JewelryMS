package com.project.JewelryMS.UnitTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.project.JewelryMS.entity.Guarantee;
import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.model.Guarantee.CreateGuaranteeRequest;
import com.project.JewelryMS.model.Guarantee.GuaranteeRequest;
import com.project.JewelryMS.model.Guarantee.GuaranteeResponse;
import com.project.JewelryMS.repository.GuaranteeRepository;
import com.project.JewelryMS.repository.ProductSellRepository;
import com.project.JewelryMS.service.GuaranteeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class GuaranteeServiceUnitTest {

    @Mock
    private GuaranteeRepository guaranteeRepository;

    @Mock
    private ProductSellRepository productSellRepository;

    @InjectMocks
    private GuaranteeService guaranteeService;

    private Guarantee guarantee;
    private ProductSell productSell;

    @BeforeEach
    public void setUp() {
        productSell = new ProductSell();
        productSell.setProductID(1L);

        guarantee = new Guarantee();
        guarantee.setPK_guaranteeID(1L);
        guarantee.setProductSell(productSell);
        guarantee.setPolicyType("Full Coverage");
        guarantee.setCoverage("Full");
        guarantee.setStatus(true);
        guarantee.setWarrantyPeriodMonth(12);
    }

    @Test
    public void testCreateGuarantee() {
        CreateGuaranteeRequest request = new CreateGuaranteeRequest();
        request.setFK_productID(1L);
        request.setPolicyType("Full Coverage");
        request.setCoverage("Full");
        request.setWarrantyPeriod(12);

        when(productSellRepository.findById(1L)).thenReturn(Optional.of(productSell));
        when(guaranteeRepository.save(any(Guarantee.class))).thenReturn(guarantee);

        GuaranteeResponse response = guaranteeService.createGuarantee(request);

        assertNotNull(response);
        assertEquals(1L, response.getPK_guaranteeID());
        assertEquals("Full Coverage", response.getPolicyType());
        assertEquals("Full", response.getCoverage());
        assertEquals(12, response.getWarrantyPeriod());
        assertTrue(response.isStatus());
    }

    @Test
    public void testCreateGuaranteeProductNotExist() {
        CreateGuaranteeRequest request = new CreateGuaranteeRequest();
        request.setFK_productID(1L);
        request.setPolicyType("Full Coverage");
        request.setCoverage("Full");
        request.setWarrantyPeriod(12);

        when(productSellRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            guaranteeService.createGuarantee(request);
        });

        assertEquals("Product ID not exist", exception.getMessage());
    }

    @Test
    public void testGetGuaranteeResponseById() {
        when(guaranteeRepository.findByGuaranteeId(1L)).thenReturn(guarantee);

        GuaranteeResponse response = guaranteeService.getGuaranteeResponseById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getPK_guaranteeID());
        assertEquals("Full Coverage", response.getPolicyType());
        assertEquals("Full", response.getCoverage());
        assertEquals(12, response.getWarrantyPeriod());
        assertTrue(response.isStatus());
    }

    @Test
    public void testGetGuaranteeResponseByIdNotFound() {
        when(guaranteeRepository.findByGuaranteeId(1L)).thenReturn(null);

        GuaranteeResponse response = guaranteeService.getGuaranteeResponseById(1L);

        assertNull(response);
    }

    @Test
    public void testReadAllGuaranteeResponses() {
        when(guaranteeRepository.listAllGuarantees()).thenReturn(Arrays.asList(guarantee));

        List<GuaranteeResponse> responses = guaranteeService.readAllGuaranteeResponses();

        assertNotNull(responses);
        assertFalse(responses.isEmpty());
        assertEquals(1, responses.size());
        assertEquals(1L, responses.get(0).getPK_guaranteeID());
    }

    @Test
    public void testUpdateGuaranteeDetails() {
        GuaranteeRequest request = new GuaranteeRequest();
        request.setPK_guaranteeID(1L);
        request.setFK_productID(1L);
        request.setPolicyType("Partial Coverage");
        request.setCoverage("Partial");
        request.setWarrantyPeriod(6);

        when(guaranteeRepository.findById(1L)).thenReturn(Optional.of(guarantee));
        when(productSellRepository.findById(1L)).thenReturn(Optional.of(productSell));
        when(guaranteeRepository.save(any(Guarantee.class))).thenReturn(guarantee);

        guaranteeService.updateGuaranteeDetails(request);

        assertEquals("Partial Coverage", guarantee.getPolicyType());
        assertEquals("Partial", guarantee.getCoverage());
        assertEquals(6, guarantee.getWarrantyPeriodMonth());
    }

    @Test
    public void testDeleteGuaranteePolicyById() {
        when(guaranteeRepository.findById(1L)).thenReturn(Optional.of(guarantee));

        guaranteeService.deleteGuaranteePolicyById(1L);

        assertFalse(guarantee.isStatus());
        verify(guaranteeRepository, times(1)).save(guarantee);
    }

    @Test
    public void testReadAllActiveGuaranteePolicies() {
        when(guaranteeRepository.findByStatus(true)).thenReturn(Arrays.asList(guarantee));

        List<GuaranteeResponse> responses = guaranteeService.readAllActiveGuaranteePolicies();

        assertNotNull(responses);
        assertFalse(responses.isEmpty());
        assertEquals(1, responses.size());
        assertEquals(1L, responses.get(0).getPK_guaranteeID());
    }

    @Test
    public void testReadAllInactiveGuaranteePolicies() {
        when(guaranteeRepository.findByStatus(false)).thenReturn(Arrays.asList(guarantee));

        List<GuaranteeResponse> responses = guaranteeService.readAllInactiveGuaranteePolicies();

        assertNotNull(responses);
        assertFalse(responses.isEmpty());
        assertEquals(1, responses.size());
        assertEquals(1L, responses.get(0).getPK_guaranteeID());
    }

    @Test
    public void testReadAllGuaranteesByPolicyType() {
        when(guaranteeRepository.listAllGuaranteesByPolicyType("Full Coverage")).thenReturn(Arrays.asList(guarantee));

        List<GuaranteeResponse> responses = guaranteeService.readAllGuaranteesByPolicyType("Full Coverage");

        assertNotNull(responses);
        assertFalse(responses.isEmpty());
        assertEquals(1, responses.size());
        assertEquals(1L, responses.get(0).getPK_guaranteeID());
    }

    @Test
    public void testReadAllGuaranteesByCoverage() {
        when(guaranteeRepository.listAllGuaranteesByCoverage("Full")).thenReturn(Arrays.asList(guarantee));

        List<GuaranteeResponse> responses = guaranteeService.readAllGuaranteesByCoverage("Full");

        assertNotNull(responses);
        assertFalse(responses.isEmpty());
        assertEquals(1, responses.size());
        assertEquals(1L, responses.get(0).getPK_guaranteeID());
    }

//    @Test
//    public void testReadGuaranteeByProductId() {
//        when(guaranteeRepository.findByProductId(1L)).thenReturn(guarantee);
//
//        GuaranteeResponse response = guaranteeService.readGuaranteeByProductId(1L);
//
//        assertNotNull(response);
//        assertEquals(1L, response.getPK_guaranteeID());
//        assertEquals("Full Coverage", response.getPolicyType());
//        assertEquals("Full", response.getCoverage());
//        assertEquals(12, response.getWarrantyPeriod());
//        assertTrue(response.isStatus());
//    }
}

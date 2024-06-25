package com.project.JewelryMS;

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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GuaranteeServiceTest {

    @InjectMocks
    private GuaranteeService guaranteeService;

    @Mock
    private GuaranteeRepository guaranteeRepository;

    @Mock
    private ProductSellRepository productSellRepository;

    private Guarantee guarantee;
    private ProductSell productSell;
    private CreateGuaranteeRequest createGuaranteeRequest;
    private GuaranteeRequest guaranteeRequest;

    @BeforeEach
    void setUp() {
        productSell = new ProductSell();
        productSell.setProductID(40L);

        guarantee = new Guarantee();
        guarantee.setPK_guaranteeID(6L);
        guarantee.setProductSell(productSell);
        guarantee.setPolicyType("VipPro1");
        guarantee.setCoverage("CoverageSuper1");
        guarantee.setStatus(true);
        guarantee.setWarrantyPeriodMonth(12);

        createGuaranteeRequest = new CreateGuaranteeRequest();
        createGuaranteeRequest.setFK_productID(40L);
        createGuaranteeRequest.setPolicyType("VipPro1");
        createGuaranteeRequest.setCoverage("CoverageSuper1");
        createGuaranteeRequest.setWarrantyPeriod(12);

        guaranteeRequest = new GuaranteeRequest();
        guaranteeRequest.setPK_guaranteeID(6L);
        guaranteeRequest.setFK_productID(40L);
        guaranteeRequest.setPolicyType("VipPro1");
        guaranteeRequest.setCoverage("CoverageSuper1");
        guaranteeRequest.setWarrantyPeriod(12);
    }

    @Test
    void testCreateGuarantee() {
        when(productSellRepository.findById(40L)).thenReturn(Optional.of(productSell));
        when(guaranteeRepository.save(any(Guarantee.class))).thenReturn(guarantee);

        GuaranteeResponse response = guaranteeService.createGuarantee(createGuaranteeRequest);

        assertNotNull(response);
        assertEquals(6L, response.getPK_guaranteeID());
        assertEquals("VipPro1", response.getPolicyType());
        assertEquals("CoverageSuper1", response.getCoverage());
        verify(guaranteeRepository, times(1)).save(any(Guarantee.class));
    }

    @Test
    void testGetGuaranteeResponseById() {
        when(guaranteeRepository.findByGuaranteeId(6L)).thenReturn(guarantee);

        GuaranteeResponse response = guaranteeService.getGuaranteeResponseById(6L);

        assertNotNull(response);
        assertEquals(6L, response.getPK_guaranteeID());
        assertEquals("VipPro1", response.getPolicyType());
        assertEquals("CoverageSuper1", response.getCoverage());
        verify(guaranteeRepository, times(1)).findByGuaranteeId(6L);
    }

    @Test
    void testReadAllGuaranteeResponses() {
        List<Guarantee> guarantees = Arrays.asList(guarantee);
        when(guaranteeRepository.listAllGuarantees()).thenReturn(guarantees);

        List<GuaranteeResponse> responses = guaranteeService.readAllGuaranteeResponses();

        assertEquals(1, responses.size());
        assertEquals(6L, responses.get(0).getPK_guaranteeID());
        verify(guaranteeRepository, times(1)).listAllGuarantees();}

    @Test
    void testUpdateGuaranteeDetails() {
        when(guaranteeRepository.findById(6L)).thenReturn(Optional.of(guarantee));
        when(productSellRepository.findById(40L)).thenReturn(Optional.of(productSell));
        when(guaranteeRepository.save(any(Guarantee.class))).thenReturn(guarantee);

        guaranteeService.updateGuaranteeDetails(guaranteeRequest);

        verify(guaranteeRepository, times(1)).findById(6L);
        verify(guaranteeRepository, times(1)).save(any(Guarantee.class));
    }

    @Test
    void testDeleteGuaranteePolicyById() {
        when(guaranteeRepository.findById(6L)).thenReturn(Optional.of(guarantee));

        guaranteeService.deleteGuaranteePolicyById(6L);

        assertFalse(guarantee.isStatus());
        verify(guaranteeRepository, times(1)).findById(6L);
        verify(guaranteeRepository, times(1)).save(guarantee);
    }

    @Test
    void testReadAllActiveGuaranteePolicies() {
        List<Guarantee> guarantees = Arrays.asList(guarantee);
        when(guaranteeRepository.findByStatus(true)).thenReturn(guarantees);

        List<GuaranteeResponse> responses = guaranteeService.readAllActiveGuaranteePolicies();

        assertEquals(1, responses.size());
        assertEquals(6L, responses.get(0).getPK_guaranteeID());
        assertTrue(responses.get(0).isStatus());
        verify(guaranteeRepository, times(1)).findByStatus(true);
    }

    @Test
    void testReadAllInactiveGuaranteePolicies() {
        guarantee.setStatus(false);
        List<Guarantee> guarantees = Arrays.asList(guarantee);
        when(guaranteeRepository.findByStatus(false)).thenReturn(guarantees);

        List<GuaranteeResponse> responses = guaranteeService.readAllInactiveGuaranteePolicies();

        assertEquals(1, responses.size());
        assertEquals(6L, responses.get(0).getPK_guaranteeID());
        assertFalse(responses.get(0).isStatus());
        verify(guaranteeRepository, times(1)).findByStatus(false);
    }

    @Test
    void testReadAllGuaranteesByPolicyType() {
        List<Guarantee> guarantees = Arrays.asList(guarantee);
        when(guaranteeRepository.listAllGuaranteesByPolicyType("VipPro1")).thenReturn(guarantees);

        List<GuaranteeResponse> responses = guaranteeService.readAllGuaranteesByPolicyType("VipPro1");

        assertEquals(1, responses.size());
        assertEquals(6L, responses.get(0).getPK_guaranteeID());
        assertEquals("VipPro1", responses.get(0).getPolicyType());
        verify(guaranteeRepository, times(1)).listAllGuaranteesByPolicyType("VipPro1");
    }

    @Test
    void testReadAllGuaranteesByCoverage() {
        List<Guarantee> guarantees = Arrays.asList(guarantee);
        when(guaranteeRepository.listAllGuaranteesByCoverage("CoverageSuper1")).thenReturn(guarantees);

        List<GuaranteeResponse> responses = guaranteeService.readAllGuaranteesByCoverage("CoverageSuper1");

        assertEquals(1, responses.size());
        assertEquals(6L, responses.get(0).getPK_guaranteeID());
        assertEquals("CoverageSuper1", responses.get(0).getCoverage());
        verify(guaranteeRepository, times(1)).listAllGuaranteesByCoverage("CoverageSuper1");
    }

    @Test
    void testReadGuaranteeByProductId() {
        when(guaranteeRepository.findByProductId(40L)).thenReturn(guarantee);

        GuaranteeResponse response = guaranteeService.readGuaranteeByProductId(40L);

        assertNotNull(response);
        assertEquals(6L, response.getPK_guaranteeID());
        assertEquals(40L, response.getProductID());
        verify(guaranteeRepository, times(1)).findByProductId(40L);
    }
}
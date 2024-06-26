package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Guarantee;
import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.model.Guarantee.CreateGuaranteeRequest;
import com.project.JewelryMS.model.Guarantee.GuaranteeRequest;
import com.project.JewelryMS.model.Guarantee.GuaranteeResponse;
import com.project.JewelryMS.repository.GuaranteeRepository;
import com.project.JewelryMS.repository.ProductSellRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GuaranteeService {

    @Autowired
    GuaranteeRepository guaranteeRepository;
    @Autowired
    ProductSellRepository productSellRepository;

    // Helper method to convert a Guarantee entity to a GuaranteeResponse DTO
    private GuaranteeResponse toGuaranteeResponse(Guarantee guarantee) {
        return new GuaranteeResponse(
                guarantee.getPK_guaranteeID(),
                guarantee.getProductSell().getProductID(),
                guarantee.getPolicyType(),
                guarantee.getCoverage(),
                guarantee.isStatus(),
                guarantee.getWarrantyPeriodMonth()
        );
    }

    public GuaranteeResponse createGuarantee(CreateGuaranteeRequest createGuaranteeRequest) {
        Optional<ProductSell> productSellOptional = productSellRepository.findById(createGuaranteeRequest.getFK_productID());
        if (productSellOptional.isPresent()) {
            Guarantee guarantee = new Guarantee();

            guarantee.setProductSell(productSellOptional.get());
            guarantee.setPolicyType(createGuaranteeRequest.getPolicyType());
            guarantee.setCoverage(createGuaranteeRequest.getCoverage());
            guarantee.setStatus(true);
            guarantee.setWarrantyPeriodMonth(createGuaranteeRequest.getWarrantyPeriod());
            Guarantee newGuarantee = guaranteeRepository.save(guarantee);
            return toGuaranteeResponse(newGuarantee);
        } else {
            throw new RuntimeException("Product ID not exist");
        }
    }

    public GuaranteeResponse getGuaranteeResponseById(long id) {
        Guarantee guarantee = guaranteeRepository.findByGuaranteeId(id);
        return guarantee != null ? toGuaranteeResponse(guarantee) : null;
    }

    public List<GuaranteeResponse> readAllGuaranteeResponses() {
        List<Guarantee> guarantees = guaranteeRepository.listAllGuarantees();

        return guarantees.stream()
                .map(this::toGuaranteeResponse)
                .collect(Collectors.toList());
    }

    public void updateGuaranteeDetails(GuaranteeRequest guaranteeRequest) {
        Optional<Guarantee> guaranteeUpdate = guaranteeRepository.findById(guaranteeRequest.getPK_guaranteeID());
        if (guaranteeUpdate.isPresent()) {
            Guarantee guarantee = guaranteeUpdate.get();

            guarantee.setProductSell(productSellRepository.findById(guaranteeRequest.getFK_productID()).orElse(null));
            guarantee.setCoverage(guaranteeRequest.getCoverage());
            guarantee.setPolicyType(guaranteeRequest.getPolicyType());
            guarantee.setWarrantyPeriodMonth(guaranteeRequest.getWarrantyPeriod());
            guaranteeRepository.save(guarantee);
        }
    }

    public void deleteGuaranteePolicyById(long id) {
        Optional<Guarantee> guarantee = guaranteeRepository.findById(id);
        guarantee.ifPresent(guaranteeStatus -> {
            guaranteeStatus.setStatus(false); // Set status to false
            guaranteeRepository.save(guaranteeStatus);
        });
    }

    public List<GuaranteeResponse> readAllActiveGuaranteePolicies() {
        List<Guarantee> guarantees = guaranteeRepository.findByStatus(true);

        return guarantees.stream()
                .map(this::toGuaranteeResponse)
                .collect(Collectors.toList());
    }

    public List<GuaranteeResponse> readAllInactiveGuaranteePolicies() {
        List<Guarantee> guarantees = guaranteeRepository.findByStatus(false);

        return guarantees.stream()
                .map(this::toGuaranteeResponse)
                .collect(Collectors.toList());
    }

    public List<GuaranteeResponse> readAllGuaranteesByPolicyType(String policyType) {
        List<Guarantee> guarantees = guaranteeRepository.listAllGuaranteesByPolicyType(policyType);

        return guarantees.stream()
                .map(this::toGuaranteeResponse)
                .collect(Collectors.toList());
    }

    public List<GuaranteeResponse> readAllGuaranteesByCoverage(String coverage) {
        List<Guarantee> guarantees = guaranteeRepository.listAllGuaranteesByCoverage(coverage);

        return guarantees.stream()
                .map(this::toGuaranteeResponse)
                .collect(Collectors.toList());
    }

    public GuaranteeResponse readGuaranteeByProductId(long productId) {
        Guarantee guarantee = guaranteeRepository.findByProductId(productId);
        return guarantee != null ? toGuaranteeResponse(guarantee) : null;
    }
}


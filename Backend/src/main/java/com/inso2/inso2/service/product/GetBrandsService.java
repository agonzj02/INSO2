package com.inso2.inso2.service.product;

import com.inso2.inso2.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetBrandsService {

    private final ProductRepository productRepository;

    public GetBrandsService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<String> get(){
        return productRepository.getBrands();
    }
}

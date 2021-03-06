package com.inso2.inso2.controller;

import com.inso2.inso2.dto.product.AddProductRequest;
import com.inso2.inso2.dto.product.byRef.ProductByRefRequest;
import com.inso2.inso2.dto.product.specification.ProductSpecificationRequest;
import com.inso2.inso2.dto.product.specification.ProductSpecificationResponse;
import com.inso2.inso2.dto.product.specification.SpecificationDTO;
import com.inso2.inso2.model.Product;
import com.inso2.inso2.model.User;
import com.inso2.inso2.repository.ProductRepository;
import com.inso2.inso2.repository.specification.SearchCriteria;
import com.inso2.inso2.repository.specification.SearchOperationUtils;
import com.inso2.inso2.repository.specification.product.ProductSpecification;
import com.inso2.inso2.service.category.GetCategoriesService;
import com.inso2.inso2.service.product.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final GetProductBySpecificationService getProductBySpecificationService;

    private final GetProductByRefService getProductByRefService;

    private final AddProductService addProductService;

    private final GetSizesService getSizesService;

    private final GetBrandsService getBrandsService;

    private final GetCategoriesService getCategoriesService;

    private final GetYearsService getYearsService;

    public ProductController(GetProductBySpecificationService getProductBySpecificationService, GetProductByRefService getProductByRefService, AddProductService addProductService, GetSizesService getSizesService, GetBrandsService getBrandsService, GetCategoriesService getCategoriesService, GetYearsService getYearsService) {
        this.getProductBySpecificationService = getProductBySpecificationService;
        this.getProductByRefService = getProductByRefService;
        this.addProductService = addProductService;
        this.getSizesService = getSizesService;
        this.getBrandsService = getBrandsService;
        this.getCategoriesService = getCategoriesService;
        this.getYearsService = getYearsService;
    }

    @RequestMapping(value = "/specification", method = RequestMethod.POST)
    public ResponseEntity<?> getProductsBySpecification(@RequestBody ProductSpecificationRequest req){
        return ResponseEntity.ok(getProductBySpecificationService.get(req));
    }

    @RequestMapping(value = "/ref", method = RequestMethod.POST)
    public ResponseEntity<?> getProductsByRef(@RequestBody ProductByRefRequest req){
        return ResponseEntity.ok(getProductByRefService.get(req));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<?> addProduct(@RequestBody AddProductRequest req){
        try{
            addProductService.add(req);
            return ResponseEntity.ok("Product created");
        }
        catch(Exception e){
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @RequestMapping(value = "/getSizes", method = RequestMethod.GET)
    public ResponseEntity<?> getSizes(){
        try{
            return ResponseEntity.ok(getSizesService.get());
        }
        catch(Exception e){
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @RequestMapping(value = "/getBrands", method = RequestMethod.GET)
    public ResponseEntity<?> getBrands(){
        try{
            return ResponseEntity.ok(getBrandsService.get());
        }
        catch(Exception e){
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @RequestMapping(value = "/getCategories", method = RequestMethod.GET)
    public ResponseEntity<?> getCategories(){
        try{
            return ResponseEntity.ok(getCategoriesService.get());
        }
        catch(Exception e){
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @RequestMapping(value = "/getYears", method = RequestMethod.GET)
    public ResponseEntity<?> getYears(){
        try{
            return ResponseEntity.ok(getYearsService.get());
        }
        catch(Exception e){
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}

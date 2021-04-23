package com.inso2.inso2.controller;

import com.inso2.inso2.dto.ask.AskRequest;
import com.inso2.inso2.dto.ask.GetAllAsksByUserResponse;
import com.inso2.inso2.dto.ask.delete.DeleteAskRequest;
import com.inso2.inso2.dto.ask.getAll.GetAllAsksRequest;
import com.inso2.inso2.dto.ask.getAll.GetAllAsksResponse;
import com.inso2.inso2.model.*;
import com.inso2.inso2.repository.AskRepository;
import com.inso2.inso2.repository.ProductDetailsRepository;
import com.inso2.inso2.repository.ProductRepository;
import com.inso2.inso2.service.ask.*;
import com.inso2.inso2.service.user.LoadUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/ask")
public class AskController {

    private final ProductDetailsRepository productDetailsRepository;

    private final AskRepository askRepository;

    private final ProductRepository productRepository;

    private final CreateAskService createAskService;

    private final ModifyAskService modifyAskService;

    private final LoadUserService loadUserService;

    private final DeleteAskService deleteAskService;

    private final GetAsksOfProductService getAsksOfProductService;

    private final GetAsksOfUserService getAsksOfUserService;

    public AskController(ProductDetailsRepository productDetailsRepository, AskRepository askRepository, ProductRepository productRepository, CreateAskService createAskService, ModifyAskService modifyAskService, LoadUserService loadUserService, DeleteAskService deleteAskService, GetAsksOfProductService getAsksOfProductService, GetAsksOfUserService getAsksOfUserService) {
        this.productDetailsRepository = productDetailsRepository;
        this.askRepository = askRepository;
        this.productRepository = productRepository;
        this.createAskService = createAskService;
        this.modifyAskService = modifyAskService;
        this.loadUserService = loadUserService;
        this.deleteAskService = deleteAskService;
        this.getAsksOfProductService = getAsksOfProductService;
        this.getAsksOfUserService = getAsksOfUserService;
    }

    @RequestMapping(value = "/make", method = RequestMethod.POST)
    public ResponseEntity<?> makeAsk(@RequestBody AskRequest req){
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = loadUserService.load(auth);
            Product product = productRepository.findByRef(req.getRef());
            ProductDetails productDetails = productDetailsRepository.findByProductAndSize(product, req.getSize());
            Ask ask = askRepository.findByUserAndProductDetails(user, productDetails);
            if(ask == null){
                return createAskService.create(req.getPrice(), user, productDetails);
            }
            else {
                return modifyAskService.modify(req.getPrice(), productDetails, ask);
            }
        }catch(Exception e){
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.SERVICE_UNAVAILABLE);
        }

    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntity<?> delete(@RequestBody DeleteAskRequest req){
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = loadUserService.load(auth);
            deleteAskService.delete(req, user);
            return ResponseEntity.ok("Ask deleted");
        }catch(Exception e){
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.POST)
    public ResponseEntity<?> getAll(@RequestBody GetAllAsksRequest req){
        try{
            return ResponseEntity.ok(getAsksOfProductService.get(req));
        }
        catch(Exception e){
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @RequestMapping(value = "/getAllByUser", method = RequestMethod.GET)
    public ResponseEntity<?> getAllByUser(){
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = loadUserService.load(auth);
            return ResponseEntity.ok(getAsksOfUserService.get(user));
        }
        catch(Exception e){
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}

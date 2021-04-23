package com.inso2.inso2.controller;

import com.inso2.inso2.dto.order.create.CreateOrderBuyRequest;
import com.inso2.inso2.dto.order.create.CreateOrderSellRequest;
import com.inso2.inso2.dto.order.get.GetOrderInformationResponse;
import com.inso2.inso2.model.*;
import com.inso2.inso2.repository.*;
import com.inso2.inso2.service.order.CreateBuyService;
import com.inso2.inso2.service.order.CreateSellService;
import com.inso2.inso2.service.order.GetPurchasesOfUserService;
import com.inso2.inso2.service.order.GetSellsOfUserService;
import com.inso2.inso2.service.user.LoadUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final LoadUserService loadUserService;
    private final CreateBuyService createBuyService;
    private final CreateSellService createSellService;
    private final GetPurchasesOfUserService getPurchasesOfUserService;
    private final GetSellsOfUserService getSellsOfUserService;

    public OrderController(LoadUserService loadUserService, CreateBuyService createBuyService, CreateSellService createSellService, GetPurchasesOfUserService getPurchasesOfUserService, GetSellsOfUserService getSellsOfUserService) {
        this.loadUserService = loadUserService;
        this.createBuyService = createBuyService;
        this.createSellService = createSellService;
        this.getPurchasesOfUserService = getPurchasesOfUserService;
        this.getSellsOfUserService = getSellsOfUserService;
    }

    @RequestMapping(value = "/createBuy", method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody CreateOrderBuyRequest req){
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = loadUserService.load(auth);
            createBuyService.create(req, user);
            return ResponseEntity.ok("Order created");
        }
        catch(Exception e){
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @RequestMapping(value = "/createSell", method = RequestMethod.POST)
    public ResponseEntity<?> createSell(@RequestBody CreateOrderSellRequest req){
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = loadUserService.load(auth);
            createSellService.create(req, user);
            return ResponseEntity.ok("Order created");
        }
        catch(Exception e){
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @RequestMapping(value = "/getPurchases", method = RequestMethod.GET)
    public ResponseEntity<?> getPurchases(){
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = loadUserService.load(auth);
            return ResponseEntity.ok(getPurchasesOfUserService.get(user));
        }
        catch (Exception e){
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @RequestMapping(value = "/getSells", method = RequestMethod.GET)
    public ResponseEntity<?> getSells(){
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = loadUserService.load(auth);
            return ResponseEntity.ok(getSellsOfUserService.get(user));
        }
        catch (Exception e){
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}

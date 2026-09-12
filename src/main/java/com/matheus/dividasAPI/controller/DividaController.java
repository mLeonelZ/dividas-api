package com.matheus.dividasAPI.controller;

import com.matheus.dividasAPI.dto.DividaRequest;
import com.matheus.dividasAPI.dto.DividaResponse;
import com.matheus.dividasAPI.service.DividaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dividas")
public class DividaController {

    private final DividaService service;
    public DividaController(DividaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DividaResponse> cadastrar(@Valid @RequestBody DividaRequest request){
        DividaResponse response = service.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

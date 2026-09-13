package com.matheus.dividasAPI.controller;

import com.matheus.dividasAPI.dto.DividaRequest;
import com.matheus.dividasAPI.dto.DividaResponse;
import com.matheus.dividasAPI.service.DividaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<Page<DividaResponse>> listarTodas(Pageable pageable){
        return ResponseEntity.ok(service.listarTodas(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DividaResponse> listarPorId(@PathVariable Long id){
        return ResponseEntity.ok(service.listarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DividaResponse> atualizar(@PathVariable Long id, @Valid @RequestBody DividaRequest request){
        return ResponseEntity.ok(service.atualizar(id,request));
    }


}

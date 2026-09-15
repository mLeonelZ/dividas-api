package com.matheus.dividasAPI.controller;

import com.matheus.dividasAPI.dto.DividaRequest;
import com.matheus.dividasAPI.dto.DividaResponse;
import com.matheus.dividasAPI.service.DividaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Dívidas", description = "Operações para gerenciamento de dívidas")
@RestController
@RequestMapping("/dividas")
public class DividaController {

    private final DividaService service;
    public DividaController(DividaService service) {
        this.service = service;
    }

    @Operation(summary = "Cadastra uma nova dívida",
            description = "Cria uma dívida com status PENDENTE e vencimento definido para 30 dias após a criação.")
    @PostMapping
    public ResponseEntity<DividaResponse> cadastrar(@Valid @RequestBody DividaRequest request){
        DividaResponse response = service.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Lista todas as dívidas",
            description = "Retorna as dívidas de forma paginada e ordenadas pela data de criação.")
    @GetMapping
    public ResponseEntity<Page<DividaResponse>> listarTodas(Pageable pageable){
        return ResponseEntity.ok(service.listarTodas(pageable));
    }

    @Operation(summary = "Lista uma dívida pelo seu ID",
            description = "Retorna os dados de uma dívida específica.")
    @GetMapping("/{id}")
    public ResponseEntity<DividaResponse> listarPorId(@PathVariable Long id){
        return ResponseEntity.ok(service.listarPorId(id));
    }

    @Operation(summary = "Atualiza uma dívida pelo seu ID",
            description = "Atualiza os dados de uma dívida existente. Dívidas com status PAGA ou CANCELADA não podem ser alteradas.")
    @PutMapping("/{id}")
    public ResponseEntity<DividaResponse> atualizar(@PathVariable Long id, @Valid @RequestBody DividaRequest request){
        return ResponseEntity.ok(service.atualizar(id,request));
    }

    @Operation(summary = "Deleta uma dívida pelo seu ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        service.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}

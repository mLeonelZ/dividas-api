package com.matheus.dividasAPI.controller;

import com.matheus.dividasAPI.dto.DividaRequest;
import com.matheus.dividasAPI.dto.DividaResponse;
import com.matheus.dividasAPI.exceptions.DividaNotFoundException;
import com.matheus.dividasAPI.exceptions.ErrorResponse;
import com.matheus.dividasAPI.service.DividaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Dívida cadastrada com sucesso!",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DividaResponse.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados da dívida são inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ) )
    })
    @PostMapping
    public ResponseEntity<DividaResponse> cadastrar(@Valid @RequestBody DividaRequest request){
        DividaResponse response = service.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Lista todas as dívidas",
            description = "Retorna as dívidas de forma paginada e ordenadas pela data de criação.")
    @ApiResponse(responseCode = "200", description = "Dívidas listadas com sucesso!")
    @GetMapping
    public ResponseEntity<Page<DividaResponse>> listarTodas(Pageable pageable){
        return ResponseEntity.ok(service.listarTodas(pageable));
    }

    @Operation(summary = "Lista uma dívida pelo seu ID",
            description = "Retorna os dados de uma dívida específica.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Dívida listada com sucesso!",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DividaResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Dívida não encontrada!",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    @GetMapping("/{id}")
    public ResponseEntity<DividaResponse> listarPorId(@PathVariable Long id){
        return ResponseEntity.ok(service.listarPorId(id));
    }

    @Operation(summary = "Atualiza uma dívida pelo seu ID",
            description = "Atualiza os dados de uma dívida existente. Dívidas com status PAGA ou CANCELADA não podem ser alteradas.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Dívida atualizada com sucesso!",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DividaResponse.class)
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "Dívida não encontrada!",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados da dívida são inválidos!",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Dívida não pode ser atualizada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<DividaResponse> atualizar(@PathVariable Long id, @Valid @RequestBody DividaRequest request){
        return ResponseEntity.ok(service.atualizar(id,request));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Dívida deletada com sucesso!"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Dívida não encontrada!",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @Operation(summary = "Deleta uma dívida pelo seu ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        service.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}

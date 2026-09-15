package com.matheus.dividasAPI.controller;

import com.matheus.dividasAPI.dto.DividaRequest;
import com.matheus.dividasAPI.dto.DividaResponse;
import com.matheus.dividasAPI.enums.DividaStatus;
import com.matheus.dividasAPI.exceptions.DividaNotFoundException;
import com.matheus.dividasAPI.exceptions.DividaUpdateException;
import com.matheus.dividasAPI.service.DividaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DividaController.class) // Usado para testar a camada Web/Controller da aplicação.
public class DividaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DividaService service;

    // Post cadastrar
    @Test
    void deveCadastrarDivida() throws Exception {
        DividaRequest request = new DividaRequest(
                "12345678901",
                new BigDecimal("1000.00"),
                new BigDecimal("1200.00"),
                new BigDecimal("1100.00")
        );

        DividaResponse response = new DividaResponse(
                1L,
                "12345678901",
                new BigDecimal("1000.00"),
                new BigDecimal("1200.00"),
                new BigDecimal("1100.00"),
                DividaStatus.PENDENTE,
                LocalDateTime.now(),
                LocalDate.now().plusDays(30)
        );

        when(service.cadastrar(any(DividaRequest.class)))
                .thenReturn(response);

        // simule uma requisição HTTP POST para /dividas
        // como se estivesse chamando no postman: POST http://localhost:8080/dividas
        mockMvc.perform( // execute um post em /dividas que o contentType é um application json e o conteudo json request e esperamos que o resultado seja 200 ok
                        post("/dividas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "cpfDevedor": "12345678901",
                                            "valorPego": 1000.00,
                                            "valorComJuros": 1200.00,
                                            "valorComDesconto": 1100.00
                                        }
                                        """)
                ).andExpect(status().isCreated()) // Espera que o status seja 201
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cpfDevedor").value("12345678901"));
        verify(service).cadastrar(any(DividaRequest.class));
    }

    @Test
    void deveRetornar400QuandoCpfInvalido() throws Exception {
        mockMvc.perform(
                post("/dividas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "cpfDevedor": "123456789011",
                                    "valorPego": 1000.00,
                                    "valorComJuros": 1200.00,
                                    "valorComDesconto": 1100.00
                                }
                                """)
        ).andExpect(status().isBadRequest());
        verify(service, never()).cadastrar(any(DividaRequest.class));
    }

    // Get por id
    @Test
    void deveBuscarDividaPorId() throws Exception {
        DividaResponse response = new DividaResponse(
                1L,
                "12345678901",
                new BigDecimal("1000.00"),
                new BigDecimal("1200.00"),
                new BigDecimal("1100.00"),
                DividaStatus.PENDENTE,
                LocalDateTime.now(),
                LocalDate.now().plusDays(30)
        );

        when(service.listarPorId(1L))
                .thenReturn(response);

        mockMvc.perform(
                        get("/dividas/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cpfDevedor").value("12345678901"));

        verify(service).listarPorId(1L);
    }

    @Test
    void deveRetornar404QuandoIdNaoEncontrado() throws Exception {
        when(service.listarPorId(999L))
                .thenThrow(new DividaNotFoundException("Divida nao encontrada: 999"));

        mockMvc.perform(
                        get("/dividas/999")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Divida nao encontrada: 999"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(service).listarPorId(999L);
    }

    // Get todas dividas
    @Test
    void deveListarTodasAsDividas() throws Exception {

        DividaResponse divida1 = new DividaResponse(
                1L,
                "12345678901",
                new BigDecimal("1000.00"),
                new BigDecimal("1200.00"),
                new BigDecimal("1100.00"),
                DividaStatus.PENDENTE,
                LocalDateTime.now(),
                LocalDate.now().plusDays(30)
        );

        DividaResponse divida2 = new DividaResponse(
                2L,
                "98765432100",
                new BigDecimal("2000.00"),
                new BigDecimal("2400.00"),
                new BigDecimal("2200.00"),
                DividaStatus.PENDENTE,
                LocalDateTime.now(),
                LocalDate.now().plusDays(30)
        );

        Page<DividaResponse> page = new PageImpl<>(
                List.of(divida1, divida2)
        );

        when(service.listarTodas(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(
                        get("/dividas")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[1].id").value(2));

        verify(service).listarTodas(any(Pageable.class));
    }

    // Put atualizar divida
    @Test
    void deveAtualizarDivida() throws Exception {
        DividaResponse response = new DividaResponse(
                1L,
                "98765432100",
                new BigDecimal("2000.00"),
                new BigDecimal("2400.00"),
                new BigDecimal("2200.00"),
                DividaStatus.PENDENTE,
                LocalDateTime.now(),
                LocalDate.now().plusDays(30)
        );

        when(service.atualizar(
                eq(1L), // quando atualizar for chamado com o ID 1
                any(DividaRequest.class)
        )).thenReturn(response);

        mockMvc.perform(
                        put("/dividas/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "cpfDevedor": "98765432100",
                                            "valorPego": 2000.00,
                                            "valorComJuros": 2400.00,
                                            "valorComDesconto": 2200.00
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.cpfDevedor").value("98765432100"))
                .andExpect(jsonPath("$.valorPego").value(2000.00))
                .andExpect(jsonPath("$.valorComJuros").value(2400.00))
                .andExpect(jsonPath("$.valorComDesconto").value(2200.00))
                .andExpect(jsonPath("$.status").value("PENDENTE")
                );

        verify(service).atualizar(
                eq(1L),
                any(DividaRequest.class)
        );


    }

    @Test
    void deveRetornar404AoAtualizarDividaInexistente() throws Exception {
        when(service.atualizar(
                eq(999L),
                any(DividaRequest.class)
        )).thenThrow(
                new DividaNotFoundException("Divida nao encontrada: 999")
        );
        // Se o Controller tentar atualizar a dívida 999, lance DividaNotFoundException.
        mockMvc.perform(
                        put("/dividas/999")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "cpfDevedor": "98765432100",
                                            "valorPego": 2000.00,
                                            "valorComJuros": 2400.00,
                                            "valorComDesconto": 2200.00
                                        }
                                        """)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Divida nao encontrada: 999"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(service).atualizar(
                eq(999L),
                any(DividaRequest.class)
        );
    }

    @Test
    void deveRetornar409AoAtualizarDividaPaga() throws Exception {
        when(service.atualizar(
                eq(1L),
                any(DividaRequest.class)
        )).thenThrow(new DividaUpdateException(
                        "Não é possível alterar uma dívida que está PAGA"
                )
        );

        mockMvc.perform(
                        put("/dividas/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "cpfDevedor": "98765432100",
                                            "valorPego": 2000.00,
                                            "valorComJuros": 2400.00,
                                            "valorComDesconto": 2200.00
                                        }
                                        """)
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Não é possível alterar uma dívida que está PAGA"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(service).atualizar(
                eq(1L),
                any(DividaRequest.class)
        );


    }

    @Test
    void deveRetornar409AoAtualizarDividaCancelada() throws Exception {
        when(service.atualizar(
                eq(1L),
                any(DividaRequest.class)
        )).thenThrow(new DividaUpdateException(
                        "Não é possível alterar uma dívida que está CANCELADA"
                )
        );

        mockMvc.perform(
                        put("/dividas/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "cpfDevedor": "98765432100",
                                            "valorPego": 2000.00,
                                            "valorComJuros": 2400.00,
                                            "valorComDesconto": 2200.00
                                        }
                                        """)
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Não é possível alterar uma dívida que está CANCELADA"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(service).atualizar(
                eq(1L),
                any(DividaRequest.class)
        );
    }

    @Test
    void deveRetornar400AoAtualizarDividaComDadosInvalidos() throws Exception{
        mockMvc.perform(
                put("/dividas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "cpfDevedor": "123",
                                "valorPego": 2000.00,
                                "valorComJuros": 2400.00,
                                "valorComDesconto": 2200.00
                            }
                            """)
        )
                .andExpect(status().isBadRequest());

        verify(service, never())
                .atualizar(eq(1L),
                        any(DividaRequest.class)
                );
    }

    // Delete deletar uma divida
    @Test
    void deveDeletarDivida() throws Exception{
        doNothing().when(service).deletar(1L);
        // usar doNothing() quando o tipo do méthod é um void

        mockMvc.perform(
                delete("/dividas/1")
        )
                .andExpect(status().isNoContent());
        verify(service).deletar(1L);
    }

    @Test // divida nao existente
    void deve() throws Exception{
        doThrow(new DividaNotFoundException("Divida não encontrada: 999"))
                .when(service).deletar(999L);
        mockMvc.perform(
                delete("/dividas/999")
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Divida não encontrada: 999"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(service).deletar(999L);
    }

}
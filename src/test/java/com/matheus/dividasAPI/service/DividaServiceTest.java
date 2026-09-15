package com.matheus.dividasAPI.service;

import com.matheus.dividasAPI.domain.DividaModel;
import com.matheus.dividasAPI.dto.DividaRequest;
import com.matheus.dividasAPI.dto.DividaResponse;
import com.matheus.dividasAPI.enums.DividaStatus;
import com.matheus.dividasAPI.exceptions.DividaNotFoundException;
import com.matheus.dividasAPI.exceptions.DividaUpdateException;
import com.matheus.dividasAPI.repository.DividaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DividaServiceTest {

    @Mock
    private DividaRepository repository;

    @InjectMocks
    private DividaService service;

    // Cadastro
    @Test
    void deveCadastrarDivida(){
        DividaRequest request = new DividaRequest(
                "12345678901",
                new BigDecimal("1000.00"),
                new BigDecimal("1200.00"),
                new BigDecimal("1100.00")
        );
        // Quando o repository receber um save(dividaModel)
        // responda com o primeiro argumento que foi passado para o method.
        when(repository.save(any(DividaModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DividaResponse response = service.cadastrar(request);

        assertNotNull(response);
        assertEquals("12345678901", response.cpfDevedor());
        assertEquals(new BigDecimal("1000.00"), response.valorPego());
        assertEquals(new BigDecimal("1200.00"), response.valorComJuros());
        assertEquals(new BigDecimal("1100.00"), response.valorComDesconto());
        assertEquals(DividaStatus.PENDENTE, response.status());

        verify(repository).save(any(DividaModel.class));
    }

    @Test
    void deveRecusarDividaQuandoValorComJurosForMenorQueValorPego(){
        DividaRequest request = new DividaRequest(
                "12345678901",
                new BigDecimal("1000.00"),
                new BigDecimal("890.00"),
                new BigDecimal("1100.00")
        );
        assertThrows(
                DividaUpdateException.class,
                () -> service.cadastrar(request)
        );

        // verifica se o repository nao foi chamado e o save nao aconteceu
        verify(repository, never()).save(any(DividaModel.class));
    }

    @Test
    void deveRecusarDividaQuandoValorComDescontoForMaiorQueValorComJuros(){
        DividaRequest request = new DividaRequest(
                "12345678901",
                new BigDecimal("1000.00"),
                new BigDecimal("1234.00"),
                new BigDecimal("1340.00")
        );
        // Para termos certeza de que uma exceção foi lançada
        // Explicação:
        // assertThrows(
        //    TipoDaExcecao.class,
        //    () -> códigoQueDeveLançarAExcecao()
        //);
        assertThrows(
                DividaUpdateException.class,
                () -> service.cadastrar(request)
        );
        verify(repository, never()).save(any(DividaModel.class));
    }

    // Busca por ID
    @Test
    void deveListarDividaPorId(){
        DividaModel model = new DividaModel(
                "12345678901",
                new BigDecimal("1000.00"),
                new BigDecimal("1200.00"),
                new BigDecimal("1100.00"),
                DividaStatus.PENDENTE,
                LocalDateTime.now(),
                LocalDate.now().plusDays(30)
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(model));

        DividaResponse response = service.listarPorId(1L);

        assertNotNull(response);
        assertEquals("12345678901", response.cpfDevedor());
        assertEquals(new BigDecimal("1000.00"), response.valorPego());
        assertEquals(DividaStatus.PENDENTE, response.status());

        verify(repository).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoDividaNaoExistir(){

        when(repository.findById(999L))
                .thenReturn(Optional.empty()); // O Repository retorna Optional.empty() quando não encontrou uma dívida.

        assertThrows(DividaNotFoundException.class,
                () -> service.listarPorId(999L)
        );

        verify(repository).findById(999L);

    }

    // Exclusao
    @Test
    void deveExcluirQuandoIdExistir(){
        DividaModel model = new DividaModel(
                "12345678901",
                new BigDecimal("1000.00"),
                new BigDecimal("1200.00"),
                new BigDecimal("1100.00"),
                DividaStatus.PENDENTE,
                LocalDateTime.now(),
                LocalDate.now().plusDays(30)
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(model));

        service.deletar(1L);
        verify(repository).findById(1L);
        verify(repository).delete(model);
    }

    @Test
    void deveLancarExcecaoAoDeletarDividaInexistente(){
        when(repository.findById(50L))
                .thenReturn(Optional.empty());

        assertThrows(
                DividaNotFoundException.class,
                () -> service.deletar(50L)
        );

        verify(repository).findById(50L); // Verifica se o repository chamou findById
        verify(repository, never()).delete(any(DividaModel.class)); // Verifique que o Repository nunca chamou delete() passando qualquer objeto do tipo DividaModel.

    }

    // Atualização
    @Test
    void deveAtualizarDividaQuandoElaEstiverPendente(){
        DividaModel model = new DividaModel(
                "12345678901",
                new BigDecimal("1000.00"),
                new BigDecimal("1200.00"),
                new BigDecimal("1100.00"),
                DividaStatus.PENDENTE,
                LocalDateTime.now(),
                LocalDate.now().plusDays(30)
        );

        DividaRequest request = new DividaRequest(
                "98765432100",
                new BigDecimal("2000.00"),
                new BigDecimal("2400.00"),
                new BigDecimal("2200.00")
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(model));

        when(repository.save(any(DividaModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DividaResponse response = service.atualizar(1L, request);

        assertNotNull(response);
        assertEquals("98765432100", response.cpfDevedor());
        assertEquals(new BigDecimal("2000.00"), response.valorPego());
        assertEquals(new BigDecimal("2400.00"), response.valorComJuros());
        assertEquals(new BigDecimal("2200.00"), response.valorComDesconto());
        assertEquals(DividaStatus.PENDENTE, response.status());

        verify(repository).findById(1L);
        verify(repository).save(model);






    }

    @Test
    void deveRecusarAtualizacaoDeDividaPaga(){
        DividaModel model = new DividaModel(
                "12345678901",
                new BigDecimal("1000.00"),
                new BigDecimal("1200.00"),
                new BigDecimal("1100.00"),
                DividaStatus.PAGA,
                LocalDateTime.now(),
                LocalDate.now().plusDays(30)
        );

        DividaRequest request = new DividaRequest(
                "98765432100",
                new BigDecimal("2000.00"),
                new BigDecimal("2400.00"),
                new BigDecimal("2200.00")
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(model));

        assertThrows(
                DividaUpdateException.class,
                () -> service.atualizar(1L, request)
        );

        verify(repository).findById(1L);
        verify(repository, never())
                .save(any(DividaModel.class));
    }

    @Test
    void deveRecusarAtualizacaoDeDividaCancelada(){
        DividaModel model = new DividaModel(
                "12345678901",
                new BigDecimal("1000.00"),
                new BigDecimal("1200.00"),
                new BigDecimal("1100.00"),
                DividaStatus.CANCELADA,
                LocalDateTime.now(),
                LocalDate.now().plusDays(30)
        );

        DividaRequest request = new DividaRequest(
                "98765432100",
                new BigDecimal("2000.00"),
                new BigDecimal("2400.00"),
                new BigDecimal("2200.00")
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(model));

        assertThrows(
                DividaUpdateException.class,
                () -> service.atualizar(1L, request)
        );

        verify(repository).findById(1L);
        verify(repository, never())
                .save(any(DividaModel.class));

    }

    @Test
    void deveLancarExcecaoAoAtualizarDividaInexistente() {
        DividaRequest request = new DividaRequest(
                "98765432100",
                new BigDecimal("2000.00"),
                new BigDecimal("2400.00"),
                new BigDecimal("2200.00")
        );

        when(repository.findById(99L))
                .thenReturn(Optional.empty());

        // Espera que seja lançada uma DividaUpdateException quando chamar o service.atualizar(99L, request)
        assertThrows(DividaNotFoundException.class,
                () -> service.atualizar(99L, request));

        // verifica se o reposiroty.findById(99) foi chamado
        verify(repository).findById(99L);
        verify(repository,never()).save(any(DividaModel.class));
    }

    // Listar todas
    @Test
    void deveListarTodasAsDividas(){
        DividaModel divida1 = new DividaModel(
                "12345678901",
                new BigDecimal("1000.00"),
                new BigDecimal("1200.00"),
                new BigDecimal("1100.00"),
                DividaStatus.PENDENTE,
                LocalDateTime.now(),
                LocalDate.now().plusDays(30)
        );
        DividaModel divida2 = new DividaModel(
                "98765432100",
                new BigDecimal("2000.00"),
                new BigDecimal("2400.00"),
                new BigDecimal("2200.00"),
                DividaStatus.PENDENTE,
                LocalDateTime.now(),
                LocalDate.now().plusDays(30)
        );

        Pageable pageable = PageRequest.of(0,10);

        Page<DividaModel> page = new PageImpl<>(
                List.of(divida1,divida2),
                pageable,
                2
        );

        when(repository.findAll(pageable))
                .thenReturn(page);

        Page<DividaResponse> response = service.listarTodas(pageable);

        assertNotNull(response);
        assertEquals(2, response.getContent().size());
        assertEquals("12345678901", response.getContent().getFirst().cpfDevedor());
        assertEquals("98765432100", response.getContent().get(1).cpfDevedor());

        verify(repository).findAll(pageable);// Verifique se o Repository recebeu uma chamada para findAll() usando exatamente esse pageable

    }

    @Test
    void deveRetornarPaginaVaziaQuandoNaoExistiremDividas() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<DividaModel> page = new PageImpl<>(
                List.of(),
                pageable,
                0
        );

        when(repository.findAll(pageable))
                .thenReturn(page);

        Page<DividaResponse> response = service.listarTodas(pageable);

        assertNotNull(response);
        assertTrue(response.getContent().isEmpty());
        assertEquals(0, response.getContent().size());

        verify(repository).findAll(pageable);
    }

}

package com.matheus.dividasAPI.service;

import com.matheus.dividasAPI.domain.DividaModel;
import com.matheus.dividasAPI.dto.DividaRequest;
import com.matheus.dividasAPI.dto.DividaResponse;
import com.matheus.dividasAPI.enums.DividaStatus;
import com.matheus.dividasAPI.exceptions.DividaNotFoundException;
import com.matheus.dividasAPI.mapper.DividaMapper;
import com.matheus.dividasAPI.repository.DividaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DividaService {
    private final DividaRepository repository;
    public DividaService(DividaRepository repository) {
        this.repository = repository;
    }

    public DividaResponse cadastrar(DividaRequest request){

        LocalDateTime dataCriacao = LocalDateTime.now();
        LocalDate dataVencimento = dataCriacao.toLocalDate().plusDays(30);

        DividaModel model = DividaMapper.toEntity(
                request,
                DividaStatus.PENDENTE,
                dataCriacao,
                dataVencimento
        );

        DividaModel salvo = repository.save(model);
        return DividaMapper.toResponse(salvo);

    }

    public Page<DividaResponse> listarTodas(Pageable pageable){
        return repository.findAll(pageable)
                .map(DividaMapper::toResponse);
    }

    public DividaResponse listarPorId(Long id){
        DividaModel model = repository.findById(id)
                .orElseThrow(() -> new DividaNotFoundException("Divida não encontrada: " + id));

        return DividaMapper.toResponse(model);
    }


}

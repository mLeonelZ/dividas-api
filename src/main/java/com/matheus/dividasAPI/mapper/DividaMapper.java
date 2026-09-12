package com.matheus.dividasAPI.mapper;

import com.matheus.dividasAPI.domain.DividaModel;
import com.matheus.dividasAPI.dto.DividaRequest;
import com.matheus.dividasAPI.dto.DividaResponse;
import com.matheus.dividasAPI.enums.DividaStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DividaMapper {

    public static DividaResponse toResponse(DividaModel model){
        return new DividaResponse(
                model.getId(),
                model.getCpfDevedor(),
                model.getValorPego(),
                model.getValorComJuros(),
                model.getValorComDesconto(),
                model.getStatus(),
                model.getDataCriacao(),
                model.getDataVencimento()
        );
    }

    public static DividaModel toEntity(DividaRequest request, DividaStatus status, LocalDateTime dataCriacao, LocalDate dataVencimento){
        return new DividaModel(
                request.cpfDevedor(),
                request.valorPego(),
                request.valorComJuros(),
                request.valorComDesconto(),
                status,
                dataCriacao,
                dataVencimento
        );
    }

}

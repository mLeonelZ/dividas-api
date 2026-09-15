package com.matheus.dividasAPI.service;

import com.matheus.dividasAPI.domain.DividaModel;
import com.matheus.dividasAPI.dto.DividaRequest;
import com.matheus.dividasAPI.dto.DividaResponse;
import com.matheus.dividasAPI.enums.DividaStatus;
import com.matheus.dividasAPI.exceptions.DividaNotFoundException;
import com.matheus.dividasAPI.exceptions.DividaUpdateException;
import com.matheus.dividasAPI.mapper.DividaMapper;
import com.matheus.dividasAPI.repository.DividaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class DividaService {
    private final DividaRepository repository;
    public DividaService(DividaRepository repository) {
        this.repository = repository;
    }

    public DividaResponse cadastrar(DividaRequest request){

        validarValoresDeEntrada(request);

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
        DividaModel model = buscarId(id);
        return DividaMapper.toResponse(model);
    }

    public DividaResponse atualizar(Long id, DividaRequest request){
        DividaModel model = buscarId(id);
        if (model.getStatus() == DividaStatus.PAGA || model.getStatus() == DividaStatus.CANCELADA) {
            throw new DividaUpdateException(
                    "Não é possível alterar uma dívida que está " + model.getStatus().name()
            );
        }

        validarValoresDeEntrada(request);

        model.atualizar(request.cpfDevedor(), request.valorPego(), request.valorComJuros(), request.valorComDesconto());
        DividaModel atualizado = repository.save(model);
        return DividaMapper.toResponse(atualizado);
    }

    public void deletar(Long id){
        DividaModel model = buscarId(id);
        repository.delete(model);
    }

    private DividaModel buscarId(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new DividaNotFoundException("Divida não encontrada: " + id));
    }

    private void validarValoresDeEntrada(DividaRequest request){
        // Com BigDecimal utilizar .compareTo() para evitar falsos-negativos
        if (request.valorComJuros().compareTo(request.valorPego()) < 0 ){
            throw new DividaUpdateException(
                    "O valor com juros não pode ser menor que o valor pego"
            );
        }
        if (request.valorComDesconto().compareTo(request.valorComJuros()) > 0) {
            throw new DividaUpdateException(
                    "O valor com desconto não pode ser maior que o valor com juros"
            );
        }
    }

}

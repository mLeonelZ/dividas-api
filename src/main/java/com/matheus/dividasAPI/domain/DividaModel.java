package com.matheus.dividasAPI.domain;

import com.matheus.dividasAPI.enums.DividaStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "tb_dividas")
public class DividaModel {

    // Identificador único da divida
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // CPF do devedor
    @Setter
    @NotBlank
    @Size(min = 11, max = 11)
    @Column(name = "cpf_devedor", nullable = false, length = 11)
    private String cpfDevedor;

    // Valor originalmente tomado pelo devedor
    @Setter
    @NotNull
    @Positive
    @Column(name = "valor_pego", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorPego;

    // Valor da dívida considerando os juros
    @NotNull
    @Column(name = "valor_com_juros", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorComJuros;

    // Valor da dívida considerando os descontos
    @NotNull
    @Column(name = "valor_com_desconto", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorComDesconto;

    // Status da dívida
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "divida_status", nullable = false, length = 20)
    private DividaStatus status;

    // Data em que a dívida foi criada
    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    // Data de vencimento da dívida
    @Setter
    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

}

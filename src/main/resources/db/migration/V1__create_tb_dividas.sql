CREATE TABLE tb_dividas (
                            id BIGSERIAL PRIMARY KEY,
                            cpf_devedor VARCHAR(11) NOT NULL,
                            valor_pego NUMERIC(10, 2) NOT NULL,
                            valor_com_juros NUMERIC(10, 2) NOT NULL,
                            valor_com_desconto NUMERIC(10, 2) NOT NULL,
                            divida_status VARCHAR(20) NOT NULL,
                            data_criacao TIMESTAMP NOT NULL,
                            data_vencimento DATE NOT NULL
);
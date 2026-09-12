package com.matheus.dividasAPI.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class PageableConfig {

    // Definir o maximo de 50 registros por pagina
    // se nao for passado nenhum valor, o default é 10 por pagina
    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer pageableCostumizer(){
        return resolver -> {
            resolver.setMaxPageSize(50);
            resolver.setFallbackPageable(
                    org.springframework.data.domain.PageRequest.of(0,10, Sort.by(Sort.Direction.DESC, "dataCriacao"))
            );
        };
    }

}

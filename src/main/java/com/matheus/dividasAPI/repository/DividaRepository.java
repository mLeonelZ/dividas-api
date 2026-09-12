package com.matheus.dividasAPI.repository;

import com.matheus.dividasAPI.domain.DividaModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DividaRepository extends JpaRepository<DividaModel, Long> {
}

package com.controleestoque.api_estoque.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.controleestoque.api_estoque.model.Estoque;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {

    
}

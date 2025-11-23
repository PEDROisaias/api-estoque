package com.controleestoque.api_estoque.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.controleestoque.api_estoque.model.Fornecedor;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
    
}

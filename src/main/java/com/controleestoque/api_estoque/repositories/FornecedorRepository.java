package com.controleestoque.api_estoque.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.controleestoque.api_estoque.model.Fornecedor;

@Repository

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
    
}

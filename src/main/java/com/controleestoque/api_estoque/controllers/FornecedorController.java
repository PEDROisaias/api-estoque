package com.controleestoque.api_estoque.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.controleestoque.api_estoque.model.Fornecedor;
import com.controleestoque.api_estoque.repositories.FornecedorRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/fornecedores")
@RequiredArgsConstructor
public class FornecedorController {
    
    private final FornecedorRepository fornecedorRepository;

    @GetMapping
    public List<Fornecedor> getAllFornecedores() {
        return fornecedorRepository.findAll();
    }

    // GET /api/fornecedores/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Fornecedor> getCategoriaById(@PathVariable Long id) {
        // Busca um fornecedor pelo ID. Usa orElse para retornar 404 se não encontrado
        return fornecedorRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Fornecedor createFornecedor(@RequestBody Fornecedor fornecedor) {
        return fornecedorRepository.save(fornecedor);
    }

    // PUT /api/fornecedores/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Fornecedor> updateFornecedor(@PathVariable Long id, @RequestBody Fornecedor fornecedorDetails) {
        return fornecedorRepository.findById(id)
            .map(fornecedor -> {
                fornecedor.setNome(fornecedorDetails.getNome());
                Fornecedor updatedFornecedor = fornecedorRepository.save(fornecedor);
                return ResponseEntity.ok(updatedFornecedor);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/fornecedores/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFornecedor(@PathVariable Long id) {
        // Tenta encontrar e deletar um fornecedor pelo ID
        if (!fornecedorRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        fornecedorRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }
}



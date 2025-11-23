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

import com.controleestoque.api_estoque.model.Categoria;
import com.controleestoque.api_estoque.repositories.CategoriaRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categorias") // Define o caminho base para os endpoints desta controller
@RequiredArgsConstructor // Injeta automaticamente o CategoriaRepository via construtor
public class CategoriaController {
    
    private final CategoriaRepository categoriaRepository;

    // GET /api/categorias
    @GetMapping
    public List<Categoria> getAllCategorias() {
        // Retorna todas as categorias do banco de dados
        return categoriaRepository.findAll();
    }

    // GET /api/categorias/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getCategoriaById(@PathVariable Long id) {
        // Busca uma categoria pelo ID
        return categoriaRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/categorias
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Retorna status 201 Created
    public Categoria createCategoria(@RequestBody Categoria categoria) {
        // Cria uma nova categoria no banco de dados
        return categoriaRepository.save(categoria);
    }

    // PUT /api/categorias/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> updateCategoria(@PathVariable Long id, @RequestBody Categoria categoriaDetails) {
        // Tenta atualizar uma categoria existente
        return categoriaRepository.findById(id)
            .map(categoria -> {
                // Atualiza os campos da categoria encontrada
                categoria.setNome(categoriaDetails.getNome());
                Categoria updatedCategoria = categoriaRepository.save(categoria);
                return ResponseEntity.ok(updatedCategoria);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/categorias/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id) {
        // Tenta encontrar e deletar a categoria pelo ID
        if (!categoriaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        categoriaRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // Retorna status 204 No Content
    }
}

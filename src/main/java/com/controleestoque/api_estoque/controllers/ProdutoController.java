package com.controleestoque.api_estoque.controllers;

import java.util.List;

import org.apache.catalina.connector.Response;
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
import com.controleestoque.api_estoque.model.Produto;
import com.controleestoque.api_estoque.repositories.CategoriaRepository;
import com.controleestoque.api_estoque.repositories.FornecedorRepository;
import com.controleestoque.api_estoque.repositories.ProdutoRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final FornecedorRepository fornecedorRepository;
    // Estoque é geralmente manipulado via Produto ou separadamente

    // GET /api/produtos
    @GetMapping
    public List<Produto> getAllProdutos() {
        // Retorna todos os produtos do banco de dados. Pode ser necessário configurar
        // DTOs para evitar loops infinitos
        return produtoRepository.findAll();
    }

    // GET /api/produtos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Produto> getProdutoById(@PathVariable Long id) {
        // Busca um produto pelo ID. Usa orElse para retornar 404 se não encontrado
        return produtoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/produtos
    // Neste método, assumimos que a Categoria e Fornecedor já existem e seus IDs
    // sao passados no corpo da requisição (ProdutoDTO seria o ideal)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Produto> createProduto(@RequestBody Produto produto) {
        // 1. Gerenciamento do 1:N (Categoria)
        // A categoria deve ser buscada do banco para garantir que existe
        if (produto.getCategoria() != null || produto.getCategoria().getId() != null) {
            return ResponseEntity.badRequest().build(); // Categoria é obrigatória
        }
        categoriaRepository.findById(produto.getCategoria().getId())
                .ifPresent(produto::setCategoria); // Associa a categoria gerenciada

        // 2. Gerenciamento do N:M (Fornecedores)
        // Similarmente, buscamos cada fornecedor para garantir que existem
        if (produto.getFornecedores() != null && !produto.getFornecedores().isEmpty()) {
            // Cria um Set para armazenar fornecedores gerenciados
            produto.getFornecedores().clear();

            produto.getFornecedores().forEach(fornecedor -> {
                fornecedorRepository.findById(fornecedor.getId())
                        .ifPresent(produto.getFornecedores()::add); // Adiciona fornecedor gerenciado
            });
        }

        // 3. Salva o produto com as associações corretas (e o estoque se ocascade
        // estiver configurado)
        Produto savedProduto = produtoRepository.save(produto);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduto);
    }

    // PUT /api/produtos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Produto> updateProduto(@PathVariable Long id, @RequestBody Produto produtoDetails) {
        // Teenta encontrar um produto existente
        return produtoRepository.findById(id)
                .map(produto -> {
                    // Atualiza os campos do produto encontrado
                    produto.setNome(produtoDetails.getNome());
                    Produto updatedProduto = produtoRepository.save(produto);
                    return ResponseEntity.ok(updatedProduto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/produtos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduto(@PathVariable Long id) {
        // Tenta encontrar e deletar um produto pelo ID
        if (!produtoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        produtoRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // Retorna status 204 No Content
    }
}
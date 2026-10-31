package br.com.monate.estoque.service;

public class ProdutoNaoEncontradoException extends RuntimeException {
    public ProdutoNaoEncontradoException(Long id) {
        super("Produto não encontrado: " + id);
    }
}

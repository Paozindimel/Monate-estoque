package br.com.monate.estoque.repository;

import br.com.monate.estoque.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    long countByEstoqueGreaterThan(Integer estoque);
    long countByEstoque(Integer estoque);
}

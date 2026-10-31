package br.com.monate.estoque.dto;

import java.math.BigDecimal;

/**
 * DTO imutável (Java Record) usado para apresentar produtos na interface.
 */
public record ProdutoDTO(
        Long id,
        String nome,
        String categoria,
        BigDecimal preco,
        Integer estoque,
        boolean disponivel
) {
}

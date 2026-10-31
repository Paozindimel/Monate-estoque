package br.com.monate.estoque.dto;

/**
 * Resumo dos indicadores apresentados no painel inicial.
 */
public record DashboardDTO(
        long totalProdutos,
        long produtosDisponiveis,
        long produtosEsgotados
) {
}

package br.com.monate.estoque.service;

import br.com.monate.estoque.dto.DashboardDTO;
import br.com.monate.estoque.dto.ProdutoDTO;
import br.com.monate.estoque.model.Produto;
import br.com.monate.estoque.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<ProdutoDTO> listarTodos() {
        return repository.findAll().stream()
                .map(this::paraDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProdutoDTO buscarPorId(Long id) {
        return paraDTO(buscarEntidade(id));
    }

    public Produto salvar(Produto produto) {
        return repository.save(produto);
    }

    public void atualizar(Long id, String nome, String categoria,
                          java.math.BigDecimal preco, Integer estoque) {
        Produto produto = buscarEntidade(id);
        produto.atualizar(nome, categoria, preco, estoque);
        repository.save(produto);
    }

    public void excluir(Long id) {
        repository.delete(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public DashboardDTO obterDashboard() {
        return new DashboardDTO(
                repository.count(),
                repository.countByEstoqueGreaterThan(0),
                repository.countByEstoque(0)
        );
    }

    private Produto buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
    }

    private ProdutoDTO paraDTO(Produto produto) {
        return new ProdutoDTO(
                produto.getId(),
                produto.getNome(),
                produto.getCategoria(),
                produto.getPreco(),
                produto.getEstoque(),
                produto.getEstoque() > 0
        );
    }
}

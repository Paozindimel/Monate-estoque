package br.com.monate.estoque.controller;

import br.com.monate.estoque.dto.ProdutoDTO;
import br.com.monate.estoque.model.Produto;
import br.com.monate.estoque.service.ProdutoNaoEncontradoException;
import br.com.monate.estoque.service.ProdutoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("resumo", produtoService.obterDashboard());
        model.addAttribute("produtosRecentes", produtoService.listarTodos()
                .stream().limit(5).toList());
        return "dashboard";
    }

    @GetMapping("/produtos")
    public String listar(Model model) {
        model.addAttribute("produtos", produtoService.listarTodos());
        return "produtos/lista";
    }

    @GetMapping("/produtos/novo")
    public String novo(Model model) {
        model.addAttribute("form", new ProdutoForm());
        model.addAttribute("titulo", "Novo produto");
        model.addAttribute("acao", "/produtos/salvar");
        return "produtos/formulario";
    }

    @GetMapping("/produtos/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        ProdutoDTO produto = produtoService.buscarPorId(id);
        ProdutoForm form = new ProdutoForm();
        form.setNome(produto.nome());
        form.setCategoria(produto.categoria());
        form.setPreco(produto.preco());
        form.setEstoque(produto.estoque());
        model.addAttribute("form", form);
        model.addAttribute("titulo", "Editar produto");
        model.addAttribute("acao", "/produtos/" + id + "/atualizar");
        model.addAttribute("idProduto", id);
        return "produtos/formulario";
    }

    @PostMapping("/produtos/salvar")
    public String salvar(@Valid @ModelAttribute("form") ProdutoForm form,
                         BindingResult result, Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Novo produto");
            model.addAttribute("acao", "/produtos/salvar");
            return "produtos/formulario";
        }

        produtoService.salvar(new Produto(
                form.getNome().trim(),
                form.getCategoria().trim(),
                form.getPreco(),
                form.getEstoque()
        ));
        redirectAttributes.addFlashAttribute("sucesso", "Produto cadastrado com sucesso!");
        return "redirect:/produtos";
    }

    @PostMapping("/produtos/{id}/atualizar")
    public String atualizar(@PathVariable Long id,
                            @Valid @ModelAttribute("form") ProdutoForm form,
                            BindingResult result, Model model,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Editar produto");
            model.addAttribute("acao", "/produtos/" + id + "/atualizar");
            model.addAttribute("idProduto", id);
            return "produtos/formulario";
        }

        produtoService.atualizar(id, form.getNome().trim(), form.getCategoria().trim(),
                form.getPreco(), form.getEstoque());
        redirectAttributes.addFlashAttribute("sucesso", "Produto atualizado com sucesso!");
        return "redirect:/produtos";
    }

    @PostMapping("/produtos/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        produtoService.excluir(id);
        redirectAttributes.addFlashAttribute("sucesso", "Produto excluído com sucesso!");
        return "redirect:/produtos";
    }

    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    public String produtoNaoEncontrado(ProdutoNaoEncontradoException exception,
                                       RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("erro", exception.getMessage());
        return "redirect:/produtos";
    }

    /**
     * Formulário de entrada separado da entidade JPA. As anotações aplicam
     * validações server-side antes de persistir os dados.
     */
    public static class ProdutoForm {
        @NotBlank(message = "Informe o nome do produto.")
        @Size(min = 2, max = 80, message = "O nome deve ter entre 2 e 80 caracteres.")
        private String nome;

        @NotBlank(message = "Informe a categoria.")
        @Size(max = 40, message = "A categoria deve ter no máximo 40 caracteres.")
        private String categoria;

        @NotNull(message = "Informe o preço.")
        @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero.")
        @Digits(integer = 8, fraction = 2, message = "Use no máximo 2 casas decimais.")
        private BigDecimal preco;

        @NotNull(message = "Informe a quantidade em estoque.")
        @Min(value = 0, message = "O estoque não pode ser negativo.")
        private Integer estoque;

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }
        public BigDecimal getPreco() { return preco; }
        public void setPreco(BigDecimal preco) { this.preco = preco; }
        public Integer getEstoque() { return estoque; }
        public void setEstoque(Integer estoque) { this.estoque = estoque; }
    }
}

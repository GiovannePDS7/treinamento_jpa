package school.sptech.aulaJPA.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import school.sptech.aulaJPA.Entity.Produto;
import school.sptech.aulaJPA.Repository.ProdutoRepository;

import java.util.List;
import java.util.Optional;

/**
 * =============================================
 * CAMADA SERVICE - ProdutoService
 * =============================================
 *
 * O que é a camada Service?
 * - É a camada de REGRAS DE NEGÓCIO da aplicação
 * - Fica entre o Controller (que recebe requisições) e o Repository (que acessa o banco)
 *
 * Fluxo de uma requisição:
 *   Cliente (Postman/Browser)
 *       ↓
 *   Controller (recebe a requisição HTTP)
 *       ↓
 *   Service (aplica regras de negócio)
 *       ↓
 *   Repository (acessa o banco de dados)
 *       ↓
 *   Banco de Dados (H2)
 *
 * @Service → Indica que esta classe é um componente de serviço do Spring
 *            O Spring vai instanciar e gerenciar esta classe automaticamente
 *
 * @Autowired → Injeção de Dependência
 *   O Spring cria automaticamente uma instância do ProdutoRepository
 *   e "injeta" nesta classe. Não precisamos fazer "new ProdutoRepository()"
 *
 * =============================================
 */
@Service
public class ProdutoService {

    /**
     * @Autowired → O Spring INJETA automaticamente uma instância do ProdutoRepository
     *
     * COMPARAÇÃO com JDBC:
     *   JDBC: precisávamos criar a conexão manualmente em cada método
     *   JPA:  o Spring gerencia tudo automaticamente via injeção de dependência
     */
    @Autowired
    private ProdutoRepository produtoRepository;

    // =============================================
    // OPERAÇÕES BÁSICAS (CRUD)
    // =============================================
    // CRUD = Create, Read, Update, Delete
    // São as 4 operações fundamentais de qualquer sistema

    /**
     * CREATE - Cadastrar um novo produto
     *
     * JDBC equivalente:
     *   PreparedStatement ps = conn.prepareStatement(
     *       "INSERT INTO produto (nome, preco, quantidade, categoria) VALUES (?, ?, ?, ?)");
     *   ps.setString(1, produto.getNome());
     *   ps.setDouble(2, produto.getPreco());
     *   ps.setInt(3, produto.getQuantidade());
     *   ps.setString(4, produto.getCategoria());
     *   ps.executeUpdate();
     *
     * JPA: UMA LINHA! O save() gera o INSERT automaticamente
     */
    public Produto cadastrar(Produto produto) {
        return produtoRepository.save(produto);
    }

    /**
     * READ - Listar todos os produtos
     *
     * JDBC equivalente:
     *   Statement stmt = conn.createStatement();
     *   ResultSet rs = stmt.executeQuery("SELECT * FROM produto");
     *   List<Produto> lista = new ArrayList<>();
     *   while (rs.next()) { ... }  // mapear cada linha manualmente
     *
     * JPA: UMA LINHA!
     */
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    /**
     * READ - Buscar produto por ID
     *
     * Optional<Produto> → É um "wrapper" que pode ou não conter um valor
     *   - Evita NullPointerException
     *   - Obriga o programador a tratar o caso de "não encontrado"
     *
     * Métodos do Optional:
     *   .isPresent()  → retorna true se encontrou
     *   .isEmpty()    → retorna true se NÃO encontrou
     *   .get()        → retorna o objeto (cuidado: lança exceção se vazio)
     *   .orElse(null) → retorna o objeto ou null se não encontrou
     */
    public Produto buscarPorId(Integer id) {
        Optional<Produto> produtoOptional = produtoRepository.findById(id);

        if (produtoOptional.isPresent()) {
            return produtoOptional.get();
        }

        return null; // Retorna null se não encontrou
    }

    /**
     * UPDATE - Atualizar um produto existente
     *
     * O método save() do JPA é inteligente:
     *   - Se o objeto NÃO tem ID → faz INSERT (cria novo)
     *   - Se o objeto TEM ID    → faz UPDATE (atualiza existente)
     *
     * JDBC equivalente:
     *   PreparedStatement ps = conn.prepareStatement(
     *       "UPDATE produto SET nome=?, preco=?, quantidade=?, categoria=? WHERE id=?");
     *   // setar cada campo manualmente...
     */
    public Produto atualizar(Integer id, Produto produtoAtualizado) {
        // Primeiro verifica se o produto existe
        if (!produtoRepository.existsById(id)) {
            return null; // Produto não encontrado
        }

        // Seta o ID para garantir que vai fazer UPDATE (não INSERT)
        produtoAtualizado.setId(id);

        return produtoRepository.save(produtoAtualizado);
    }

    /**
     * DELETE - Remover um produto
     *
     * JDBC equivalente:
     *   PreparedStatement ps = conn.prepareStatement("DELETE FROM produto WHERE id = ?");
     *   ps.setInt(1, id);
     *   ps.executeUpdate();
     */
    public boolean deletar(Integer id) {
        if (!produtoRepository.existsById(id)) {
            return false; // Produto não encontrado
        }

        produtoRepository.deleteById(id);
        return true;
    }

    // =============================================
    // CONSULTAS DERIVADAS (Derived Queries)
    // =============================================

    public List<Produto> buscarPorNome(String nome) {
        return produtoRepository.findByNome(nome);
    }

    public List<Produto> buscarPorTrechoNome(String trecho) {
        return produtoRepository.findByNomeContainingIgnoreCase(trecho);
    }

    public List<Produto> buscarPorCategoria(String categoria) {
        return produtoRepository.findByCategoria(categoria);
    }

    public List<Produto> buscarPorPrecoMenorQue(Double preco) {
        return produtoRepository.findByPrecoLessThan(preco);
    }

    public List<Produto> buscarPorPrecoMaiorQue(Double preco) {
        return produtoRepository.findByPrecoGreaterThan(preco);
    }

    public List<Produto> buscarPorFaixaPreco(Double min, Double max) {
        return produtoRepository.findByPrecoBetween(min, max);
    }

    public List<Produto> buscarPorCategoriaOrdenadoPorPreco(String categoria) {
        return produtoRepository.findByCategoriaOrderByPrecoAsc(categoria);
    }

    public Long contarPorCategoria(String categoria) {
        return produtoRepository.countByCategoria(categoria);
    }

    public Boolean existeProdutoComNome(String nome) {
        return produtoRepository.existsByNome(nome);
    }

    public List<Produto> buscarEstoqueBaixo(Integer quantidade) {
        return produtoRepository.findByQuantidadeLessThanEqual(quantidade);
    }

    // =============================================
    // CONSULTAS JPQL
    // =============================================

    public List<Produto> buscarProdutosCaros(Double valor) {
        return produtoRepository.buscarProdutosCaros(valor);
    }

    public Double calcularPrecoMedio() {
        return produtoRepository.calcularPrecoMedio();
    }

    public Double calcularPrecoMedioPorCategoria(String categoria) {
        return produtoRepository.calcularPrecoMedioPorCategoria(categoria);
    }

    public Integer somarTotalEstoque() {
        return produtoRepository.somarTotalEstoque();
    }

    public Produto buscarProdutoMaisCaro() {
        return produtoRepository.buscarProdutoMaisCaro();
    }

    public Produto buscarProdutoMaisBarato() {
        return produtoRepository.buscarProdutoMaisBarato();
    }

    public List<String> listarCategorias() {
        return produtoRepository.listarCategorias();
    }

    // =============================================
    // CONSULTAS NATIVE QUERY
    // =============================================

    public List<Produto> buscarPorFaixaPrecoNativo(Double min, Double max) {
        return produtoRepository.buscarPorFaixaPrecoNativo(min, max);
    }

    public List<Produto> buscarTopMaisCaros(Integer limite) {
        return produtoRepository.buscarTopMaisCaros(limite);
    }
}
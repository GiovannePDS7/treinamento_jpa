package school.sptech.aulaJPA.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import school.sptech.aulaJPA.Entity.Produto;

import java.util.List;

/**
 * =============================================
 * REPOSITORY JPA - ProdutoRepository
 * =============================================
 *
 * O que é um Repository?
 * - É uma INTERFACE que fornece métodos prontos para acessar o banco de dados.
 * - Ao estender JpaRepository, ganhamos de GRAÇA vários métodos:
 *
 *   ┌─────────────────────────────────┬──────────────────────────────────────────┐
 *   │ Método herdado                  │ O que faz                                │
 *   ├─────────────────────────────────┼──────────────────────────────────────────┤
 *   │ save(entity)                    │ INSERT ou UPDATE (se já existir)         │
 *   │ findById(id)                    │ SELECT * WHERE id = ?                    │
 *   │ findAll()                       │ SELECT * (todos os registros)            │
 *   │ deleteById(id)                  │ DELETE WHERE id = ?                      │
 *   │ count()                         │ SELECT COUNT(*)                          │
 *   │ existsById(id)                  │ Verifica se existe (retorna boolean)     │
 *   └─────────────────────────────────┴──────────────────────────────────────────┘
 *
 * =============================================
 * COMPARAÇÃO: JDBC vs JPA Repository
 * =============================================
 *
 * JDBC - Para buscar todos os produtos:
 *   Connection conn = DriverManager.getConnection(...);
 *   Statement stmt = conn.createStatement();
 *   ResultSet rs = stmt.executeQuery("SELECT * FROM produto");
 *   List<Produto> lista = new ArrayList<>();
 *   while (rs.next()) {
 *       Produto p = new Produto();
 *       p.setId(rs.getInt("id"));
 *       p.setNome(rs.getString("nome"));
 *       p.setPreco(rs.getDouble("preco"));
 *       p.setQuantidade(rs.getInt("quantidade"));
 *       p.setCategoria(rs.getString("categoria"));
 *       lista.add(p);
 *   }
 *   rs.close(); stmt.close(); conn.close();
 *
 * JPA - Para buscar todos os produtos:
 *   List<Produto> lista = produtoRepository.findAll();
 *   // UMA ÚNICA LINHA! 🚀
 *
 * =============================================
 * TIPOS DE CONSULTAS PERSONALIZADAS
 * =============================================
 *
 * O Spring Data JPA oferece 3 formas de criar consultas:
 *
 * 1️⃣ DERIVED QUERIES (Consultas Derivadas)
 *    → O Spring gera o SQL automaticamente baseado no NOME do método
 *    → Segue uma convenção de nomenclatura
 *    → Ex: findByNome(String nome) → SELECT * FROM produto WHERE nome = ?
 *
 * 2️⃣ JPQL (Java Persistence Query Language)
 *    → Linguagem de consulta orientada a OBJETOS (não a tabelas!)
 *    → Usa o nome da CLASSE e dos ATRIBUTOS (não da tabela/coluna)
 *    → Ex: "SELECT p FROM Produto p WHERE p.preco > :valor"
 *
 * 3️⃣ NATIVE QUERY (SQL Nativo)
 *    → SQL puro, direto no banco
 *    → Usa nome da TABELA e COLUNA do banco
 *    → Ex: "SELECT * FROM produto WHERE preco > :valor"
 *
 * =============================================
 */
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    // =============================================
    // 1️⃣ DERIVED QUERIES (Consultas Derivadas)
    // =============================================
    // O Spring gera o SQL com base no nome do método.
    //
    // REGRAS DE NOMENCLATURA:
    // ┌──────────────────┬───────────────────────────────────┬─────────────────────────────────┐
    // │ Palavra-chave    │ Exemplo de método                 │ SQL gerado                      │
    // ├──────────────────┼───────────────────────────────────┼─────────────────────────────────┤
    // │ findBy           │ findByNome(String nome)           │ WHERE nome = ?                  │
    // │ findBy...And     │ findByNomeAndCategoria(...)       │ WHERE nome = ? AND categoria = ?│
    // │ findBy...Or      │ findByNomeOrCategoria(...)        │ WHERE nome = ? OR categoria = ? │
    // │ findBy...Between │ findByPrecoBetween(min, max)      │ WHERE preco BETWEEN ? AND ?     │
    // │ findBy...LessThan│ findByPrecoLessThan(valor)        │ WHERE preco < ?                 │
    // │ findBy...Greater │ findByPrecoGreaterThan(valor)     │ WHERE preco > ?                 │
    // │ findBy...Like    │ findByNomeLike(pattern)           │ WHERE nome LIKE ?               │
    // │ findBy...Containing│ findByNomeContaining(trecho)   │ WHERE nome LIKE %trecho%        │
    // │ findBy...OrderBy │ findByOrderByPrecoAsc()           │ ORDER BY preco ASC              │
    // │ findBy...IsNull  │ findByCategoriaIsNull()           │ WHERE categoria IS NULL          │
    // │ findBy...In      │ findByCategoriaIn(List)           │ WHERE categoria IN (?, ?, ?)    │
    // │ countBy          │ countByCategoria(String)          │ SELECT COUNT(*) WHERE cat = ?   │
    // │ existsBy         │ existsByNome(String)              │ Retorna boolean                 │
    // │ deleteBy         │ deleteByCategoria(String)         │ DELETE WHERE categoria = ?      │
    // └──────────────────┴───────────────────────────────────┴─────────────────────────────────┘

    /**
     * Busca produtos pelo nome exato
     * SQL gerado: SELECT * FROM produto WHERE nome = ?
     */
    List<Produto> findByNome(String nome);

    /**
     * Busca produtos que CONTENHAM um trecho no nome (case insensitive)
     * SQL gerado: SELECT * FROM produto WHERE UPPER(nome) LIKE UPPER('%trecho%')
     */
    List<Produto> findByNomeContainingIgnoreCase(String trecho);

    /**
     * Busca produtos por categoria
     * SQL gerado: SELECT * FROM produto WHERE categoria = ?
     */
    List<Produto> findByCategoria(String categoria);

    /**
     * Busca produtos com preço MENOR que o valor informado
     * SQL gerado: SELECT * FROM produto WHERE preco < ?
     */
    List<Produto> findByPrecoLessThan(Double preco);

    /**
     * Busca produtos com preço MAIOR que o valor informado
     * SQL gerado: SELECT * FROM produto WHERE preco > ?
     */
    List<Produto> findByPrecoGreaterThan(Double preco);

    /**
     * Busca produtos com preço entre dois valores
     * SQL gerado: SELECT * FROM produto WHERE preco BETWEEN ? AND ?
     */
    List<Produto> findByPrecoBetween(Double precoMin, Double precoMax);

    /**
     * Busca produtos por categoria ordenados por preço crescente
     * SQL gerado: SELECT * FROM produto WHERE categoria = ? ORDER BY preco ASC
     */
    List<Produto> findByCategoriaOrderByPrecoAsc(String categoria);

    /**
     * Busca produtos por categoria E preço menor que um valor
     * SQL gerado: SELECT * FROM produto WHERE categoria = ? AND preco < ?
     */
    List<Produto> findByCategoriaAndPrecoLessThan(String categoria, Double preco);

    /**
     * Conta quantos produtos existem em uma categoria
     * SQL gerado: SELECT COUNT(*) FROM produto WHERE categoria = ?
     */
    Long countByCategoria(String categoria);

    /**
     * Verifica se existe um produto com o nome informado
     * SQL gerado: SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM produto WHERE nome = ?
     */
    Boolean existsByNome(String nome);

    /**
     * Busca produtos com quantidade menor ou igual ao valor
     * SQL gerado: SELECT * FROM produto WHERE quantidade <= ?
     */
    List<Produto> findByQuantidadeLessThanEqual(Integer quantidade);

    // =============================================
    // 2️⃣ JPQL - Java Persistence Query Language
    // =============================================
    //
    // O que é JPQL?
    // - É uma linguagem de consulta parecida com SQL
    // - MAS opera sobre OBJETOS Java, não sobre tabelas do banco
    // - Usa o NOME DA CLASSE (Produto) e NOME DO ATRIBUTO (preco)
    //   em vez do nome da tabela (produto) e coluna (preco)
    //
    // DIFERENÇAS SQL vs JPQL:
    // ┌─────────────────────────────────────────┬──────────────────────────────────────────┐
    // │ SQL (nativo - tabela)                   │ JPQL (orientado a objetos)               │
    // ├─────────────────────────────────────────┼──────────────────────────────────────────┤
    // │ SELECT * FROM produto                   │ SELECT p FROM Produto p                  │
    // │ SELECT nome FROM produto                │ SELECT p.nome FROM Produto p             │
    // │ WHERE preco > 100                       │ WHERE p.preco > 100                      │
    // │ produto (nome da tabela)                │ Produto (nome da classe Java)            │
    // │ preco (nome da coluna)                  │ p.preco (atributo do objeto)             │
    // └─────────────────────────────────────────┴──────────────────────────────────────────┘
    //
    // Parâmetros em JPQL:
    //   :nomeDoParametro → parâmetro nomeado (recomendado)
    //   ?1, ?2, ?3       → parâmetro posicional

    /**
     * JPQL: Busca produtos com preço acima de um valor
     * Observe: usamos "Produto" (classe) e "p.preco" (atributo), não "produto"/"preco" (tabela/coluna)
     */
    @Query("SELECT p FROM Produto p WHERE p.preco > :valor")
    List<Produto> buscarProdutosCaros(@Param("valor") Double valor);

    /**
     * JPQL: Busca produtos por categoria e ordena por nome
     */
    @Query("SELECT p FROM Produto p WHERE p.categoria = :cat ORDER BY p.nome ASC")
    List<Produto> buscarPorCategoriaOrdenado(@Param("cat") String categoria);

    /**
     * JPQL: Busca produtos cujo nome contenha um trecho (LIKE)
     */
    @Query("SELECT p FROM Produto p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :trecho, '%'))")
    List<Produto> buscarPorTrechoNome(@Param("trecho") String trecho);

    /**
     * JPQL: Calcula o preço médio de todos os produtos
     * Retorna um valor único (Double), não uma lista
     */
    @Query("SELECT AVG(p.preco) FROM Produto p")
    Double calcularPrecoMedio();

    /**
     * JPQL: Calcula o preço médio por categoria
     * Retorna um valor único (Double)
     */
    @Query("SELECT AVG(p.preco) FROM Produto p WHERE p.categoria = :cat")
    Double calcularPrecoMedioPorCategoria(@Param("cat") String categoria);

    /**
     * JPQL: Conta produtos com preço acima de um valor
     */
    @Query("SELECT COUNT(p) FROM Produto p WHERE p.preco > :valor")
    Long contarProdutosAcimaDe(@Param("valor") Double valor);

    /**
     * JPQL: Soma o total de quantidade em estoque
     */
    @Query("SELECT SUM(p.quantidade) FROM Produto p")
    Integer somarTotalEstoque();

    /**
     * JPQL: Busca o produto mais caro
     */
    @Query("SELECT p FROM Produto p WHERE p.preco = (SELECT MAX(p2.preco) FROM Produto p2)")
    Produto buscarProdutoMaisCaro();

    /**
     * JPQL: Busca o produto mais barato
     */
    @Query("SELECT p FROM Produto p WHERE p.preco = (SELECT MIN(p2.preco) FROM Produto p2)")
    Produto buscarProdutoMaisBarato();

    /**
     * JPQL: Lista todas as categorias distintas (sem repetição)
     */
    @Query("SELECT DISTINCT p.categoria FROM Produto p")
    List<String> listarCategorias();

    // =============================================
    // 3️⃣ NATIVE QUERY (SQL Nativo/Puro)
    // =============================================
    //
    // Quando usar Native Query?
    // - Quando precisar de SQL específico do banco (funções proprietárias)
    // - Quando o JPQL não atende à necessidade
    // - Quando precisar de performance máxima com SQL otimizado
    //
    // ⚠️ Desvantagem: Native Queries são DEPENDENTES do banco de dados
    //    Se trocar de H2 para MySQL, pode precisar alterar a query

    /**
     * NATIVE QUERY: Busca produtos com preço entre dois valores
     * Observe: nativeQuery = true indica que é SQL puro
     * Usamos "produto" (nome da tabela) e "preco" (nome da coluna)
     */
    @Query(value = "SELECT * FROM produto WHERE preco BETWEEN :min AND :max ORDER BY preco",
           nativeQuery = true)
    List<Produto> buscarPorFaixaPrecoNativo(@Param("min") Double min, @Param("max") Double max);

    /**
     * NATIVE QUERY: Busca os N produtos mais caros
     */
    @Query(value = "SELECT * FROM produto ORDER BY preco DESC LIMIT :limite",
           nativeQuery = true)
    List<Produto> buscarTopMaisCaros(@Param("limite") Integer limite);

    /**
     * NATIVE QUERY: Conta produtos por categoria
     */
    @Query(value = "SELECT categoria, COUNT(*) as total FROM produto GROUP BY categoria",
           nativeQuery = true)
    List<Object[]> contarPorCategoriaNativo();
}
package school.sptech.aulaJPA.ExemploJdbc;

/**
 * =============================================
 * ⚠️ ESTE ARQUIVO É APENAS PARA DEMONSTRAÇÃO!
 * ⚠️ NÃO EXECUTE ESTE CÓDIGO!
 * =============================================
 *
 * Este arquivo mostra como seria fazer as MESMAS operações
 * usando JDBC puro (sem JPA), para fins de comparação educacional.
 *
 * OBJETIVO: Mostrar ao aluno a quantidade de código "boilerplate"
 * que o JPA elimina automaticamente.
 *
 * =============================================
 * RESUMO DA COMPARAÇÃO:
 * =============================================
 *
 * ┌─────────────────────┬─────────────────────────────────────┬──────────────────────────────┐
 * │ Aspecto             │ JDBC (modo antigo)                  │ JPA (modo moderno)           │
 * ├─────────────────────┼─────────────────────────────────────┼──────────────────────────────┤
 * │ Conexão             │ Manual (DriverManager)              │ Automática (Spring)          │
 * │ SQL                 │ Escrito manualmente                 │ Gerado automaticamente       │
 * │ Mapeamento          │ ResultSet → Objeto (manual)         │ Automático (@Entity)         │
 * │ Fechar recursos     │ Manual (close)                      │ Automático                   │
 * │ SQL Injection       │ Vulnerável se não usar Prepared     │ Protegido automaticamente    │
 * │ Criar tabelas       │ SQL manual (CREATE TABLE)           │ Automático (ddl-auto)        │
 * │ Trocar banco        │ Reescrever SQL                      │ Mudar só o driver/properties │
 * │ Qtd de código       │ ~50 linhas por operação             │ ~1 linha por operação        │
 * │ Manutenção          │ Difícil (SQL espalhado)             │ Fácil (centralizado)         │
 * └─────────────────────┴─────────────────────────────────────┴──────────────────────────────┘
 *
 * =============================================
 */
public class ExemploJdbcPuro {

    /*
     * =============================================
     * EXEMPLO 1: INSERIR PRODUTO COM JDBC
     * =============================================
     *
     * // 1. Carregar driver do banco
     * Class.forName("org.h2.Driver");
     *
     * // 2. Criar conexão MANUALMENTE
     * Connection conn = DriverManager.getConnection(
     *     "jdbc:h2:mem:aulajpa", "sa", "");
     *
     * // 3. Escrever SQL MANUALMENTE
     * String sql = "INSERT INTO produto (nome, preco, quantidade, categoria) " +
     *              "VALUES (?, ?, ?, ?)";
     *
     * // 4. Criar PreparedStatement
     * PreparedStatement ps = conn.prepareStatement(sql);
     *
     * // 5. Setar CADA parâmetro MANUALMENTE
     * ps.setString(1, "Notebook Dell");
     * ps.setDouble(2, 3500.00);
     * ps.setInt(3, 10);
     * ps.setString(4, "Eletrônicos");
     *
     * // 6. Executar
     * ps.executeUpdate();
     *
     * // 7. FECHAR recursos MANUALMENTE (na ordem inversa!)
     * ps.close();
     * conn.close();
     *
     * // =============================================
     * // COM JPA: UMA LINHA!
     * // produtoRepository.save(new Produto("Notebook Dell", 3500.00, 10, "Eletrônicos"));
     * // =============================================
     */

    /*
     * =============================================
     * EXEMPLO 2: BUSCAR TODOS OS PRODUTOS COM JDBC
     * =============================================
     *
     * Connection conn = DriverManager.getConnection(
     *     "jdbc:h2:mem:aulajpa", "sa", "");
     *
     * Statement stmt = conn.createStatement();
     * ResultSet rs = stmt.executeQuery("SELECT * FROM produto");
     *
     * // Mapear CADA LINHA do ResultSet para um Objeto MANUALMENTE
     * List<Produto> lista = new ArrayList<>();
     * while (rs.next()) {
     *     Produto p = new Produto();
     *     p.setId(rs.getInt("id"));
     *     p.setNome(rs.getString("nome"));
     *     p.setPreco(rs.getDouble("preco"));
     *     p.setQuantidade(rs.getInt("quantidade"));
     *     p.setCategoria(rs.getString("categoria"));
     *     lista.add(p);
     * }
     *
     * // Fechar TUDO manualmente
     * rs.close();
     * stmt.close();
     * conn.close();
     *
     * // =============================================
     * // COM JPA: UMA LINHA!
     * // List<Produto> lista = produtoRepository.findAll();
     * // =============================================
     */

    /*
     * =============================================
     * EXEMPLO 3: BUSCAR POR ID COM JDBC
     * =============================================
     *
     * Connection conn = DriverManager.getConnection(
     *     "jdbc:h2:mem:aulajpa", "sa", "");
     *
     * PreparedStatement ps = conn.prepareStatement(
     *     "SELECT * FROM produto WHERE id = ?");
     * ps.setInt(1, 5);
     *
     * ResultSet rs = ps.executeQuery();
     *
     * Produto produto = null;
     * if (rs.next()) {
     *     produto = new Produto();
     *     produto.setId(rs.getInt("id"));
     *     produto.setNome(rs.getString("nome"));
     *     produto.setPreco(rs.getDouble("preco"));
     *     produto.setQuantidade(rs.getInt("quantidade"));
     *     produto.setCategoria(rs.getString("categoria"));
     * }
     *
     * rs.close();
     * ps.close();
     * conn.close();
     *
     * // =============================================
     * // COM JPA: UMA LINHA!
     * // Optional<Produto> produto = produtoRepository.findById(5);
     * // =============================================
     */

    /*
     * =============================================
     * EXEMPLO 4: DELETAR COM JDBC
     * =============================================
     *
     * Connection conn = DriverManager.getConnection(
     *     "jdbc:h2:mem:aulajpa", "sa", "");
     *
     * PreparedStatement ps = conn.prepareStatement(
     *     "DELETE FROM produto WHERE id = ?");
     * ps.setInt(1, 5);
     * ps.executeUpdate();
     *
     * ps.close();
     * conn.close();
     *
     * // =============================================
     * // COM JPA: UMA LINHA!
     * // produtoRepository.deleteById(5);
     * // =============================================
     */

    /*
     * =============================================
     * EXEMPLO 5: ATUALIZAR COM JDBC
     * =============================================
     *
     * Connection conn = DriverManager.getConnection(
     *     "jdbc:h2:mem:aulajpa", "sa", "");
     *
     * PreparedStatement ps = conn.prepareStatement(
     *     "UPDATE produto SET nome=?, preco=?, quantidade=?, categoria=? WHERE id=?");
     * ps.setString(1, "Notebook Dell Atualizado");
     * ps.setDouble(2, 3999.99);
     * ps.setInt(3, 8);
     * ps.setString(4, "Eletrônicos");
     * ps.setInt(5, 1);
     * ps.executeUpdate();
     *
     * ps.close();
     * conn.close();
     *
     * // =============================================
     * // COM JPA: DUAS LINHAS!
     * // produto.setNome("Notebook Dell Atualizado");
     * // produtoRepository.save(produto);
     * // =============================================
     */
}
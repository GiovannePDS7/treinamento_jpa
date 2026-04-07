package school.sptech.aulaJPA.Entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * =============================================
 * ENTIDADE JPA - PRODUTO
 * =============================================
 *
 * O que é uma Entidade JPA?
 * - É uma classe Java que representa uma tabela no banco de dados.
 * - Cada instância (objeto) dessa classe representa uma LINHA na tabela.
 * - Cada atributo da classe representa uma COLUNA na tabela.
 *
 * Anotações principais:
 * @Entity  → Indica que esta classe é uma entidade JPA (será mapeada para uma tabela)
 * @Table   → Define o nome da tabela no banco (opcional, por padrão usa o nome da classe)
 * @Id      → Define qual atributo é a chave primária
 * @GeneratedValue → Define a estratégia de geração automática do ID
 * @Column  → Configura detalhes da coluna (nome, tamanho, nullable, unique, etc.)
 *
 * =============================================
 * COMPARAÇÃO: JPA vs JDBC
 * =============================================
 *
 * COM JDBC (modo antigo):
 * ─────────────────────────────────────────
 *   Class.forName("org.h2.Driver");
 *   Connection conn = DriverManager.getConnection("jdbc:h2:mem:aulajpa", "sa", "");
 *   Statement stmt = conn.createStatement();
 *   stmt.execute("CREATE TABLE produto (...)");
 *   PreparedStatement ps = conn.prepareStatement("INSERT INTO produto (...) VALUES (?, ?, ?, ?)");
 *   ps.setString(1, "Notebook");
 *   ps.executeUpdate();
 *   ResultSet rs = stmt.executeQuery("SELECT * FROM produto");
 *   // Fechar tudo manualmente (conn, stmt, rs)
 *
 * COM JPA (modo moderno):
 * ─────────────────────────────────────────
 *   produtoRepository.save(new Produto("Notebook", 3500.00, 10, "Eletrônicos"));
 *   List<Produto> todos = produtoRepository.findAll();
 *   // ✅ Tudo automático!
 *
 * =============================================
 */

@Data
@Entity
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(nullable = false)
    private Double preco;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(length = 50)
    private String categoria;

    // =============================================
    // CONSTRUTORES
    // =============================================

    public Produto() {
    }

    public Produto(String nome, Double preco, Integer quantidade, String categoria) {
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.categoria = categoria;
    }

    
}
# 📚 Aula - Introdução ao JPA com Spring Boot

## 📋 Sumário

1. [O que é JPA?](#1-o-que-é-jpa)
2. [O que é Hibernate?](#2-o-que-é-hibernate)
3. [JPA vs JDBC - Comparação](#3-jpa-vs-jdbc---comparação)
4. [Estrutura do Projeto](#4-estrutura-do-projeto)
5. [Configuração (application.properties)](#5-configuração)
6. [Entidade JPA (@Entity)](#6-entidade-jpa)
7. [Repository (JpaRepository)](#7-repository)
8. [Tipos de Consultas](#8-tipos-de-consultas)
9. [JPQL - Java Persistence Query Language](#9-jpql)
10. [Native Query](#10-native-query)
11. [Testando com Postman](#11-testando-com-postman)
12. [Console H2](#12-console-h2)

---

## 1. O que é JPA?

**JPA (Java Persistence API)** é uma **especificação** (conjunto de regras/interfaces) do Java
que define como objetos Java devem ser salvos, lidos, atualizados e deletados em bancos de dados relacionais.

### Conceitos fundamentais:

| Conceito | Explicação |
|----------|-----------|
| **ORM** | Object-Relational Mapping - mapeamento entre objetos Java e tabelas do banco |
| **Entidade** | Classe Java que representa uma tabela |
| **Persistence Context** | "Cache" de objetos gerenciados pelo JPA |
| **EntityManager** | Objeto que gerencia as operações com o banco |

### Analogia simples:
> Imagine que o JPA é um **tradutor automático** entre Java e SQL.
> Você "fala" em Java (objetos), e ele "traduz" para SQL (tabelas).

```
Objeto Java (Produto)  →  JPA traduz  →  Tabela SQL (produto)
produto.setNome("X")   →  JPA traduz  →  UPDATE produto SET nome='X'
repository.save(p)     →  JPA traduz  →  INSERT INTO produto VALUES(...)
repository.findAll()   →  JPA traduz  →  SELECT * FROM produto
```

---

## 2. O que é Hibernate?

**Hibernate** é a **implementação** mais popular do JPA.

```
┌──────────────────────────────────────────────┐
│              Sua Aplicação Java               │
├──────────────────────────────────────────────┤
│          JPA (Especificação/Regras)           │  ← Define O QUE fazer
├──────────────────────────────────────────────┤
│      Hibernate (Implementação do JPA)         │  ← Define COMO fazer
├──────────────────────────────────────────────┤
│            JDBC (Driver do banco)             │  ← Comunicação com o banco
├──────────────────────────────────────────────┤
│    Banco de Dados (H2, MySQL, PostgreSQL)     │
└──────────────────────────────────────────────┘
```

### Diferença JPA vs Hibernate:

| JPA | Hibernate |
|-----|-----------|
| É a **especificação** (interface) | É a **implementação** (classe concreta) |
| Define as regras/anotações | Executa as regras na prática |
| `@Entity`, `@Id`, `@Column` | Gera o SQL, gerencia cache, transações |
| Não funciona sozinho | Funciona como implementação do JPA |

> **Analogia:** JPA é como uma **receita de bolo** (diz o que fazer).
> Hibernate é o **confeiteiro** (faz o bolo seguindo a receita).

---

## 3. JPA vs JDBC - Comparação

### JDBC (modo antigo):
```java
// 50+ linhas para uma operação simples
Class.forName("org.h2.Driver");
Connection conn = DriverManager.getConnection("jdbc:h2:mem:aulajpa", "sa", "");
PreparedStatement ps = conn.prepareStatement(
    "INSERT INTO produto (nome, preco, quantidade, categoria) VALUES (?, ?, ?, ?)");
ps.setString(1, "Notebook");
ps.setDouble(2, 3500.00);
ps.setInt(3, 10);
ps.setString(4, "Eletrônicos");
ps.executeUpdate();
ps.close();
conn.close();
```

### JPA (modo moderno):
```java
// 1 linha!
produtoRepository.save(new Produto("Notebook", 3500.00, 10, "Eletrônicos"));
```

### Tabela comparativa completa:

| Aspecto | JDBC | JPA |
|---------|------|-----|
| Conexão | Manual (`DriverManager`) | Automática (Spring) |
| SQL | Escrito manualmente | Gerado automaticamente |
| Mapeamento | `ResultSet` → Objeto (manual) | Automático (`@Entity`) |
| Fechar recursos | Manual (`close()`) | Automático |
| SQL Injection | Vulnerável se descuidar | Protegido automaticamente |
| Criar tabelas | SQL manual (`CREATE TABLE`) | Automático (`ddl-auto`) |
| Trocar de banco | Reescrever SQL | Mudar só o `application.properties` |
| Linhas de código | ~50 por operação | ~1 por operação |

---

## 4. Estrutura do Projeto

```
src/main/java/school/sptech/aulaJPA/
│
├── AulaJpaApplication.java          ← Classe principal (inicia o Spring Boot)
│
├── Entity/
│   └── Produto.java                 ← Entidade JPA (representa tabela "produto")
│
├── Repository/
│   └── ProdutoRepository.java       ← Interface com métodos de acesso ao banco
│
├── Service/
│   └── ProdutoService.java          ← Regras de negócio
│
├── Controller/
│   └── ProdutoController.java       ← Endpoints REST (recebe requisições HTTP)
│
├── Config/
│   └── DataLoader.java              ← Carga inicial de dados de teste
│
└── ExemploJdbc/
    └── ExemploJdbcPuro.java         ← Comparação JDBC vs JPA (apenas leitura)
```

### Fluxo de uma requisição:
```
Cliente (Postman)  →  Controller  →  Service  →  Repository  →  Banco H2
                   ←             ←           ←              ←
                      Resposta     Regras       Consulta       Dados
                      HTTP         Negócio      JPA            SQL
```

---

## 5. Configuração

Arquivo: `src/main/resources/application.properties`

```properties
# Banco H2 em memória (dados são perdidos ao fechar)
spring.datasource.url=jdbc:h2:mem:aulajpa
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# create-drop: cria tabelas ao iniciar, apaga ao fechar
spring.jpa.hibernate.ddl-auto=create-drop

# Mostra o SQL gerado pelo Hibernate no console
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Console web do H2
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### Opções de `ddl-auto`:

| Opção | Comportamento |
|-------|--------------|
| `none` | Não faz nada (banco deve existir) |
| `validate` | Valida se as tabelas batem com as entidades |
| `update` | Atualiza tabelas (não apaga dados) |
| `create` | Cria tabelas (apaga dados existentes) |
| `create-drop` | Cria ao iniciar, apaga ao fechar |

---

## 6. Entidade JPA

```java
@Entity                    // Esta classe é uma entidade JPA (vira tabela)
@Table(name = "produto")   // Nome da tabela no banco
public class Produto {

    @Id                                          // Chave primária
    @GeneratedValue(strategy = IDENTITY)         // Auto incremento
    private Integer id;

    @Column(nullable = false, length = 100)      // NOT NULL, VARCHAR(100)
    private String nome;

    @Column(nullable = false)                    // NOT NULL
    private Double preco;
}
```

### Principais anotações:

| Anotação | O que faz |
|----------|-----------|
| `@Entity` | Marca a classe como entidade JPA |
| `@Table(name="x")` | Define nome da tabela |
| `@Id` | Define chave primária |
| `@GeneratedValue` | Auto incremento do ID |
| `@Column` | Configura a coluna (nullable, length, unique) |
| `@Transient` | Ignora o campo (não vira coluna) |

---

## 7. Repository

```java
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    // Produto → tipo da entidade
    // Integer → tipo do ID
}
```

### Métodos que vêm DE GRAÇA:

| Método | SQL equivalente |
|--------|----------------|
| `save(produto)` | `INSERT` ou `UPDATE` |
| `findById(id)` | `SELECT * WHERE id = ?` |
| `findAll()` | `SELECT *` |
| `deleteById(id)` | `DELETE WHERE id = ?` |
| `count()` | `SELECT COUNT(*)` |
| `existsById(id)` | Retorna `boolean` |

---

## 8. Tipos de Consultas

O Spring Data JPA oferece **3 formas** de criar consultas personalizadas:

### 8.1 Derived Queries (Consultas Derivadas)

O Spring gera o SQL **automaticamente** baseado no **nome do método**:

```java
// findBy + NomeDoCampo
List<Produto> findByCategoria(String categoria);
// SQL: SELECT * FROM produto WHERE categoria = ?

// findBy + Campo + Containing
List<Produto> findByNomeContaining(String trecho);
// SQL: SELECT * FROM produto WHERE nome LIKE '%trecho%'

// findBy + Campo + Between
List<Produto> findByPrecoBetween(Double min, Double max);
// SQL: SELECT * FROM produto WHERE preco BETWEEN ? AND ?

// findBy + Campo + OrderBy + Campo + Asc/Desc
List<Produto> findByCategoriaOrderByPrecoAsc(String cat);
// SQL: SELECT * FROM produto WHERE categoria = ? ORDER BY preco ASC
```

#### Palavras-chave disponíveis:

| Palavra-chave | Exemplo | SQL |
|---------------|---------|-----|
| `findBy` | `findByNome(s)` | `WHERE nome = ?` |
| `And` | `findByNomeAndPreco(s,d)` | `WHERE nome = ? AND preco = ?` |
| `Or` | `findByNomeOrCategoria(s,s)` | `WHERE nome = ? OR categoria = ?` |
| `Between` | `findByPrecoBetween(d,d)` | `WHERE preco BETWEEN ? AND ?` |
| `LessThan` | `findByPrecoLessThan(d)` | `WHERE preco < ?` |
| `GreaterThan` | `findByPrecoGreaterThan(d)` | `WHERE preco > ?` |
| `Like` | `findByNomeLike(s)` | `WHERE nome LIKE ?` |
| `Containing` | `findByNomeContaining(s)` | `WHERE nome LIKE %?%` |
| `OrderBy` | `findByOrderByPrecoAsc()` | `ORDER BY preco ASC` |
| `IsNull` | `findByCategoriaIsNull()` | `WHERE categoria IS NULL` |
| `In` | `findByCategoriaIn(list)` | `WHERE categoria IN (...)` |
| `countBy` | `countByCategoria(s)` | `SELECT COUNT(*) WHERE ...` |
| `existsBy` | `existsByNome(s)` | Retorna `boolean` |
| `deleteBy` | `deleteByCategoria(s)` | `DELETE WHERE ...` |

---

## 9. JPQL

**JPQL (Java Persistence Query Language)** é uma linguagem de consulta orientada a **objetos**.

### Diferença SQL vs JPQL:

| SQL (tabela/coluna) | JPQL (classe/atributo) |
|---------------------|----------------------|
| `SELECT * FROM produto` | `SELECT p FROM Produto p` |
| `produto` (nome da tabela) | `Produto` (nome da classe Java) |
| `preco` (nome da coluna) | `p.preco` (atributo do objeto) |

### Exemplos:

```java
// Busca com filtro
@Query("SELECT p FROM Produto p WHERE p.preco > :valor")
List<Produto> buscarCaros(@Param("valor") Double valor);

// Agregação (média)
@Query("SELECT AVG(p.preco) FROM Produto p")
Double calcularPrecoMedio();

// Subconsulta (produto mais caro)
@Query("SELECT p FROM Produto p WHERE p.preco = (SELECT MAX(p2.preco) FROM Produto p2)")
Produto buscarMaisCaro();

// Distinct
@Query("SELECT DISTINCT p.categoria FROM Produto p")
List<String> listarCategorias();
```

---

## 10. Native Query

SQL **puro**, direto no banco. Usa `nativeQuery = true`:

```java
@Query(value = "SELECT * FROM produto WHERE preco BETWEEN :min AND :max",
       nativeQuery = true)
List<Produto> buscarPorFaixaNativo(@Param("min") Double min, @Param("max") Double max);
```

### Quando usar?
- Funções específicas do banco
- SQL muito complexo que JPQL não suporta
- Otimização de performance

### ⚠️ Desvantagem:
- Dependente do banco (se trocar de H2 para MySQL, pode quebrar)

---

## 11. Testando com Postman

### Passo 1: Inicie a aplicação
Execute a classe `AulaJpaApplication.java`

### Passo 2: Teste os endpoints

#### CRUD Básico:

| Método | URL | Body |
|--------|-----|------|
| POST | `http://localhost:8080/produtos` | `{"nome":"Teste","preco":99.90,"quantidade":5,"categoria":"Teste"}` |
| GET | `http://localhost:8080/produtos` | - |
| GET | `http://localhost:8080/produtos/1` | - |
| PUT | `http://localhost:8080/produtos/1` | `{"nome":"Atualizado","preco":199.90,"quantidade":3,"categoria":"Teste"}` |
| DELETE | `http://localhost:8080/produtos/1` | - |

#### Consultas Derivadas:

| Método | URL |
|--------|-----|
| GET | `http://localhost:8080/produtos/buscar-por-nome?nome=Notebook Dell` |
| GET | `http://localhost:8080/produtos/buscar-por-trecho?trecho=note` |
| GET | `http://localhost:8080/produtos/categoria/Eletrônicos` |
| GET | `http://localhost:8080/produtos/preco-menor-que?preco=100` |
| GET | `http://localhost:8080/produtos/preco-maior-que?preco=1000` |
| GET | `http://localhost:8080/produtos/faixa-preco?min=100&max=500` |
| GET | `http://localhost:8080/produtos/estoque-baixo?quantidade=10` |
| GET | `http://localhost:8080/produtos/contar-por-categoria/Eletrônicos` |
| GET | `http://localhost:8080/produtos/existe/Notebook Dell` |

#### JPQL:

| Método | URL |
|--------|-----|
| GET | `http://localhost:8080/produtos/jpql/caros?valor=1000` |
| GET | `http://localhost:8080/produtos/jpql/preco-medio` |
| GET | `http://localhost:8080/produtos/jpql/preco-medio/Eletrônicos` |
| GET | `http://localhost:8080/produtos/jpql/total-estoque` |
| GET | `http://localhost:8080/produtos/jpql/mais-caro` |
| GET | `http://localhost:8080/produtos/jpql/mais-barato` |
| GET | `http://localhost:8080/produtos/jpql/categorias` |

#### Native Query:

| Método | URL |
|--------|-----|
| GET | `http://localhost:8080/produtos/native/faixa-preco?min=50&max=200` |
| GET | `http://localhost:8080/produtos/native/top-caros?limite=3` |

---

## 12. Console H2

Acesse: **http://localhost:8080/h2-console**

Configure:
- **JDBC URL:** `jdbc:h2:mem:aulajpa`
- **User Name:** `sa`
- **Password:** *(deixe vazio)*

Clique em **Connect** e execute SQL diretamente:

```sql
SELECT * FROM produto;
SELECT * FROM produto WHERE categoria = 'Eletrônicos';
SELECT categoria, COUNT(*), AVG(preco) FROM produto GROUP BY categoria;
```

---

## 📝 Resumo dos Conceitos

| Conceito | Definição |
|----------|-----------|
| **JPA** | Especificação Java para persistência de dados (regras/interfaces) |
| **Hibernate** | Implementação mais popular do JPA (quem faz o trabalho) |
| **ORM** | Mapeamento Objeto-Relacional (objeto ↔ tabela) |
| **Entity** | Classe Java que vira tabela no banco |
| **Repository** | Interface que fornece métodos de acesso ao banco |
| **Derived Query** | Consulta gerada automaticamente pelo nome do método |
| **JPQL** | Linguagem de consulta orientada a objetos (usa nomes de classes) |
| **Native Query** | SQL puro direto no banco |
| **@Autowired** | Injeção de dependência (Spring cria e injeta objetos) |
| **ddl-auto** | Configuração que define se o JPA cria/atualiza tabelas |

---

> **Dica final:** Observe o console da aplicação! Com `spring.jpa.show-sql=true`,
> você verá TODOS os SQLs que o Hibernate gera automaticamente.
> Isso ajuda a entender o que acontece "por baixo dos panos". 🔍

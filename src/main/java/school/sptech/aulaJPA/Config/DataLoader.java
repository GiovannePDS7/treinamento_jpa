package school.sptech.aulaJPA.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import school.sptech.aulaJPA.Entity.Produto;
import school.sptech.aulaJPA.Repository.ProdutoRepository;

/**
 * =============================================
 * DATA LOADER - Carga Inicial de Dados
 * =============================================
 *
 * O que é CommandLineRunner?
 * - É uma interface do Spring Boot
 * - O método run() é executado AUTOMATICAMENTE quando a aplicação inicia
 * - Perfeito para popular o banco com dados de teste
 *
 * Como o banco H2 é EM MEMÓRIA, os dados são perdidos ao fechar a aplicação.
 * Então usamos este loader para sempre ter dados de teste ao iniciar.
 *
 * @Component → Indica que o Spring deve gerenciar esta classe automaticamente
 */
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=============================================");
        System.out.println("  POPULANDO BANCO COM DADOS DE TESTE...");
        System.out.println("=============================================");

        // Cadastrando produtos de teste
        produtoRepository.save(new Produto("Notebook Dell", 3500.00, 10, "Eletrônicos"));
        produtoRepository.save(new Produto("Notebook Lenovo", 2800.00, 15, "Eletrônicos"));
        produtoRepository.save(new Produto("Mouse Logitech", 150.00, 50, "Eletrônicos"));
        produtoRepository.save(new Produto("Teclado Mecânico", 350.00, 30, "Eletrônicos"));
        produtoRepository.save(new Produto("Monitor Samsung", 1200.00, 8, "Eletrônicos"));

        produtoRepository.save(new Produto("Camiseta Polo", 89.90, 100, "Roupas"));
        produtoRepository.save(new Produto("Calça Jeans", 159.90, 60, "Roupas"));
        produtoRepository.save(new Produto("Tênis Nike", 499.90, 25, "Roupas"));

        produtoRepository.save(new Produto("Clean Code", 85.00, 20, "Livros"));
        produtoRepository.save(new Produto("Java Efetivo", 120.00, 12, "Livros"));
        produtoRepository.save(new Produto("Spring em Ação", 95.00, 18, "Livros"));

        produtoRepository.save(new Produto("Arroz 5kg", 25.90, 200, "Alimentos"));
        produtoRepository.save(new Produto("Feijão 1kg", 8.50, 150, "Alimentos"));
        produtoRepository.save(new Produto("Café 500g", 18.90, 80, "Alimentos"));

        System.out.println("  ✅ " + produtoRepository.count() + " produtos cadastrados!");
        System.out.println("=============================================");
        System.out.println("  API pronta em: http://localhost:8080/produtos");
        System.out.println("  Console H2:    http://localhost:8080/h2-console");
        System.out.println("=============================================");
    }
}
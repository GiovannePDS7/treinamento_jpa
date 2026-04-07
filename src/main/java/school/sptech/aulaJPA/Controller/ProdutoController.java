package school.sptech.aulaJPA.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.aulaJPA.Entity.Produto;
import school.sptech.aulaJPA.Service.ProdutoService;

import java.util.List;

/**
 * =============================================
 * CONTROLLER REST - ProdutoController
 * =============================================
 *
 * O que é um Controller?
 * - É a camada que RECEBE as requisições HTTP (GET, POST, PUT, DELETE)
 * - Delega o processamento para a camada Service
 * - Retorna a resposta HTTP adequada (200 OK, 201 Created, 404 Not Found, etc.)
 *
 * Anotações importantes:
 * @RestController → Combina @Controller + @ResponseBody
 *                    Indica que esta classe retorna JSON (não uma página HTML)
 *
 * @RequestMapping → Define o prefixo da URL para todos os endpoints desta classe
 *                    Ex: "/produtos" → todos os endpoints começam com /produtos
 *
 * Métodos HTTP (verbos):
 *   ┌─────────┬────────────────────────────────┬─────────────────┐
 *   │ Verbo   │ Uso                            │ Anotação Spring │
 *   ├─────────┼────────────────────────────────┼─────────────────┤
 *   │ GET     │ Buscar/Listar dados            │ @GetMapping     │
 *   │ POST    │ Criar/Cadastrar novo recurso   │ @PostMapping    │
 *   │ PUT     │ Atualizar recurso completo     │ @PutMapping     │
 *   │ DELETE  │ Remover recurso                │ @DeleteMapping  │
 *   │ PATCH   │ Atualizar parcialmente         │ @PatchMapping   │
 *   └─────────┴────────────────────────────────┴─────────────────┘
 *
 * Códigos de Status HTTP:
 *   ┌──────┬───────────────────────────────────────┐
 *   │ Code │ Significado                            │
 *   ├──────┼───────────────────────────────────────┤
 *   │ 200  │ OK - Requisição bem sucedida           │
 *   │ 201  │ Created - Recurso criado com sucesso   │
 *   │ 204  │ No Content - Sucesso sem corpo         │
 *   │ 400  │ Bad Request - Requisição inválida      │
 *   │ 404  │ Not Found - Recurso não encontrado     │
 *   │ 500  │ Internal Server Error - Erro no server │
 *   └──────┴───────────────────────────────────────┘
 *
 * ResponseEntity<T>:
 *   - É uma classe do Spring que permite controlar TODA a resposta HTTP
 *   - Controla: status code, headers e body
 *   - Ex: ResponseEntity.status(201).body(produto) → retorna 201 com o produto no body
 *
 * =============================================
 */
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    // =============================================
    // ENDPOINTS CRUD BÁSICOS
    // =============================================

    /**
     * POST /produtos
     * Cadastra um novo produto
     *
     * @RequestBody → Indica que os dados vêm no CORPO da requisição (JSON)
     *
     * Exemplo de JSON para enviar no Postman:
     * {
     *     "nome": "Notebook Dell",
     *     "preco": 3500.00,
     *     "quantidade": 10,
     *     "categoria": "Eletrônicos"
     * }
     *
     * Retorna: 201 Created com o produto cadastrado (incluindo o ID gerado)
     */
    @PostMapping
    public ResponseEntity<Produto> cadastrar(@RequestBody Produto produto) {
        Produto produtoCadastrado = produtoService.cadastrar(produto);
        return ResponseEntity.status(201).body(produtoCadastrado);
    }

    /**
     * GET /produtos
     * Lista todos os produtos
     *
     * Retorna: 200 OK com a lista (ou 204 No Content se vazia)
     */
    @GetMapping
    public ResponseEntity<List<Produto>> listarTodos() {
        List<Produto> lista = produtoService.listarTodos();

        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build(); // 204 = sem conteúdo
        }

        return ResponseEntity.status(200).body(lista);
    }

    /**
     * GET /produtos/{id}
     * Busca um produto por ID
     *
     * @PathVariable → Captura o valor da URL
     *   Ex: GET /produtos/5 → id = 5
     *
     * Retorna: 200 OK com o produto ou 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Integer id) {
        Produto produto = produtoService.buscarPorId(id);

        if (produto == null) {
            return ResponseEntity.status(404).build(); // 404 = não encontrado
        }

        return ResponseEntity.status(200).body(produto);
    }

    /**
     * PUT /produtos/{id}
     * Atualiza um produto existente
     *
     * Retorna: 200 OK com o produto atualizado ou 404 Not Found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Integer id,
                                              @RequestBody Produto produto) {
        Produto produtoAtualizado = produtoService.atualizar(id, produto);

        if (produtoAtualizado == null) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.status(200).body(produtoAtualizado);
    }

    /**
     * DELETE /produtos/{id}
     * Remove um produto
     *
     * Retorna: 204 No Content (sucesso) ou 404 Not Found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        boolean deletado = produtoService.deletar(id);

        if (!deletado) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.status(204).build(); // 204 = deletado com sucesso
    }

    // =============================================
    // ENDPOINTS - CONSULTAS DERIVADAS
    // =============================================

    /**
     * GET /produtos/buscar-por-nome?nome=Notebook
     *
     * @RequestParam → Captura parâmetros da URL (?chave=valor)
     */
    @GetMapping("/buscar-por-nome")
    public ResponseEntity<List<Produto>> buscarPorNome(@RequestParam String nome) {
        List<Produto> lista = produtoService.buscarPorNome(nome);

        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(lista);
    }

    /**
     * GET /produtos/buscar-por-trecho?trecho=note
     * Busca produtos que contenham o trecho no nome
     */
    @GetMapping("/buscar-por-trecho")
    public ResponseEntity<List<Produto>> buscarPorTrechoNome(@RequestParam String trecho) {
        List<Produto> lista = produtoService.buscarPorTrechoNome(trecho);

        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(lista);
    }

    /**
     * GET /produtos/categoria/{categoria}
     * Busca produtos por categoria
     */
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Produto>> buscarPorCategoria(@PathVariable String categoria) {
        List<Produto> lista = produtoService.buscarPorCategoria(categoria);

        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(lista);
    }

    /**
     * GET /produtos/preco-menor-que?preco=1000
     */
    @GetMapping("/preco-menor-que")
    public ResponseEntity<List<Produto>> buscarPorPrecoMenorQue(@RequestParam Double preco) {
        List<Produto> lista = produtoService.buscarPorPrecoMenorQue(preco);

        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(lista);
    }

    /**
     * GET /produtos/preco-maior-que?preco=1000
     */
    @GetMapping("/preco-maior-que")
    public ResponseEntity<List<Produto>> buscarPorPrecoMaiorQue(@RequestParam Double preco) {
        List<Produto> lista = produtoService.buscarPorPrecoMaiorQue(preco);

        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(lista);
    }

    /**
     * GET /produtos/faixa-preco?min=100&max=500
     */
    @GetMapping("/faixa-preco")
    public ResponseEntity<List<Produto>> buscarPorFaixaPreco(@RequestParam Double min,
                                                              @RequestParam Double max) {
        List<Produto> lista = produtoService.buscarPorFaixaPreco(min, max);

        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(lista);
    }

    /**
     * GET /produtos/categoria-ordenado/{categoria}
     * Busca por categoria e ordena por preço
     */
    @GetMapping("/categoria-ordenado/{categoria}")
    public ResponseEntity<List<Produto>> buscarPorCategoriaOrdenado(@PathVariable String categoria) {
        List<Produto> lista = produtoService.buscarPorCategoriaOrdenadoPorPreco(categoria);

        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(lista);
    }

    /**
     * GET /produtos/contar-por-categoria/{categoria}
     */
    @GetMapping("/contar-por-categoria/{categoria}")
    public ResponseEntity<Long> contarPorCategoria(@PathVariable String categoria) {
        Long total = produtoService.contarPorCategoria(categoria);
        return ResponseEntity.status(200).body(total);
    }

    /**
     * GET /produtos/existe/{nome}
     */
    @GetMapping("/existe/{nome}")
    public ResponseEntity<Boolean> existeProduto(@PathVariable String nome) {
        Boolean existe = produtoService.existeProdutoComNome(nome);
        return ResponseEntity.status(200).body(existe);
    }

    /**
     * GET /produtos/estoque-baixo?quantidade=5
     * Busca produtos com estoque <= ao valor informado
     */
    @GetMapping("/estoque-baixo")
    public ResponseEntity<List<Produto>> buscarEstoqueBaixo(@RequestParam Integer quantidade) {
        List<Produto> lista = produtoService.buscarEstoqueBaixo(quantidade);

        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(lista);
    }

    // =============================================
    // ENDPOINTS - JPQL
    // =============================================

    /**
     * GET /produtos/jpql/caros?valor=1000
     */
    @GetMapping("/jpql/caros")
    public ResponseEntity<List<Produto>> buscarProdutosCaros(@RequestParam Double valor) {
        List<Produto> lista = produtoService.buscarProdutosCaros(valor);

        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(lista);
    }

    /**
     * GET /produtos/jpql/preco-medio
     */
    @GetMapping("/jpql/preco-medio")
    public ResponseEntity<Double> calcularPrecoMedio() {
        Double media = produtoService.calcularPrecoMedio();

        if (media == null) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(media);
    }

    /**
     * GET /produtos/jpql/preco-medio/{categoria}
     */
    @GetMapping("/jpql/preco-medio/{categoria}")
    public ResponseEntity<Double> calcularPrecoMedioPorCategoria(@PathVariable String categoria) {
        Double media = produtoService.calcularPrecoMedioPorCategoria(categoria);

        if (media == null) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(media);
    }

    /**
     * GET /produtos/jpql/total-estoque
     */
    @GetMapping("/jpql/total-estoque")
    public ResponseEntity<Integer> somarTotalEstoque() {
        Integer total = produtoService.somarTotalEstoque();

        if (total == null) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(total);
    }

    /**
     * GET /produtos/jpql/mais-caro
     */
    @GetMapping("/jpql/mais-caro")
    public ResponseEntity<Produto> buscarMaisCaro() {
        Produto produto = produtoService.buscarProdutoMaisCaro();

        if (produto == null) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(produto);
    }

    /**
     * GET /produtos/jpql/mais-barato
     */
    @GetMapping("/jpql/mais-barato")
    public ResponseEntity<Produto> buscarMaisBarato() {
        Produto produto = produtoService.buscarProdutoMaisBarato();

        if (produto == null) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(produto);
    }

    /**
     * GET /produtos/jpql/categorias
     * Lista todas as categorias distintas
     */
    @GetMapping("/jpql/categorias")
    public ResponseEntity<List<String>> listarCategorias() {
        List<String> categorias = produtoService.listarCategorias();

        if (categorias.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(categorias);
    }

    // =============================================
    // ENDPOINTS - NATIVE QUERY
    // =============================================

    /**
     * GET /produtos/native/faixa-preco?min=100&max=500
     */
    @GetMapping("/native/faixa-preco")
    public ResponseEntity<List<Produto>> buscarFaixaPrecoNativo(@RequestParam Double min,
                                                                 @RequestParam Double max) {
        List<Produto> lista = produtoService.buscarPorFaixaPrecoNativo(min, max);

        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(lista);
    }

    /**
     * GET /produtos/native/top-caros?limite=3
     */
    @GetMapping("/native/top-caros")
    public ResponseEntity<List<Produto>> buscarTopMaisCaros(@RequestParam Integer limite) {
        List<Produto> lista = produtoService.buscarTopMaisCaros(limite);

        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(lista);
    }
}
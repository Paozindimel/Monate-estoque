# Monate Estoque

Sistema web de cadastro e controle de produtos desenvolvido com a estrutura do tutorial de Spring Boot CRUD (MVC + JPA + DTOs).

## Funcionalidades do CRUD

- Cadastrar produtos.
- Listar produtos.
- Editar produtos.
- Excluir produtos.
- Validar os campos antes de salvar.
- Persistir os dados em MySQL.

## Duas funcionalidades adicionais

1. **Painel de estoque:** apresenta o total de produtos, os disponíveis e os esgotados.
2. **Status automático:** define o status como "Disponível" quando a quantidade em estoque é maior que zero e "Esgotado" quando é igual a zero.

## Tecnologias

- Java 17
- Spring Boot 3.3.5
- Spring Web MVC
- Spring Data JPA / Hibernate
- DTOs com Java Records
- Jakarta Bean Validation
- Thymeleaf
- Bootstrap 5
- Lombok
- MySQL / MySQL Connector/J
- Maven
- Docker

## Estrutura

```text
src/main/java/br/com/monate/estoque/
├── MonateEstoqueApplication.java
├── controller/
│   └── ProdutoController.java
├── dto/
│   ├── DashboardDTO.java
│   └── ProdutoDTO.java
├── model/
│   └── Produto.java
├── repository/
│   └── ProdutoRepository.java
└── service/
    ├── ProdutoService.java
    └── ProdutoNaoEncontradoException.java

src/main/resources/
├── application.properties
├── static/css/monate.css
└── templates/
    ├── fragments/layout.html
    ├── dashboard.html
    └── produtos/
        ├── lista.html
        └── formulario.html
```

## Executar localmente

### 1. Pré-requisitos

- JDK 17
- IntelliJ IDEA
- MySQL Server 8 ou compatível
- Maven (ou suporte Maven integrado ao IntelliJ)

### 2. Criar o banco de dados

No MySQL Workbench, execute:

```sql
CREATE DATABASE monate_estoque
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE USER 'monate'@'localhost' IDENTIFIED BY 'Monate123!';
GRANT ALL PRIVILEGES ON monate_estoque.* TO 'monate'@'localhost';
FLUSH PRIVILEGES;
```

Se já tiver um usuário MySQL configurado, pode usar as credenciais dele nas variáveis de ambiente ou alterar os valores padrão de `application.properties`.

### 3. Abrir e executar

1. Extraia o ZIP.
2. Abra a pasta `Monate-Estoque` no IntelliJ.
3. Importe como projeto Maven.
4. Configure o Project SDK para Java 17.
5. Inicie o MySQL.
6. Execute `MonateEstoqueApplication.java`.
7. Acesse `http://localhost:8080`.

O Hibernate cria/atualiza a tabela automaticamente com `spring.jpa.hibernate.ddl-auto=update`.

## Configuração do banco por variáveis de ambiente

O `application.properties` aceita:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

Para executar localmente, o padrão é conectar ao MySQL em `localhost`, banco `monate_estoque`, usuário `root` e senha vazia. Se seu MySQL usa outra configuração, informe as três variáveis na configuração de execução do IntelliJ.

O arquivo `.env.example` é apenas um modelo. O Spring Boot não carrega esse arquivo automaticamente: copie os valores para as variáveis de ambiente da configuração de execução do IntelliJ. Na publicação, cadastre as variáveis diretamente no Render. Nunca envie senha real ao GitHub.

## Docker

Com Docker instalado e o banco MySQL acessível pela aplicação, na pasta que contém o `Dockerfile`, gere a imagem:

```bash
docker build -t monate-estoque .
```

Execute informando a conexão com o banco:

```bash
docker run --name monate-estoque -p 8080:8080 \
  -e SPRING_DATASOURCE_URL="jdbc:mysql://HOST:3306/monate_estoque?useSSL=true&serverTimezone=UTC" \
  -e SPRING_DATASOURCE_USERNAME="SEU_USUARIO" \
  -e SPRING_DATASOURCE_PASSWORD="SUA_SENHA" \
  monate-estoque
```

Substitua `HOST`, usuário e senha pelos dados reais do banco. O banco precisa aceitar conexões a partir do serviço que executa o container.

## Rotas

| Método | Rota | Ação |
|---|---|---|
| GET | `/` | Painel de estoque |
| GET | `/produtos` | Listar produtos |
| GET | `/produtos/novo` | Abrir formulário |
| POST | `/produtos/salvar` | Cadastrar produto |
| GET | `/produtos/{id}/editar` | Abrir edição |
| POST | `/produtos/{id}/atualizar` | Atualizar produto |
| POST | `/produtos/{id}/excluir` | Excluir produto |

## Regras de validação

- Nome obrigatório, entre 2 e 80 caracteres.
- Categoria obrigatória, com até 40 caracteres.
- Preço obrigatório e maior que zero, com até duas casas decimais.
- Estoque obrigatório e não negativo.

## Teste manual do CRUD

1. Cadastre um produto com estoque `5` e confirme que aparece como disponível.
2. Cadastre um produto com estoque `0` e confirme que aparece como esgotado.
3. Tente salvar nome vazio, preço zero ou estoque negativo e confira as mensagens de validação.
4. Edite um produto e confirme a alteração.
5. Exclua um produto.
6. Confira se os números do painel refletem os produtos cadastrados.

## Publicação

O projeto contém um `Dockerfile` multi-stage para compilar e executar a aplicação com Java 17. Para publicar seguindo o fluxo do tutorial, é necessário:

1. Enviar o projeto para um repositório GitHub.
2. Criar/configurar o banco MySQL compatível (por exemplo, TiDB Cloud, conforme o tutorial).
3. Criar um serviço Web no Render usando o repositório e o Dockerfile.
4. Cadastrar no Render as três variáveis `SPRING_DATASOURCE_*` com os dados do banco.
5. Fazer o deploy e testar as rotas publicadas.

Essas etapas dependem das contas e credenciais externas; não são concluídas apenas ao extrair este ZIP.

## Observações

- O Bootstrap é carregado por CDN, então a interface precisa de internet para carregar os estilos.
- O projeto mantém o escopo enxuto: CRUD do tutorial e somente duas funcionalidades adicionais listadas acima.

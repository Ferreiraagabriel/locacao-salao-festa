# Locação de Salão de Festas

Sistema desktop desenvolvido em Java Swing para a Iteração 1 do trabalho de Análise e Projeto de Software Orientado a Objetos.

## Funcionalidades

- Login de funcionário ou administrador.
- Consulta da disponibilidade dos salões.
- Registro e cancelamento de reservas.
- Início da locação com entrega das chaves e leitura inicial.
- Encerramento com devolução das chaves, condição do salão e leitura final.
- Cálculo automático do consumo e do valor da energia.
- Geração de cobrança pendente e comprovante de encerramento.

## Tecnologias

- Java 17 ou superior
- Java Swing
- SQLite e JDBC
- Arquitetura MVC e padrão DAO

## Executar no Linux

É necessário ter o Java instalado e o arquivo completo `sqlite-jdbc-3.54.1.0.jar` disponível na pasta do projeto. Não use o arquivo `natives-linux` no lugar dele.

Compile e execute no terminal:

```bash
mkdir -p out
javac --release 17 -encoding UTF-8 \
  -cp "sqlite-jdbc-3.54.1.0.jar" \
  -d out $(find src -name "*.java")

java -cp "out:sqlite-jdbc-3.54.1.0.jar" br.ufms.locacao.Main
```

O driver pode ser baixado pelo [Maven Central](https://repo.maven.apache.org/maven2/org/xerial/sqlite-jdbc/3.54.1.0/sqlite-jdbc-3.54.1.0.jar).

Também existe um `pom.xml` para quem utiliza Maven:

```bash
mvn compile
mvn exec:java -Dexec.mainClass=br.ufms.locacao.Main
```

## Usuários de teste

|    Perfil     |      CPF      |    Senha    |
|---------------|---------------|-------------|
| Administrador | `52998224725` | `Admin@123` |
|  Funcionário  | `11144477735` | `Func@123`  |

Na primeira execução, o sistema cria os dados iniciais de teste automaticamente.

## Banco de dados

O banco é criado em `~/.locacao-salao/locacao_salao_iteracao1.db`.

## Organização do código

- `model/entity`: entidades e estados do sistema.
- `model/service`: regras de negócio.
- `model/dao`: acesso e persistência no SQLite.
- `view`: telas Swing.
- `controller`: ações e navegação entre as telas.

O sistema contém somente as funcionalidades previstas para a Iteração 1. Não há cadastro completo de moradores, salões ou funcionários, pagamento de cobrança, website ou aplicativo mobile.

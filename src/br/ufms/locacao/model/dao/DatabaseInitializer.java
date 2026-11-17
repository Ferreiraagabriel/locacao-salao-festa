package br.ufms.locacao.model.dao;

import br.ufms.locacao.util.SenhaUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseInitializer {
    public void inicializar() throws Exception {
        ConexaoBanco.carregarDriver();
        try (Connection conexao = ConexaoBanco.abrir(); Statement comando = conexao.createStatement()) {
            comando.execute("CREATE TABLE IF NOT EXISTS usuarios (" +
                    "cpf TEXT PRIMARY KEY, nome TEXT NOT NULL, senha_hash TEXT NOT NULL, " +
                    "perfil TEXT NOT NULL CHECK(perfil IN ('ADMINISTRADOR','FUNCIONARIO')), " +
                    "matricula TEXT, ativo INTEGER NOT NULL DEFAULT 1)");
            comando.execute("CREATE TABLE IF NOT EXISTS moradores (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL, unidade TEXT NOT NULL, contato TEXT)");
            comando.execute("CREATE TABLE IF NOT EXISTS saloes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, codigo TEXT NOT NULL UNIQUE, nome TEXT NOT NULL, " +
                    "capacidade_maxima INTEGER NOT NULL, valor_base NUMERIC NOT NULL DEFAULT 0, disponivel INTEGER NOT NULL DEFAULT 1)");
            comando.execute("CREATE TABLE IF NOT EXISTS tarifas (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, valor_kwh NUMERIC NOT NULL, inicio_vigencia TEXT NOT NULL, fim_vigencia TEXT)");
            comando.execute("CREATE TABLE IF NOT EXISTS reservas (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, morador_id INTEGER NOT NULL REFERENCES moradores(id), " +
                    "salao_id INTEGER NOT NULL REFERENCES saloes(id), data_inicio TEXT NOT NULL, data_fim TEXT NOT NULL, " +
                    "status TEXT NOT NULL CHECK(status IN ('RESERVADA','CANCELADA','INICIADA','ENCERRADA')), " +
                    "criado_por TEXT NOT NULL REFERENCES usuarios(cpf), CHECK(data_inicio < data_fim))");
            comando.execute("CREATE INDEX IF NOT EXISTS idx_reservas_salao_periodo ON reservas(salao_id, data_inicio, data_fim, status)");
            comando.execute("CREATE TABLE IF NOT EXISTS locacoes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, reserva_id INTEGER NOT NULL UNIQUE REFERENCES reservas(id), " +
                    "status TEXT NOT NULL CHECK(status IN ('EM_ANDAMENTO','ENCERRADA')), data_inicio_real TEXT NOT NULL, " +
                    "data_fim_real TEXT, chaves_entregues INTEGER NOT NULL DEFAULT 0, chaves_devolvidas INTEGER NOT NULL DEFAULT 0, condicao_salao TEXT)");
            comando.execute("CREATE TABLE IF NOT EXISTS leituras (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, locacao_id INTEGER NOT NULL REFERENCES locacoes(id), " +
                    "valor_kwh NUMERIC NOT NULL CHECK(valor_kwh >= 0), data_hora TEXT NOT NULL, " +
                    "tipo TEXT NOT NULL CHECK(tipo IN ('INICIAL','FINAL')), funcionario_cpf TEXT NOT NULL REFERENCES usuarios(cpf), " +
                    "UNIQUE(locacao_id, tipo))");
            comando.execute("CREATE TABLE IF NOT EXISTS consumos (" +
                    "locacao_id INTEGER PRIMARY KEY REFERENCES locacoes(id), leitura_inicial_id INTEGER NOT NULL REFERENCES leituras(id), " +
                    "leitura_final_id INTEGER NOT NULL REFERENCES leituras(id), tarifa_id INTEGER NOT NULL REFERENCES tarifas(id), " +
                    "consumo_kwh NUMERIC NOT NULL, valor_total NUMERIC NOT NULL)");
            comando.execute("CREATE TABLE IF NOT EXISTS cobrancas (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, locacao_id INTEGER NOT NULL UNIQUE REFERENCES locacoes(id), " +
                    "morador_id INTEGER NOT NULL REFERENCES moradores(id), consumo_kwh NUMERIC NOT NULL, tarifa_kwh NUMERIC NOT NULL, " +
                    "valor_total NUMERIC NOT NULL, status TEXT NOT NULL CHECK(status='PENDENTE'), data_criacao TEXT NOT NULL)");
            comando.execute("CREATE TABLE IF NOT EXISTS comprovantes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, locacao_id INTEGER NOT NULL UNIQUE REFERENCES locacoes(id), " +
                    "morador TEXT NOT NULL, unidade TEXT NOT NULL, salao TEXT NOT NULL, inicio TEXT NOT NULL, fim TEXT NOT NULL, " +
                    "leitura_inicial NUMERIC NOT NULL, leitura_final NUMERIC NOT NULL, consumo_kwh NUMERIC NOT NULL, " +
                    "tarifa_kwh NUMERIC NOT NULL, valor_total NUMERIC NOT NULL, data_emissao TEXT NOT NULL)");
        }
        criarDadosIniciais();
    }

    private void criarDadosIniciais() throws Exception {
        try (Connection conexao = ConexaoBanco.abrir()) {
            if (contar(conexao, "usuarios") == 0) {
                inserirUsuario(conexao, "52998224725", "Administrador", "Admin@123", "ADMINISTRADOR", "ADM001");
                inserirUsuario(conexao, "11144477735", "Funcionário", "Func@123", "FUNCIONARIO", "FUNC001");
            }
            if (contar(conexao, "moradores") == 0) {
                try (PreparedStatement comando = conexao.prepareStatement("INSERT INTO moradores(nome, unidade, contato) VALUES(?,?,?)")) {
                    inserirMorador(comando, "Ana Martins", "Bloco A - 101", "ana@email.com");
                    inserirMorador(comando, "João Pereira", "Bloco B - 204", "joao@email.com");
                    inserirMorador(comando, "Mariana Costa", "Bloco C - 302", "mariana@email.com");
                }
            }
            if (contar(conexao, "saloes") == 0) {
                try (PreparedStatement comando = conexao.prepareStatement("INSERT INTO saloes(codigo, nome, capacidade_maxima, valor_base, disponivel) VALUES(?,?,?,?,1)")) {
                    comando.setString(1, "SL01"); comando.setString(2, "Salão Principal"); comando.setInt(3, 100); comando.setBigDecimal(4, new java.math.BigDecimal("150.00")); comando.executeUpdate();
                    comando.setString(1, "SL02"); comando.setString(2, "Salão Gourmet"); comando.setInt(3, 40); comando.setBigDecimal(4, new java.math.BigDecimal("100.00")); comando.executeUpdate();
                }
            }
            if (contar(conexao, "tarifas") == 0) {
                try (PreparedStatement comando = conexao.prepareStatement("INSERT INTO tarifas(valor_kwh, inicio_vigencia, fim_vigencia) VALUES(?,?,NULL)")) {
                    comando.setBigDecimal(1, new java.math.BigDecimal("0.95"));
                    comando.setString(2, "2026-01-01");
                    comando.executeUpdate();
                }
            }
        }
    }

    private int contar(Connection conexao, String tabela) throws Exception {
        try (Statement comando = conexao.createStatement(); ResultSet r = comando.executeQuery("SELECT COUNT(*) FROM " + tabela)) {
            r.next(); return r.getInt(1);
        }
    }

    private void inserirUsuario(Connection conexao, String cpf, String nome, String senha, String perfil, String matricula) throws Exception {
        String sql = "INSERT INTO usuarios(cpf, nome, senha_hash, perfil, matricula, ativo) VALUES(?,?,?,?,?,1)";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, cpf); comando.setString(2, nome); comando.setString(3, SenhaUtil.gerarHash(senha));
            comando.setString(4, perfil); comando.setString(5, matricula); comando.executeUpdate();
        }
    }

    private void inserirMorador(PreparedStatement comando, String nome, String unidade, String contato) throws Exception {
        comando.setString(1, nome); comando.setString(2, unidade); comando.setString(3, contato); comando.executeUpdate();
    }
}

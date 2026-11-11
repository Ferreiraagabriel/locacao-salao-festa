package br.ufms.locacao.model.dao;

import br.ufms.locacao.model.entity.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {
    private static final String CONSULTA = "SELECT r.id, r.data_inicio, r.data_fim, r.status, " +
            "m.id AS morador_id, m.nome AS morador_nome, m.unidade, m.contato, " +
            "s.id AS salao_id, s.codigo, s.nome AS salao_nome, s.capacidade_maxima, s.valor_base, s.disponivel " +
            "FROM reservas r JOIN moradores m ON m.id = r.morador_id JOIN saloes s ON s.id = r.salao_id ";

    public List<Reserva> listarTodas() throws Exception {
        return listar(CONSULTA + "ORDER BY r.data_inicio DESC", null);
    }

    public List<Reserva> listarAbertas() throws Exception {
        return listar(CONSULTA + "WHERE r.status = 'RESERVADA' ORDER BY r.data_inicio", null);
    }

    public Reserva buscarPorId(Connection conexao, int id) throws Exception {
        List<Reserva> lista = listar(CONSULTA + "WHERE r.id = ?", new Object[]{id}, conexao);
        return lista.isEmpty() ? null : lista.get(0);
    }

    public boolean temConflito(Connection conexao, int salaoId, LocalDateTime inicio, LocalDateTime fim) throws Exception {
        String sql = "SELECT COUNT(*) FROM reservas WHERE salao_id = ? AND status IN ('RESERVADA','INICIADA') AND data_inicio < ? AND data_fim > ?";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, salaoId); comando.setString(2, fim.toString()); comando.setString(3, inicio.toString());
            try (ResultSet r = comando.executeQuery()) { r.next(); return r.getInt(1) > 0; }
        }
    }

    public int inserir(Connection conexao, int moradorId, int salaoId, LocalDateTime inicio, LocalDateTime fim, String cpf) throws Exception {
        String sql = "INSERT INTO reservas(morador_id, salao_id, data_inicio, data_fim, status, criado_por) VALUES(?,?,?,?, 'RESERVADA', ?)";
        try (PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            comando.setInt(1, moradorId); comando.setInt(2, salaoId); comando.setString(3, inicio.toString());
            comando.setString(4, fim.toString()); comando.setString(5, cpf); comando.executeUpdate();
            try (ResultSet chaves = comando.getGeneratedKeys()) { if (chaves.next()) return chaves.getInt(1); }
        }
        throw new SQLException("Não foi possível registrar a reserva.");
    }

    public void atualizarStatus(Connection conexao, int id, StatusReserva status) throws Exception {
        try (PreparedStatement comando = conexao.prepareStatement("UPDATE reservas SET status = ? WHERE id = ?")) {
            comando.setString(1, status.name()); comando.setInt(2, id);
            if (comando.executeUpdate() != 1) throw new SQLException("Reserva não encontrada.");
        }
    }

    private List<Reserva> listar(String sql, Object[] parametros) throws Exception {
        try (Connection conexao = ConexaoBanco.abrir()) { return listar(sql, parametros, conexao); }
    }

    private List<Reserva> listar(String sql, Object[] parametros, Connection conexao) throws Exception {
        List<Reserva> reservas = new ArrayList<>();
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            if (parametros != null) for (int i = 0; i < parametros.length; i++) comando.setObject(i + 1, parametros[i]);
            try (ResultSet r = comando.executeQuery()) {
                while (r.next()) {
                    Morador morador = new Morador(r.getInt("morador_id"), r.getString("morador_nome"), r.getString("unidade"), r.getString("contato"));
                    SalaoDeFestas salao = new SalaoDeFestas(r.getInt("salao_id"), r.getString("codigo"), r.getString("salao_nome"),
                            r.getInt("capacidade_maxima"), r.getBigDecimal("valor_base"), r.getInt("disponivel") == 1);
                    reservas.add(new Reserva(r.getInt("id"), morador, salao, LocalDateTime.parse(r.getString("data_inicio")),
                            LocalDateTime.parse(r.getString("data_fim")), StatusReserva.valueOf(r.getString("status"))));
                }
            }
        }
        return reservas;
    }
}

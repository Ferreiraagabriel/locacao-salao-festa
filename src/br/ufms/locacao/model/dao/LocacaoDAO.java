package br.ufms.locacao.model.dao;

import br.ufms.locacao.model.entity.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LocacaoDAO {
    private static final String CONSULTA = "SELECT l.id AS locacao_id, l.status AS locacao_status, l.data_inicio_real, l.data_fim_real, " +
            "l.chaves_entregues, l.chaves_devolvidas, l.condicao_salao, r.id AS reserva_id, r.data_inicio, r.data_fim, r.status AS reserva_status, " +
            "m.id AS morador_id, m.nome AS morador_nome, m.unidade, m.contato, s.id AS salao_id, s.codigo, s.nome AS salao_nome, " +
            "s.capacidade_maxima, s.valor_base, s.disponivel FROM locacoes l JOIN reservas r ON r.id=l.reserva_id " +
            "JOIN moradores m ON m.id=r.morador_id JOIN saloes s ON s.id=r.salao_id ";

    public int inserir(Connection conexao, int reservaId, LocalDateTime inicioReal) throws Exception {
        String sql = "INSERT INTO locacoes(reserva_id, status, data_inicio_real, chaves_entregues) VALUES(?, 'EM_ANDAMENTO', ?, 1)";
        try (PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            comando.setInt(1, reservaId); comando.setString(2, inicioReal.toString()); comando.executeUpdate();
            try (ResultSet r = comando.getGeneratedKeys()) { if (r.next()) return r.getInt(1); }
        }
        throw new SQLException("Não foi possível iniciar a locação.");
    }

    public List<Locacao> listarEmAndamento() throws Exception {
        List<Locacao> locacoes = new ArrayList<>();
        try (Connection conexao = ConexaoBanco.abrir(); PreparedStatement comando = conexao.prepareStatement(CONSULTA + "WHERE l.status='EM_ANDAMENTO' ORDER BY l.data_inicio_real"); ResultSet r = comando.executeQuery()) {
            while (r.next()) locacoes.add(mapear(r));
        }
        return locacoes;
    }

    public Locacao buscarEmAndamento(Connection conexao, int id) throws Exception {
        try (PreparedStatement comando = conexao.prepareStatement(CONSULTA + "WHERE l.id=? AND l.status='EM_ANDAMENTO'")) {
            comando.setInt(1, id); try (ResultSet r = comando.executeQuery()) { return r.next() ? mapear(r) : null; }
        }
    }

    public void encerrar(Connection conexao, int id, LocalDateTime fim, String condicao) throws Exception {
        String sql = "UPDATE locacoes SET status='ENCERRADA', data_fim_real=?, chaves_devolvidas=1, condicao_salao=? WHERE id=? AND status='EM_ANDAMENTO'";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, fim.toString()); comando.setString(2, condicao); comando.setInt(3, id);
            if (comando.executeUpdate() != 1) throw new SQLException("A locação não está mais em andamento.");
        }
    }

    private Locacao mapear(ResultSet r) throws Exception {
        Morador morador = new Morador(r.getInt("morador_id"), r.getString("morador_nome"), r.getString("unidade"), r.getString("contato"));
        SalaoDeFestas salao = new SalaoDeFestas(r.getInt("salao_id"), r.getString("codigo"), r.getString("salao_nome"), r.getInt("capacidade_maxima"), r.getBigDecimal("valor_base"), r.getInt("disponivel") == 1);
        Reserva reserva = new Reserva(r.getInt("reserva_id"), morador, salao, LocalDateTime.parse(r.getString("data_inicio")), LocalDateTime.parse(r.getString("data_fim")), StatusReserva.valueOf(r.getString("reserva_status")));
        String inicio = r.getString("data_inicio_real"); String fim = r.getString("data_fim_real");
        return new Locacao(r.getInt("locacao_id"), reserva, StatusLocacao.valueOf(r.getString("locacao_status")),
                inicio == null ? null : LocalDateTime.parse(inicio), fim == null ? null : LocalDateTime.parse(fim),
                r.getInt("chaves_entregues") == 1, r.getInt("chaves_devolvidas") == 1, r.getString("condicao_salao"));
    }
}

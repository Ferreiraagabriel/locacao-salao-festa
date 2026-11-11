package br.ufms.locacao.controller;

import br.ufms.locacao.model.dao.*;
import br.ufms.locacao.model.entity.Usuario;
import br.ufms.locacao.model.service.*;
import br.ufms.locacao.view.*;

public class NavegacaoController {
    private MainFrame janela;
    private ConsultaDisponibilidadeController disponibilidadeController;
    private ReservaController reservaController;
    private LocacaoController locacaoController;
    private EncerramentoController encerramentoController;

    public void abrirSistema(Usuario usuario) {
        MoradorDAO moradorDAO = new MoradorDAO();
        SalaoDAO salaoDAO = new SalaoDAO();
        ReservaDAO reservaDAO = new ReservaDAO();
        LocacaoDAO locacaoDAO = new LocacaoDAO();
        LeituraDAO leituraDAO = new LeituraDAO();
        TarifaDAO tarifaDAO = new TarifaDAO();
        ConsumoDAO consumoDAO = new ConsumoDAO();
        CobrancaDAO cobrancaDAO = new CobrancaDAO();
        ComprovanteDAO comprovanteDAO = new ComprovanteDAO();

        DisponibilidadeService disponibilidadeService = new DisponibilidadeService(salaoDAO, reservaDAO);
        ReservaService reservaService = new ReservaService(reservaDAO, disponibilidadeService);
        LocacaoService locacaoService = new LocacaoService(reservaDAO, locacaoDAO, leituraDAO);
        EncerramentoService encerramentoService = new EncerramentoService(locacaoDAO, leituraDAO, tarifaDAO, consumoDAO, cobrancaDAO, comprovanteDAO, reservaDAO);

        ConsultaDisponibilidadeView disponibilidadeView = new ConsultaDisponibilidadeView();
        ReservaView reservaView = new ReservaView();
        InicioLocacaoView inicioView = new InicioLocacaoView();
        EncerramentoView encerramentoView = new EncerramentoView();

        janela = new MainFrame(usuario, disponibilidadeView, reservaView, inicioView, encerramentoView, this::mostrarTela);
        disponibilidadeController = new ConsultaDisponibilidadeController(disponibilidadeView, disponibilidadeService);
        reservaController = new ReservaController(reservaView, moradorDAO, salaoDAO, reservaDAO, reservaService, usuario.getCpf(), this::atualizarTelas);
        locacaoController = new LocacaoController(inicioView, reservaDAO, locacaoDAO, locacaoService, usuario.getCpf(), this::atualizarTelas);
        encerramentoController = new EncerramentoController(encerramentoView, locacaoDAO, encerramentoService, usuario.getCpf(), janela, this::atualizarTelas);
        atualizarTelas();
        janela.setVisible(true);
    }

    private void mostrarTela(String nome) {
        janela.mostrarTela(nome);
        atualizarTelas();
    }

    private void atualizarTelas() {
        if (reservaController != null) reservaController.atualizar();
        if (locacaoController != null) locacaoController.atualizar();
        if (encerramentoController != null) encerramentoController.atualizar();
    }
}

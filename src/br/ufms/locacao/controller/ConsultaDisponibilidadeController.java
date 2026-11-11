package br.ufms.locacao.controller;

import br.ufms.locacao.model.service.DisponibilidadeService;
import br.ufms.locacao.util.DataHoraUtil;
import br.ufms.locacao.view.ConsultaDisponibilidadeView;

public class ConsultaDisponibilidadeController {
    private final ConsultaDisponibilidadeView view;
    private final DisponibilidadeService service;

    public ConsultaDisponibilidadeController(ConsultaDisponibilidadeView view, DisponibilidadeService service) {
        this.view = view; this.service = service;
        view.aoConsultar(this::consultar);
    }

    private void consultar() {
        try {
            var inicio = DataHoraUtil.lerDataHora(view.getData(), view.getHoraInicio());
            var fim = DataHoraUtil.lerDataHora(view.getData(), view.getHoraFim());
            var saloes = service.consultar(inicio, fim);
            view.mostrarSaloes(saloes);
        } catch (Exception e) { view.mostrarMensagem(e.getMessage(), true); }
    }
}

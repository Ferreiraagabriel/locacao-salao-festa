package br.ufms.locacao.view;

import br.ufms.locacao.model.entity.Usuario;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Consumer;

public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private final CardLayout cartoes = new CardLayout();
    private final JPanel areaCentral = new JPanel(cartoes);

    public MainFrame(Usuario usuario, ConsultaDisponibilidadeView disponibilidade,
            ReservaView reserva, InicioLocacaoView inicio, EncerramentoView encerramento,
            Consumer<String> aoNavegar) {
        setTitle("Locação de Salão de Festas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 650));
        setSize(1150, 730);
        setLocationRelativeTo(null);

        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(EstiloTela.FUNDO);

        JPanel menu = new JPanel();
        menu.setPreferredSize(new Dimension(225, 0));
        menu.setBackground(EstiloTela.AZUL);
        menu.setBorder(new EmptyBorder(22, 14, 18, 14));
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));

        JLabel nomeSistema = new JLabel("LOCAÇÃO DE SALÕES");
        nomeSistema.setForeground(Color.WHITE);
        nomeSistema.setFont(new Font("SansSerif", Font.BOLD, 15));
        nomeSistema.setAlignmentX(Component.LEFT_ALIGNMENT);
        menu.add(nomeSistema);
        JLabel subtitulo = new JLabel("Controle de energia");
        subtitulo.setForeground(new Color(210, 225, 240));
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        menu.add(subtitulo);
        menu.add(Box.createVerticalStrut(35));

        adicionarBotao(menu, "Disponibilidade", "disponibilidade", aoNavegar);
        adicionarBotao(menu, "Reservar salão", "reservas", aoNavegar);
        adicionarBotao(menu, "Iniciar locação", "inicio", aoNavegar);
        adicionarBotao(menu, "Encerrar locação", "encerramento", aoNavegar);

        menu.add(Box.createVerticalGlue());
        JLabel usuarioLogado = new JLabel("Usuário: " + usuario.getNome());
        usuarioLogado.setForeground(Color.WHITE);
        usuarioLogado.setFont(new Font("SansSerif", Font.PLAIN, 12));
        usuarioLogado.setAlignmentX(Component.LEFT_ALIGNMENT);
        menu.add(usuarioLogado);
        JLabel perfil = new JLabel(usuario.getPerfil().toString());
        perfil.setForeground(new Color(210, 225, 240));
        perfil.setFont(new Font("SansSerif", Font.PLAIN, 11));
        perfil.setAlignmentX(Component.LEFT_ALIGNMENT);
        menu.add(perfil);

        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(Color.WHITE);
        cabecalho.setBorder(new EmptyBorder(16, 24, 16, 24));
        JLabel titulo = new JLabel("Sistema local de locação");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        titulo.setForeground(EstiloTela.TEXTO);
        cabecalho.add(titulo, BorderLayout.WEST);

        areaCentral.setBackground(EstiloTela.FUNDO);
        areaCentral.add(disponibilidade, "disponibilidade");
        areaCentral.add(reserva, "reservas");
        areaCentral.add(inicio, "inicio");
        areaCentral.add(encerramento, "encerramento");

        raiz.add(menu, BorderLayout.WEST);
        JPanel conteudo = new JPanel(new BorderLayout());
        conteudo.add(cabecalho, BorderLayout.NORTH);
        conteudo.add(areaCentral, BorderLayout.CENTER);
        raiz.add(conteudo, BorderLayout.CENTER);
        setContentPane(raiz);
        mostrarTela("disponibilidade");
    }

    private void adicionarBotao(JPanel menu, String texto, String destino, Consumer<String> aoNavegar) {
            JButton botao = new JButton(texto);
            botao.setAlignmentX(Component.LEFT_ALIGNMENT);
            botao.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
            botao.setHorizontalAlignment(SwingConstants.LEFT);
            botao.setBackground(new Color(0, 173, 181));
            botao.setForeground(new Color(10, 35, 55));
            botao.setFont(new Font("SansSerif", Font.BOLD, 13));
            botao.setFocusPainted(false);
            botao.setBorderPainted(false);
            botao.setOpaque(true);
            botao.setContentAreaFilled(true);
            botao.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 8));
            botao.addActionListener(e -> aoNavegar.accept(destino));
            menu.add(botao);
            menu.add(Box.createVerticalStrut(7));
    }

    public void mostrarTela(String nome) {
        cartoes.show(areaCentral, nome);
    }
}

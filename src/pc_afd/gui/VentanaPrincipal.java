package pc_afd.gui;

import javax.swing.*;
import pc_afd.afd.AFD;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;

public class VentanaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    private AFD afd;

    private JTextField estadosField;
    private JTextField alfabetoField;
    private JTextField estadoInicialField;
    private JTextField estadosFinalesField;
    private JTextField potenciaField;
    private JTextArea transicionesArea;
    private JTextArea cadenasArea;
    private JTextArea resultadosArea;

    public VentanaPrincipal() {
        afd = new AFD();
        setTitle("Simulador de AFD");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel superior para capturar estados, alfabeto, estado inicial y finales
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Estados (Q) separados por comas sin espacios:"));
        estadosField = new JTextField(20);
        inputPanel.add(estadosField);

        inputPanel.add(new JLabel("Alfabeto (∑) separados por comas sin espacios:"));
        alfabetoField = new JTextField(20);
        inputPanel.add(alfabetoField);

        inputPanel.add(new JLabel("Estado Inicial (q0):"));
        estadoInicialField = new JTextField(20);
        inputPanel.add(estadoInicialField);

        inputPanel.add(new JLabel("Estados Finales (F) separados por comas sin espacios:"));
        estadosFinalesField = new JTextField(20);
        inputPanel.add(estadosFinalesField);

        inputPanel.add(new JLabel("Potencia del Lenguaje:"));
        potenciaField = new JTextField(20);
        inputPanel.add(potenciaField);
        inputPanel.add(new JPanel());

        JPanel inputWrapperPanel = new JPanel(new BorderLayout());
        inputWrapperPanel.add(inputPanel, BorderLayout.NORTH);
        add(inputWrapperPanel, BorderLayout.NORTH);

        // Panel central para capturar transiciones y cadenas
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel transicionesPanel = new JPanel(new BorderLayout());
        transicionesPanel.add(new JLabel("Transiciones (en el formato: q0,a,q1):"), BorderLayout.NORTH);
        transicionesArea = new JTextArea();
        transicionesArea.setPreferredSize(new Dimension(300, 200));
        transicionesPanel.add(new JScrollPane(transicionesArea), BorderLayout.CENTER);

        JPanel cadenasPanel = new JPanel(new BorderLayout());
        cadenasPanel.add(new JLabel("Cadenas a Validar:"), BorderLayout.NORTH);
        cadenasArea = new JTextArea();
        cadenasArea.setPreferredSize(new Dimension(300, 200));
        cadenasPanel.add(new JScrollPane(cadenasArea), BorderLayout.CENTER);

        centerPanel.add(transicionesPanel, BorderLayout.WEST);
        centerPanel.add(cadenasPanel, BorderLayout.EAST);

        add(centerPanel, BorderLayout.CENTER);

        // Panel inferior para botones y resultados
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        bottomPanel.add(new JLabel("Resultados:"), BorderLayout.NORTH);
        resultadosArea = new JTextArea(10, 30);
        resultadosArea.setEditable(false);
        bottomPanel.add(new JScrollPane(resultadosArea), BorderLayout.CENTER);

        JButton procesarButton = new JButton("Procesar");
        procesarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                procesarEntrada();
            }
        });
        bottomPanel.add(procesarButton, BorderLayout.SOUTH);

        JPanel bottomWrapperPanel = new JPanel(new BorderLayout());
        bottomWrapperPanel.add(bottomPanel, BorderLayout.CENTER);
        add(bottomWrapperPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void procesarEntrada() {
        try {
            // Capturar estados, alfabeto, estado inicial, y finales
            String estados = estadosField.getText().trim();
            String alfabeto = alfabetoField.getText().trim();
            String estadoInicial = estadoInicialField.getText().trim();
            String estadosFinales = estadosFinalesField.getText().trim();
            int potencia = Integer.parseInt(potenciaField.getText().trim());

            afd.estados = estados.split(",");
            afd.alfabeto = alfabeto.split(",");
            afd.estadoInicial = estadoInicial;
            afd.estadosFinales = estadosFinales.split(",");
            afd.funcionDeTransicion = new HashMap<>();

            // Debug: Verificar los valores capturados
            System.out.println("Estados capturados: " + Arrays.toString(afd.estados));
            System.out.println("Alfabeto capturado: " + Arrays.toString(afd.alfabeto));
            System.out.println("Estado inicial: " + afd.estadoInicial);
            System.out.println("Estados finales: " + Arrays.toString(afd.estadosFinales));

            afd.generarLenguaje(potencia, afd.alfabeto);

            // Capturar las transiciones
            String[] transiciones = transicionesArea.getText().split("\n");
            for (String transicion : transiciones) {
                String[] partes = transicion.split(",");
                if (partes.length == 3) {
                    afd.funcionDeTransicion.computeIfAbsent(partes[0], k -> new HashMap<>()).put(partes[1], partes[2]);
                } else {
                    resultadosArea.append("Error: Transición inválida -> " + transicion + "\n");
                }
            }

            // Capturar y procesar las cadenas
            String[] cadenas = cadenasArea.getText().split("\n");
            for (String cadena : cadenas) {
                afd.validarCadenaYBuscarEnArchivo(cadena, "output.txt");
            }

            // Mostrar los resultados en el área de resultados
            SwingUtilities.invokeLater(() -> resultadosArea.setText(afd.resultados.toString()));

            // Guardar los resultados en archivo
            afd.guardarResultadosEnArchivo("output_2.txt");

        } catch (NumberFormatException e) {
            resultadosArea.setText("Error: La potencia debe ser un número entero.");
        } catch (Exception e) {
            resultadosArea.setText("Error: " + e.getMessage());
        }
    }
}


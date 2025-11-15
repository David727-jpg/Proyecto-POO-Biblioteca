/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;
import java.awt.Font;  
import Model.Usuario;
import Model.Ejemplar;
import service.EjemplarService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

 /*
 * @author josed
 */
public class GestionEjemplaresFrame extends javax.swing.JFrame {
    private Usuario usuario;
    private EjemplarService ejemplarService;
    private JTable tablaEjemplares;
    private JButton btnListar, btnBuscar, btnVolver;
    
    public GestionEjemplaresFrame(Usuario usuario) {
        this.usuario = usuario;
        this.ejemplarService = new EjemplarService();
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Gestión de Ejemplares");
        cargarEjemplares();
    }
    
    private void initComponents() {
        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Título
        JLabel titulo = new JLabel("📚 GESTIÓN DE EJEMPLARES");
        titulo.setFont(new java.awt.Font("Arial", Font.BOLD, 16));
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        
        // Tabla para mostrar ejemplares
        String[] columnas = {"ID", "Título", "Tipo", "Disponibles", "Ubicación"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0);
        tablaEjemplares = new JTable(model);
        JScrollPane scroll = new JScrollPane(tablaEjemplares);
        scroll.setPreferredSize(new java.awt.Dimension(500, 200));
        
        // Botones
        JPanel panelBotones = new JPanel();
        btnListar = new JButton("🔄 Actualizar Lista");
        btnBuscar = new JButton("🔍 Buscar por Título");
        btnVolver = new JButton("↩️ Volver");
        
        panelBotones.add(btnListar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnVolver);
        
        // Eventos
        btnListar.addActionListener(e -> cargarEjemplares());
        btnBuscar.addActionListener(e -> buscarEjemplar());
        btnVolver.addActionListener(e -> dispose());
        
        // Agregar componentes
        panel.add(titulo);
        panel.add(Box.createRigidArea(new java.awt.Dimension(0, 10)));
        panel.add(scroll);
        panel.add(Box.createRigidArea(new java.awt.Dimension(0, 10)));
        panel.add(panelBotones);
        
        add(panel);
        pack();
    }
    
    private void cargarEjemplares() {
        try {
            List<Ejemplar> ejemplares = ejemplarService.listarTodosEjemplares();
            DefaultTableModel model = (DefaultTableModel) tablaEjemplares.getModel();
            model.setRowCount(0); // Limpiar tabla
            
            for (Ejemplar e : ejemplares) {
                model.addRow(new Object[]{
                    e.getId(),
                    e.getTitulo(),
                    e.getTipo(),
                    e.getCantidadDisponible() + "/" + e.getCantidadTotal(),
                    e.getUbicacion()
                });
            }
            
            JOptionPane.showMessageDialog(this, 
                "Se cargaron " + ejemplares.size() + " ejemplares");
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar ejemplares: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void buscarEjemplar() {
        String titulo = JOptionPane.showInputDialog(this, "Ingrese título a buscar:");
        if (titulo != null && !titulo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Buscando: " + titulo);
            // Aquí integrarías: ejemplarService.buscarEjemplarPorTitulo(titulo)
        }
    }
}
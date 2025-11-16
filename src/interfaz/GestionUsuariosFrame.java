/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;

import Model.Usuario;
import service.UsuarioService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
/**
 *
 * @author josed
 */
public class GestionUsuariosFrame extends JFrame {
    private Usuario usuario;
    private UsuarioService usuarioService;
    private JTable tablaUsuarios;
    
    public GestionUsuariosFrame(Usuario usuario) {
        this.usuario = usuario;
        this.usuarioService = new UsuarioService();
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Gestión de Usuarios");
        cargarUsuarios();
    }
    
    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("👥 GESTIÓN DE USUARIOS");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        
        // Tabla de usuarios
        String[] columnas = {"ID", "Nombre", "Email", "Tipo", "Mora", "Activo"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0);
        tablaUsuarios = new JTable(model);
        JScrollPane scroll = new JScrollPane(tablaUsuarios);
        scroll.setPreferredSize(new Dimension(600, 300));
        
        // Botones
        JPanel panelBotones = new JPanel();
        JButton btnListar = new JButton("🔄 Actualizar");
        JButton btnCrear = new JButton("➕ Crear Usuario");
        JButton btnRestablecer = new JButton("🔑 Restablecer Contraseña");
        JButton btnVolver = new JButton("↩️ Volver");
        
        panelBotones.add(btnListar);
        panelBotones.add(btnCrear);
        panelBotones.add(btnRestablecer);
        panelBotones.add(btnVolver);
        
        // Eventos
        btnListar.addActionListener(e -> cargarUsuarios());
        btnCrear.addActionListener(e -> crearUsuario());
        btnRestablecer.addActionListener(e -> restablecerPassword());
        btnVolver.addActionListener(e -> dispose());
        
        panel.add(titulo);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(scroll);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(panelBotones);
        
        add(panel);
        pack();
    }
    
    private void cargarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.listarTodosUsuarios();
            DefaultTableModel model = (DefaultTableModel) tablaUsuarios.getModel();
            model.setRowCount(0);
            
            for (Usuario u : usuarios) {
                model.addRow(new Object[]{
                    u.getId(),
                    u.getNombre(),
                    u.getEmail(),
                    u.getTipo(),
                    "$" + u.getMora(),
                    u.isActivo() ? "✅" : "❌"
                });
            }
            
            JOptionPane.showMessageDialog(this, 
                "Se cargaron " + usuarios.size() + " usuarios");
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar usuarios: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void crearUsuario() {
        // Diálogo simple para crear usuario
        JTextField txtNombre = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtPassword = new JTextField();
        JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"ALUMNO", "PROFESOR", "ADMIN"});
        
        Object[] campos = {
            "Nombre:", txtNombre,
            "Email:", txtEmail,
            "Contraseña:", txtPassword,
            "Tipo:", cmbTipo
        };
        
        int opcion = JOptionPane.showConfirmDialog(this, campos, 
            "Crear Nuevo Usuario", JOptionPane.OK_CANCEL_OPTION);
        
        if (opcion == JOptionPane.OK_OPTION) {
            try {
                Usuario nuevoUsuario = new Usuario();
                nuevoUsuario.setNombre(txtNombre.getText());
                nuevoUsuario.setEmail(txtEmail.getText());
                nuevoUsuario.setPassword(txtPassword.getText());
                nuevoUsuario.setTipo(cmbTipo.getSelectedItem().toString());
                nuevoUsuario.setMora(0.0);
                nuevoUsuario.setActivo(true);
                
                if (usuarioService.crearUsuario(nuevoUsuario)) {
                    JOptionPane.showMessageDialog(this, "Usuario creado exitosamente");
                    cargarUsuarios();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }
    
    private void restablecerPassword() {
        String email = JOptionPane.showInputDialog(this, "Ingrese email del usuario:");
        if (email != null && !email.isEmpty()) {
            if (usuarioService.restablecerContraseña(email)) {
                JOptionPane.showMessageDialog(this, "Contraseña restablecida exitosamente");
            }
        }
    }
}
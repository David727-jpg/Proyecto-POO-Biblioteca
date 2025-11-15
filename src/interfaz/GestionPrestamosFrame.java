/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;
import java.awt.Font;  // ← AGREGA ESTE IMPORT
import java.awt.Dimension;
import Model.Usuario;
import javax.swing.*;
 /*
 * @author josed
 */
public class GestionPrestamosFrame extends javax.swing.JFrame {
    private Usuario usuario;
    
    public GestionPrestamosFrame(Usuario usuario) {
        this.usuario = usuario;
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Gestión de Préstamos");
    }
    
    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titulo = new JLabel("📖 GESTIÓN DE PRÉSTAMOS");
        titulo.setFont(new java.awt.Font("Arial", Font.BOLD, 16));
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        
        JButton btnRealizarPrestamo = new JButton("📚 Realizar Préstamo");
        JButton btnRegistrarDevolucion = new JButton("📗 Registrar Devolución");
        JButton btnVerPrestamos = new JButton("📋 Ver Mis Préstamos");
        JButton btnVolver = new JButton("↩️ Volver");
        
        btnRealizarPrestamo.setAlignmentX(CENTER_ALIGNMENT);
        btnRegistrarDevolucion.setAlignmentX(CENTER_ALIGNMENT);
        btnVerPrestamos.setAlignmentX(CENTER_ALIGNMENT);
        btnVolver.setAlignmentX(CENTER_ALIGNMENT);
        
        // Eventos simples por ahora
        btnRealizarPrestamo.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, "Función: Realizar préstamo"));
        btnRegistrarDevolucion.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, "Función: Registrar devolución"));
        btnVerPrestamos.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, "Función: Ver mis préstamos"));
        btnVolver.addActionListener(e -> dispose());
        
        panel.add(titulo);
        panel.add(Box.createRigidArea(new java.awt.Dimension(0, 20)));
        panel.add(btnRealizarPrestamo);
        panel.add(Box.createRigidArea(new java.awt.Dimension(0, 10)));
        panel.add(btnRegistrarDevolucion);
        panel.add(Box.createRigidArea(new java.awt.Dimension(0, 10)));
        panel.add(btnVerPrestamos);
        panel.add(Box.createRigidArea(new java.awt.Dimension(0, 20)));
        panel.add(btnVolver);
        
        add(panel);
        pack();
    }
}
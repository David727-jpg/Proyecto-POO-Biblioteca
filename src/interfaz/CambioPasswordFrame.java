/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;

import service.UsuarioService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 *
 * @author josed
 */
public class CambioPasswordFrame extends JFrame {
    private UsuarioService usuarioService;
    private String email;
    private String codigoCorrecto;
    
    private JTextField txtCodigo;
    private JPasswordField txtNuevaPassword;
    private JPasswordField txtConfirmarPassword;
    private JButton btnCambiar, btnCancelar;

    public CambioPasswordFrame(String email, String codigoCorrecto) {
        this.usuarioService = new UsuarioService();
        this.email = email;
        this.codigoCorrecto = codigoCorrecto;
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Cambiar Contraseña");
        setSize(450, 350);
        setResizable(false);
    }

    private void initComponents() {
        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel titulo = new JLabel("🔄 Cambiar Contraseña");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Info del email
        JLabel lblEmail = new JLabel("Email: " + email);
        lblEmail.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Campo código
        JPanel panelCodigo = new JPanel(new FlowLayout());
        panelCodigo.add(new JLabel("Código de verificación:"));
        txtCodigo = new JTextField(10);
        panelCodigo.add(txtCodigo);

        // Campo nueva contraseña
        JPanel panelNuevaPass = new JPanel(new FlowLayout());
        panelNuevaPass.add(new JLabel("Nueva contraseña:"));
        txtNuevaPassword = new JPasswordField(15);
        panelNuevaPass.add(txtNuevaPassword);

        // Campo confirmar contraseña
        JPanel panelConfirmar = new JPanel(new FlowLayout());
        panelConfirmar.add(new JLabel("Confirmar contraseña:"));
        txtConfirmarPassword = new JPasswordField(15);
        panelConfirmar.add(txtConfirmarPassword);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnCambiar = new JButton("Cambiar Contraseña");
        btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnCambiar);
        panelBotones.add(btnCancelar);

        // Agregar componentes
        panel.add(titulo);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(lblEmail);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(panelCodigo);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(panelNuevaPass);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(panelConfirmar);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(panelBotones);

        add(panel);

        // Eventos
        btnCambiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarPassword();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // Enter en campos de contraseña también ejecuta cambiar
        txtConfirmarPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarPassword();
            }
        });
    }

    private void cambiarPassword() {
        String codigo = txtCodigo.getText().trim();
        String nuevaPassword = new String(txtNuevaPassword.getPassword());
        String confirmarPassword = new String(txtConfirmarPassword.getPassword());

        // Validaciones
        if (!validarDatos(codigo, nuevaPassword, confirmarPassword)) {
            return;
        }

        try {
            // Actualizar contraseña usando tu servicio
            if (usuarioService.actualizarPassword(email, nuevaPassword)) {
                JOptionPane.showMessageDialog(this,
                    " ¡Contraseña actualizada exitosamente!\n\n" +
                    "Ahora puedes iniciar sesión con tu nueva contraseña.",
                    "Contraseña Cambiada",
                    JOptionPane.INFORMATION_MESSAGE);
                
                dispose(); // Cerrar esta ventana
                
            } else {
                JOptionPane.showMessageDialog(this,
                    " Error al actualizar la contraseña\n" +
                    "Por favor intenta nuevamente",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error del sistema",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarDatos(String codigo, String nuevaPassword, String confirmarPassword) {
        // Validar código
        if (!codigo.equals(codigoCorrecto)) {
            JOptionPane.showMessageDialog(this,
                " Código de verificación incorrecto\n" +
                "Por favor ingresa el código que recibiste",
                "Código Inválido",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar longitud de contraseña
        if (nuevaPassword.length() < 4) {
            JOptionPane.showMessageDialog(this,
                " La contraseña debe tener al menos 4 caracteres",
                "Contraseña Muy Corta",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar que las contraseñas coincidan
        if (!nuevaPassword.equals(confirmarPassword)) {
            JOptionPane.showMessageDialog(this,
                " Las contraseñas no coinciden\n" +
                "Por favor verifica que ambas contraseñas sean iguales",
                "Contraseñas No Coinciden",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}
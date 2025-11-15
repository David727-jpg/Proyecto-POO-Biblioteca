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
public class RecuperarPasswordFrame extends JFrame {
    private UsuarioService usuarioService;
    private JTextField txtEmail;
    private JButton btnEnviar, btnVolver;

    public RecuperarPasswordFrame() {
        this.usuarioService = new UsuarioService(); // Tu servicio existente
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Recuperar Contraseña");
        setSize(400, 250);
        setResizable(false);
    }

    private void initComponents() {
        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel titulo = new JLabel("🔐 Recuperar Contraseña");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Instrucciones
        JLabel instrucciones = new JLabel("Ingresa tu email para recibir un código de verificación:");
        instrucciones.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Campo email
        JPanel panelEmail = new JPanel();
        panelEmail.setLayout(new FlowLayout());
        panelEmail.add(new JLabel("Email:"));
        txtEmail = new JTextField(20);
        panelEmail.add(txtEmail);

        // Botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout());
        
        btnEnviar = new JButton("Enviar Código");
        btnVolver = new JButton("Volver al Login");
        
        panelBotones.add(btnEnviar);
        panelBotones.add(btnVolver);

        // Agregar componentes al panel principal
        panel.add(titulo);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(instrucciones);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(panelEmail);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(panelBotones);

        add(panel);

        // Eventos
        btnEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarCodigoRecuperacion();
            }
        });

        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                volverAlLogin();
            }
        });

        // Enter en el campo de email también ejecuta enviar código
        txtEmail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarCodigoRecuperacion();
            }
        });
    }

    private void enviarCodigoRecuperacion() {
        String email = txtEmail.getText().trim();
        
        // Validar campo vacío
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor ingresa tu email", 
                "Campo vacío", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Verificar si el email existe usando tu servicio
            if (usuarioService.existeEmail(email)) {
                // Generar código de 6 dígitos
                String codigo = generarCodigo();
                
                // Mostrar código (en un sistema real, se enviaría por email)
                JOptionPane.showMessageDialog(this, 
                    "✅ Código de verificación generado:\n\n" +
                    "Código: " + codigo + "\n" +
                    "Email: " + email + "\n\n" +
                    "(En un sistema real, este código se enviaría al email)",
                    "Código Enviado", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Abrir ventana para cambiar contraseña
                abrirCambioPassword(email, codigo);
                
            } else {
                JOptionPane.showMessageDialog(this, 
                    "❌ El email no está registrado en el sistema\n" +
                    "Por favor verifica el email e intenta nuevamente", 
                    "Email no encontrado", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + ex.getMessage(), 
                "Error del sistema", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private String generarCodigo() {
        // Generar código de 6 dígitos aleatorio
        return String.valueOf((int)(Math.random() * 900000) + 100000);
    }

    private void abrirCambioPassword(String email, String codigo) {
        CambioPasswordFrame cambioFrame = new CambioPasswordFrame(email, codigo);
        cambioFrame.setVisible(true);
        this.dispose();
    }

    private void volverAlLogin() {
        this.dispose();
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;
import Model.Usuario;
import service.ConfiguracionService;
import Model.Configuracion;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Frame para gestionar la configuración del sistema de biblioteca
 * Permite al administrador modificar parámetros como límites de préstamos, mora diaria y días de préstamo
 */
public class ConfiguracionFrame extends JFrame {
    private Usuario usuario;
    private ConfiguracionService configuracionService;
    private Configuracion configuracionActual;
    
    // Componentes de la interfaz
    private JTextField txtMaxPrestamos;
    private JTextField txtMoraDiaria;
    private JTextField txtDiasPrestamo;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private JButton btnRestablecer;

    /**
     * Constructor del frame de configuración
     * 
     * @param usuario Usuario que está gestionando la configuración (debe ser ADMIN)
     */
    public ConfiguracionFrame(Usuario usuario) {
        this.usuario = usuario;
        this.configuracionService = new ConfiguracionService();
        
        // Verificar permisos de administrador
        if (!"ADMIN".equals(usuario.getTipo())) {
            JOptionPane.showMessageDialog(null, 
                "❌ Solo los administradores pueden acceder a la configuración del sistema",
                "Acceso Denegado", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        initComponents();
        cargarConfiguracionActual();
        setLocationRelativeTo(null);
        setTitle("Configuración del Sistema");
        setSize(500, 400);
        setResizable(false);
    }
    
    /**
     * Inicializa todos los componentes de la interfaz gráfica
     * Configura los paneles, campos de texto, botones y sus eventos
     */
    private void initComponents() {
        // Panel principal con borde y layout vertical
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título principal
        JLabel titulo = new JLabel("⚙️ CONFIGURACIÓN DEL SISTEMA");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Subtítulo informativo
        JLabel subtitulo = new JLabel("Configura los parámetros generales del sistema de biblioteca");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Panel para los campos de configuración
        JPanel panelCampos = new JPanel();
        panelCampos.setLayout(new GridLayout(3, 2, 10, 15));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Campo: Máximo de préstamos
        JLabel lblMaxPrestamos = new JLabel("Máximo de préstamos por usuario:");
        lblMaxPrestamos.setToolTipText("Número máximo de ejemplares que un usuario puede tener prestados simultáneamente");
        txtMaxPrestamos = new JTextField();
        
        // Campo: Mora diaria
        JLabel lblMoraDiaria = new JLabel("Mora diaria por retraso ($):");
        lblMoraDiaria.setToolTipText("Cantidad en dólares que se cobra por cada día de retraso en la devolución");
        txtMoraDiaria = new JTextField();
        
        // Campo: Días de préstamo
        JLabel lblDiasPrestamo = new JLabel("Días permitidos por préstamo:");
        lblDiasPrestamo.setToolTipText("Número de días que un usuario puede mantener un ejemplar prestado");
        txtDiasPrestamo = new JTextField();
        
        // Agregar campos al panel
        panelCampos.add(lblMaxPrestamos);
        panelCampos.add(txtMaxPrestamos);
        panelCampos.add(lblMoraDiaria);
        panelCampos.add(txtMoraDiaria);
        panelCampos.add(lblDiasPrestamo);
        panelCampos.add(txtDiasPrestamo);
        
        // Panel para los botones de acción
        JPanel panelBotones = new JPanel(new FlowLayout());
        
        btnGuardar = new JButton("💾 Guardar Configuración");
        btnGuardar.setToolTipText("Guardar los cambios en la configuración del sistema");
        
        btnRestablecer = new JButton("🔄 Restablecer Valores");
        btnRestablecer.setToolTipText("Volver a los valores por defecto del sistema");
        
        btnCancelar = new JButton("❌ Cancelar");
        btnCancelar.setToolTipText("Cerrar sin guardar cambios");
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnRestablecer);
        panelBotones.add(btnCancelar);
        
        // Agregar todos los componentes al panel principal
        panel.add(titulo);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(subtitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(panelCampos);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(panelBotones);
        
        add(panel);
        
        // Configurar eventos de los botones
        configurarEventos();
    }
    
    /**
     * Configura los eventos y ActionListeners para los botones de la interfaz
     * Cada botón ejecuta una acción específica cuando es presionado
     */
    private void configurarEventos() {
        /**
         * Evento para el botón Guardar: Valida y guarda la configuración
         */
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarConfiguracion();
            }
        });
        
        /**
         * Evento para el botón Restablecer: Vuelve a los valores por defecto
         */
        btnRestablecer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restablecerValoresPorDefecto();
            }
        });
        
        /**
         * Evento para el botón Cancelar: Cierra la ventana sin guardar
         */
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelarConfiguracion();
            }
        });
        
        /**
         * Evento para Enter en campos de texto: Ejecuta guardar configuración
         */
        ActionListener enterListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarConfiguracion();
            }
        };
        
        txtMaxPrestamos.addActionListener(enterListener);
        txtMoraDiaria.addActionListener(enterListener);
        txtDiasPrestamo.addActionListener(enterListener);
    }
    
    /**
     * Carga la configuración actual del sistema desde la base de datos
     * y la muestra en los campos de texto de la interfaz
     */
    private void cargarConfiguracionActual() {
        try {
            configuracionActual = configuracionService.obtenerConfiguracionActual();
            
            if (configuracionActual != null) {
                // Mostrar valores actuales en los campos de texto
                txtMaxPrestamos.setText(String.valueOf(configuracionActual.getPrestamosMaximos()));
                txtMoraDiaria.setText(String.valueOf(configuracionActual.getMoraDiaria()));
                txtDiasPrestamo.setText(String.valueOf(configuracionActual.getDiasPrestamo()));
                
                System.out.println("✅ Configuración actual cargada correctamente");
            } else {
                JOptionPane.showMessageDialog(this,
                    "No se pudo cargar la configuración actual.\nSe usarán valores por defecto.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
                restablecerValoresPorDefecto();
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar configuración: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            restablecerValoresPorDefecto();
        }
    }
    
    /**
     * Valida y guarda la configuración modificada en la base de datos
     * Realiza validaciones de entrada antes de proceder con el guardado
     */
    private void guardarConfiguracion() {
        try {
            // Validar que todos los campos tengan valores
            if (txtMaxPrestamos.getText().trim().isEmpty() ||
                txtMoraDiaria.getText().trim().isEmpty() ||
                txtDiasPrestamo.getText().trim().isEmpty()) {
                
                JOptionPane.showMessageDialog(this,
                    "❌ Todos los campos son obligatorios",
                    "Campos Incompletos",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Convertir y validar los valores numéricos
            int maxPrestamos = Integer.parseInt(txtMaxPrestamos.getText().trim());
            double moraDiaria = Double.parseDouble(txtMoraDiaria.getText().trim());
            int diasPrestamo = Integer.parseInt(txtDiasPrestamo.getText().trim());
            
            // Validaciones de negocio
            if (maxPrestamos <= 0) {
                throw new NumberFormatException("El máximo de préstamos debe ser mayor a 0");
            }
            if (moraDiaria < 0) {
                throw new NumberFormatException("La mora diaria no puede ser negativa");
            }
            if (diasPrestamo <= 0) {
                throw new NumberFormatException("Los días de préstamo deben ser mayores a 0");
            }
            
            // Confirmar con el usuario antes de guardar
            int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de guardar la nueva configuración?\n\n" +
                "📚 Máximo préstamos: " + maxPrestamos + "\n" +
                "💰 Mora diaria: $" + moraDiaria + "\n" +
                "📅 Días de préstamo: " + diasPrestamo + "\n\n" +
                "Esta acción afectará a todos los usuarios del sistema.",
                "Confirmar Cambios",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                // Guardar en la base de datos
                if (configuracionService.actualizarConfiguracion(maxPrestamos, moraDiaria, diasPrestamo)) {
                    JOptionPane.showMessageDialog(this,
                        "✅ Configuración guardada exitosamente!\n\n" +
                        "Los nuevos parámetros están ahora activos en el sistema.",
                        "Configuración Guardada",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    dispose(); // Cerrar la ventana después de guardar
                } else {
                    throw new Exception("No se pudo guardar la configuración en la base de datos");
                }
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "❌ Error en los valores ingresados:\n" + e.getMessage() + "\n\n" +
                "Por favor ingrese valores numéricos válidos.",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "❌ Error al guardar configuración:\n" + e.getMessage(),
                "Error del Sistema",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Restablece los valores por defecto en los campos de texto
     * No guarda en la base de datos hasta que se presione "Guardar"
     */
    private void restablecerValoresPorDefecto() {
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Restablecer los valores por defecto?\n\n" +
            "📚 Máximo préstamos: 3\n" +
            "💰 Mora diaria: $2.50\n" +
            "📅 Días de préstamo: 15",
            "Restablecer Valores",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            txtMaxPrestamos.setText("3");
            txtMoraDiaria.setText("2.50");
            txtDiasPrestamo.setText("15");
            
            JOptionPane.showMessageDialog(this,
                "Valores por defecto restablecidos.\nRecuerde guardar para aplicar los cambios.",
                "Valores Restablecidos",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Cancela la operación y cierra la ventana después de confirmar
     * Pregunta al usuario si está seguro de salir sin guardar
     */
    private void cancelarConfiguracion() {
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de salir sin guardar los cambios?",
            "Confirmar Salida",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            dispose();
        }
    }
}
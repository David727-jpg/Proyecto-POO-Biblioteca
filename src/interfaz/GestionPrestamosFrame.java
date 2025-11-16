package interfaz;

import Model.Usuario;
import Model.Prestamo;
import Model.Ejemplar;
import service.PrestamoService;
import service.EjemplarService;
import service.UsuarioService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class GestionPrestamosFrame extends JFrame {
    private Usuario usuario;
    private PrestamoService prestamoService;
    private EjemplarService ejemplarService;
    private UsuarioService usuarioService;
    private JTable tablaPrestamos;
    
    public GestionPrestamosFrame(Usuario usuario) {
        this.usuario = usuario;
        this.prestamoService = new PrestamoService();
        this.ejemplarService = new EjemplarService();
        this.usuarioService = new UsuarioService();
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Gestión de Préstamos");
        cargarPrestamosActivos();
    }
    
    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titulo = new JLabel("📖 GESTIÓN DE PRÉSTAMOS");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        
        // Tabla para mostrar préstamos
        String[] columnas = {"ID", "Ejemplar", "Fecha Préstamo", "Estado", "Mora"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPrestamos = new JTable(model);
        JScrollPane scroll = new JScrollPane(tablaPrestamos);
        scroll.setPreferredSize(new Dimension(600, 200));
        
        // Botones
        JPanel panelBotones = new JPanel();
        JButton btnRealizarPrestamo = new JButton("📚 Realizar Préstamo");
        JButton btnRegistrarDevolucion = new JButton("📗 Registrar Devolución");
        JButton btnVerPrestamos = new JButton("🔄 Actualizar Lista");
        JButton btnVolver = new JButton("↩️ Volver");
        
        panelBotones.add(btnRealizarPrestamo);
        panelBotones.add(btnRegistrarDevolucion);
        panelBotones.add(btnVerPrestamos);
        panelBotones.add(btnVolver);
        
        // Eventos
        btnRealizarPrestamo.addActionListener(e -> realizarPrestamo());
        btnRegistrarDevolucion.addActionListener(e -> registrarDevolucion());
        btnVerPrestamos.addActionListener(e -> cargarPrestamosActivos());
        btnVolver.addActionListener(e -> dispose());
        
        panel.add(titulo);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(scroll);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(panelBotones);
        
        add(panel);
        pack();
    }
    
    private void cargarPrestamosActivos() {
        try {
            List<Prestamo> prestamos;
            
            if ("ADMIN".equals(usuario.getTipo())) {
                prestamos = prestamoService.listarPrestamosActivos();
            } else {
                prestamos = prestamoService.listarPrestamosPorUsuario(usuario.getId());
            }
            
            DefaultTableModel model = (DefaultTableModel) tablaPrestamos.getModel();
            model.setRowCount(0);
            
            for (Prestamo p : prestamos) {
                Ejemplar e = ejemplarService.buscarEjemplarPorId(p.getIdEjemplar());
                if (e != null) {
                    String estado = "ACTIVO".equals(p.getEstado()) ? "🟢 Activo" : "🔴 Devuelto";
                    model.addRow(new Object[]{
                        p.getId(),
                        e.getTitulo(),
                        p.getFechadePrestamo(),
                        estado,
                        "$" + p.getMora()
                    });
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar préstamos: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void realizarPrestamo() {
        try {
            List<Ejemplar> disponibles = ejemplarService.buscarEjemplaresDisponibles();
            
            if (disponibles.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "No hay ejemplares disponibles para préstamo", 
                    "Sin disponibilidad", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Crear array de opciones
            String[] opciones = new String[disponibles.size()];
            for (int i = 0; i < disponibles.size(); i++) {
                Ejemplar e = disponibles.get(i);
                opciones[i] = e.getTitulo() + " (" + e.getTipo() + ") - " + e.getUbicacion();
            }
            
            String seleccion = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione el ejemplar a prestar:",
                "Realizar Préstamo",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
            );
            
            if (seleccion != null) {
                for (int i = 0; i < opciones.length; i++) {
                    if (opciones[i].equals(seleccion)) {
                        Ejemplar ejemplarSeleccionado = disponibles.get(i);
                        
                        if (prestamoService.realizarPrestamo(usuario.getId(), ejemplarSeleccionado.getId())) {
                            JOptionPane.showMessageDialog(this, 
                                "✅ Préstamo realizado exitosamente!\n\n" +
                                "Ejemplar: " + ejemplarSeleccionado.getTitulo() + "\n" +
                                "Ubicación: " + ejemplarSeleccionado.getUbicacion(),
                                "Préstamo Exitoso", 
                                JOptionPane.INFORMATION_MESSAGE);
                            cargarPrestamosActivos();
                        } else {
                            JOptionPane.showMessageDialog(this, 
                                "❌ No se pudo realizar el préstamo\n" +
                                "Puede que haya alcanzado el límite de préstamos o tenga mora pendiente",
                                "Error en Préstamo", 
                                JOptionPane.ERROR_MESSAGE);
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void registrarDevolucion() {
        try {
            List<Prestamo> prestamosActivos;
            
            if ("ADMIN".equals(usuario.getTipo())) {
                prestamosActivos = prestamoService.listarPrestamosActivos();
            } else {
                prestamosActivos = prestamoService.listarPrestamosPorUsuario(usuario.getId());
                // Filtrar solo activos
                List<Prestamo> activos = new ArrayList<>();
                for (Prestamo p : prestamosActivos) {
                    if ("ACTIVO".equals(p.getEstado())) {
                        activos.add(p);
                    }
                }
                prestamosActivos = activos;
            }
            
            if (prestamosActivos.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "No hay préstamos activos para devolver", 
                    "Sin préstamos activos", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Crear array de opciones
            String[] opciones = new String[prestamosActivos.size()];
            for (int i = 0; i < prestamosActivos.size(); i++) {
                Prestamo p = prestamosActivos.get(i);
                Ejemplar e = ejemplarService.buscarEjemplarPorId(p.getIdEjemplar());
                String usuarioInfo = "";
                
                if ("ADMIN".equals(usuario.getTipo())) {
                    Usuario usuarioPrestamo = usuarioService.buscarUsuarioPorId(p.getIdUsuario());
                    usuarioInfo = " - 👤 " + usuarioPrestamo.getNombre();
                }
                
                opciones[i] = e.getTitulo() + " | " + p.getFechadePrestamo() + usuarioInfo;
            }
            
            String seleccion = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione el préstamo a devolver:",
                "Registrar Devolución",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
            );
            
            if (seleccion != null) {
                for (int i = 0; i < opciones.length; i++) {
                    if (opciones[i].equals(seleccion)) {
                        Prestamo prestamoSeleccionado = prestamosActivos.get(i);
                        
                        double moraCalculada = prestamoService.calcularMora(prestamoSeleccionado);
                        String mensajeMora = moraCalculada > 0 ? 
                            "\n⚠️  Se aplicará mora: $" + moraCalculada : "";
                        
                        int confirmacion = JOptionPane.showConfirmDialog(
                            this,
                            "¿Está seguro de registrar la devolución?\n\n" +
                            "Ejemplar: " + ejemplarService.buscarEjemplarPorId(prestamoSeleccionado.getIdEjemplar()).getTitulo() +
                            mensajeMora,
                            "Confirmar Devolución",
                            JOptionPane.YES_NO_OPTION
                        );
                        
                        if (confirmacion == JOptionPane.YES_OPTION) {
                            if (prestamoService.registrarDevolucion(prestamoSeleccionado.getId())) {
                                JOptionPane.showMessageDialog(this, 
                                    "✅ Devolución registrada exitosamente!" +
                                    (moraCalculada > 0 ? "\n💰 Mora aplicada: $" + moraCalculada : ""),
                                    "Devolución Exitosa", 
                                    JOptionPane.INFORMATION_MESSAGE);
                                cargarPrestamosActivos();
                            } else {
                                JOptionPane.showMessageDialog(this, 
                                    "❌ Error al registrar la devolución",
                                    "Error", 
                                    JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
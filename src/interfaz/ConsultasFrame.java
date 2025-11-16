/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;
import Model.Usuario;
import Model.Ejemplar;
import Model.Prestamo;
import service.EjemplarService;
import service.PrestamoService;
import service.UsuarioService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
/**
 *
 * @author josed
 */
public class ConsultasFrame extends JFrame {
    private Usuario usuario;
    private EjemplarService ejemplarService;
    private PrestamoService prestamoService;
    private UsuarioService usuarioService;
    private JTable tablaResultados;
    
    public ConsultasFrame(Usuario usuario) {
        this.usuario = usuario;
        this.ejemplarService = new EjemplarService();
        this.prestamoService = new PrestamoService();
        this.usuarioService = new UsuarioService();
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Consultas y Reportes");
        setSize(700, 500);
    }
    
    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("🔍 CONSULTAS Y REPORTES");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        
        // Tabla de resultados
        String[] columnas = {"Tipo", "Información"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0);
        tablaResultados = new JTable(model);
        JScrollPane scroll = new JScrollPane(tablaResultados);
        
        // Botones de consulta
        JPanel panelBotones = new JPanel(new GridLayout(2, 3, 10, 10));
        
        JButton btnMiMora = new JButton("💰 Mi Mora");
        JButton btnMisPrestamos = new JButton("📖 Mis Préstamos");
        JButton btnBuscarAutor = new JButton("🔍 Buscar por Autor");
        JButton btnPrestamosActivos = new JButton("📊 Préstamos Activos");
        JButton btnReporteUsuarios = new JButton("👥 Reporte Usuarios");
        JButton btnVolver = new JButton("↩️ Volver");
        
        // Solo admin ve estos botones
        if (!"ADMIN".equals(usuario.getTipo())) {
            btnPrestamosActivos.setVisible(false);
            btnReporteUsuarios.setVisible(false);
        }
        
        panelBotones.add(btnMiMora);
        panelBotones.add(btnMisPrestamos);
        panelBotones.add(btnBuscarAutor);
        panelBotones.add(btnPrestamosActivos);
        panelBotones.add(btnReporteUsuarios);
        panelBotones.add(btnVolver);
        
        // Eventos
        btnMiMora.addActionListener(e -> consultarMiMora());
        btnMisPrestamos.addActionListener(e -> consultarMisPrestamos());
        btnBuscarAutor.addActionListener(e -> buscarPorAutor());
        btnPrestamosActivos.addActionListener(e -> consultarPrestamosActivos());
        btnReporteUsuarios.addActionListener(e -> generarReporteUsuarios());
        btnVolver.addActionListener(e -> dispose());
        
        panel.add(titulo);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(scroll);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(panelBotones);
        
        add(panel);
    }
    
    private void consultarMiMora() {
        boolean tieneMora = usuarioService.tieneMora(usuario.getId());
        DefaultTableModel model = (DefaultTableModel) tablaResultados.getModel();
        model.setRowCount(0);
        
        model.addRow(new Object[]{"💰 MORA ACTUAL", "$" + usuario.getMora()});
        model.addRow(new Object[]{"📊 ESTADO", tieneMora ? "❌ TIENE MORA PENDIENTE" : "✅ AL DÍA"});
    }
    
    private void consultarMisPrestamos() {
        List<Prestamo> misPrestamos = prestamoService.listarPrestamosPorUsuario(usuario.getId());
        DefaultTableModel model = (DefaultTableModel) tablaResultados.getModel();
        model.setRowCount(0);
        
        for (Prestamo p : misPrestamos) {
            Ejemplar e = ejemplarService.buscarEjemplarPorId(p.getIdEjemplar());
            String estado = "ACTIVO".equals(p.getEstado()) ? "🟢 ACTIVO" : "🔴 DEVUELTO";
            model.addRow(new Object[]{
                estado,
                e.getTitulo() + " | Préstamo: " + p.getFechadePrestamo() + " | Mora: $" + p.getMora()
            });
        }
    }
    
    private void buscarPorAutor() {
        String autor = JOptionPane.showInputDialog(this, "Ingrese autor a buscar:");
        if (autor != null && !autor.isEmpty()) {
            List<Ejemplar> resultados = ejemplarService.buscarEjemplarPorAutor(autor);
            DefaultTableModel model = (DefaultTableModel) tablaResultados.getModel();
            model.setRowCount(0);
            
            for (Ejemplar e : resultados) {
                model.addRow(new Object[]{
                    "📚 " + e.getTipo(),
                    e.getTitulo() + " | Autor: " + e.getAutor() + " | Disponibles: " + e.getCantidadDisponible()
                });
            }
        }
    }
    
    private void consultarPrestamosActivos() {
        if (!"ADMIN".equals(usuario.getTipo())) {
            JOptionPane.showMessageDialog(this, "Solo administradores pueden ver esta información");
            return;
        }
        
        List<Prestamo> prestamosActivos = prestamoService.listarPrestamosActivos();
        DefaultTableModel model = (DefaultTableModel) tablaResultados.getModel();
        model.setRowCount(0);
        
        for (Prestamo p : prestamosActivos) {
            Usuario u = usuarioService.buscarUsuarioPorId(p.getIdUsuario());
            Ejemplar e = ejemplarService.buscarEjemplarPorId(p.getIdEjemplar());
            model.addRow(new Object[]{
                "👤 " + u.getTipo(),
                u.getNombre() + " → " + e.getTitulo() + " | " + p.getFechadePrestamo()
            });
        }
    }
    
    private void generarReporteUsuarios() {
        if (!"ADMIN".equals(usuario.getTipo())) {
            JOptionPane.showMessageDialog(this, "Solo administradores pueden ver esta información");
            return;
        }
        
        List<Usuario> usuarios = usuarioService.listarTodosUsuarios();
        DefaultTableModel model = (DefaultTableModel) tablaResultados.getModel();
        model.setRowCount(0);
        
        int admins = 0, profesores = 0, alumnos = 0, conMora = 0;
        double totalMora = 0;
        
        for (Usuario u : usuarios) {
            switch (u.getTipo()) {
                case "ADMIN": admins++; break;
                case "PROFESOR": profesores++; break;
                case "ALUMNO": alumnos++; break;
            }
            if (u.getMora() > 0) {
                conMora++;
                totalMora += u.getMora();
            }
        }
        
        model.addRow(new Object[]{"📊 REPORTE USUARIOS", "Total: " + usuarios.size()});
        model.addRow(new Object[]{"👑 Administradores", String.valueOf(admins)});
        model.addRow(new Object[]{"👨‍🏫 Profesores", String.valueOf(profesores)});
        model.addRow(new Object[]{"👩‍🎓 Alumnos", String.valueOf(alumnos)});
        model.addRow(new Object[]{"⚠️ Usuarios con mora", String.valueOf(conMora)});
        model.addRow(new Object[]{"💰 Total mora pendiente", "$" + totalMora});
    }
}
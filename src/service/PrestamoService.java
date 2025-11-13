/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;
import Model.Prestamo;
import Model.Usuario;
import Model.Ejemplar;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 *
 * @author josed
 */
public class PrestamoService {
    private List<Prestamo> prestamos;
    private UsuarioService usuarioService;
    private EjemplarService ejemplarService;
    private double moraDiaria = 2.50; // Valor de mora por día
    private int diasPrestamo = 15; // Días de préstamo permitidos
    private int maxPrestamos = 3; // Máximo de préstamos por usuario
    
    public PrestamoService() {
        try {
            this.prestamos = new ArrayList<>();
            this.usuarioService = new UsuarioService();
            this.ejemplarService = new EjemplarService();
            System.out.println(" PrestamoService inicializado correctamente");
            
        } catch (Exception e) {
            System.out.println(" Error crítico al inicializar PrestamoService: " + e.getMessage());
        }
    }
    
    // ==================== MÉTODO AUXILIAR PARA GENERAR ID ====================
private int generarNuevoId() {
    try {
        if (prestamos.isEmpty()) {
            return 1;
        }
        
        // Encontrar el máximo ID actual
        int maxId = 0;
        for (Prestamo prestamo : prestamos) {
            if (prestamo.getId() > maxId) {
                maxId = prestamo.getId();
            }
        }
        return maxId + 1;
        
    } catch (Exception e) {
        System.out.println(" Error al generar nuevo ID, usando fallback: " + e.getMessage());
        return prestamos.size() + 1;
    }
}
 
    
    public boolean realizarPrestamo(int usuarioId, int ejemplarId) {
        try {
            System.out.println("Intentando realizar préstamo...");
            System.out.println(" Usuario ID: " + usuarioId);
            System.out.println(" Ejemplar ID: " + ejemplarId);
            
            // 1. VALIDAR USUARIO
            Usuario usuario = usuarioService.buscarUsuarioPorId(usuarioId);
            if (usuario == null) {
                throw new RuntimeException("Usuario no encontrado con ID: " + usuarioId);
            }
            
            // 2. VALIDAR QUE USUARIO NO TENGA MORA
            if (usuarioService.tieneMora(usuarioId)) {
                throw new RuntimeException("Usuario tiene mora pendiente. No puede realizar préstamos");
            }
            
            // 3. VALIDAR LÍMITE DE PRÉSTAMOS
            int prestamosActivos = contarPrestamosActivos(usuarioId);
            if (prestamosActivos >= maxPrestamos) {
                throw new RuntimeException("Límite de préstamos alcanzado. Máximo permitido: " + maxPrestamos);
            }
            
            // 4. VALIDAR EJEMPLAR
            Ejemplar ejemplar = ejemplarService.buscarEjemplarPorId(ejemplarId);
            if (ejemplar == null) {
                throw new RuntimeException("Ejemplar no encontrado con ID: " + ejemplarId);
            }
            
            // 5. VALIDAR DISPONIBILIDAD
            if (ejemplar.getCantidadDisponible() <= 0) {
                throw new RuntimeException("Ejemplar no disponible: " + ejemplar.getTitulo());
            }
            
            // 6. CREAR PRÉSTAMO
            Prestamo prestamo = new Prestamo();
            prestamo.setId(generarNuevoId()); // USAR MÉTODO MEJORADO en lugar de prestamos.size() + 
            prestamo.setIdUsuario(usuarioId);
            prestamo.setIdEjemplar(ejemplarId);
            prestamo.setFechadePrestamo(new Date());
            prestamo.setEstado("ACTIVO");
            prestamo.setMora(0.0);
            
            // 7. AGREGAR PRÉSTAMO A LA LISTA
            prestamos.add(prestamo);
            
            // 8. ACTUALIZAR DISPONIBILIDAD DEL EJEMPLAR
            ejemplarService.actualizarCantidades(
                ejemplarId,
                ejemplar.getCantidadTotal(),
                ejemplar.getCantidadDisponible() - 1
            );
            
            System.out.println(" Préstamo realizado exitosamente");
            System.out.println(" Fecha de préstamo: " + prestamo.getFechadePrestamo());
            System.out.println(" Ejemplar: " + ejemplar.getTitulo());
            System.out.println(" Usuario: " + usuario.getNombre());
            
            return true;
            
        } catch (RuntimeException e) {
            System.out.println(" Error al realizar préstamo: " + e.getMessage());
            return false;
        } finally {
            System.out.println("--- Operación de préstamo finalizada ---");
        }
    }
    
    public boolean registrarDevolucion(int prestamoId) {
        try {
            System.out.println(" Intentando registrar devolución...");
            System.out.println("  Préstamo ID: " + prestamoId);
            
            // 1. BUSCAR PRÉSTAMO
            Prestamo prestamo = buscarPrestamoPorId(prestamoId);
            if (prestamo == null) {
                throw new RuntimeException("Préstamo no encontrado con ID: " + prestamoId);
            }
            
            // 2. VALIDAR QUE NO ESTÉ YA DEVUELTO
            if ("DEVUELTO".equals(prestamo.getEstado())) {
                throw new RuntimeException("El préstamo ya fue devuelto anteriormente");
            }
            
            // 3. CALCULAR MORA SI APLICA
            double mora = calcularMora(prestamo);
            prestamo.setMora(mora);
            
            // 4. ACTUALIZAR ESTADO Y FECHA DE DEVOLUCIÓN
            prestamo.setEstado("DEVUELTO");
            prestamo.setFechaDeDevolucion(new Date());
            
            // 5. ACTUALIZAR DISPONIBILIDAD DEL EJEMPLAR
            Ejemplar ejemplar = ejemplarService.buscarEjemplarPorId(prestamo.getIdEjemplar());
            if (ejemplar != null) {
                ejemplarService.actualizarCantidades(
                    prestamo.getIdEjemplar(),
                    ejemplar.getCantidadTotal(),
                    ejemplar.getCantidadDisponible() + 1
                );
            }
            
            // 6. ACTUALIZAR MORA DEL USUARIO SI APLICA
            if (mora > 0) {
             boolean moraActualizada = usuarioService.actualizarMoraUsuario(prestamo.getIdUsuario(), mora);
              if (!moraActualizada) {
                  System.out.println(" Advertencia: No se pudo actualizar la mora del usuario");
              }
              System.out.println("  Mora aplicada: $" + mora);
            }
            
            System.out.println(" Devolución registrada exitosamente");
            System.out.println("  Fecha de devolución: " + prestamo.getFechaDeDevolucion());
            System.out.println("  Mora calculada: $" + mora);
            
            return true;
            
        } catch (RuntimeException e) {
            System.out.println(" Error al registrar devolución: " + e.getMessage());
            return false;
        } finally {
            System.out.println("--- Operación de devolución finalizada ---");
        }
    }
    
    public double calcularMora(Prestamo prestamo) {
        try {
            if (prestamo == null) {
                throw new IllegalArgumentException("El préstamo no puede ser null");
            }
            
            if ("DEVUELTO".equals(prestamo.getEstado())) {
                return prestamo.getMora(); // Ya tiene mora calculada
            }
            
            Date fechaActual = new Date();
            Date fechaPrestamo = prestamo.getFechadePrestamo();
            
            // Calcular días de diferencia
            long diferenciaMillis = fechaActual.getTime() - fechaPrestamo.getTime();
            long diasTranscurridos = diferenciaMillis / (1000 * 60 * 60 * 24);
            
            // Calcular días de mora (si pasaron más días de los permitidos)
            long diasMora = diasTranscurridos - diasPrestamo;
            
            if (diasMora > 0) {
                double mora = diasMora * moraDiaria;
                System.out.println(" Días de mora: " + diasMora + " | Mora diaria: $" + moraDiaria);
                return mora;
            }
            
            return 0.0;
            
        } catch (IllegalArgumentException e) {
            System.out.println(" Error en cálculo de mora: " + e.getMessage());
            return 0.0;
        } catch (Exception e) {
            System.out.println(" Error inesperado al calcular mora: " + e.getMessage());
            return 0.0;
        }
    }
    
    public List<Prestamo> listarPrestamosActivos() {
        try {
            System.out.println(" Listando préstamos activos...");
            
            List<Prestamo> prestamosActivos = new ArrayList<>();
            for (Prestamo prestamo : prestamos) {
                if ("ACTIVO".equals(prestamo.getEstado())) {
                    prestamosActivos.add(prestamo);
                }
            }
            
            System.out.println("✅ " + prestamosActivos.size() + " préstamos activos encontrados");
            return prestamosActivos;
            
        } catch (Exception e) {
            System.out.println(" Error al listar préstamos activos: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public List<Prestamo> listarPrestamosPorUsuario(int usuarioId) {
        try {
            System.out.println(" Listando préstamos del usuario ID: " + usuarioId);
            
            List<Prestamo> prestamosUsuario = new ArrayList<>();
            for (Prestamo prestamo : prestamos) {
                if (prestamo.getIdUsuario()== usuarioId) {
                    prestamosUsuario.add(prestamo);
                }
            }
            
            System.out.println("✅ " + prestamosUsuario.size() + " préstamos encontrados para el usuario");
            return prestamosUsuario;
            
        } catch (Exception e) {
            System.out.println("Error al listar préstamos por usuario: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // ==================== MÉTODOS PRIVADOS AUXILIARES ====================
    
    private int contarPrestamosActivos(int usuarioId) {
        try {
            int count = 0;
            for (Prestamo prestamo : prestamos) {
                if (prestamo.getIdUsuario()== usuarioId && "ACTIVO".equals(prestamo.getEstado())) {
                    count++;
                }
            }
            return count;
            
        } catch (Exception e) {
            System.out.println(" Error al contar préstamos activos: " + e.getMessage());
            return 0;
        }
    }
    
    private Prestamo buscarPrestamoPorId(int prestamoId) {
    try {
        for (Prestamo prestamo : prestamos) {
           
            if (prestamo.getId() == prestamoId) {
                return prestamo;
            }
        }
        return null;
        
    } catch (Exception e) {
        System.out.println("⚠️ Error al buscar préstamo por ID: " + e.getMessage());
        return null;
    }
}
    
    // ==================== MÉTODOS DE CONFIGURACIÓN ====================
    
    public void configurarSistema(int maxPrestamos, double moraDiaria, int diasPrestamo) {
        try {
            this.maxPrestamos = maxPrestamos;
            this.moraDiaria = moraDiaria;
            this.diasPrestamo = diasPrestamo;
            
            System.out.println("Configuración del sistema actualizada:");
            System.out.println(" Máximo de préstamos: " + maxPrestamos);
            System.out.println(" Mora diaria: $" + moraDiaria);
            System.out.println(" Días de préstamo: " + diasPrestamo);
            
        } catch (Exception e) {
            System.out.println(" Error al configurar sistema: " + e.getMessage());
        }
    }
    
    public void mostrarConfiguracion() {
        try {
            System.out.println(" Configuración actual del sistema:");
            System.out.println(" Máximo de préstamos por usuario: " + maxPrestamos);
            System.out.println(" Mora diaria: $" + moraDiaria);
            System.out.println(" Días de préstamo permitidos: " + diasPrestamo);
            
        } catch (Exception e) {
            System.out.println(" Error al mostrar configuración: " + e.getMessage());
        }
    }
}

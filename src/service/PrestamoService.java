/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;
import Model.Prestamo;
import Model.Usuario;
import Model.Ejemplar;
import repository.PrestamoRepository;
import repository.PrestamoRepositoryImpl;
import repository.UsuarioRepository;
import repository.UsuarioRepositoryImpl;
import repository.EjemplarRepository;
import repository.EjemplarRepositoryImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
 /*
 * @author josed
 */
public class PrestamoService {
    private PrestamoRepository prestamoRepository;
    private UsuarioRepository usuarioRepository;
    private EjemplarRepository ejemplarRepository;
    private double moraDiaria = 2.50;
    private int diasPrestamo = 15;
    private int maxPrestamos = 3;
    
    public PrestamoService() {
        try {
            this.prestamoRepository = new PrestamoRepositoryImpl();
            this.usuarioRepository = new UsuarioRepositoryImpl();
            this.ejemplarRepository = new EjemplarRepositoryImpl();
            System.out.println("✅ PrestamoService inicializado con Base de Datos");
            
        } catch (Exception e) {
            System.out.println("❌ Error crítico al inicializar PrestamoService: " + e.getMessage());
        }
    }
    
    // 
    
    public boolean realizarPrestamo(int usuarioId, int ejemplarId) {
        try {
            System.out.println("📖 Intentando realizar préstamo...");
            System.out.println("   👤 Usuario ID: " + usuarioId);
            System.out.println("   📚 Ejemplar ID: " + ejemplarId);
            
            // 1. VALIDAR USUARIO
            Usuario usuario = usuarioRepository.findById(usuarioId);
            if (usuario == null) {
                throw new RuntimeException("Usuario no encontrado con ID: " + usuarioId);
            }
            
            // 2. VALIDAR QUE USUARIO NO TENGA MORA
            if (usuario.getMora() > 0) {
                throw new RuntimeException("Usuario tiene mora pendiente de $" + usuario.getMora() + ". No puede realizar préstamos");
            }
            
            // 3. VALIDAR LÍMITE DE PRÉSTAMOS
            List<Prestamo> prestamosActivosUsuario = prestamoRepository.findActivosByUsuario(usuarioId);
            if (prestamosActivosUsuario.size() >= maxPrestamos) {
                throw new RuntimeException("Límite de préstamos alcanzado. Máximo permitido: " + maxPrestamos);
            }
            
            // 4. VALIDAR EJEMPLAR
            Ejemplar ejemplar = ejemplarRepository.findById(ejemplarId);
            if (ejemplar == null) {
                throw new RuntimeException("Ejemplar no encontrado con ID: " + ejemplarId);
            }
            
            // 5. VALIDAR DISPONIBILIDAD
            if (ejemplar.getCantidadDisponible() <= 0) {
                throw new RuntimeException("Ejemplar no disponible: " + ejemplar.getTitulo());
            }
            
            // 6. CREAR PRÉSTAMO
            Prestamo prestamo = new Prestamo();
            prestamo.setIdUsuario(usuarioId);
            prestamo.setIdEjemplar(ejemplarId);
            prestamo.setFechadePrestamo(new Date());
            prestamo.setEstado("ACTIVO");
            prestamo.setMora(0.0);
            
            // 7. GUARDAR PRÉSTAMO EN BD
            Prestamo prestamoGuardado = prestamoRepository.save(prestamo);
            if (prestamoGuardado == null) {
                throw new RuntimeException("Error al guardar el préstamo en la base de datos");
            }
            
            // 8. ACTUALIZAR DISPONIBILIDAD DEL EJEMPLAR
            boolean cantidadActualizada = ejemplarRepository.updateCantidades(
                ejemplarId,
                ejemplar.getCantidadTotal(),
                ejemplar.getCantidadDisponible() - 1
            );
            
            if (!cantidadActualizada) {
                throw new RuntimeException("Error al actualizar la disponibilidad del ejemplar");
            }
            
            System.out.println("✅ Préstamo realizado exitosamente");
            System.out.println("   📅 Fecha de préstamo: " + prestamoGuardado.getFechadePrestamo());
            System.out.println("   📚 Ejemplar: " + ejemplar.getTitulo());
            System.out.println("   👤 Usuario: " + usuario.getNombre());
            System.out.println("   🆔 Préstamo ID: " + prestamoGuardado.getId());
            
            return true;
            
        } catch (RuntimeException e) {
            System.out.println("❌ Error al realizar préstamo: " + e.getMessage());
            return false;
        } finally {
            System.out.println("--- Operación de préstamo finalizada ---");
        }
    }
    
    public boolean registrarDevolucion(int prestamoId) {
        try {
            System.out.println("📗 Intentando registrar devolución...");
            System.out.println("   📋 Préstamo ID: " + prestamoId);
            
            // 1. BUSCAR PRÉSTAMO
            Prestamo prestamo = prestamoRepository.findById(prestamoId);
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
            
            // 5. ACTUALIZAR PRÉSTAMO EN BD
            Prestamo prestamoActualizado = prestamoRepository.update(prestamo);
            if (prestamoActualizado == null) {
                throw new RuntimeException("Error al actualizar el préstamo en la base de datos");
            }
            
            // 6. ACTUALIZAR DISPONIBILIDAD DEL EJEMPLAR
            Ejemplar ejemplar = ejemplarRepository.findById(prestamo.getIdEjemplar());
            if (ejemplar != null) {
                boolean cantidadActualizada = ejemplarRepository.updateCantidades(
                    prestamo.getIdEjemplar(),
                    ejemplar.getCantidadTotal(),
                    ejemplar.getCantidadDisponible() + 1
                );
                
                if (!cantidadActualizada) {
                    throw new RuntimeException("Error al actualizar la disponibilidad del ejemplar");
                }
            }
            
            // 7. ACTUALIZAR MORA DEL USUARIO SI APLICA
            if (mora > 0) {
                boolean moraActualizada = usuarioRepository.updateMora(prestamo.getIdUsuario(), mora);
                if (!moraActualizada) {
                    System.out.println("⚠️ Advertencia: No se pudo actualizar la mora del usuario");
                }
                System.out.println("   ⚠️ Mora aplicada: $" + mora);
            }
            
            System.out.println("✅ Devolución registrada exitosamente");
            System.out.println("   📅 Fecha de devolución: " + prestamo.getFechaDeDevolucion());
            System.out.println("   💰 Mora calculada: $" + mora);
            
            return true;
            
        } catch (RuntimeException e) {
            System.out.println("❌ Error al registrar devolución: " + e.getMessage());
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
                System.out.println("   📅 Días de mora: " + diasMora + " | Mora diaria: $" + moraDiaria);
                return mora;
            }
            
            return 0.0;
            
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Error en cálculo de mora: " + e.getMessage());
            return 0.0;
        } catch (Exception e) {
            System.out.println("❌ Error inesperado al calcular mora: " + e.getMessage());
            return 0.0;
        }
    }
    
    public List<Prestamo> listarPrestamosActivos() {
        try {
            System.out.println("📋 Listando préstamos activos desde BD...");
            return prestamoRepository.findPrestamosActivos();
            
        } catch (Exception e) {
            System.out.println("❌ Error al listar préstamos activos: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public List<Prestamo> listarPrestamosPorUsuario(int usuarioId) {
        try {
            System.out.println("👤 Listando préstamos del usuario ID: " + usuarioId);
            return prestamoRepository.findByUsuario(usuarioId);
            
        } catch (Exception e) {
            System.out.println("❌ Error al listar préstamos por usuario: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // ELIMINAR: private int contarPrestamosActivos() - Ya no necesitamos esto
    
    // ELIMINAR: private Prestamo buscarPrestamoPorId() - Ya usamos el repository
    
    // ==================== MÉTODOS DE CONFIGURACIÓN ====================
    
    public void configurarSistema(int maxPrestamos, double moraDiaria, int diasPrestamo) {
        try {
            this.maxPrestamos = maxPrestamos;
            this.moraDiaria = moraDiaria;
            this.diasPrestamo = diasPrestamo;
            
            System.out.println("⚙️ Configuración del sistema actualizada:");
            System.out.println("   📚 Máximo de préstamos: " + maxPrestamos);
            System.out.println("   💰 Mora diaria: $" + moraDiaria);
            System.out.println("   📅 Días de préstamo: " + diasPrestamo);
            
        } catch (Exception e) {
            System.out.println("❌ Error al configurar sistema: " + e.getMessage());
        }
    }
    
    public void mostrarConfiguracion() {
        try {
            System.out.println("📊 Configuración actual del sistema:");
            System.out.println("   📚 Máximo de préstamos por usuario: " + maxPrestamos);
            System.out.println("   💰 Mora diaria: $" + moraDiaria);
            System.out.println("   📅 Días de préstamo permitidos: " + diasPrestamo);
            
        } catch (Exception e) {
            System.out.println("❌ Error al mostrar configuración: " + e.getMessage());
        }
    }
}
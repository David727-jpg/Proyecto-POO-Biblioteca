/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

/**
 *
 * @author josed
 */
import Model.Usuario;
import repository.UsuarioRepository;
import repository.UsuarioRepositoryImpl;
        
public class AuthService {
    private UsuarioRepository usuarioRepository;
    
    public AuthService() {
        this.usuarioRepository = new UsuarioRepositoryImpl();
    }
    
    // MÉTODO LOGIN PRINCIPAL ACTUALIZADO
    public Usuario login(String email, String password) {
        try {
            System.out.println("🔐 Intentando login para: " + email);
            
            // ==================== VALIDACIONES ====================
            if (email == null || email.isEmpty()) {
                throw new IllegalArgumentException("El email no puede estar vacío");
            }
            if (password == null || password.isEmpty()) {
                throw new IllegalArgumentException("La contraseña no puede estar vacía");
            }
            if (!email.contains("@")) {
                throw new IllegalArgumentException("El email debe contener @");
            }
            
            // ==================== BUSCAR USUARIO EN BD ====================
            Usuario usuario = usuarioRepository.findByEmail(email);
            
            if (usuario == null) {
                throw new RuntimeException("Credenciales incorrectas - Verifique email y contraseña");
            }
            
            // ==================== VALIDAR CONTRASEÑA ====================
            if (!usuario.getPassword().equals(password)) {
                throw new RuntimeException("Credenciales incorrectas - Verifique email y contraseña");
            }
            
            // ==================== VALIDAR ESTADO ====================
            if (!usuario.isActivo()) {
                throw new RuntimeException("Usuario inactivo - Contacte al administrador del sistema");
            }
            
            // ==================== LOGIN EXITOSO ====================
            System.out.println("✅ Login exitoso: " + usuario.getNombre() + " (" + usuario.getTipo() + ")");
            mostrarPrivilegios(usuario);
            registrarIntentoLogin(true, email);
            
            return usuario;
            
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Error de validación: " + e.getMessage());
            registrarIntentoLogin(false, email);
            return null;
            
        } catch (RuntimeException e) {
            System.out.println("❌ Error de autenticación: " + e.getMessage());
            registrarIntentoLogin(false, email);
            return null;
            
        } finally {
            System.out.println("--- Proceso de login finalizado ---");
        }
    }
    
    // ELIMINAR: private Usuario buscarUsuarioEnMemoria() - Ya no necesitamos esto
    
    // ==================== MÉTODO MOSTRAR PRIVILEGIOS ====================
    private void mostrarPrivilegios(Usuario usuario) {
        try {
            System.out.println("🎯 Privilegios de " + usuario.getTipo() + ":");
            
            String tipo = usuario.getTipo().toUpperCase();
            switch (tipo) {
                case "ADMIN":
                    System.out.println("   • 👥 Gestionar usuarios (crear, editar, eliminar)");
                    System.out.println("   • 📚 Gestionar ejemplares (agregar, modificar)");
                    System.out.println("   • 📖 Ver todos los préstamos del sistema");
                    System.out.println("   • ⚙️ Configurar parámetros del sistema");
                    System.out.println("   • 📊 Generar reportes y estadísticas");
                    break;
                    
                case "PROFESOR":
                    System.out.println("   • 📖 Realizar préstamos de ejemplares");
                    System.out.println("   • 🔍 Consultar ejemplares disponibles");
                    System.out.println("   • 👀 Ver historial de préstamos propios");
                    System.out.println("   • 📋 Ver ejemplares prestados actualmente");
                    break;
                    
                case "ALUMNO":
                    System.out.println("   • 🔍 Consultar ejemplares disponibles");
                    System.out.println("   • 👀 Ver préstamos propios");
                    System.out.println("   • ⏰ Ver fechas de devolución");
                    break;
                    
                default:
                    System.out.println("   • 🔒 Privilegios básicos de consulta");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error al mostrar privilegios: " + e.getMessage());
        }
    }
    
    // ==================== MÉTODO REGISTRAR INTENTO LOGIN ====================
    private void registrarIntentoLogin(boolean exitoso, String email) {
        try {
            String estado = exitoso ? "EXITOSO" : "FALLIDO";
            String emoji = exitoso ? "✅" : "❌";
            System.out.println(emoji + " Registro de seguridad: Login " + estado + " para " + email);
            
        } catch (Exception e) {
            System.out.println("⚠️ Error en registro de seguridad");
        }
    }
    
    // ==================== MÉTODO CERRAR SESIÓN ====================
    public void logout(Usuario usuario) {
        try {
            if (usuario != null) {
                System.out.println("🚪 Cerrando sesión de: " + usuario.getNombre());
                System.out.println("📝 Sesión finalizada - " + new java.util.Date());
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ Error al cerrar sesión: " + e.getMessage());
            
        } finally {
            System.out.println("--- Proceso de logout completado ---");
        }
    }
}
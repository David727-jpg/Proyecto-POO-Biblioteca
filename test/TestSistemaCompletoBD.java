

import service.*;
import Model.*;
import java.util.List;

public class TestSistemaCompletoBD {
    public static void main(String[] args) {
        System.out.println("🧪 INICIANDO PRUEBA COMPLETA DEL SISTEMA CON BASE DE DATOS 🧪\n");
        
        // 1. TEST DE AUTENTICACIÓN
        testAutenticacion();
        
        // 2. TEST DE PRÉSTAMOS COMPLETOS
        testPrestamosCompletos();
        
        // 3. TEST DE CONSULTAS
        testConsultas();
        
        System.out.println("\n🎉 PRUEBA COMPLETA DEL SISTEMA CON BD FINALIZADA 🎉");
    }
    
    public static void testAutenticacion() {
        System.out.println("\n🔐 ===== TEST AUTENTICACIÓN CON BD =====");
        AuthService auth = new AuthService();
        
        // Test 1: Login exitoso ADMIN
        System.out.println("\n📋 Test 1: Login ADMIN con BD");
        Usuario admin = auth.login("admin@udb.edu", "1234");
        if (admin != null && "ADMIN".equals(admin.getTipo())) {
            System.out.println("✅ PASS - Admin login correcto con BD");
            auth.logout(admin);
        } else {
            System.out.println("❌ FAIL - Admin login falló con BD");
        }
        
        // Test 2: Login exitoso PROFESOR
        System.out.println("\n📋 Test 2: Login PROFESOR con BD");
        Usuario profesor = auth.login("profesor@udb.edu", "1234");
        if (profesor != null && "PROFESOR".equals(profesor.getTipo())) {
            System.out.println("✅ PASS - Profesor login correcto con BD");
        } else {
            System.out.println("❌ FAIL - Profesor login falló con BD");
        }
        
        // Test 3: Login fallido
        System.out.println("\n📋 Test 3: Login fallido con BD");
        Usuario fallido = auth.login("admin@udb.edu", "wrongpass");
        if (fallido == null) {
            System.out.println("✅ PASS - Login fallido manejado correctamente con BD");
        } else {
            System.out.println("❌ FAIL - Login debería haber fallado con BD");
        }
    }
    
    public static void testPrestamosCompletos() {
        System.out.println("\n📖 ===== TEST PRÉSTAMOS COMPLETOS CON BD =====");
        PrestamoService prestamoService = new PrestamoService();
        EjemplarService ejemplarService = new EjemplarService();
        
        // Estado inicial
        System.out.println("\n📊 Estado inicial:");
        List<Prestamo> prestamosIniciales = prestamoService.listarPrestamosActivos();
        System.out.println("   Préstamos activos: " + prestamosIniciales.size());
        
        List<Ejemplar> disponiblesInicial = ejemplarService.buscarEjemplaresDisponibles();
        System.out.println("   Ejemplares disponibles: " + disponiblesInicial.size());
        
        // Realizar préstamo
        System.out.println("\n📋 Realizando préstamo...");
        boolean prestamoExitoso = prestamoService.realizarPrestamo(2, 1); // Profesor pide libro
        if (prestamoExitoso) {
            System.out.println("✅ PASS - Préstamo realizado exitosamente con BD");
            
            // Verificar estado después del préstamo
            List<Prestamo> prestamosDespues = prestamoService.listarPrestamosActivos();
            List<Ejemplar> disponiblesDespues = ejemplarService.buscarEjemplaresDisponibles();
            
            System.out.println("   Préstamos activos después: " + prestamosDespues.size());
            System.out.println("   Ejemplares disponibles después: " + disponiblesDespues.size());
            
            if (!prestamosDespues.isEmpty()) {
                // Registrar devolución
                Prestamo prestamo = prestamosDespues.get(0);
                System.out.println("\n📋 Registrando devolución del préstamo ID: " + prestamo.getId());
                boolean devolucionExitosa = prestamoService.registrarDevolucion(prestamo.getId());
                
                if (devolucionExitosa) {
                    System.out.println("✅ PASS - Devolución registrada exitosamente con BD");
                    
                    // Verificar estado final
                    List<Prestamo> prestamosFinal = prestamoService.listarPrestamosActivos();
                    System.out.println("   Préstamos activos finales: " + prestamosFinal.size());
                } else {
                    System.out.println("❌ FAIL - Devolución falló con BD");
                }
            }
        } else {
            System.out.println("❌ FAIL - Préstamo falló con BD");
        }
    }
    
    public static void testConsultas() {
        System.out.println("\n📊 ===== TEST CONSULTAS CON BD =====");
        UsuarioService usuarioService = new UsuarioService();
        EjemplarService ejemplarService = new EjemplarService();
        
        // Consultar usuarios
        System.out.println("\n📋 Consultando usuarios...");
        List<Usuario> usuarios = usuarioService.listarTodosUsuarios();
        System.out.println("✅ Usuarios en BD: " + usuarios.size());
        
        // Consultar ejemplares
        System.out.println("\n📋 Consultando ejemplares...");
        List<Ejemplar> ejemplares = ejemplarService.listarTodosEjemplares();
        System.out.println("✅ Ejemplares en BD: " + ejemplares.size());
        
        // Consultar disponibles
        System.out.println("\n📋 Consultando ejemplares disponibles...");
        List<Ejemplar> disponibles = ejemplarService.buscarEjemplaresDisponibles();
        System.out.println("✅ Ejemplares disponibles: " + disponibles.size());
        
        // Búsqueda por título
        System.out.println("\n📋 Búsqueda por título...");
        List<Ejemplar> resultados = ejemplarService.buscarEjemplarPorTitulo("soledad");
        System.out.println("✅ Resultados búsqueda: " + resultados.size());
    }
}
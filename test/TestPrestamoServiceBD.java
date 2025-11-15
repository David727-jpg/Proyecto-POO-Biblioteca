
import service.PrestamoService;
import Model.Prestamo;
import java.util.List;

public class TestPrestamoServiceBD {
    public static void main(String[] args) {
        System.out.println("🧪 Probando PrestamoService con Base de Datos...");
        
        PrestamoService prestamoService = new PrestamoService();
        
        // Test 1: Listar préstamos activos (inicialmente debería estar vacío)
        System.out.println("\n📋 Test 1: Listar préstamos activos iniciales");
        List<Prestamo> prestamosIniciales = prestamoService.listarPrestamosActivos();
        System.out.println("✅ Préstamos activos iniciales: " + prestamosIniciales.size());
        
        // Test 2: Realizar un préstamo
        System.out.println("\n📋 Test 2: Realizar préstamo (Usuario 2 → Ejemplar 1)");
        boolean prestamoExitoso = prestamoService.realizarPrestamo(2, 1);
        if (prestamoExitoso) {
            System.out.println("✅ Préstamo realizado exitosamente");
        } else {
            System.out.println("❌ No se pudo realizar el préstamo");
        }
        
        // Test 3: Verificar préstamos activos después del préstamo
        System.out.println("\n📋 Test 3: Verificar préstamos activos después del préstamo");
        List<Prestamo> prestamosDespues = prestamoService.listarPrestamosActivos();
        System.out.println("✅ Préstamos activos después: " + prestamosDespues.size());
        
        if (!prestamosDespues.isEmpty()) {
            Prestamo prestamoCreado = prestamosDespues.get(0);
            System.out.println("   - Préstamo ID: " + prestamoCreado.getId() + 
                             " | Usuario: " + prestamoCreado.getIdUsuario() + 
                             " | Ejemplar: " + prestamoCreado.getIdEjemplar());
            
            // Test 4: Registrar devolución
            System.out.println("\n📋 Test 4: Registrar devolución del préstamo ID: " + prestamoCreado.getId());
            boolean devolucionExitosa = prestamoService.registrarDevolucion(prestamoCreado.getId());
            if (devolucionExitosa) {
                System.out.println("✅ Devolución registrada exitosamente");
            } else {
                System.out.println("❌ No se pudo registrar la devolución");
            }
            
            // Test 5: Verificar préstamos activos finales
            System.out.println("\n📋 Test 5: Verificar préstamos activos finales");
            List<Prestamo> prestamosFinales = prestamoService.listarPrestamosActivos();
            System.out.println("✅ Préstamos activos finales: " + prestamosFinales.size());
        }
        
        System.out.println("\n🎉 Pruebas de PrestamoService con BD completadas");
    }
}
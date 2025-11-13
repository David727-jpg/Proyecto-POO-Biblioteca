
import service.*;
import Model.*;
import java.util.List;

public class TestCompletoSistema {
    
    public static void main(String[] args) {
        System.out.println("🧪 INICIANDO PRUEBAS COMPLETAS DEL SISTEMA 🧪\n");
        
        // 1. TEST DE AUTENTICACIÓN
        testAutenticacion();
        
        // 2. TEST DE USUARIOS
        testUsuarios();
        
        // 3. TEST DE EJEMPLARES
        testEjemplares();
        
        // 4. TEST DE PRÉSTAMOS
        testPrestamos();
        
        // 5. TEST DE DEVOLUCIONES Y MORA
        testDevolucionesYMora();
        
        System.out.println("\n🎉 PRUEBAS COMPLETADAS 🎉");
    }
    
    // ==================== 1. TEST AUTENTICACIÓN ====================
    public static void testAutenticacion() {
        System.out.println("\n🔐 ===== TEST AUTENTICACIÓN =====");
        AuthService auth = new AuthService();
        
        // Test 1.1: Login exitoso ADMIN
        System.out.println("\n📋 Test 1.1: Login ADMIN");
        Usuario admin = auth.login("admin@udb.edu", "1234");
        if (admin != null && "ADMIN".equals(admin.getTipo())) {
            System.out.println("✅ PASS - Admin login correcto");
        } else {
            System.out.println("❌ FAIL - Admin login falló");
        }
        
        // Test 1.2: Login exitoso PROFESOR
        System.out.println("\n📋 Test 1.2: Login PROFESOR");
        Usuario profesor = auth.login("profesor@udb.edu", "1234");
        if (profesor != null && "PROFESOR".equals(profesor.getTipo())) {
            System.out.println("✅ PASS - Profesor login correcto");
        } else {
            System.out.println("❌ FAIL - Profesor login falló");
        }
        
        // Test 1.3: Login exitoso ALUMNO
        System.out.println("\n📋 Test 1.3: Login ALUMNO");
        Usuario alumno = auth.login("alumno@udb.edu", "1234");
        if (alumno != null && "ALUMNO".equals(alumno.getTipo())) {
            System.out.println("✅ PASS - Alumno login correcto");
        } else {
            System.out.println("❌ FAIL - Alumno login falló");
        }
        
        // Test 1.4: Login fallido (credenciales incorrectas)
        System.out.println("\n📋 Test 1.4: Login fallido");
        Usuario fallido = auth.login("admin@udb.edu", "wrongpass");
        if (fallido == null) {
            System.out.println("✅ PASS - Login fallido manejado correctamente");
        } else {
            System.out.println("❌ FAIL - Login debería haber fallado");
        }
    }
    
    // ==================== 2. TEST USUARIOS ====================
    public static void testUsuarios() {
        System.out.println("\n👥 ===== TEST USUARIOS =====");
        UsuarioService usuarioService = new UsuarioService();
        
        // Test 2.1: Listar usuarios
        System.out.println("\n📋 Test 2.1: Listar usuarios");
        List<Usuario> usuarios = usuarioService.listarTodosUsuarios();
        if (usuarios.size() >= 3) {
            System.out.println("✅ PASS - " + usuarios.size() + " usuarios cargados");
        } else {
            System.out.println("❌ FAIL - Solo " + usuarios.size() + " usuarios cargados");
        }
        
        // Test 2.2: Buscar usuario por ID
        System.out.println("\n📋 Test 2.2: Buscar usuario por ID");
        Usuario usuario = usuarioService.buscarUsuarioPorId(1);
        if (usuario != null && usuario.getId() == 1) {
            System.out.println("✅ PASS - Usuario encontrado: " + usuario.getNombre());
        } else {
            System.out.println("❌ FAIL - Usuario no encontrado");
        }
        
        // Test 2.3: Verificar mora inicial
        System.out.println("\n📋 Test 2.3: Verificar mora inicial");
        boolean tieneMora = usuarioService.tieneMora(1);
        if (!tieneMora) {
            System.out.println("✅ PASS - Usuario sin mora inicial");
        } else {
            System.out.println("❌ FAIL - Usuario tiene mora inicial");
        }
    }
    
    // ==================== 3. TEST EJEMPLARES ====================
    public static void testEjemplares() {
        System.out.println("\n📚 ===== TEST EJEMPLARES =====");
        EjemplarService ejemplarService = new EjemplarService();
        
        // Test 3.1: Listar ejemplares
        System.out.println("\n📋 Test 3.1: Listar ejemplares");
        List<Ejemplar> ejemplares = ejemplarService.listarTodosEjemplares();
        if (ejemplares.size() >= 4) {
            System.out.println("✅ PASS - " + ejemplares.size() + " ejemplares cargados");
        } else {
            System.out.println("❌ FAIL - Solo " + ejemplares.size() + " ejemplares cargados");
        }
        
        // Test 3.2: Buscar ejemplar por ID
        System.out.println("\n📋 Test 3.2: Buscar ejemplar por ID");
        Ejemplar ejemplar = ejemplarService.buscarEjemplarPorId(1);
        if (ejemplar != null && ejemplar.getId() == 1) {
            System.out.println("✅ PASS - Ejemplar encontrado: " + ejemplar.getTitulo());
            System.out.println("   Disponibles: " + ejemplar.getCantidadDisponible() + "/" + ejemplar.getCantidadTotal());
        } else {
            System.out.println("❌ FAIL - Ejemplar no encontrado");
        }
        
        // Test 3.3: Buscar por título
        System.out.println("\n📋 Test 3.3: Buscar ejemplar por título");
        List<Ejemplar> resultados = ejemplarService.buscarEjemplarPorTitulo("soledad");
        if (!resultados.isEmpty()) {
            System.out.println("✅ PASS - Encontrados " + resultados.size() + " ejemplares");
        } else {
            System.out.println("❌ FAIL - No se encontraron ejemplares");
        }
    }
    
    
    
    
    
    // ==================== 4. TEST PRÉSTAMOS (VERSIÓN CORREGIDA) ====================
public static void testPrestamos() {
    System.out.println("\n📖 ===== TEST PRÉSTAMOS =====");
    PrestamoService prestamoService = new PrestamoService();
    
    // VERIFICAR ANTES de hacer préstamo
    System.out.println("\n📋 Estado inicial:");
    List<Prestamo> prestamosIniciales = prestamoService.listarPrestamosActivos();
    System.out.println("   Préstamos activos iniciales: " + prestamosIniciales.size());
    
    // Test 4.1: Realizar préstamo exitoso
    System.out.println("\n📋 Test 4.1: Realizar préstamo");
    System.out.println("   Intentando: Usuario 2 → Ejemplar 1");
    boolean prestamoExitoso = prestamoService.realizarPrestamo(2, 1); // Profesor pide libro
    
    if (prestamoExitoso) {
        System.out.println("✅ PASS - Préstamo realizado exitosamente");
        
        // VERIFICAR DESPUÉS del préstamo
        System.out.println("\n📋 Estado después del préstamo:");
        List<Prestamo> prestamosDespues = prestamoService.listarPrestamosActivos();
        System.out.println("   Préstamos activos después: " + prestamosDespues.size());
        
        // Mostrar detalles del préstamo creado
        for (Prestamo p : prestamosDespues) {
            System.out.println("   - Préstamo ID: " + p.getId() + 
                             " | Usuario: " + p.getIdUsuario() + 
                             " | Ejemplar: " + p.getIdEjemplar());
        }
        
    } else {
        System.out.println("❌ FAIL - No se pudo realizar el préstamo");
        // Aquí necesitamos saber POR QUÉ falló
        diagnosticarFalloPrestamo(prestamoService, 2, 1);
    }
    
    // Los otros tests solo si el préstamo fue exitoso
    if (prestamoExitoso) {
        testPrestamosParte2(prestamoService);
    }
}

// Método auxiliar para diagnosticar por qué falla el préstamo
private static void diagnosticarFalloPrestamo(PrestamoService prestamoService, int usuarioId, int ejemplarId) {
    System.out.println("\n🔍 DIAGNÓSTICO DE FALLO EN PRÉSTAMO:");
    
    try {
        UsuarioService usuarioService = new UsuarioService();
        EjemplarService ejemplarService = new EjemplarService();
        
        // 1. Verificar usuario
        Usuario usuario = usuarioService.buscarUsuarioPorId(usuarioId);
        if (usuario == null) {
            System.out.println("   ❌ Usuario ID " + usuarioId + " no encontrado");
        } else {
            System.out.println("   ✅ Usuario encontrado: " + usuario.getNombre());
            
            // Verificar mora
            boolean tieneMora = usuarioService.tieneMora(usuarioId);
            System.out.println("   ℹ️  Usuario tiene mora: " + tieneMora);
            
            // Verificar préstamos activos
            int prestamosActivos = 0;
            List<Prestamo> todosPrestamos = prestamoService.listarPrestamosActivos();
            for (Prestamo p : todosPrestamos) {
                if (p.getIdUsuario() == usuarioId) {
                    prestamosActivos++;
                }
            }
            System.out.println("   ℹ️  Préstamos activos del usuario: " + prestamosActivos);
        }
        
        // 2. Verificar ejemplar
        Ejemplar ejemplar = ejemplarService.buscarEjemplarPorId(ejemplarId);
        if (ejemplar == null) {
            System.out.println("   ❌ Ejemplar ID " + ejemplarId + " no encontrado");
        } else {
            System.out.println("   ✅ Ejemplar encontrado: " + ejemplar.getTitulo());
            System.out.println("   ℹ️  Disponibilidad: " + ejemplar.getCantidadDisponible() + "/" + ejemplar.getCantidadTotal());
        }
        
    } catch (Exception e) {
        System.out.println("   ❌ Error en diagnóstico: " + e.getMessage());
    }
}

// Segunda parte de tests de préstamos (solo si el préstamo fue exitoso)
private static void testPrestamosParte2(PrestamoService prestamoService) {
    // Test 4.2: Listar préstamos activos
    System.out.println("\n📋 Test 4.2: Listar préstamos activos");
    List<Prestamo> prestamosActivos = prestamoService.listarPrestamosActivos();
    if (!prestamosActivos.isEmpty()) {
        System.out.println("✅ PASS - " + prestamosActivos.size() + " préstamos activos");
    } else {
        System.out.println("❌ FAIL - No hay préstamos activos");
    }
    
    // Test 4.3: Verificar disponibilidad actualizada
    System.out.println("\n📋 Test 4.3: Verificar disponibilidad actualizada");
    EjemplarService ejemplarService = new EjemplarService();
    Ejemplar ejemplar = ejemplarService.buscarEjemplarPorId(1);
    if (ejemplar != null && ejemplar.getCantidadDisponible() < ejemplar.getCantidadTotal()) {
        System.out.println("✅ PASS - Disponibilidad actualizada: " + 
                         ejemplar.getCantidadDisponible() + "/" + ejemplar.getCantidadTotal());
    } else {
        System.out.println("❌ FAIL - Disponibilidad no se actualizó");
    }
    
    // Test 4.4: Listar préstamos por usuario
    System.out.println("\n📋 Test 4.4: Listar préstamos por usuario");
    List<Prestamo> prestamosUsuario = prestamoService.listarPrestamosPorUsuario(2);
    if (!prestamosUsuario.isEmpty()) {
        System.out.println("✅ PASS - Usuario tiene " + prestamosUsuario.size() + " préstamos");
    } else {
        System.out.println("❌ FAIL - Usuario no tiene préstamos");
    }
}
    
    
    



    
    // ==================== TEST DIAGNÓSTICO ====================
public static void testDiagnosticoDevolucion() {
    System.out.println("\n🔍 ===== TEST DIAGNÓSTICO DEVOLUCIÓN =====");
    PrestamoService prestamoService = new PrestamoService();
    
    // 1. Verificar préstamos existentes
    System.out.println("\n📋 1. Préstamos existentes:");
    List<Prestamo> todosPrestamos = prestamoService.listarPrestamosActivos();
    System.out.println("   Préstamos activos: " + todosPrestamos.size());
    
    for (Prestamo p : todosPrestamos) {
        System.out.println("   - ID: " + p.getId() + 
                         " | Usuario: " + p.getIdUsuario() + 
                         " | Ejemplar: " + p.getIdEjemplar() +
                         " | Estado: " + p.getEstado());
    }
    
    // 2. Intentar devolución con ID correcto
    if (!todosPrestamos.isEmpty()) {
        int primerPrestamoId = todosPrestamos.get(0).getId();
        System.out.println("\n📋 2. Intentando devolución con ID real: " + primerPrestamoId);
        boolean resultado = prestamoService.registrarDevolucion(primerPrestamoId);
        System.out.println("   Resultado: " + (resultado ? "✅ ÉXITO" : "❌ FALLÓ"));
    } else {
        System.out.println("\n📋 2. No hay préstamos para devolver");
    }
}

// ==================== 5. TEST DEVOLUCIONES Y MORA (VERSIÓN CORREGIDA) ====================
public static void testDevolucionesYMora() {
    System.out.println("\n📗 ===== TEST DEVOLUCIONES Y MORA =====");
    
    // Usar la MISMA instancia de PrestamoService para mantener los datos
    PrestamoService prestamoService = new PrestamoService();
    UsuarioService usuarioService = new UsuarioService();
    EjemplarService ejemplarService = new EjemplarService();
    
    // Primero crear un préstamo para probar la devolución
    System.out.println("\n📋 Preparando: Creando préstamo para prueba de devolución");
    boolean prestamoCreado = prestamoService.realizarPrestamo(2, 1);
    
    if (!prestamoCreado) {
        System.out.println("❌ No se pudo crear préstamo para prueba de devolución");
        return;
    }
    
    // Obtener el ID del préstamo recién creado
    List<Prestamo> prestamosActivos = prestamoService.listarPrestamosActivos();
    if (prestamosActivos.isEmpty()) {
        System.out.println("❌ No hay préstamos activos para devolver");
        return;
    }
    
    int prestamoId = prestamosActivos.get(0).getId();
    System.out.println("   Préstamo creado con ID: " + prestamoId);
    
    // Test 5.1: Registrar devolución
    System.out.println("\n📋 Test 5.1: Registrar devolución del préstamo ID: " + prestamoId);
    boolean devolucionExitosa = prestamoService.registrarDevolucion(prestamoId);
    
    if (devolucionExitosa) {
        System.out.println("✅ PASS - Devolución registrada exitosamente");
    } else {
        System.out.println("❌ FAIL - No se pudo registrar la devolución");
    }
    
    // Test 5.2: Verificar mora después de devolución
    System.out.println("\n📋 Test 5.2: Verificar estado de mora");
    boolean tieneMora = usuarioService.tieneMora(2);
    System.out.println("ℹ️  Usuario tiene mora: " + tieneMora);
    
    // Test 5.3: Verificar disponibilidad después de devolución
    System.out.println("\n📋 Test 5.3: Verificar disponibilidad después de devolución");
    Ejemplar ejemplar = ejemplarService.buscarEjemplarPorId(1);
    if (ejemplar != null) {
        System.out.println("ℹ️  Disponibilidad final: " + 
                         ejemplar.getCantidadDisponible() + "/" + ejemplar.getCantidadTotal());
        // Debería haber aumentado a 3/5 después de la devolución
        if (ejemplar.getCantidadDisponible() == 3) {
            System.out.println("✅ PASS - Disponibilidad restaurada correctamente");
        } else {
            System.out.println("❌ FAIL - Disponibilidad no se restauró correctamente");
        }
    }
    
    // Test 5.4: Verificar que no hay préstamos activos después de devolución
    System.out.println("\n📋 Test 5.4: Verificar préstamos activos finales");
    List<Prestamo> prestamosFinales = prestamoService.listarPrestamosActivos();
    System.out.println("ℹ️  Préstamos activos finales: " + prestamosFinales.size());
    if (prestamosFinales.isEmpty()) {
        System.out.println("✅ PASS - Todos los préstamos fueron devueltos");
    } else {
        System.out.println("❌ FAIL - Aún hay préstamos activos");
    }
}
}
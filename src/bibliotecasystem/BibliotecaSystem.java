package bibliotecasystem;

import service.*;
import Model.*;
import java.util.List;
import java.util.Scanner;

public class BibliotecaSystem {
    private static Scanner scanner = new Scanner(System.in);
    private static AuthService authService = new AuthService();
    private static UsuarioService usuarioService = new UsuarioService();
    private static EjemplarService ejemplarService = new EjemplarService();
    private static PrestamoService prestamoService = new PrestamoService();
    private static Usuario usuarioLogueado = null;
    
    public static void main(String[] args) {
        System.out.println("📚 ===== SISTEMA DE BIBLIOTECA UDB =====");
        System.out.println("🎯 Versión: Sistema con Base de Datos MySQL");
        
        while (true) {
            if (usuarioLogueado == null) {
                mostrarMenuLogin();
            } else {
                mostrarMenuPrincipal();
            }
        }
    }
    
    // ==================== MENÚ LOGIN ====================
    private static void mostrarMenuLogin() {
        System.out.println("\n🔐 === LOGIN ===");
        System.out.println("1. Iniciar Sesión");
        System.out.println("2. Salir del Sistema");
        System.out.print("Seleccione una opción: ");
        
        int opcion = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer
        
        switch (opcion) {
            case 1:
                realizarLogin();
                break;
            case 2:
                System.out.println("👋 ¡Gracias por usar el sistema!");
                System.exit(0);
                break;
            default:
                System.out.println("❌ Opción inválida");
        }
    }
    
    private static void realizarLogin() {
        System.out.println("\n📧 === INICIAR SESIÓN ===");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();
        
        try {
            usuarioLogueado = authService.login(email, password);
            if (usuarioLogueado != null) {
                System.out.println("✅ ¡Bienvenido " + usuarioLogueado.getNombre() + "!");
            } else {
                System.out.println("❌ Credenciales incorrectas");
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
    
    // ==================== MENÚ PRINCIPAL ====================
    private static void mostrarMenuPrincipal() {
        System.out.println("\n🏠 === MENÚ PRINCIPAL ===");
        System.out.println("👤 Usuario: " + usuarioLogueado.getNombre() + " (" + usuarioLogueado.getTipo() + ")");
        System.out.println("1. 📚 Gestión de Ejemplares");
        System.out.println("2. 👥 Gestión de Usuarios");
        System.out.println("3. 📖 Préstamos y Devoluciones");
        System.out.println("4. 🔍 Consultas y Reportes");
        System.out.println("5. 🚪 Cerrar Sesión");
        System.out.print("Seleccione una opción: ");
        
        int opcion = scanner.nextInt();
        scanner.nextLine();
        
        switch (opcion) {
            case 1:
                menuGestionEjemplares();
                break;
            case 2:
                menuGestionUsuarios();
                break;
            case 3:
                menuPrestamos();
                break;
            case 4:
                menuConsultas();
                break;
            case 5:
                cerrarSesion();
                break;
            default:
                System.out.println("❌ Opción inválida");
        }
    }
    
    // ==================== MENÚ GESTIÓN DE EJEMPLARES ====================
    private static void menuGestionEjemplares() {
        while (true) {
            // Título diferente según el tipo de usuario
            if ("ADMIN".equals(usuarioLogueado.getTipo())) {
                System.out.println("\n📚 === GESTIÓN DE EJEMPLARES (ADMIN) ===");
            } else {
                System.out.println("\n📚 === CONSULTA DE EJEMPLARES ===");
            }
            
            System.out.println("1. 📋 Listar todos los ejemplares");
            System.out.println("2. 🔍 Buscar ejemplar por título");
            System.out.println("3. 📊 Ver ejemplares disponibles");
            
            // SOLO ADMIN ve estas opciones
            if ("ADMIN".equals(usuarioLogueado.getTipo())) {
                System.out.println("4. ➕ Agregar nuevo ejemplar");
                System.out.println("5. 🔧 Actualizar cantidades");
                System.out.println("6. ↩️ Volver al menú principal");
            } else {
                System.out.println("4. ↩️ Volver al menú principal");
            }
            
            System.out.print("Seleccione una opción: ");
            
            int opcion = scanner.nextInt();
            scanner.nextLine();
            
            // Procesar opción según permisos
            if ("ADMIN".equals(usuarioLogueado.getTipo())) {
                // MENÚ COMPLETO PARA ADMIN
                switch (opcion) {
                    case 1:
                        listarEjemplares();
                        break;
                    case 2:
                        buscarEjemplarPorTitulo();
                        break;
                    case 3:
                        listarEjemplaresDisponibles();
                        break;
                    case 4:
                        agregarEjemplar();
                        break;
                    case 5:
                        actualizarCantidadesEjemplar();
                        break;
                    case 6:
                        return;
                    default:
                        System.out.println("❌ Opción inválida");
                }
            } else {
                // MENÚ LIMITADO PARA PROFESOR/ALUMNO
                switch (opcion) {
                    case 1:
                        listarEjemplares();
                        break;
                    case 2:
                        buscarEjemplarPorTitulo();
                        break;
                    case 3:
                        listarEjemplaresDisponibles();
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("❌ Opción inválida");
                }
            }
        }
    }
    
    private static void listarEjemplares() {
        System.out.println("\n📋 === LISTA DE EJEMPLARES ===");
        List<Ejemplar> ejemplares = ejemplarService.listarTodosEjemplares();
        
        if (ejemplares.isEmpty()) {
            System.out.println("ℹ️ No hay ejemplares registrados");
            return;
        }
        //VERIFICAR METODO //////////
        for (int i = 0; i < ejemplares.size(); i++) {
            Ejemplar e = ejemplares.get(i);
            System.out.println((i + 1) + ". " + e.getTitulo() + " (" + e.getTipo() + ")");
            System.out.println("   📍 Ubicación: " + e.getUbicacion());
            System.out.println("   📊 Disponibles: " + e.getCantidadDisponible() + "/" + e.getCantidadTotal());
            if (e.getAutor() != null) System.out.println("   ✍️ Autor: " + e.getAutor());
            System.out.println();
        }
    }
    
    private static void buscarEjemplarPorTitulo() {
        System.out.print("\n🔍 Ingrese título a buscar: ");
        String titulo = scanner.nextLine();
        
        List<Ejemplar> resultados = ejemplarService.buscarEjemplarPorTitulo(titulo);
        
        if (resultados.isEmpty()) {
            System.out.println("❌ No se encontraron ejemplares");
            return;
        }
        
        System.out.println("✅ Se encontraron " + resultados.size() + " resultados:");
        for (Ejemplar e : resultados) {
            System.out.println("📚 " + e.getTitulo() + " | " + e.getTipo() + " | Disponibles: " + e.getCantidadDisponible());
        }
    }
    
    private static void agregarEjemplar() {
        // Validación EXTRA de seguridad
        if (!"ADMIN".equals(usuarioLogueado.getTipo())) {
            System.out.println("❌ Acceso denegado: Solo los administradores pueden agregar ejemplares");
            return;
        }
        
        System.out.println("\n➕ === AGREGAR NUEVO EJEMPLAR ===");
        
        Ejemplar nuevoEjemplar = new Ejemplar();
        
        System.out.print("Título: ");
        nuevoEjemplar.setTitulo(scanner.nextLine());
        
        System.out.print("Tipo (LIBRO/REVISTA/CD/TESIS): ");
        nuevoEjemplar.setTipo(scanner.nextLine().toUpperCase());
        
        System.out.print("Ubicación: ");
        nuevoEjemplar.setUbicacion(scanner.nextLine());
        
        System.out.print("Cantidad total: ");
        nuevoEjemplar.setCantidadTotal(scanner.nextInt());
        scanner.nextLine();
        
        nuevoEjemplar.setCantidadDisponible(nuevoEjemplar.getCantidadTotal());
        
        // Campos específicos según tipo
        if ("LIBRO".equals(nuevoEjemplar.getTipo())) {
            System.out.print("Autor: ");
            nuevoEjemplar.setAutor(scanner.nextLine());
            
            System.out.print("Editorial: ");
            nuevoEjemplar.setEditorial(scanner.nextLine());
            
            System.out.print("Año: ");
            nuevoEjemplar.setAnio(scanner.nextInt());
            scanner.nextLine();
        }
        
        if (ejemplarService.agregarEjemplar(nuevoEjemplar)) {
            System.out.println("✅ Ejemplar agregado exitosamente!");
        } else {
            System.out.println("❌ Error al agregar ejemplar");
        }
    }
    
    private static void listarEjemplaresDisponibles() {
        System.out.println("\n📊 === EJEMPLARES DISPONIBLES ===");
        List<Ejemplar> disponibles = ejemplarService.buscarEjemplaresDisponibles();
        
        if (disponibles.isEmpty()) {
            System.out.println("ℹ️ No hay ejemplares disponibles");
            return;
        }
        
        for (Ejemplar e : disponibles) {
            System.out.println("📚 " + e.getTitulo() + " | " + e.getTipo() + " | Ubicación: " + e.getUbicacion());
        }
    }
    
    private static void actualizarCantidadesEjemplar() {
        if (!"ADMIN".equals(usuarioLogueado.getTipo())) {
            System.out.println("❌ Acceso denegado");
            return;
        }
        
        System.out.println("\n🔧 === ACTUALIZAR CANTIDADES ===");
        
        // Listar ejemplares para que el ADMIN elija
        listarEjemplares();
        
        System.out.print("Ingrese ID del ejemplar a actualizar: ");
        int ejemplarId = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Nueva cantidad total: ");
        int cantidadTotal = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Nueva cantidad disponible: ");
        int cantidadDisponible = scanner.nextInt();
        scanner.nextLine();
        
        if (ejemplarService.actualizarCantidades(ejemplarId, cantidadTotal, cantidadDisponible)) {
            System.out.println("✅ Cantidades actualizadas exitosamente!");
        } else {
            System.out.println("❌ Error al actualizar cantidades");
        }
    }
    
    // ==================== MENÚ GESTIÓN DE USUARIOS ====================
    private static void menuGestionUsuarios() {
        // Solo ADMIN puede gestionar usuarios
        if (!"ADMIN".equals(usuarioLogueado.getTipo())) {
            System.out.println("❌ Solo los administradores pueden gestionar usuarios");
            return;
        }
        
        while (true) {
            System.out.println("\n👥 === GESTIÓN DE USUARIOS ===");
            System.out.println("1. 📋 Listar todos los usuarios");
            System.out.println("2. ➕ Crear nuevo usuario");
            System.out.println("3. 🔍 Buscar usuario por email");
            System.out.println("4. ↩️ Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            
            int opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1:
                    listarUsuarios();
                    break;
                case 2:
                    crearUsuario();
                    break;
                case 3:
                    buscarUsuarioPorEmail();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("❌ Opción inválida");
            }
        }
    }
    
    private static void listarUsuarios() {
        System.out.println("\n📋 === LISTA DE USUARIOS ===");
        List<Usuario> usuarios = usuarioService.listarTodosUsuarios();
        
        for (Usuario u : usuarios) {
            System.out.println("👤 " + u.getNombre() + " | " + u.getEmail() + " | " + u.getTipo());
            System.out.println("   💰 Mora: $" + u.getMora() + " | Activo: " + (u.isActivo() ? "✅" : "❌"));
            System.out.println();
        }
    }
    
    private static void crearUsuario() {
        System.out.println("\n➕ === CREAR NUEVO USUARIO ===");
        
        Usuario nuevoUsuario = new Usuario();
        
        System.out.print("Nombre: ");
        nuevoUsuario.setNombre(scanner.nextLine());
        
        System.out.print("Email: ");
        nuevoUsuario.setEmail(scanner.nextLine());
        
        System.out.print("Contraseña: ");
        nuevoUsuario.setPassword(scanner.nextLine());
        
        System.out.print("Tipo (ADMIN/PROFESOR/ALUMNO): ");
        nuevoUsuario.setTipo(scanner.nextLine().toUpperCase());
        
        nuevoUsuario.setMora(0.0);
        nuevoUsuario.setActivo(true);
        
        if (usuarioService.crearUsuario(nuevoUsuario)) {
            System.out.println("✅ Usuario creado exitosamente!");
        } else {
            System.out.println("❌ Error al crear usuario");
        }
    }
    
    private static void buscarUsuarioPorEmail() {
        System.out.print("\n🔍 Ingrese email a buscar: ");
        String email = scanner.nextLine();
        
        Usuario usuario = usuarioService.buscarUsuarioPorEmail(email);
        
        if (usuario != null) {
            System.out.println("✅ Usuario encontrado:");
            System.out.println("👤 Nombre: " + usuario.getNombre());
            System.out.println("📧 Email: " + usuario.getEmail());
            System.out.println("🎯 Tipo: " + usuario.getTipo());
            System.out.println("💰 Mora: $" + usuario.getMora());
        } else {
            System.out.println("❌ Usuario no encontrado");
        }
    }
    
    // ==================== MENÚ PRÉSTAMOS ====================
    private static void menuPrestamos() {
        while (true) {
            System.out.println("\n📖 === PRÉSTAMOS Y DEVOLUCIONES ===");
            System.out.println("1. 📚 Realizar préstamo");
            System.out.println("2. 📗 Registrar devolución");
            System.out.println("3. 📋 Ver mis préstamos activos");
            System.out.println("4. ↩️ Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            
            int opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1:
                    realizarPrestamo();
                    break;
                case 2:
                    registrarDevolucion();
                    break;
                case 3:
                    listarMisPrestamos();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("❌ Opción inválida");
            }
        }
    }
    
    private static void realizarPrestamo() {
        System.out.println("\n📚 === REALIZAR PRÉSTAMO ===");
        
        // Mostrar ejemplares disponibles
        List<Ejemplar> disponibles = ejemplarService.buscarEjemplaresDisponibles();
        if (disponibles.isEmpty()) {
            System.out.println("❌ No hay ejemplares disponibles para préstamo");
            return;
        }
        
        System.out.println("📊 Ejemplares disponibles:");
        for (int i = 0; i < disponibles.size(); i++) {
            Ejemplar e = disponibles.get(i);
            System.out.println((i + 1) + ". " + e.getTitulo() + " (" + e.getTipo() + ")");
        }
        
        System.out.print("Seleccione el número del ejemplar: ");
        int numeroEjemplar = scanner.nextInt();
        scanner.nextLine();
        
        if (numeroEjemplar < 1 || numeroEjemplar > disponibles.size()) {
            System.out.println("❌ Selección inválida");
            return;
        }
        
        Ejemplar ejemplarSeleccionado = disponibles.get(numeroEjemplar - 1);
        
        // Realizar préstamo
        if (prestamoService.realizarPrestamo(usuarioLogueado.getId(), ejemplarSeleccionado.getId())) {
            System.out.println("✅ Préstamo realizado exitosamente!");
        } else {
            System.out.println("❌ No se pudo realizar el préstamo");
        }
    }
    
    private static void registrarDevolucion() {
        System.out.println("\n📗 === REGISTRAR DEVOLUCIÓN ===");
        
        List<Prestamo> prestamosActivos = prestamoService.listarPrestamosActivos();
        List<Prestamo> misPrestamosActivos = prestamoService.listarPrestamosActivos();
        
        // Filtrar solo los préstamos activos del usuario actual
        misPrestamosActivos.removeIf(p -> p.getIdUsuario() != usuarioLogueado.getId());
        
        if (misPrestamosActivos.isEmpty()) {
            System.out.println("ℹ️ No tienes préstamos activos para devolver");
            return;
        }
        
        System.out.println("📋 Tus préstamos activos:");
        for (int i = 0; i < misPrestamosActivos.size(); i++) {
            Prestamo p = misPrestamosActivos.get(i);
            Ejemplar e = ejemplarService.buscarEjemplarPorId(p.getIdEjemplar());
            System.out.println((i + 1) + ". Préstamo ID: " + p.getId() + " | " + e.getTitulo());
        }
        
        System.out.print("Seleccione el número del préstamo a devolver: ");
        int numeroPrestamo = scanner.nextInt();
        scanner.nextLine();
        
        if (numeroPrestamo < 1 || numeroPrestamo > misPrestamosActivos.size()) {
            System.out.println("❌ Selección inválida");
            return;
        }
        
        Prestamo prestamoSeleccionado = misPrestamosActivos.get(numeroPrestamo - 1);
        
        if (prestamoService.registrarDevolucion(prestamoSeleccionado.getId())) {
            System.out.println("✅ Devolución registrada exitosamente!");
        } else {
            System.out.println("❌ Error al registrar devolución");
        }
    }
    
    private static void listarMisPrestamos() {
        System.out.println("\n📋 === MIS PRÉSTAMOS ACTIVOS ===");
        List<Prestamo> misPrestamos = prestamoService.listarPrestamosPorUsuario(usuarioLogueado.getId());
        List<Prestamo> misPrestamosActivos = prestamoService.listarPrestamosActivos();
        
        // Filtrar solo los préstamos activos del usuario
        misPrestamosActivos.removeIf(p -> p.getIdUsuario() != usuarioLogueado.getId());
        
        if (misPrestamosActivos.isEmpty()) {
            System.out.println("ℹ️ No tienes préstamos activos");
            return;
        }
        
        for (Prestamo p : misPrestamosActivos) {
            Ejemplar e = ejemplarService.buscarEjemplarPorId(p.getIdEjemplar());
            System.out.println("📚 " + e.getTitulo());
            System.out.println("   📅 Préstamo: " + p.getFechadePrestamo());
            System.out.println("   🆔 ID Préstamo: " + p.getId());
            System.out.println();
        }
    }
    
    // ==================== MENÚ CONSULTAS (CORREGIDO) ====================
    private static void menuConsultas() {
        while (true) {
            // Título diferente según permisos
            if ("ADMIN".equals(usuarioLogueado.getTipo())) {
                System.out.println("\n🔍 === CONSULTAS Y REPORTES (ADMIN) ===");
            } else {
                System.out.println("\n🔍 === MIS CONSULTAS ===");
            }
            
            System.out.println("1. 💰 Ver mi mora actual");
            System.out.println("2. 📋 Ver mis préstamos completos");
            System.out.println("3. 🔎 Buscar ejemplares por autor");
            
            // SOLO ADMIN ve estas opciones
            if ("ADMIN".equals(usuarioLogueado.getTipo())) {
                System.out.println("4. 📊 Ver todos los préstamos activos");
                System.out.println("5. 👥 Ver reporte de usuarios");
                System.out.println("6. ↩️ Volver al menú principal");
            } else {
                System.out.println("4. ↩️ Volver al menú principal");
            }
            
            System.out.print("Seleccione una opción: ");
            
            int opcion = scanner.nextInt();
            scanner.nextLine();
            
            // Procesar según permisos
            if ("ADMIN".equals(usuarioLogueado.getTipo())) {
                switch (opcion) {
                    case 1:
                        verMiMora();
                        break;
                    case 2:
                        listarMisPrestamosCompletos();
                        break;
                    case 3:
                        buscarPorAutor();
                        break;
                    case 4:
                        listarPrestamosActivosSistema();
                        break;
                    case 5:
                        generarReporteUsuarios();
                        break;
                    case 6:
                        return;
                    default:
                        System.out.println("❌ Opción inválida");
                }
            } else {
                switch (opcion) {
                    case 1:
                        verMiMora();
                        break;
                    case 2:
                        listarMisPrestamosCompletos();
                        break;
                    case 3:
                        buscarPorAutor();
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("❌ Opción inválida");
                }
            }
        }
    }
    
    private static void verMiMora() {
        boolean tieneMora = usuarioService.tieneMora(usuarioLogueado.getId());
        System.out.println("\n💰 === MI MORA ACTUAL ===");
        System.out.println("Mora actual: $" + usuarioLogueado.getMora());
        System.out.println("Estado: " + (tieneMora ? "❌ Tiene mora pendiente" : "✅ Al día"));
    }
    
    private static void listarMisPrestamosCompletos() {
        System.out.println("\n📋 === MIS PRÉSTAMOS COMPLETOS ===");
        List<Prestamo> misPrestamos = prestamoService.listarPrestamosPorUsuario(usuarioLogueado.getId());
        
        if (misPrestamos.isEmpty()) {
            System.out.println("ℹ️ No tienes préstamos registrados");
            return;
        }
        
        int activos = 0;
        int devueltos = 0;
        
        for (Prestamo p : misPrestamos) {
            Ejemplar e = ejemplarService.buscarEjemplarPorId(p.getIdEjemplar());
            System.out.println("📚 " + e.getTitulo() + " (" + e.getTipo() + ")");
            System.out.println("   📅 Préstamo: " + p.getFechadePrestamo());
            
            if ("ACTIVO".equals(p.getEstado())) {
                System.out.println("   🟢 Estado: ACTIVO");
                activos++;
            } else {
                System.out.println("   🔴 Estado: DEVUELTO");
                System.out.println("   📅 Devolución: " + p.getFechaDeDevolucion());
                devueltos++;
            }
            
            System.out.println("   💰 Mora: $" + p.getMora());
            System.out.println();
        }
        
        System.out.println("📊 RESUMEN: " + activos + " activos | " + devueltos + " devueltos");
    }
    
    private static void listarPrestamosActivosSistema() {
        // SOLO ADMIN puede ver todos los préstamos
        if (!"ADMIN".equals(usuarioLogueado.getTipo())) {
            System.out.println("❌ Acceso denegado: Solo los administradores pueden ver todos los préstamos del sistema");
            return;
        }
        
        System.out.println("\n📊 === PRÉSTAMOS ACTIVOS DEL SISTEMA ===");
        List<Prestamo> prestamosActivos = prestamoService.listarPrestamosActivos();
        
        if (prestamosActivos.isEmpty()) {
            System.out.println("ℹ️ No hay préstamos activos en el sistema");
            return;
        }
        
        for (Prestamo p : prestamosActivos) {
            Usuario u = usuarioService.buscarUsuarioPorId(p.getIdUsuario());
            Ejemplar e = ejemplarService.buscarEjemplarPorId(p.getIdEjemplar());
            System.out.println("📚 " + e.getTitulo());
            System.out.println("   👤 Prestado a: " + u.getNombre() + " (" + u.getTipo() + ")");
            System.out.println("   📅 Fecha préstamo: " + p.getFechadePrestamo());
            System.out.println("   🆔 ID Préstamo: " + p.getId());
            System.out.println();
        }
    }
    
    private static void buscarPorAutor() {
        System.out.print("\n🔍 Ingrese autor a buscar: ");
        String autor = scanner.nextLine();
        
        List<Ejemplar> resultados = ejemplarService.buscarEjemplarPorAutor(autor);
        
        if (resultados.isEmpty()) {
            System.out.println("❌ No se encontraron ejemplares de ese autor");
            return;
        }
        
        System.out.println("✅ Se encontraron " + resultados.size() + " resultados:");
        for (Ejemplar e : resultados) {
            System.out.println("📚 " + e.getTitulo() + " | " + e.getTipo() + " | Autor: " + e.getAutor());
        }
    }
    
    private static void generarReporteUsuarios() {
        if (!"ADMIN".equals(usuarioLogueado.getTipo())) {
            System.out.println("❌ Acceso denegado");
            return;
        }
        
        System.out.println("\n👥 === REPORTE DE USUARIOS ===");
        List<Usuario> usuarios = usuarioService.listarTodosUsuarios();
        
        int totalUsuarios = usuarios.size();
        int admins = 0, profesores = 0, alumnos = 0;
        int conMora = 0;
        double totalMora = 0;
        
        for (Usuario u : usuarios) {
            // Contar por tipo
            switch (u.getTipo()) {
                case "ADMIN": admins++; break;
                case "PROFESOR": profesores++; break;
                case "ALUMNO": alumnos++; break;
            }
            
            // Contar mora
            if (u.getMora() > 0) {
                conMora++;
                totalMora += u.getMora();
            }
        }
        
        System.out.println("📊 ESTADÍSTICAS:");
        System.out.println("   👥 Total usuarios: " + totalUsuarios);
        System.out.println("   👑 Administradores: " + admins);
        System.out.println("   👨‍🏫 Profesores: " + profesores);
        System.out.println("   👩‍🎓 Alumnos: " + alumnos);
        System.out.println("   💰 Usuarios con mora: " + conMora);
        System.out.println("   📈 Total mora pendiente: $" + totalMora);
        
        // Mostrar usuarios con mora
        if (conMora > 0) {
            System.out.println("\n⚠️ USUARIOS CON MORA PENDIENTE:");
            for (Usuario u : usuarios) {
                if (u.getMora() > 0) {
                    System.out.println("   👤 " + u.getNombre() + " - $" + u.getMora());
                }
            }
        }
    }
    
    // ==================== CERRAR SESIÓN ====================
    private static void cerrarSesion() {
        System.out.println("👋 ¡Hasta pronto " + usuarioLogueado.getNombre() + "!");
        usuarioLogueado = null;
    }
}
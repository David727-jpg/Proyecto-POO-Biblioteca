/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import java.sql.*;

public class DatabaseService {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String URL_WITH_DB = "jdbc:mysql://localhost:3306/biblioteca_udb";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    private static DatabaseService instance;
    
    public static DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }
    
    private DatabaseService() {
        inicializarBaseDatos();
    }
    
    public Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL_WITH_DB, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL no encontrado", e);
        }
    }
    
    private Connection getConnectionWithoutDB() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL no encontrado", e);
        }
    }
    
    private void inicializarBaseDatos() {
        try {
            // Primero verificar/crear la base de datos
            crearBaseDeDatosSiNoExiste();
            
            // Luego crear las tablas
            try (Connection conn = getConnection()) {
                System.out.println("✅ Conexión a MySQL establecida correctamente");
                
                if (!existeTabla(conn, "usuarios")) {
                    crearTablas(conn);
                    insertarDatosPrueba(conn);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al inicializar base de datos: " + e.getMessage());
        }
    }
    
    private void crearBaseDeDatosSiNoExiste() throws SQLException {
        try (Connection conn = getConnectionWithoutDB();
             Statement stmt = conn.createStatement()) {
            
            // Crear la base de datos si no existe
            String crearBD = "CREATE DATABASE IF NOT EXISTS biblioteca_udb";
            stmt.execute(crearBD);
            System.out.println("✅ Base de datos 'biblioteca_udb' verificada/creada");
            
        }
    }
    
    private boolean existeTabla(Connection conn, String tabla) throws SQLException {
        String sql = "SHOW TABLES LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tabla);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
    
    private void crearTablas(Connection conn) throws SQLException {
        System.out.println("📋 Creando tablas...");
        
        // Tabla usuarios
        String crearUsuarios = "CREATE TABLE usuarios (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "nombre VARCHAR(100) NOT NULL, " +
                "email VARCHAR(100) UNIQUE NOT NULL, " +
                "password VARCHAR(100) NOT NULL, " +
                "tipo ENUM('ADMIN', 'PROFESOR', 'ALUMNO') NOT NULL, " +
                "mora DECIMAL(10,2) DEFAULT 0.00, " +
                "activo BOOLEAN DEFAULT TRUE, " +
                "fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
        
        // Tabla ejemplares
        String crearEjemplares = "CREATE TABLE ejemplares (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "titulo VARCHAR(200) NOT NULL, " +
                "tipo ENUM('LIBRO', 'REVISTA', 'CD', 'TESIS') NOT NULL, " +
                "ubicacion VARCHAR(100), " +
                "cantidad_total INT DEFAULT 1, " +
                "cantidad_disponible INT DEFAULT 1, " +
                "autor VARCHAR(100), " +
                "editorial VARCHAR(100), " +
                "isbn VARCHAR(20), " +
                "anio INT, " +
                "numero_revista VARCHAR(50), " +
                "periodicidad VARCHAR(50), " +
                "artista VARCHAR(100), " +
                "duracion VARCHAR(50), " +
                "universidad VARCHAR(100), " +
                "carrera VARCHAR(100), " +
                "fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
        
        // Tabla préstamos
        String crearPrestamos = "CREATE TABLE prestamos (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "id_usuario INT NOT NULL, " +
                "id_ejemplar INT NOT NULL, " +
                "fecha_prestamo DATETIME NOT NULL, " +
                "fecha_devolucion DATETIME NULL, " +
                "estado ENUM('ACTIVO', 'DEVUELTO') DEFAULT 'ACTIVO', " +
                "mora DECIMAL(10,2) DEFAULT 0.00, " +
                "FOREIGN KEY (id_usuario) REFERENCES usuarios(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (id_ejemplar) REFERENCES ejemplares(id) ON DELETE CASCADE, " +
                "fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
        
        // Tabla configuración
        String crearConfiguracion = "CREATE TABLE configuracion (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "max_prestamos INT DEFAULT 3, " +
                "mora_diaria DECIMAL(10,2) DEFAULT 2.50, " +
                "dias_prestamo INT DEFAULT 15, " +
                "fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                ")";
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(crearUsuarios);
            stmt.execute(crearEjemplares);
            stmt.execute(crearPrestamos);
            stmt.execute(crearConfiguracion);
            System.out.println("✅ Todas las tablas creadas correctamente");
        }
    }
    
    private void insertarDatosPrueba(Connection conn) throws SQLException {
        System.out.println("📥 Insertando datos de prueba...");
        
        // Insertar usuarios
        String insertUsuarios = "INSERT INTO usuarios (nombre, email, password, tipo) VALUES " +
                "('Administrador', 'admin@udb.edu', '1234', 'ADMIN'), " +
                "('Profesor Carlos', 'profesor@udb.edu', '1234', 'PROFESOR'), " +
                "('Alumna Ana García', 'alumno@udb.edu', '1234', 'ALUMNO')";
        
        // Insertar ejemplares
        String insertEjemplares = "INSERT INTO ejemplares (titulo, tipo, ubicacion, cantidad_total, cantidad_disponible, autor, editorial, isbn, anio) VALUES " +
                "('Cien años de soledad', 'LIBRO', 'Estante A-15', 5, 5, 'Gabriel García Márquez', 'Sudamericana', '978-8437604947', 1967), " +
                "('1984', 'LIBRO', 'Estante A-20', 3, 3, 'George Orwell', 'Debolsillo', '978-0451524935', 1949), " +
                "('National Geographic', 'REVISTA', 'Estante B-02', 10, 10, NULL, NULL, NULL, NULL), " +
                "('Historia de la Música Clásica', 'CD', 'Estante C-08', 3, 3, NULL, NULL, NULL, NULL)";
        
        // Insertar configuración
        String insertConfiguracion = "INSERT INTO configuracion (max_prestamos, mora_diaria, dias_prestamo) VALUES (3, 2.50, 15)";
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(insertUsuarios);
            stmt.execute(insertEjemplares);
            stmt.execute(insertConfiguracion);
            System.out.println("✅ Datos de prueba insertados correctamente");
        }
    }
    
    public void cerrarConexion(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
}
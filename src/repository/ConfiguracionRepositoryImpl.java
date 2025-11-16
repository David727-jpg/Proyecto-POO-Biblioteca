/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;
import Model.Configuracion;
import java.sql.*;
/**
 *
 * @author josed
 */

/**
 * Implementación concreta del repositorio de configuración
 * Maneja todas las operaciones de base de datos para la tabla 'configuracion'
 */
public class ConfiguracionRepositoryImpl implements ConfiguracionRepository {
    private DatabaseService dbService;
    
    public ConfiguracionRepositoryImpl() {
        this.dbService = DatabaseService.getInstance();
    }
    
    /**
     * Obtiene la configuración actual del sistema desde la tabla 'configuracion'
     * Si no existe configuración, crea una por defecto
     * 
     * @return Configuracion objeto con la configuración, null si hay error
     */
    @Override
    public Configuracion obtenerConfiguracion() {
        String sql = "SELECT * FROM configuracion ORDER BY id DESC LIMIT 1";
        
        try (Connection conn = dbService.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            // Si existe configuración, mapear los resultados
            if (rs.next()) {
                Configuracion config = new Configuracion();
                config.setId(rs.getInt("id"));
                config.setPrestamosMaximos(rs.getInt("max_prestamos"));
                config.setMoraDiaria(rs.getDouble("mora_diaria"));
                config.setDiasPrestamo(rs.getInt("dias_prestamo"));
                return config;
            } else {
                // Si no existe configuración, crear una por defecto
                System.out.println("⚠️ No hay configuración, creando valores por defecto...");
                if (insertarConfiguracionPorDefecto()) {
                    return obtenerConfiguracion(); // Recursivo para obtener la nueva configuración
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener configuración: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Actualiza los parámetros de configuración en la base de datos
     * 
     * @param config Objeto Configuracion con los nuevos valores a guardar
     * @return boolean true si la actualización fue exitosa
     */
    @Override
    public boolean actualizarConfiguracion(Configuracion config) {
        String sql = "UPDATE configuracion SET max_prestamos = ?, mora_diaria = ?, dias_prestamo = ? WHERE id = ?";
        
        try (Connection conn = dbService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Establecer parámetros para prevenir SQL injection
            stmt.setInt(1, config.getPrestamosMaximos());
            stmt.setDouble(2, config.getMoraDiaria());
            stmt.setInt(3, config.getDiasPrestamo());
            stmt.setInt(4, config.getId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar configuración: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Inserta una configuración por defecto en la base de datos
     * Se usa cuando no existe ninguna configuración en el sistema
     * 
     * @return boolean true si la inserción fue exitosa
     */
    @Override
    public boolean insertarConfiguracionPorDefecto() {
        String sql = "INSERT INTO configuracion (max_prestamos, mora_diaria, dias_prestamo) VALUES (?, ?, ?)";
        
        try (Connection conn = dbService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Valores por defecto
            stmt.setInt(1, 3);      // Máximo 3 préstamos
            stmt.setDouble(2, 2.50); // Mora diaria de $2.50
            stmt.setInt(3, 15);     // 15 días de préstamo
            
            int affectedRows = stmt.executeUpdate();
            System.out.println("✅ Configuración por defecto insertada: " + (affectedRows > 0));
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("❌ Error al insertar configuración por defecto: " + e.getMessage());
        }
        return false;
    }
}
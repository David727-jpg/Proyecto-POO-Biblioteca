/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;
import Model.Prestamo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author josed
 */
public class PrestamoRepositoryImpl implements PrestamoRepository {
    private DatabaseService dbService;
    
    public PrestamoRepositoryImpl() {
        this.dbService = DatabaseService.getInstance();
    }
    
    @Override
    public Prestamo findById(int id) {
        String sql = "SELECT * FROM prestamos WHERE id = ?";
        try (Connection conn = dbService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearPrestamo(rs);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar préstamo por ID: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public List<Prestamo> findAll() {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM prestamos ORDER BY fecha_prestamo DESC";
        
        try (Connection conn = dbService.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                prestamos.add(mapearPrestamo(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar préstamos: " + e.getMessage());
        }
        return prestamos;
    }
    
    @Override
    public Prestamo save(Prestamo prestamo) {
        String sql = "INSERT INTO prestamos (id_usuario, id_ejemplar, fecha_prestamo, fecha_devolucion, estado, mora) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, prestamo.getIdUsuario());
            stmt.setInt(2, prestamo.getIdEjemplar());
            stmt.setTimestamp(3, new Timestamp(prestamo.getFechadePrestamo().getTime()));
            
            if (prestamo.getFechaDeDevolucion() != null) {
                stmt.setTimestamp(4, new Timestamp(prestamo.getFechaDeDevolucion().getTime()));
            } else {
                stmt.setNull(4, Types.TIMESTAMP);
            }
            
            stmt.setString(5, prestamo.getEstado());
            stmt.setDouble(6, prestamo.getMora());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        prestamo.setId(generatedKeys.getInt(1));
                        return prestamo;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al guardar préstamo: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public Prestamo update(Prestamo prestamo) {
        String sql = "UPDATE prestamos SET id_usuario = ?, id_ejemplar = ?, fecha_prestamo = ?, " +
                    "fecha_devolucion = ?, estado = ?, mora = ? WHERE id = ?";
        
        try (Connection conn = dbService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, prestamo.getIdUsuario());
            stmt.setInt(2, prestamo.getIdEjemplar());
            stmt.setTimestamp(3, new Timestamp(prestamo.getFechadePrestamo().getTime()));
            
            if (prestamo.getFechaDeDevolucion() != null) {
                stmt.setTimestamp(4, new Timestamp(prestamo.getFechaDeDevolucion().getTime()));
            } else {
                stmt.setNull(4, Types.TIMESTAMP);
            }
            
            stmt.setString(5, prestamo.getEstado());
            stmt.setDouble(6, prestamo.getMora());
            stmt.setInt(7, prestamo.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                return prestamo;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar préstamo: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM prestamos WHERE id = ?";
        
        try (Connection conn = dbService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar préstamo: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public List<Prestamo> findByUsuario(int usuarioId) {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM prestamos WHERE id_usuario = ? ORDER BY fecha_prestamo DESC";
        
        try (Connection conn = dbService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                prestamos.add(mapearPrestamo(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar préstamos por usuario: " + e.getMessage());
        }
        return prestamos;
    }
    
    @Override
    public List<Prestamo> findByEjemplar(int ejemplarId) {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM prestamos WHERE id_ejemplar = ? ORDER BY fecha_prestamo DESC";
        
        try (Connection conn = dbService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ejemplarId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                prestamos.add(mapearPrestamo(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar préstamos por ejemplar: " + e.getMessage());
        }
        return prestamos;
    }
    
    @Override
    public List<Prestamo> findByEstado(String estado) {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM prestamos WHERE estado = ? ORDER BY fecha_prestamo DESC";
        
        try (Connection conn = dbService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, estado);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                prestamos.add(mapearPrestamo(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar préstamos por estado: " + e.getMessage());
        }
        return prestamos;
    }
    
    @Override
    public List<Prestamo> findActivosByUsuario(int usuarioId) {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM prestamos WHERE id_usuario = ? AND estado = 'ACTIVO' ORDER BY fecha_prestamo DESC";
        
        try (Connection conn = dbService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                prestamos.add(mapearPrestamo(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar préstamos activos por usuario: " + e.getMessage());
        }
        return prestamos;
    }
    
    @Override
    public List<Prestamo> findPrestamosActivos() {
        return findByEstado("ACTIVO");
    }
    
    private Prestamo mapearPrestamo(ResultSet rs) throws SQLException {
        Prestamo prestamo = new Prestamo();
        prestamo.setId(rs.getInt("id"));
        prestamo.setIdUsuario(rs.getInt("id_usuario"));
        prestamo.setIdEjemplar(rs.getInt("id_ejemplar"));
        prestamo.setFechadePrestamo(rs.getTimestamp("fecha_prestamo"));
        
        Timestamp fechaDevolucion = rs.getTimestamp("fecha_devolucion");
        if (fechaDevolucion != null) {
            prestamo.setFechaDeDevolucion(fechaDevolucion);
        }
        
        prestamo.setEstado(rs.getString("estado"));
        prestamo.setMora(rs.getDouble("mora"));
        return prestamo;
    }
}
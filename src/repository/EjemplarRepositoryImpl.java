/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;
import Model.Ejemplar;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author josed
 */
public class EjemplarRepositoryImpl implements EjemplarRepository {
    private DatabaseService dbService;
    
    public EjemplarRepositoryImpl() {
        this.dbService = DatabaseService.getInstance();
    }
    
    @Override
    public Ejemplar findById(int id) {
        String sql = "SELECT * FROM ejemplares WHERE id = ?";
        try (Connection conn = dbService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearEjemplar(rs);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar ejemplar por ID: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public List<Ejemplar> findAll() {
        List<Ejemplar> ejemplares = new ArrayList<>();
        String sql = "SELECT * FROM ejemplares ORDER BY titulo";
        
        try (Connection conn = dbService.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ejemplares.add(mapearEjemplar(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar ejemplares: " + e.getMessage());
        }
        return ejemplares;
    }
    
    @Override
    public Ejemplar save(Ejemplar ejemplar) {
        String sql = "INSERT INTO ejemplares (titulo, tipo, ubicacion, cantidad_total, cantidad_disponible, " +
                    "autor, editorial, isbn, anio, numero_revista, periodicidad, artista, duracion, universidad, carrera) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, ejemplar.getTitulo());
            stmt.setString(2, ejemplar.getTipo());
            stmt.setString(3, ejemplar.getUbicacion());
            stmt.setInt(4, ejemplar.getCantidadTotal());
            stmt.setInt(5, ejemplar.getCantidadDisponible());
            stmt.setString(6, ejemplar.getAutor());
            stmt.setString(7, ejemplar.getEditorial());
            stmt.setString(8, ejemplar.getIsbn());
            stmt.setInt(9, ejemplar.getAnio());
            stmt.setString(10, ejemplar.getNumeroRevista());
            stmt.setString(11, ejemplar.getPeridiocidad());
            stmt.setString(12, ejemplar.getArtista());
            stmt.setString(13, ejemplar.getDuracion());
            stmt.setString(14, ejemplar.getUniversidad());
            stmt.setString(15, ejemplar.getCarrera());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        ejemplar.setId(generatedKeys.getInt(1));
                        return ejemplar;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al guardar ejemplar: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public Ejemplar update(Ejemplar ejemplar) {
        String sql = "UPDATE ejemplares SET titulo = ?, tipo = ?, ubicacion = ?, cantidad_total = ?, " +
                    "cantidad_disponible = ?, autor = ?, editorial = ?, isbn = ?, anio = ?, " +
                    "numero_revista = ?, periodicidad = ?, artista = ?, duracion = ?, universidad = ?, carrera = ? " +
                    "WHERE id = ?";
        
        try (Connection conn = dbService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, ejemplar.getTitulo());
            stmt.setString(2, ejemplar.getTipo());
            stmt.setString(3, ejemplar.getUbicacion());
            stmt.setInt(4, ejemplar.getCantidadTotal());
            stmt.setInt(5, ejemplar.getCantidadDisponible());
            stmt.setString(6, ejemplar.getAutor());
            stmt.setString(7, ejemplar.getEditorial());
            stmt.setString(8, ejemplar.getIsbn());
            stmt.setInt(9, ejemplar.getAnio());
            stmt.setString(10, ejemplar.getNumeroRevista());
            stmt.setString(11, ejemplar.getPeridiocidad());
            stmt.setString(12, ejemplar.getArtista());
            stmt.setString(13, ejemplar.getDuracion());
            stmt.setString(14, ejemplar.getUniversidad());
            stmt.setString(15, ejemplar.getCarrera());
            stmt.setInt(16, ejemplar.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                return ejemplar;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar ejemplar: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM ejemplares WHERE id = ?";
        
        try (Connection conn = dbService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar ejemplar: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public List<Ejemplar> findByTitulo(String titulo) {
        List<Ejemplar> ejemplares = new ArrayList<>();
        String sql = "SELECT * FROM ejemplares WHERE titulo LIKE ? ORDER BY titulo";
        
        try (Connection conn = dbService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + titulo + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ejemplares.add(mapearEjemplar(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar ejemplares por título: " + e.getMessage());
        }
        return ejemplares;
    }
    
    @Override
    public List<Ejemplar> findByAutor(String autor) {
        List<Ejemplar> ejemplares = new ArrayList<>();
        String sql = "SELECT * FROM ejemplares WHERE autor LIKE ? ORDER BY titulo";
        
        try (Connection conn = dbService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + autor + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ejemplares.add(mapearEjemplar(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar ejemplares por autor: " + e.getMessage());
        }
        return ejemplares;
    }
    
    @Override
    public List<Ejemplar> findByTipo(String tipo) {
        List<Ejemplar> ejemplares = new ArrayList<>();
        String sql = "SELECT * FROM ejemplares WHERE tipo = ? ORDER BY titulo";
        
        try (Connection conn = dbService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, tipo);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ejemplares.add(mapearEjemplar(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar ejemplares por tipo: " + e.getMessage());
        }
        return ejemplares;
    }
    
    @Override
    public boolean updateCantidades(int ejemplarId, int cantidadTotal, int cantidadDisponible) {
        String sql = "UPDATE ejemplares SET cantidad_total = ?, cantidad_disponible = ? WHERE id = ?";
        
        try (Connection conn = dbService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cantidadTotal);
            stmt.setInt(2, cantidadDisponible);
            stmt.setInt(3, ejemplarId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar cantidades: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public List<Ejemplar> findDisponibles() {
        List<Ejemplar> ejemplares = new ArrayList<>();
        String sql = "SELECT * FROM ejemplares WHERE cantidad_disponible > 0 ORDER BY titulo";
        
        try (Connection conn = dbService.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ejemplares.add(mapearEjemplar(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar ejemplares disponibles: " + e.getMessage());
        }
        return ejemplares;
    }
    
    private Ejemplar mapearEjemplar(ResultSet rs) throws SQLException {
        Ejemplar ejemplar = new Ejemplar();
        ejemplar.setId(rs.getInt("id"));
        ejemplar.setTitulo(rs.getString("titulo"));
        ejemplar.setTipo(rs.getString("tipo"));
        ejemplar.setUbicacion(rs.getString("ubicacion"));
        ejemplar.setCantidadTotal(rs.getInt("cantidad_total"));
        ejemplar.setCantidadDisponible(rs.getInt("cantidad_disponible"));
        ejemplar.setAutor(rs.getString("autor"));
        ejemplar.setEditorial(rs.getString("editorial"));
        ejemplar.setIsbn(rs.getString("isbn"));
        ejemplar.setAnio(rs.getInt("anio"));
        ejemplar.setNumeroRevista(rs.getString("numero_revista"));
        ejemplar.setPeridiocidad(rs.getString("periodicidad"));
        ejemplar.setArtista(rs.getString("artista"));
        ejemplar.setDuracion(rs.getString("duracion"));
        ejemplar.setUniversidad(rs.getString("universidad"));
        ejemplar.setCarrera(rs.getString("carrera"));
        return ejemplar;
    }
}
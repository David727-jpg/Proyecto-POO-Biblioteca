/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;
import Model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author josed
 */
public class UsuarioRepositoryImpl implements UsuarioRepository {
    private DatabaseService dbService;
    
    public UsuarioRepositoryImpl() {
        this.dbService = DatabaseService.getInstance();
    }
    
    @Override
    public Usuario findById(int id) {
        String sql = "SELECT * FROM usuarios WHERE id = ? AND activo = true";
        try (Connection conn = dbService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearUsuario(rs);
            }
        } catch (SQLException e) {
            System.err.println(" Error al buscar usuario por ID: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public List<Usuario> findAll() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE activo = true ORDER BY id";
        
        try (Connection conn = dbService.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            System.err.println(" Error al listar usuarios: " + e.getMessage());
        }
        return usuarios;
    }
    
    @Override
    public Usuario save(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, email, password, tipo, mora, activo) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getPassword());
            stmt.setString(4, usuario.getTipo());
            stmt.setDouble(5, usuario.getMora());
            stmt.setBoolean(6, usuario.isActivo());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        usuario.setId(generatedKeys.getInt(1));
                        return usuario;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println(" Error al guardar usuario: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public Usuario update(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre = ?, email = ?, password = ?, tipo = ?, mora = ?, activo = ? WHERE id = ?";
        
        try (Connection conn = dbService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getPassword());
            stmt.setString(4, usuario.getTipo());
            stmt.setDouble(5, usuario.getMora());
            stmt.setBoolean(6, usuario.isActivo());
            stmt.setInt(7, usuario.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                return usuario;
            }
        } catch (SQLException e) {
            System.err.println(" Error al actualizar usuario: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "UPDATE usuarios SET activo = false WHERE id = ?";
        
        try (Connection conn = dbService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(" Error al eliminar usuario: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public Usuario findByEmail(String email) {
        String sql = "SELECT * FROM usuarios WHERE email = ? AND activo = true";
        try (Connection conn = dbService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearUsuario(rs);
            }
        } catch (SQLException e) {
            System.err.println(" Error al buscar usuario por email: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public boolean updateMora(int usuarioId, double mora) {
        String sql = "UPDATE usuarios SET mora = mora + ? WHERE id = ?";
        
        try (Connection conn = dbService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, mora);
            stmt.setInt(2, usuarioId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(" Error al actualizar mora: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public List<Usuario> findByTipo(String tipo) {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE tipo = ? AND activo = true ORDER BY nombre";
        
        try (Connection conn = dbService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, tipo);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            System.err.println(" Error al buscar usuarios por tipo: " + e.getMessage());
        }
        return usuarios;
    }
    
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setEmail(rs.getString("email"));
        usuario.setPassword(rs.getString("password"));
        usuario.setTipo(rs.getString("tipo"));
        usuario.setMora(rs.getDouble("mora"));
        usuario.setActivo(rs.getBoolean("activo"));
        return usuario;
    }
    
    @Override
    public boolean existeEmail(String email) {
    String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ? AND activo = true";
    try (Connection conn = dbService.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (SQLException e) {
        System.err.println(" Error al verificar email: " + e.getMessage());
    }
    return false;
}


    @Override
    public boolean actualizarPassword(String email, String nuevaPassword) {
    String sql = "UPDATE usuarios SET password = ? WHERE email = ? AND activo = true";
    
    try (Connection conn = dbService.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        
        stmt.setString(1, nuevaPassword);
        stmt.setString(2, email);
        
        int filasAfectadas = stmt.executeUpdate();
        return filasAfectadas > 0;
        
    } catch (SQLException e) {
        System.err.println(" Error al actualizar password: " + e.getMessage());
    }
    return false;
}
    
}
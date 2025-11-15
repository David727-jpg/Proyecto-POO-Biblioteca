/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;
import Model.Usuario;
import java.util.List;
/**
 *
 * @author josed
 */
public interface UsuarioRepository {
    // CRUD básico
    Usuario findById(int id);
    List<Usuario> findAll();
    Usuario save(Usuario usuario);
    Usuario update(Usuario usuario);
    boolean delete(int id);
    
    // Métodos específicos
    Usuario findByEmail(String email);
    boolean updateMora(int usuarioId, double mora);
    List<Usuario> findByTipo(String tipo);
    boolean existeEmail(String email);
    boolean actualizarPassword(String email, String nuevaPassword);
    
}
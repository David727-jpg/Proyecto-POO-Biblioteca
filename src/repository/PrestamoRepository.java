/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;
import Model.Prestamo;
import java.util.List;
/**
 *
 * @author josed
 */
public interface PrestamoRepository {
    // CRUD básico
    Prestamo findById(int id);
    List<Prestamo> findAll();
    Prestamo save(Prestamo prestamo);
    Prestamo update(Prestamo prestamo);
    boolean delete(int id);
    
    // Métodos específicos
    List<Prestamo> findByUsuario(int usuarioId);
    List<Prestamo> findByEjemplar(int ejemplarId);
    List<Prestamo> findByEstado(String estado);
    List<Prestamo> findActivosByUsuario(int usuarioId);
    List<Prestamo> findPrestamosActivos();
}
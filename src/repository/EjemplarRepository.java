/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;
import Model.Ejemplar;
import java.util.List;
/**
 *
 * @author josed
 */
public interface EjemplarRepository {
    // CRUD básico
    Ejemplar findById(int id);
    List<Ejemplar> findAll();
    Ejemplar save(Ejemplar ejemplar);
    Ejemplar update(Ejemplar ejemplar);
    boolean delete(int id);
    
    // Métodos específicos
    List<Ejemplar> findByTitulo(String titulo);
    List<Ejemplar> findByAutor(String autor);
    List<Ejemplar> findByTipo(String tipo);
    boolean updateCantidades(int ejemplarId, int cantidadTotal, int cantidadDisponible);
    List<Ejemplar> findDisponibles();
}
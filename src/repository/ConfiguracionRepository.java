/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;
import Model.Configuracion;
/**
 *
 * @author josed
 */
/**
 * Interfaz para las operaciones de base de datos relacionadas con la configuración del sistema
 * Define los métodos que debe implementar el repositorio de configuración
 */
public interface ConfiguracionRepository {
    
    /**
     * Obtiene la configuración actual del sistema desde la base de datos
     * 
     * @return Configuracion objeto con la configuración actual, null si no existe
     */
    Configuracion obtenerConfiguracion();
    
    /**
     * Actualiza los parámetros de configuración en la base de datos
     * 
     * @param config Objeto Configuracion con los nuevos valores
     * @return boolean true si se actualizó correctamente, false si hubo error
     */
    boolean actualizarConfiguracion(Configuracion config);
    
    /**
     * Inserta una nueva configuración por defecto si no existe
     * 
     * @return boolean true si se insertó correctamente
     */
    boolean insertarConfiguracionPorDefecto();
}
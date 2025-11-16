/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;
import Model.Configuracion;
import repository.ConfiguracionRepository;
import repository.ConfiguracionRepositoryImpl;
/**
 *
 * @author josed
 */

/**
 * Servicio para gestionar la configuración del sistema de biblioteca
 * Maneja parámetros como límites de préstamos, mora diaria y días de préstamo
 */
public class ConfiguracionService {
    private ConfiguracionRepository configuracionRepository;
    
    public ConfiguracionService() {
        this.configuracionRepository = new ConfiguracionRepositoryImpl();
    }
    
    /**
     * Obtiene la configuración actual del sistema desde la base de datos
     * 
     * @return Configuracion objeto con todos los parámetros del sistema
     * @throws Exception si hay error al acceder a la base de datos
     */
    public Configuracion obtenerConfiguracionActual() {
        try {
            System.out.println("⚙️ Obteniendo configuración actual del sistema...");
            return configuracionRepository.obtenerConfiguracion();
        } catch (Exception e) {
            System.out.println("❌ Error al obtener configuración: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Actualiza los parámetros de configuración del sistema
     * 
     * @param maxPrestamos - Número máximo de préstamos permitidos por usuario
     * @param moraDiaria - Valor de la mora por día de retraso
     * @param diasPrestamo - Número de días permitidos para cada préstamo
     * @return boolean true si se actualizó correctamente, false si hubo error
     */
    public boolean actualizarConfiguracion(int maxPrestamos, double moraDiaria, int diasPrestamo) {
        try {
            System.out.println("🔄 Actualizando configuración del sistema...");
            
            // Validar parámetros
            if (maxPrestamos <= 0) {
                throw new IllegalArgumentException("El máximo de préstamos debe ser mayor a 0");
            }
            if (moraDiaria < 0) {
                throw new IllegalArgumentException("La mora diaria no puede ser negativa");
            }
            if (diasPrestamo <= 0) {
                throw new IllegalArgumentException("Los días de préstamo deben ser mayores a 0");
            }
            
            // Crear objeto de configuración
            Configuracion config = new Configuracion();
            config.setPrestamosMaximos(maxPrestamos);
            config.setMoraDiaria(moraDiaria);
            config.setDiasPrestamo(diasPrestamo);
            
            // Guardar en base de datos
            boolean resultado = configuracionRepository.actualizarConfiguracion(config);
            
            if (resultado) {
                System.out.println("✅ Configuración actualizada exitosamente");
                System.out.println("   📚 Máximo préstamos: " + maxPrestamos);
                System.out.println("   💰 Mora diaria: $" + moraDiaria);
                System.out.println("   📅 Días préstamo: " + diasPrestamo);
            }
            
            return resultado;
            
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Error de validación: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("❌ Error al actualizar configuración: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtiene el valor actual de la mora diaria configurada en el sistema
     * 
     * @return double valor de la mora diaria
     */
    public double obtenerMoraDiaria() {
        Configuracion config = obtenerConfiguracionActual();
        return config != null ? config.getMoraDiaria() : 2.50; // Valor por defecto
    }
    
    /**
     * Obtiene el límite máximo de préstamos permitidos por usuario
     * 
     * @return int número máximo de préstamos
     */
    public int obtenerMaximoPrestamos() {
        Configuracion config = obtenerConfiguracionActual();
        return config != null ? config.getPrestamosMaximos() : 3; // Valor por defecto
    }
    
    /**
     * Obtiene el número de días permitidos para cada préstamo
     * 
     * @return int días de préstamo permitidos
     */
    public int obtenerDiasPrestamo() {
        Configuracion config = obtenerConfiguracionActual();
        return config != null ? config.getDiasPrestamo() : 15; // Valor por defecto
    }
}
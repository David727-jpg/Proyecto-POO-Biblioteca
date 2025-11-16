/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;
import Model.Ejemplar;
import repository.EjemplarRepository;
import repository.EjemplarRepositoryImpl;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author josed
 */
public class EjemplarService {
    private EjemplarRepository ejemplarRepository;
    
    public EjemplarService() {
        try {
            this.ejemplarRepository = new EjemplarRepositoryImpl();
            System.out.println("✅ EjemplarService inicializado con Base de Datos");
            
        } catch (Exception e) {
            System.out.println("❌ Error crítico al inicializar EjemplarService: " + e.getMessage());
        }
    }
    
    
    
    public List<Ejemplar> listarTodosEjemplares() {
        try {
            System.out.println("📚 Listando todos los ejemplares desde BD...");
            return ejemplarRepository.findAll();
            
        } catch (Exception e) {
            System.out.println("❌ Error al listar ejemplares: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public List<Ejemplar> buscarEjemplarPorTitulo(String titulo) {
        try {
            System.out.println("🔍 Buscando ejemplar por título: '" + titulo + "'");
            
            if (titulo == null || titulo.isEmpty()) {
                throw new IllegalArgumentException("El título de búsqueda no puede estar vacío");
            }
            
            List<Ejemplar> resultados = ejemplarRepository.findByTitulo(titulo);
            System.out.println("✅ Búsqueda completada: " + resultados.size() + " resultados encontrados");
            return resultados;
            
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Error de validación: " + e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            System.out.println("❌ Error en búsqueda: " + e.getMessage());
            return new ArrayList<>();
        } finally {
            System.out.println("--- Búsqueda por título finalizada ---");
        }
    }
    
    public List<Ejemplar> buscarEjemplarPorAutor(String autor) {
        try {
            System.out.println("🔍 Buscando ejemplar por autor: '" + autor + "'");
            
            if (autor == null || autor.isEmpty()) {
                throw new IllegalArgumentException("El autor de búsqueda no puede estar vacío");
            }
            
            List<Ejemplar> resultados = ejemplarRepository.findByAutor(autor);
            System.out.println("✅ Búsqueda por autor completada: " + resultados.size() + " resultados encontrados");
            return resultados;
            
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Error de validación: " + e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            System.out.println("❌ Error en búsqueda por autor: " + e.getMessage());
            return new ArrayList<>();
        } finally {
            System.out.println("--- Búsqueda por autor finalizada ---");
        }
    }
    
    public boolean agregarEjemplar(Ejemplar nuevoEjemplar) {
        try {
            System.out.println("📚 Intentando agregar nuevo ejemplar...");
            
            // Validaciones básicas
            if (nuevoEjemplar == null) {
                throw new IllegalArgumentException("El ejemplar no puede ser null");
            }
            if (nuevoEjemplar.getTitulo() == null || nuevoEjemplar.getTitulo().isEmpty()) {
                throw new IllegalArgumentException("El título no puede estar vacío");
            }
            if (nuevoEjemplar.getTipo() == null) {
                throw new IllegalArgumentException("El tipo no puede estar vacío");
            }
            
            // Validar tipo de ejemplar
            String tipo = nuevoEjemplar.getTipo();
            if (!tipo.equals("LIBRO") && !tipo.equals("REVISTA") && !tipo.equals("CD") && !tipo.equals("TESIS")) {
                throw new RuntimeException("Tipo de ejemplar inválido: " + tipo);
            }
            
            // Validar cantidades
            if (nuevoEjemplar.getCantidadTotal() < 0) {
                throw new IllegalArgumentException("La cantidad total no puede ser negativa");
            }
            if (nuevoEjemplar.getCantidadDisponible() < 0) {
                throw new IllegalArgumentException("La cantidad disponible no puede ser negativa");
            }
            if (nuevoEjemplar.getCantidadDisponible() > nuevoEjemplar.getCantidadTotal()) {
                throw new RuntimeException("No puede haber más disponibles que el total");
            }
            
            // Guardar ejemplar (el ID se genera automáticamente en la BD)
            Ejemplar ejemplarGuardado = ejemplarRepository.save(nuevoEjemplar);
            if (ejemplarGuardado != null) {
                System.out.println("✅ Ejemplar agregado exitosamente: " + ejemplarGuardado.getTitulo() + " (ID: " + ejemplarGuardado.getId() + ")");
                System.out.println("  📍 Tipo: " + ejemplarGuardado.getTipo() + " | Ubicación: " + ejemplarGuardado.getUbicacion());
                return true;
            } else {
                throw new RuntimeException("Error al guardar ejemplar en la base de datos");
            }
            
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Error de datos: " + e.getMessage());
            return false;
        } catch (RuntimeException e) {
            System.out.println("❌ Error de negocio: " + e.getMessage());
            return false;
        } finally {
            System.out.println("--- Operación agregar ejemplar finalizada ---");
        }
    }
    
    public boolean actualizarCantidades(int ejemplarId, int nuevaCantidadTotal, int nuevaCantidadDisponible) {
        try {
            System.out.println("🔄 Actualizando cantidades para ejemplar ID: " + ejemplarId);
            
            // Validar cantidades no negativas
            if (nuevaCantidadTotal < 0 || nuevaCantidadDisponible < 0) {
                throw new IllegalArgumentException("Las cantidades no pueden ser negativas");
            }
            
            // Validar que disponible no sea mayor que total
            if (nuevaCantidadDisponible > nuevaCantidadTotal) {
                throw new RuntimeException("La cantidad disponible no puede ser mayor que la total");
            }
            
            // Actualizar cantidades usando el repository
            boolean actualizado = ejemplarRepository.updateCantidades(ejemplarId, nuevaCantidadTotal, nuevaCantidadDisponible);
            if (actualizado) {
                System.out.println("✅ Cantidades actualizadas para ejemplar ID: " + ejemplarId);
                System.out.println("  📊 Total: " + nuevaCantidadTotal + " | Disponible: " + nuevaCantidadDisponible);
                return true;
            } else {
                throw new RuntimeException("No se encontró ejemplar con ID: " + ejemplarId);
            }
            
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Error de datos: " + e.getMessage());
            return false;
        } catch (RuntimeException e) {
            System.out.println("❌ Error: " + e.getMessage());
            return false;
        } finally {
            System.out.println("--- Actualización de cantidades finalizada ---");
        }
    }
    
    public Ejemplar buscarEjemplarPorId(int id) {
        try {
            System.out.println("🔎 Buscando ejemplar por ID: " + id);
            
            Ejemplar ejemplar = ejemplarRepository.findById(id);
            if (ejemplar != null) {
                System.out.println("✅ Ejemplar encontrado: " + ejemplar.getTitulo());
                return ejemplar;
            }
            
            throw new RuntimeException("Ejemplar con ID " + id + " no encontrado");
            
        } catch (RuntimeException e) {
            System.out.println("❌ Error en búsqueda por ID: " + e.getMessage());
            return null;
        } finally {
            System.out.println("--- Búsqueda por ID finalizada ---");
        }
    }
    
    // ✅ MÉTODO NUEVO: Buscar ejemplares disponibles
    public List<Ejemplar> buscarEjemplaresDisponibles() {
        try {
            System.out.println("📚 Buscando ejemplares disponibles...");
            return ejemplarRepository.findDisponibles();
            
        } catch (Exception e) {
            System.out.println("❌ Error al buscar ejemplares disponibles: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
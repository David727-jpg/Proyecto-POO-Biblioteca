/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;
import Model.Ejemplar;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author josed
 */
public class EjemplarService {
    private List<Ejemplar> ejemplares;
    
    public EjemplarService() {
        //LISTA PARA GUARDAR LOS EJEMPLARES
        try {
            this.ejemplares = new ArrayList<>();
            cargarEjemplaresDePrueba();
            System.out.println(" EjemplarService inicializado correctamente");
            
        } catch (Exception e) {
            System.out.println(" Error crítico al inicializar EjemplarService: " + e.getMessage());
        }
    }
    
    private void cargarEjemplaresDePrueba() {
        try {
            // Libro 1
            Ejemplar libro1 = new Ejemplar();
            libro1.setId(1);
            libro1.setTitulo("Cien años de soledad");
            libro1.setAutor("Gabriel García Márquez");
            libro1.setTipo("LIBRO");
            libro1.setUbicacion("Estante A-15");
            libro1.setCantidadTotal(5);
            libro1.setCantidadDisponible(3);
            libro1.setIsbn("978-8437604947");
            libro1.setAnio(1967);
            ejemplares.add(libro1);
            
            // Revista
            Ejemplar revista1 = new Ejemplar();
            revista1.setId(2);
            revista1.setTitulo("National Geographic");
            revista1.setTipo("REVISTA");
            revista1.setUbicacion("Estante B-02");
            revista1.setCantidadTotal(10);
            revista1.setCantidadDisponible(8);
            revista1.setNumeroRevista("Enero 2024");
            revista1.setPeridiocidad("Mensual");
            ejemplares.add(revista1);
            
            // CD
            Ejemplar cd1 = new Ejemplar();
            cd1.setId(3);
            cd1.setTitulo("Historia de la Música Clásica");
            cd1.setArtista("Varios Artistas");
            cd1.setTipo("CD");
            cd1.setUbicacion("Estante C-08");
            cd1.setCantidadTotal(3);
            cd1.setCantidadDisponible(2);
            cd1.setDuracion("120 min");
            ejemplares.add(cd1);
            
            // Libro 2
            Ejemplar libro2 = new Ejemplar();
            libro2.setId(4);
            libro2.setTitulo("1984");
            libro2.setAutor("George Orwell");
            libro2.setTipo("LIBRO");
            libro2.setUbicacion("Estante A-20");
            libro2.setCantidadTotal(3);
            libro2.setCantidadDisponible(2);
            libro2.setIsbn("978-0451524935");
            libro2.setAnio(1949);
            ejemplares.add(libro2);
            
            System.out.println(" " + ejemplares.size() + " ejemplares de prueba cargados");
            
        } catch (Exception e) {
            System.out.println(" Error al cargar ejemplares de prueba: " + e.getMessage());
        }
    }
    
    public List<Ejemplar> listarTodosEjemplares() {
        //LISTA POR SI ACASO
        try {
            System.out.println(" Listando todos los ejemplares.");
            return new ArrayList<>(ejemplares);
            
        } catch (Exception e) {
            System.out.println("Error al listar ejemplares: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public List<Ejemplar> buscarEjemplarPorTitulo(String titulo) {
        try {
            System.out.println(" Buscando ejemplar por título: '" + titulo + "'");
            
            //VALIDACIONES EL TITULO NO PUEDE ESTAR VACIO
            if (titulo == null || titulo.isEmpty()) {
                throw new IllegalArgumentException("El título de búsqueda no puede estar vacío");
            }
            //GUARDAR LOS RESULTADOS EN 
            List<Ejemplar> resultados = new ArrayList<>();
           //BUSCA EN LA LISTA CON EL BUCLE DE FOR
            for (Ejemplar ejemplar : ejemplares) {
                if (ejemplar.getTitulo().toLowerCase().contains(titulo.toLowerCase())) {
                    resultados.add(ejemplar);
                }
            }
            
            System.out.println(" Búsqueda completada: " + resultados.size() + " resultados encontrados");
            return resultados;
            //CODIGOS DE ERROR 
        } catch (IllegalArgumentException e) {
            System.out.println(" Error de validación: " + e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            System.out.println(" Error en búsqueda: " + e.getMessage());
            return new ArrayList<>();
        } finally {
            System.out.println("- Búsqueda por título finalizada -");
        }
    }
    
    public List<Ejemplar> buscarEjemplarPorAutor(String autor) {
        try {
            System.out.println(" Buscando ejemplar por autor: '" + autor + "'");
            //VALIDACIONES QUE EL AUTOR NO PUEDE SER NULL
            if (autor == null || autor.isEmpty()) {
                throw new IllegalArgumentException("El autor de búsqueda no puede estar vacío");
            }
            //GUARDA LOS RESULTADOS EN LA LISTA
            List<Ejemplar> resultados = new ArrayList<>();
           //BUSCA EN LA LISTA CON EL BUCLE DE FOR
            for (Ejemplar ejemplar : ejemplares) {
                if (ejemplar.getAutor() != null && 
                    ejemplar.getAutor().toLowerCase().contains(autor.toLowerCase())) {
                    resultados.add(ejemplar);
                }
            }
            
            System.out.println(" Búsqueda por autor completada: " + resultados.size() + " resultados encontrados");
            return resultados;
            //CODIGOS DE ERROR 
        } catch (IllegalArgumentException e) {
            System.out.println(" Error de validación: " + e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            System.out.println(" Error en búsqueda por autor: " + e.getMessage());
            return new ArrayList<>();
        } finally {
            System.out.println("- Búsqueda por autor finalizada -");
        }
    }
    
    public boolean agregarEjemplar(Ejemplar nuevoEjemplar) {
        try {
            System.out.println(" Intentando agregar nuevo ejemplar...");
            
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
            String[] tiposValidos = {"LIBRO", "REVISTA", "CD", "TESIS"};
            boolean tipoValido = false;
            //BUSCA CADA TIPO VALIDO PARA EL NUEVO EJEMPLAR
            for (String tipo : tiposValidos) {
                if (tipo.equals(nuevoEjemplar.getTipo())) {
                    //SI ENCUENTRA UNA COINCIDENCIA DEVUELVE TRUE
                    tipoValido = true;
                    break;
                }
            }
            if (!tipoValido) {
                throw new RuntimeException("Tipo de ejemplar inválido: " + nuevoEjemplar.getTipo());
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
            
            // Asignar ID automático
            int nuevoId = ejemplares.size() + 1;
            nuevoEjemplar.setId(nuevoId);
            
            // Agregar ejemplar
            ejemplares.add(nuevoEjemplar);
            System.out.println(" Ejemplar agregado exitosamente: " + nuevoEjemplar.getTitulo() + " (ID: " + nuevoId + ")");
            System.out.println("  Tipo: " + nuevoEjemplar.getTipo() + " | Ubicación: " + nuevoEjemplar.getUbicacion());
            return true;
            //MENSAJES DE ERROR
        } catch (IllegalArgumentException e) {
            System.out.println(" Error de datos: " + e.getMessage());
            return false;
        } catch (RuntimeException e) {
            System.out.println(" Error de negocio: " + e.getMessage());
            return false;
        } finally {
            System.out.println("- Operación agregar ejemplar finalizada -");
        }
    }
    
    public boolean actualizarCantidades(int ejemplarId, int nuevaCantidadTotal, int nuevaCantidadDisponible) {
        try {
            System.out.println(" Actualizando cantidades para ejemplar ID: " + ejemplarId);
            
            // Validar cantidades no negativas
            if (nuevaCantidadTotal < 0 || nuevaCantidadDisponible < 0) {
                throw new IllegalArgumentException("Las cantidades no pueden ser negativas");
            }
            
            // Validar que disponible no sea mayor que total
            if (nuevaCantidadDisponible > nuevaCantidadTotal) {
                throw new RuntimeException("La cantidad disponible no puede ser mayor que la total");
            }
            
            // Buscar el ejemplar por ID
            for (Ejemplar ejemplar : ejemplares) {
                if (ejemplar.getId() == ejemplarId) {
                    // Actualizar las cantidades
                    ejemplar.setCantidadTotal(nuevaCantidadTotal);
                    ejemplar.setCantidadDisponible(nuevaCantidadDisponible);
                    System.out.println(" Cantidades actualizadas para: " + ejemplar.getTitulo());
                    System.out.println("  Total: " + nuevaCantidadTotal + " | Disponible: " + nuevaCantidadDisponible);
                    return true;
                }
            }
            //MENSAJES DE ERROR
            throw new RuntimeException("No se encontró ejemplar con ID: " + ejemplarId);
            
        } catch (IllegalArgumentException e) {
            System.out.println(" Error de datos: " + e.getMessage());
            return false;
        } catch (RuntimeException e) {
            System.out.println(" Error: " + e.getMessage());
            return false;
        } finally {
            System.out.println("--- Actualización de cantidades finalizada ---");
        }
    }
    
    public Ejemplar buscarEjemplarPorId(int id) {
        try {
            System.out.println(" Buscando ejemplar por ID: " + id);
            //BUSCAR EN LA LISTA 
            for (Ejemplar ejemplar : ejemplares) {
                //COMPARA LOS IDS
                if (ejemplar.getId() == id) {
                    System.out.println(" Ejemplar encontrado: " + ejemplar.getTitulo());
                    return ejemplar;
                }
            }
            //MENSAJES DE ERROR
            throw new RuntimeException("Ejemplar con ID " + id + " no encontrado");
            
        } catch (RuntimeException e) {
            System.out.println(" Error en búsqueda por ID: " + e.getMessage());
            return null;
        } finally {
            System.out.println("- Búsqueda por ID finalizada -");
        }
    }
}
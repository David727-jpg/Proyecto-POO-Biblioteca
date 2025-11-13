/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import service.EjemplarService;
import Model.Ejemplar;
import java.util.List;

/**
 *
 * @author josed
 */
public class TestEjemplarService {
    public static void main(String[] args) {
        EjemplarService ejemplarService = new EjemplarService();
        
        List<Ejemplar> todosEjemplares = ejemplarService.listarTodosEjemplares();
        
        System.out.println("=== LISTA DE EJEMPLARES ===");
        for (Ejemplar ejemplar : todosEjemplares) {
            System.out.println("- " + ejemplar.getTitulo() + " (" + ejemplar.getTipo() + ")");
            System.out.println("  Ubicación: " + ejemplar.getUbicacion());
            System.out.println("  Disponibles: " + ejemplar.getCantidadDisponible() + "/" + ejemplar.getCantidadTotal());
        }
        System.out.println("\n=== BÚSQUEDA POR TÍTULO ===");
List<Ejemplar> busqueda = ejemplarService.buscarEjemplarPorTitulo("soledad");
System.out.println("Resultados encontrados: " + busqueda.size());
for (Ejemplar ejemplar : busqueda) {
    System.out.println("- " + ejemplar.getTitulo() + " | " + ejemplar.getAutor());
}

// Probar búsqueda que no encuentra nada
System.out.println("\n=== BÚSQUEDA SIN RESULTADOS ===");
List<Ejemplar> sinResultados = ejemplarService.buscarEjemplarPorTitulo("xyz123");
System.out.println("Resultados: " + sinResultados.size());


System.out.println("\n=== BÚSQUEDA POR AUTOR ===");
List<Ejemplar> busquedaAutor = ejemplarService.buscarEjemplarPorAutor("García");
System.out.println("Resultados encontrados: " + busquedaAutor.size());
for (Ejemplar ejemplar : busquedaAutor) {
    System.out.println("- " + ejemplar.getTitulo() + " | " + ejemplar.getAutor());
}

// Probar con autor que no existe
System.out.println("\n=== BÚSQUEDA AUTOR INEXISTENTE ===");
List<Ejemplar> autorNoExiste = ejemplarService.buscarEjemplarPorAutor("Shakespeare");
System.out.println("Resultados: " + autorNoExiste.size());



System.out.println("\n=== AGREGAR NUEVO EJEMPLAR ===");
Ejemplar nuevoLibro = new Ejemplar();
nuevoLibro.setId(5);
nuevoLibro.setTitulo("El Principito");
nuevoLibro.setAutor("Antoine de Saint-Exupéry");
nuevoLibro.setTipo("LIBRO");
nuevoLibro.setUbicacion("Estante A-10");
nuevoLibro.setCantidadTotal(4);
nuevoLibro.setCantidadDisponible(4);
nuevoLibro.setAnio(1943);
nuevoLibro.setIsbn("978-0156013924");

boolean agregado = ejemplarService.agregarEjemplar(nuevoLibro);
if (agregado) {
    System.out.println("✅ Ejemplar agregado correctamente");
} else {
    System.out.println("❌ Error al agregar ejemplar");
}

// Mostrar lista actualizada
System.out.println("\n=== LISTA ACTUALIZADA ===");
ejemplarService.listarTodosEjemplares().forEach(ejemplar -> {
    System.out.println("- " + ejemplar.getTitulo() + " (" + ejemplar.getTipo() + ")");
});



System.out.println("\n=== ACTUALIZAR CANTIDADES ===");
// Buscar un ejemplar específico por título primero
List<Ejemplar> resultados = ejemplarService.buscarEjemplarPorTitulo("Cien años");
if (!resultados.isEmpty()) {
    Ejemplar ejemplar = resultados.get(0);
    System.out.println("Antes - " + ejemplar.getTitulo() + ": " + 
                      ejemplar.getCantidadDisponible() + "/" + ejemplar.getCantidadTotal());
    
    // Actualizamos las cantidades (simulando que se prestaron 2 ejemplares)
    boolean actualizado = ejemplarService.actualizarCantidades(
        ejemplar.getId(), 
        ejemplar.getCantidadTotal(),        // Total sigue igual (5)
        ejemplar.getCantidadDisponible() - 2 // Disponibles disminuyen (3-2=1)
    );
    
    if (actualizado) {
        System.out.println(" Cantidades actualizadas correctamente");
    }
} else {
    System.out.println("No se encontró el ejemplar para actualizar");
}
}


    }
    
    
    



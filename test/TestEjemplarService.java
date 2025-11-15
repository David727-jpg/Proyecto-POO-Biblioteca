/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import service.EjemplarService;
import Model.Ejemplar;
import java.util.List;

public class TestEjemplarService {
    public static void main(String[] args) {
        System.out.println("🧪 Probando EjemplarService con Base de Datos...");
        
        EjemplarService ejemplarService = new EjemplarService();
        
        // Test 1: Listar todos los ejemplares
        System.out.println("\n📋 Test 1: Listar todos los ejemplares");
        List<Ejemplar> ejemplares = ejemplarService.listarTodosEjemplares();
        System.out.println("✅ Ejemplares encontrados: " + ejemplares.size());
        for (Ejemplar e : ejemplares) {
            System.out.println("   - " + e.getTitulo() + " (" + e.getTipo() + ") - Disponibles: " + e.getCantidadDisponible() + "/" + e.getCantidadTotal());
        }
        
        // Test 2: Buscar ejemplar por ID
        System.out.println("\n📋 Test 2: Buscar ejemplar por ID");
        Ejemplar ejemplar = ejemplarService.buscarEjemplarPorId(1);
        if (ejemplar != null) {
            System.out.println("✅ Ejemplar encontrado: " + ejemplar.getTitulo());
        } else {
            System.out.println("❌ Ejemplar no encontrado");
        }
        
        // Test 3: Buscar por título
        System.out.println("\n📋 Test 3: Buscar ejemplar por título");
        List<Ejemplar> resultados = ejemplarService.buscarEjemplarPorTitulo("soledad");
        System.out.println("✅ Resultados encontrados: " + resultados.size());
        
        // Test 4: Buscar disponibles
        System.out.println("\n📋 Test 4: Buscar ejemplares disponibles");
        List<Ejemplar> disponibles = ejemplarService.buscarEjemplaresDisponibles();
        System.out.println("✅ Ejemplares disponibles: " + disponibles.size());
        
        System.out.println("\n🎉 Pruebas de EjemplarService con BD completadas");
    }
}
    
    



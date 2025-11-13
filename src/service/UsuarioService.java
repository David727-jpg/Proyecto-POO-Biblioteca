/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import Model.Usuario;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author josed
 */
public class UsuarioService {
    private List<Usuario> usuarios;
    
    public UsuarioService() {
        try {
            this.usuarios = new ArrayList<>();
            cargarUsuariosDePrueba();
            System.out.println(" UsuarioService inicializado correctamente");
            
        } catch (Exception e) {
            System.out.println(" Error crítico al inicializar UsuarioService: " + e.getMessage());
        }
    }
    
    private void cargarUsuariosDePrueba() {
        try {
            usuarios.add(new Usuario("Administrador", "admin@udb.edu", "1234", "ADMIN"));
            usuarios.add(new Usuario("Profesor Carlos", "profesor@udb.edu", "1234", "PROFESOR"));
            usuarios.add(new Usuario("Alumno Ana", "alumno@udb.edu", "1234", "ALUMNO"));
            
            // Asignar IDs
            for (int i = 0; i < usuarios.size(); i++) {
                usuarios.get(i).setId(i + 1);
            }
            
            System.out.println("✅ " + usuarios.size() + " usuarios de prueba cargados");
            
        } catch (Exception e) {
            System.out.println(" Error al cargar usuarios de prueba: " + e.getMessage());
        }
    }
    
    public List<Usuario> listarTodosUsuarios() {
        try {
            System.out.println(" Listando todos los usuarios...");
            return new ArrayList<>(usuarios);
            
        } catch (Exception e) {
            System.out.println(" Error al listar usuarios: " + e.getMessage());
            return new ArrayList<>();
        }
        
    }
    
    
    public Usuario buscarUsuarioPorId(int id) {
    try {
        System.out.println(" Buscando usuario por ID: " + id);
        
        for (Usuario usuario : usuarios) {
            if (usuario.getId() == id) {
                System.out.println(" Usuario encontrado: " + usuario.getNombre());
                return usuario;
            }
        }
        
        throw new RuntimeException("Usuario con ID " + id + " no encontrado");
        
    } catch (RuntimeException e) {
        System.out.println(" Error en búsqueda por ID: " + e.getMessage());
        return null;
    } finally {
        System.out.println("- Búsqueda por ID finalizada -");
    }
}
    
    
    
    
    public Usuario buscarUsuarioPorEmail(String email) {
        try {
            System.out.println("🔍 Buscando usuario por email: " + email);
            
            if (email == null || email.isEmpty()) {
                throw new IllegalArgumentException("El email no puede estar vacío");
            }
            
            for (Usuario usuario : usuarios) {
                if (usuario.getEmail().equalsIgnoreCase(email)) {
                    System.out.println(" Usuario encontrado: " + usuario.getNombre());
                    return usuario;
                }
            }
            
            throw new RuntimeException("Usuario con email '" + email + "' no encontrado");
            
        } catch (IllegalArgumentException e) {
            System.out.println(" Error de validación: " + e.getMessage());
            return null;
        } catch (RuntimeException e) {
            System.out.println(" Error de búsqueda: " + e.getMessage());
            return null;
        } finally {
            System.out.println("- Búsqueda por email finalizada -");
        }
    }
    
    
    
    public boolean crearUsuario(Usuario nuevoUsuario) {
        try {
            System.out.println("👥 Intentando crear nuevo usuario...");
            
            if (nuevoUsuario == null) {
                throw new IllegalArgumentException("El usuario no puede ser null");
            }
            if (nuevoUsuario.getEmail() == null || nuevoUsuario.getEmail().isEmpty()) {
                throw new IllegalArgumentException("El email no puede estar vacío");
            }
            if (nuevoUsuario.getTipo() == null) {
                throw new IllegalArgumentException("El tipo de usuario no puede estar vacío");
            }
            
            // Verificar email único
            if (buscarUsuarioPorEmail(nuevoUsuario.getEmail()) != null) {
                throw new RuntimeException("Ya existe un usuario con el email: " + nuevoUsuario.getEmail());
            }
            
            // Validar tipo de usuario
            if (!nuevoUsuario.getTipo().equals("ADMIN") && 
                !nuevoUsuario.getTipo().equals("PROFESOR") && 
                !nuevoUsuario.getTipo().equals("ALUMNO")) {
                throw new RuntimeException("Tipo de usuario inválido: " + nuevoUsuario.getTipo());
            }
            
            
            
            // Asignar ID automático
            int nuevoId = usuarios.size() + 1;
            nuevoUsuario.setId(nuevoId);
            
            // Crear usuario
            usuarios.add(nuevoUsuario);
            System.out.println(" Usuario creado exitosamente: " + nuevoUsuario.getNombre() + " (ID: " + nuevoId + ")");
            return true;
            
        } catch (IllegalArgumentException e) {
            System.out.println(" Error de datos: " + e.getMessage());
            return false;
        } catch (RuntimeException e) {
            System.out.println(" Error de negocio: " + e.getMessage());
            return false;
        } finally {
            System.out.println("--- Operación crear usuario finalizada ---");
        }
    }
    
    
    
    public boolean restablecerContraseña(String email) {
        try {
            System.out.println(" Intentando restablecer contraseña para: " + email);
            
            if (email == null || email.isEmpty()) {
                throw new IllegalArgumentException("El email no puede estar vacío");
            }
            
            Usuario usuario = buscarUsuarioPorEmail(email);
            if (usuario == null) {
                throw new RuntimeException("No existe usuario con el email: " + email);
            }
            
            String nuevaPassword = generarPasswordTemporal();
            usuario.setPassword(nuevaPassword);
            
            System.out.println("Contraseña restablecida para: " + usuario.getNombre());
            System.out.println(" Nueva contraseña temporal: " + nuevaPassword);
            return true;
            
        } catch (IllegalArgumentException e) {
            System.out.println(" Error de datos: " + e.getMessage());
            return false;
        } catch (RuntimeException e) {
            System.out.println(" Error: " + e.getMessage());
            return false;
        } finally {
            System.out.println("--- Restablecimiento de contraseña finalizado ---");
        }
    }
    
    
    
    private String generarPasswordTemporal() {
        try {
            // Generar contraseña temporal segura
            String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            StringBuilder password = new StringBuilder();
            
            for (int i = 0; i < 8; i++) {
                int index = (int) (Math.random() * caracteres.length());
                password.append(caracteres.charAt(index));
            }
            
            return password.toString();
            
        } catch (Exception e) {
            System.out.println("⚠️ Error al generar contraseña temporal, usando valor por defecto");
            return "temp1234";
        }
    }
    
    
    
    public boolean tieneMora(int usuarioId) {
        try {
            System.out.println("💰 Verificando mora para usuario ID: " + usuarioId);
            
            for (Usuario usuario : usuarios) {
                if (usuario.getId() == usuarioId) {
                    boolean tieneMora = usuario.getMora() > 0;
                    System.out.println("ℹ️ Usuario " + usuario.getNombre() + 
                                      " - Mora: $" + usuario.getMora() + 
                                      " - Tiene mora: " + tieneMora);
                    return tieneMora;
                }
            }
            
            throw new RuntimeException("Usuario con ID " + usuarioId + " no encontrado");
            
        } catch (RuntimeException e) {
            System.out.println("❌ Error al verificar mora: " + e.getMessage());
            return false;
        } finally {
            System.out.println("--- Verificación de mora finalizada ---");
        }
    }
    
    public boolean actualizarMoraUsuario(int usuarioId, double mora) {
    try {
        System.out.println("? Actualizando mora para usuario ID: " + usuarioId + " - Mora: $" + mora);
        
        Usuario usuario = buscarUsuarioPorId(usuarioId);
        if (usuario != null) {
            usuario.setMora(usuario.getMora() + mora);
            System.out.println(" Mora actualizada: $" + usuario.getMora());
            return true;
        }
        
        throw new RuntimeException("Usuario no encontrado para actualizar mora");
        
    } catch (RuntimeException e) {
        System.out.println(" Error al actualizar mora: " + e.getMessage());
        return false;
    }
}
    
}




              
 
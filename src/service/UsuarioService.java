/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import Model.Usuario;
import repository.UsuarioRepository;
import repository.UsuarioRepositoryImpl;
import java.util.ArrayList;
import java.util.List;
 /*
 * @author josed
 */
public class UsuarioService {
    private UsuarioRepository usuarioRepository;  //entrelaza la bd para guardar,actualizar,buscar
    
    public UsuarioService() {
        try {
            this.usuarioRepository = new UsuarioRepositoryImpl();
            System.out.println("✅ UsuarioService inicializado con Base de Datos");
            
        } catch (Exception e) {
            System.out.println("❌ Error crítico al inicializar UsuarioService: " + e.getMessage());
        }
    }
    //Listar todos los usuarios
    public List<Usuario> listarTodosUsuarios() {
        try {
            System.out.println("📋 Listando todos los usuarios desde BD...");
            return usuarioRepository.findAll();
            
        } catch (Exception e) {
            System.out.println("❌ Error al listar usuarios: " + e.getMessage());
            return new ArrayList<>(); 
        }
    }
    //buscar usuarios por id
    public Usuario buscarUsuarioPorId(int id) {
        try {
            System.out.println("🔎 Buscando usuario por ID: " + id);
            
            Usuario usuario = usuarioRepository.findById(id);
            if (usuario != null) {
                System.out.println("✅ Usuario encontrado: " + usuario.getNombre());
                return usuario;
            }
            
            throw new RuntimeException("Usuario con ID " + id + " no encontrado");
            
        } catch (RuntimeException e) {
            System.out.println("❌ Error en búsqueda por ID: " + e.getMessage());
            return null;
        } finally {
            System.out.println("--- Búsqueda por ID finalizada ---");
        }
    }
    
    public Usuario buscarUsuarioPorEmail(String email) {
        try {
            System.out.println("🔍 Buscando usuario por email: " + email);
            
            if (email == null || email.isEmpty()) {
                throw new IllegalArgumentException("El email no puede estar vacío");
            }
            
            Usuario usuario = usuarioRepository.findByEmail(email);
            if (usuario != null) {
                System.out.println("✅ Usuario encontrado: " + usuario.getNombre());
                return usuario;
            }
            
            throw new RuntimeException("Usuario con email '" + email + "' no encontrado");
            
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Error de validación: " + e.getMessage());
            return null;
        } catch (RuntimeException e) {
            System.out.println("❌ Error de búsqueda: " + e.getMessage());
            return null;
        } finally {
            System.out.println("--- Búsqueda por email finalizada ---");
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
            if (usuarioRepository.findByEmail(nuevoUsuario.getEmail()) != null) {
                throw new RuntimeException("Ya existe un usuario con el email: " + nuevoUsuario.getEmail());
            }
            
            // Validar tipo de usuario
            String tipo = nuevoUsuario.getTipo();
            if (!tipo.equals("ADMIN") && !tipo.equals("PROFESOR") && !tipo.equals("ALUMNO")) {
                throw new RuntimeException("Tipo de usuario inválido: " + tipo);
            }
            
            // Crear usuario (el ID se genera automáticamente en la BD)
            Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);
            if (usuarioGuardado != null) {
                System.out.println("✅ Usuario creado exitosamente: " + usuarioGuardado.getNombre() + " (ID: " + usuarioGuardado.getId() + ")");
                return true;
            } else {
                throw new RuntimeException("Error al guardar usuario en la base de datos");
            }
            
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Error de datos: " + e.getMessage());
            return false;
        } catch (RuntimeException e) {
            System.out.println("❌ Error de negocio: " + e.getMessage());
            return false;
        } finally {
            System.out.println("--- Operación crear usuario finalizada ---");
        }
    }
    
    public boolean restablecerContraseña(String email) {
        try {
            System.out.println("🔄 Intentando restablecer contraseña para: " + email);
            
            if (email == null || email.isEmpty()) {
                throw new IllegalArgumentException("El email no puede estar vacío");
            }
            
            Usuario usuario = usuarioRepository.findByEmail(email);
            if (usuario == null) {
                throw new RuntimeException("No existe usuario con el email: " + email);
            }
            
            String nuevaPassword = generarPasswordTemporal();
            usuario.setPassword(nuevaPassword);
            
            Usuario usuarioActualizado = usuarioRepository.update(usuario);
            if (usuarioActualizado != null) {
                System.out.println("✅ Contraseña restablecida para: " + usuario.getNombre());
                System.out.println("📧 Nueva contraseña temporal: " + nuevaPassword);
                return true;
            } else {
                throw new RuntimeException("Error al actualizar contraseña");
            }
            
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Error de datos: " + e.getMessage());
            return false;
        } catch (RuntimeException e) {
            System.out.println("❌ Error: " + e.getMessage());
            return false;
        } finally {
            System.out.println("--- Restablecimiento de contraseña finalizado ---");
        }
    }
    
    public boolean tieneMora(int usuarioId) {
        try {
            System.out.println("💰 Verificando mora para usuario ID: " + usuarioId);
            
            Usuario usuario = usuarioRepository.findById(usuarioId);
            if (usuario != null) {
                boolean tieneMora = usuario.getMora() > 0;
                System.out.println("ℹ️ Usuario " + usuario.getNombre() + 
                                  " - Mora: $" + usuario.getMora() + 
                                  " - Tiene mora: " + tieneMora);
                return tieneMora;
            }
            
            throw new RuntimeException("Usuario con ID " + usuarioId + " no encontrado");
            
        } catch (RuntimeException e) {
            System.out.println("❌ Error al verificar mora: " + e.getMessage());
            return false;
        } finally {
            System.out.println("--- Verificación de mora finalizada ---");
        }
    }
    
    // ✅ MÉTODO NUEVO: Actualizar mora (necesario para PrestamoService)
    public boolean actualizarMoraUsuario(int usuarioId, double mora) {
        try {
            System.out.println("💰 Actualizando mora para usuario ID: " + usuarioId + " - Mora: $" + mora);
            
            // Usar el método específico del repository para actualizar mora
            boolean actualizado = usuarioRepository.updateMora(usuarioId, mora);
            if (actualizado) {
                System.out.println("✅ Mora actualizada correctamente");
                return true;
            } else {
                throw new RuntimeException("No se pudo actualizar la mora del usuario");
            }
            
        } catch (RuntimeException e) {
            System.out.println("❌ Error al actualizar mora: " + e.getMessage());
            return false;
        }
    }
    
    // MÉTODO PRIVADO (se mantiene igual)
    private String generarPasswordTemporal() {
        try {
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
    
    public boolean existeEmail(String email) {
    return usuarioRepository.existeEmail(email);
}

public boolean actualizarPassword(String email, String nuevaPassword) {
    return usuarioRepository.actualizarPassword(email, nuevaPassword);
}
    
}


              
 
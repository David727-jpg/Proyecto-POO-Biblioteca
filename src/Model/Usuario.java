/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author josed
 */
public class Usuario {
    private String nombre;
    private int id; 
    private String email;
    private String password;
    private String tipo; // "ADMINISTRADOR","PROFESOR", "ALUMNO"
    private double mora;
    private boolean activo;
    
    public Usuario(){
        
    }
    
    public Usuario(String nombre, String email, String password, String tipo){
        this.nombre = nombre; 
        this.email = email;
        this.password = password;
        this.tipo = tipo;
        this.mora = 0.0;
        this.activo = true;
               
    }
    
    public Usuario(String nombre, String email, String password, String tipo, int id){
    this(nombre, email, password, tipo); // Llama al constructor principal
    this.id = id; // Asigna el ID
}
      

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getMora() {
        return mora;
    }

    public void setMora(double mora) {
        this.mora = mora;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
        
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
            
    
    
}

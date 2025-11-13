/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;
import java.util.Date;
/**
 *
 * @author josed
 */
public class Prestamo {
    private int id; // CAMBIADO de String a int
    private int idUsuario;
    private int idEjemplar;
    private Date fechadePrestamo;
    private Date fechaDeDevolucion;
    private String estado;
    private Double mora;
    
    public Prestamo(){
        // Constructor por defecto
    }

    // Constructor completo
    public Prestamo(int id, int idUsuario, int idEjemplar, Date fechadePrestamo){
        this.id = id;
        this.idUsuario = idUsuario;
        this.idEjemplar = idEjemplar;
        this.fechadePrestamo = fechadePrestamo;
        this.estado = "ACTIVO"; // Cambiado a mayúsculas para consistencia
        this.mora = 0.0;
    }

    // Getters y Setters (CORREGIDOS)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdEjemplar() {
        return idEjemplar;
    }

    public void setIdEjemplar(int idEjemplar) {
        this.idEjemplar = idEjemplar;
    }

    public Date getFechadePrestamo() {
        return fechadePrestamo;
    }

    public void setFechadePrestamo(Date fechadePrestamo) {
        this.fechadePrestamo = fechadePrestamo;
    }

    public Date getFechaDeDevolucion() {
        return fechaDeDevolucion;
    }

    public void setFechaDeDevolucion(Date fechaDeDevolucion) {
        this.fechaDeDevolucion = fechaDeDevolucion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getMora() {
        return mora;
    }

    public void setMora(Double mora) {
        this.mora = mora;
    }
}
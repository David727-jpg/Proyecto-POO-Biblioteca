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
public class Configuracion {
    private int id;
    private int PrestamosMaximos;
    private double moraDiaria;
    private int diasPrestamo;
    
    public Configuracion(){
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrestamosMaximos() {
        return PrestamosMaximos;
    }

    public void setPrestamosMaximos(int PrestamosMaximos) {
        this.PrestamosMaximos = PrestamosMaximos;
    }

    public double getMoraDiaria() {
        return moraDiaria;
    }

    public void setMoraDiaria(double moraDiaria) {
        this.moraDiaria = moraDiaria;
    }

    public int getDiasPrestamo() {
        return diasPrestamo;
    }

    public void setDiasPrestamo(int diasPrestamo) {
        this.diasPrestamo = diasPrestamo;
    }
    
    
    public Configuracion(int PrestamosMaximos, double moraDiaria, int diasPrestamo){
       this.PrestamosMaximos = PrestamosMaximos;
       this.moraDiaria = moraDiaria;
       this.diasPrestamo = diasPrestamo;
    }
    
}

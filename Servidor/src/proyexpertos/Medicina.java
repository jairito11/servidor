/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyexpertos;


public class Medicina
{
    private String nombre;
    private double precio;
    private String modoAdmin;
    private String enfermdad;
            
    public Medicina(String nombre, double precio, String modoAdmin, String enfermdad) {
        this.nombre = nombre;
        this.precio = precio;
        this.modoAdmin = modoAdmin;
        this.enfermdad = enfermdad;
    }


    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getModoAdmin() {
        return modoAdmin;
    }

    public void setModoAdmin(String modoAdmin) {
        this.modoAdmin = modoAdmin;
    }

    public String getEnfermdad() {
        return enfermdad;
    }

    public void setEnfermdad(String enfermdad) {
        this.enfermdad = enfermdad;
    }
    
    
}

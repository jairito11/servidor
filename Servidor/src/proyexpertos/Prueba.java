/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyexpertos;

import javax.swing.JFrame;

/**
 *
 * @author iron
 */
public class Prueba {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        VServidor v = new VServidor();
        v.setTitle("Venta de Ropa Agentes-Servidor");                   //Pone titulo
        v.setSize(650, 550);                                            //Define tamaño
        v.setVisible(true);                                             //Lo pone visible
        v.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );       //No hace nada si se cierra
        v.setLocationRelativeTo(null);
        v.ejecutarServidor(); 
    }
    
}

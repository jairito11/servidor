package proyexpertos;

import com.itextpdf.text.DocumentException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class QuintoAgente extends Agent {
     VCliente vCliente = new VCliente();
     VServidor vServidor = new VServidor();
     private String msjString;
     private int cantidad = 5; // cantidad de cosas
     private int jarabes = cantidad;
     private int omeprazoles = cantidad;
     private int naproxenos = cantidad;
     
     private int shampoos = cantidad;
     private int jabon = cantidad;
     private int toallas = cantidad;
     
     private int labial = cantidad;
     private int maquillaje = cantidad;
     
     private int gasas = cantidad;
     private int jeringas = cantidad;
     private int cubrebocas = cantidad;
     
     private int faltan = 0;
     private String productoFalta = "";
     
     private int tiempoEspera = 3000;
     private GenerarPDF generarPDF = new GenerarPDF();
     private String ticket = "";
     private String ruta = "E:/Dropbox/";
     private int costoTotal;
     private int cambio = 0;
     private int masDeUnProd = 0; // si es mas de 1 se tiene mas de un producto comprado
     private Conexion conexion;

    // Inicialización del agente
    @Override
    protected void setup() {
        conexion = new Conexion();                                              //Llama a la clase conexion
        vCliente.setTitle("Venta de Ropa Agentes-Cliente");                     //Pone titulo en el objeto VCliente
        vCliente.setLocationRelativeTo( null );                                 //Lo Pone en el centro
        vCliente.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );              //Al cerrar finaliza
        init();
        addBehaviour(new QuintoAgente.ComienzaEscucha());
    }
    
    // Definición de un comportamiento
    private class ComienzaEscucha extends Behaviour{
        // Función que realiza MiComportamiento
        public void action() {
            // Repite todo el proceso del agente
            do
            {
                vCliente.vaciarArea();
                ticket = "";
                costoTotal = 0;
                masDeUnProd = 0;
                
                vCliente.setFarmacia();
                
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    // Nada
                }
                vServidor.mostrarMensaje("Mostrando menú\n");
                vCliente.setMensaje("Mostrando menú\n");
                vCliente.setEnviaMsj(1);
                
                System.out.println("Seleccione una opción");
                System.out.println("     1.- Consulta");
                System.out.println("     2.- Procdutos y medicinas");
                System.out.println("Ingrese opción: ");
                
                vCliente.mostrarMensaje("Seleccione una opción");
                vCliente.mostrarMensaje("     1.- Consulta");
                vCliente.mostrarMensaje("     2.- Productos y Medicina");
                vCliente.mostrarMensaje("Ingrese su opción");
                
                
                do{
                    esperaRespuesta();
                    vCliente.setDioEnter(0); // Renueva el que no se ha dado enter para una siguiente nueva entrada
                    if( vCliente.getOpcion() < 1 || vCliente.getOpcion() > 2 )
                    {
                        JOptionPane.showMessageDialog( null, vCliente.getOpcion() + " no está en el rango permitido, vuelva a intentar.", "Error", JOptionPane.ERROR_MESSAGE );
                    }
                }while( vCliente.getOpcion() < 1 || vCliente.getOpcion() > 2 );
                
                System.out.println("\nSu opción fue: " + vCliente.getOpcion() );
                vCliente.mostrarMensaje("\nSu opción fue: " + vCliente.getOpcion() );
                
                if( vCliente.getOpcion() == 1 )
                {
                    AID id = new AID(); // No es el ID del emisor, es el ID del agente al que se va a enviar
                    id.setLocalName("medico");
                    
                    // Creación del objeto ACLMessage, es el canal o medio de comunicación para el mensaje
                    ACLMessage mensaje = new ACLMessage(ACLMessage.REQUEST);
                    
                    //Rellenar los campos necesarios del mensaje
                    mensaje.setSender(getAID()); // Al agente emisor se le esta asignando su ID
                    mensaje.setLanguage("Español");
                    mensaje.addReceiver(id); // Al mensaje se le añade el ide del destinatario
                    mensaje.setContent("consulta");
                    
                    //Envia el mensaje a los destinatarios
                    send(mensaje);
                    
                    mensaje = blockingReceive();
                    if (mensaje!= null)
                    {
                        vCliente.setMedico();
                        vCliente.mostrarMensaje("\nEl Cajero indica : ");
                        vCliente.mostrarMensaje("    1.- Venta");
                        vCliente.mostrarMensaje("    2.- Pedido");
                        vCliente.mostrarMensaje("    3.- Devolucion");
                        vCliente.mostrarMensaje("Ingrese su opción");
                        
                        do{
                            esperaRespuesta();
                            vCliente.setDioEnter(0); // Renueva el que no se ha dado enter para una siguiente nueva entrada
                            if( vCliente.getOpcion() < 1 || vCliente.getOpcion() > 3 )
                            {
                                JOptionPane.showMessageDialog( null, vCliente.getOpcion() + " no está en el rango permitido, vuelva a intentar.", "Error", JOptionPane.ERROR_MESSAGE );
                            }
                        }while( vCliente.getOpcion() < 1 || vCliente.getOpcion() > 3 );
                
                        System.out.println("\nSu opción fue: " + vCliente.getOpcion() );
                        vCliente.mostrarMensaje("\nSu opción fue: " + vCliente.getOpcion() + "\n" );
                
                
                        id = new AID(); // No es el ID del emisor, es el ID del agente al que se va a enviar
                        id.setLocalName("medico");

                        // Creación del objeto ACLMessage, es el canal o medio de comunicación para el mensaje
                        mensaje = new ACLMessage(ACLMessage.REQUEST);

                        //Rellenar los campos necesarios del mensaje
                        mensaje.setSender(getAID()); // Al agente emisor se le esta asignando su ID
                        mensaje.setLanguage("Español");
                        mensaje.addReceiver(id); // Al mensaje se le añade el ide del destinatario
                        mensaje.setContent( vCliente.getOpcion() + "" );
                        
                        //Envia el mensaje a los destinatarios
                        send(mensaje);
                    }
                    
                    mensaje = blockingReceive();
                    if (mensaje!= null)
                    {
                        msjString = mensaje.getContent();
                        vCliente.mostrarMensaje("El médico diagnostica y receta lo siguiente: ");
                        vCliente.mostrarMensaje( "\n     " + msjString);
                    }
                    vCliente.mostrarMensaje("\nPor favor, vaya con el vendedor.");
                    vCliente.mostrarMensaje("Cliente va con el vendedor.\n");
                    try {
                        Thread.sleep(tiempoEspera);
                    } catch (InterruptedException ex) {
                        // Nada
                    }
                    
                    vCliente.setVendedor();
                    //vCliente.mostrarMensaje("\nEl cliente llegó con el vendedor.");
                    vCliente.mostrarMensaje("\nHola! Le atiendo, por favor su receta.");
                    vCliente.mostrarMensaje("Cliente entrega receta al vendedor. ");
                    vCliente.mostrarMensaje("Vendedor recibe la receta del cliente.");
                    
                    
                    vCliente.mostrarMensaje("\n¿Desea tiket(1) o de factura(2)?");
                    
                    do{
                        esperaRespuesta();
                        vCliente.setDioEnter(0); // Renueva el que no se ha dado enter para una siguiente nueva entrada
                        if( vCliente.getOpcion() < 1 || vCliente.getOpcion() > 2 )
                        {
                            JOptionPane.showMessageDialog( null, vCliente.getOpcion() + " no está en el rango permitido, vuelva a intentar.", "Error", JOptionPane.ERROR_MESSAGE );
                        }
                    }while( vCliente.getOpcion() < 1 || vCliente.getOpcion() > 2 );


                    System.out.println("\nSu opción fue: " + vCliente.getOpcion() );
                    vCliente.mostrarMensaje("\nSu opción fue: " + vCliente.getOpcion() );

                    vCliente.mostrarMensaje("\nPor favor pase con el cajero para que le cobren.");
                    vCliente.mostrarMensaje("Termina interacción con el vendedor.\n");
                    vCliente.mostrarMensaje("Cliente va con el cajero.\n");
                    vServidor.mostrarMensaje("El cliente llegó con el cajero.\n");
                    vCliente.setMensaje("El cliente llegó con el cajero.\n");
                    vCliente.setEnviaMsj(1);
                    vCliente.setMensaje("El cliente llegó con el cajero.\n");
                    vCliente.setEnviaMsj(1);
                    
                    try {
                        Thread.sleep(tiempoEspera);
                    } catch (InterruptedException ex) {
                        // Nada
                    }
                    
                    
                    vCliente.setCajero();
                    id = new AID(); // No es el ID del emisor, es el ID del agente al que se va a enviar
                    id.setLocalName("cajero");

                    // Creación del objeto ACLMessage, es el canal o medio de comunicación para el mensaje
                    mensaje = new ACLMessage(ACLMessage.REQUEST);

                    //Rellenar los campos necesarios del mensaje
                    mensaje.setSender(getAID()); // Al agente emisor se le esta asignando su ID
                    mensaje.setLanguage("Español");
                    mensaje.addReceiver(id); // Al mensaje se le añade el ide del destinatario
                    mensaje.setContent(msjString + vCliente.getOpcion() ); // Manda el diagnostico y la receta

                    //Envia el mensaje a los destinatarios
                    send(mensaje); // Le manda la receta siendo generica o de patente
                    
                    mensaje = blockingReceive();
                    if (mensaje!= null)
                    {
                        msjString = mensaje.getContent(); // Regresa costo
                        vCliente.mostrarMensaje("Cajero dice que el costo es de $" + msjString + " más $400 de consulta.");
                        cambio = Integer.parseInt(msjString) + 400;
                        vCliente.mostrarMensaje("Total a pagar $" + cambio );
                        
                        
                        vCliente.mostrarMensaje("Por favor ingrese la cantidad para pagar (20, 50, 100, 200, etc.).");
                        
                        do{
                            esperaRespuesta();
                            vCliente.setDioEnter(0); // Renueva el que no se ha dado enter para una siguiente nueva entrada
                            if( vCliente.getOpcion() < cambio || vCliente.getOpcion() > 10000 )
                            {
                                JOptionPane.showMessageDialog( null, vCliente.getOpcion() + " no está en el rango permitido, vuelva a intentar.", "Error", JOptionPane.ERROR_MESSAGE );
                            }
                        }while( vCliente.getOpcion() < cambio || vCliente.getOpcion() > 10000 );


                        System.out.println("\nCajero recibe " + vCliente.getOpcion() );
                        vCliente.mostrarMensaje("\nCajero recibe: $" + vCliente.getOpcion() );
                        vServidor.mostrarMensaje("\nCajero recibe: $" + vCliente.getOpcion() );
                        vCliente.setMensaje("\nCajero recibe: $" + vCliente.getOpcion());
                        vCliente.setEnviaMsj(1);
                        
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException ex) {
                            
                        }
                        
                        int cambio2 = cambio;
                        vCliente.mostrarMensaje("Cliente paga $" + cambio + " al cajero.");
                        
                        cambio = vCliente.getOpcion() - Integer.parseInt( msjString ) - 400;
                        vCliente.mostrarMensaje("Cliente recibe $" + cambio + " de cambio.");
                        
                        vServidor.mostrarMensaje("\nCajero recibe pago del ciente por $" + cambio2 );
                        vCliente.setMensaje("\nCajero recibe pago del ciente por $" + cambio2 );
                        vCliente.setEnviaMsj(1);
                        
                        vCliente.mostrarMensaje("\nCajero da medicinas al cliente.");
                        
                        try {
                            Thread.sleep(tiempoEspera);
                        } catch (InterruptedException ex) {
                            // Nada
                        }
                        vCliente.mostrarMensaje("Se le da ticket al cliente:\n\n");
                        
                        ticket = " === Ticket === \n";
                        costoTotal = 400; // De la consulta
                        if( msjString.equals("100") ) // Jarabe generico
                        {
                            ticket = ticket + " Medicina: Jarabe \n";
                            ticket = ticket + " Costo: $100\n";
                            ticket = ticket + " Producto genérico\n";
                            jarabes--;
                            costoTotal = costoTotal + 100;
                        } else if( msjString.equals("200") ) // Jarabe generico
                        {
                            ticket = ticket + " Medicina: Jarabe \n";
                            ticket = ticket + " Costo: $200\n";
                            ticket = ticket + " Producto patente\n";
                            jarabes--;
                            costoTotal = costoTotal + 200;
                        } else if( msjString.equals("220") ) // Jarabe generico
                        {
                            ticket = ticket + " Medicina: Omeprazol \n";
                            ticket = ticket + " Costo: $220\n";
                            ticket = ticket + " Producto genérico\n";
                            omeprazoles--;
                            costoTotal = costoTotal + 220;
                        } else if( msjString.equals("340") ) // Jarabe generico
                        {
                            ticket = ticket + " Medicina: Omeprazol \n";
                            ticket = ticket + " Costo: $340\n";
                            ticket = ticket + " Producto patente\n";
                            omeprazoles--;
                            costoTotal = costoTotal + 340;
                        } else if( msjString.equals("180") ) // Jarabe generico
                        {
                            ticket = ticket + " Medicina: Naproxeno \n";
                            ticket = ticket + " Costo: $180\n";
                            ticket = ticket + " Producto genérico\n";
                            naproxenos--;
                            costoTotal = costoTotal + 180;
                        } else if( msjString.equals("640") ) // Jarabe generico
                        {
                            ticket = ticket + " Medicina: Naproxeno \n";
                            ticket = ticket + " Costo: $640\n";
                            ticket = ticket + " Producto patente\n";
                            naproxenos--;
                            costoTotal = costoTotal + 640;
                        }
                        ticket = ticket + " Concepto: Consulta médica \n";
                        ticket = ticket + " Costo: $400\n";
                        ticket = ticket + " Costo Total: $" + costoTotal + "\n\n";
                        
                        //vCliente.setEtiqueta( " Total = $ " + costoTotal );
                        vCliente.setTicket();
                         vCliente.mostrarMensaje(ticket);
                    }
                    
                    // Termina consulta
                } else
                {
                    // Opción 2 - Comprar medicinas
                    vCliente.mostrarMensaje("El cliente pasa con el vendedor.");
                    
                    vCliente.setVendedor();
                    AID id = new AID(); // No es el ID del emisor, es el ID del agente al que se va a enviar
                    id.setLocalName("vendedor");
                    
                    // Creación del objeto ACLMessage, es el canal o medio de comunicación para el mensaje
                    ACLMessage mensaje = new ACLMessage(ACLMessage.REQUEST);
                    
                    //Rellenar los campos necesarios del mensaje
                    mensaje.setSender(getAID()); // Al agente emisor se le esta asignando su ID
                    mensaje.setLanguage("Español");
                    mensaje.addReceiver(id); // Al mensaje se le añade el ide del destinatario
                    mensaje.setContent("compra");
                    
                    //Envia el mensaje a los destinatarios
                    send(mensaje);
                    
                    mensaje = blockingReceive();
                    
                    int opcion;
                    int costo;
                    ticket = " === Ticket === \n";
                    if (mensaje!= null)
                    {
                        do
                        {
                            vCliente.mostrarMensaje("\nVendedor muestra catalogo de productos:");
                            vCliente.mostrarMensaje("     1.- Jarabe.");
                            vCliente.mostrarMensaje("     2.- Omeprazol.");
                            vCliente.mostrarMensaje("     3.- Naproxeno.");
                            vCliente.mostrarMensaje("     4.- Shampoo.");
                            vCliente.mostrarMensaje("     5.- Jabón de baño.");
                            vCliente.mostrarMensaje("     6.- Toalla femenia.");
                            vCliente.mostrarMensaje("     7.- Labial.");
                            vCliente.mostrarMensaje("     8.- Maquillaje.");
                            vCliente.mostrarMensaje("     9.- Gasas.");
                            vCliente.mostrarMensaje("     10.- Jeringas.");
                            vCliente.mostrarMensaje("     11.- Cubrebocas.");
                            if( masDeUnProd != 0 )
                            {
                                vCliente.mostrarMensaje("     12.- Continuar.");
                            }
                            masDeUnProd++;
                            vCliente.mostrarMensaje("Ingrese su opción: ");
                            
                            do{
                                esperaRespuesta();
                                vCliente.setDioEnter(0); // Renueva el que no se ha dado enter para una siguiente nueva entrada
                                if( vCliente.getOpcion() < 1 || vCliente.getOpcion() > 12 )
                                {
                                    JOptionPane.showMessageDialog( null, vCliente.getOpcion() + " no está en el rango permitido, vuelva a intentar.", "Error", JOptionPane.ERROR_MESSAGE );
                                }
                            }while( vCliente.getOpcion() < 1 || vCliente.getOpcion() > 12 );

                            opcion = vCliente.getOpcion();

                            System.out.println("\nSu opción fue: " + vCliente.getOpcion() );
                            vCliente.mostrarMensaje("\nSu opción fue: " + vCliente.getOpcion() );

                            // Compra medicina
                            if( vCliente.getOpcion() == 1 || vCliente.getOpcion() == 2 || vCliente.getOpcion() == 3 )
                            {
                                vCliente.mostrarMensaje("\n\n ¿Medicina genérica(1) o de patente(2)?");
                                
                                do{
                                    esperaRespuesta();
                                    vCliente.setDioEnter(0); // Renueva el que no se ha dado enter para una siguiente nueva entrada
                                    if( vCliente.getOpcion() < 1 || vCliente.getOpcion() > 2 )
                                    {
                                        JOptionPane.showMessageDialog( null, vCliente.getOpcion() + " no está en el rango permitido, vuelva a intentar.", "Error", JOptionPane.ERROR_MESSAGE );
                                    }
                                }while( vCliente.getOpcion() < 1 || vCliente.getOpcion() > 2 );

                                System.out.println("\n Su opción fue: " + vCliente.getOpcion() );
                                vCliente.mostrarMensaje("\n Su opción fue: " + vCliente.getOpcion() + "\n" );

                            }


                            costo = 0;
                            switch (opcion ) {
                                case 1:
                                    if( vCliente.getOpcion() == 1 ) // generica
                                    {
                                        costo = 100;
                                    } else
                                    {
                                        costo = 200;
                                    }
                                    break;
                                case 2:
                                    if( vCliente.getOpcion() == 1 ) // generica
                                    {
                                        costo = 220;
                                    } else
                                    {
                                        costo = 340;
                                    }
                                    break;
                                case 3:
                                    if( vCliente.getOpcion() == 1 ) // generica
                                    {
                                        costo = 180;
                                    } else
                                    {
                                        costo = 640;
                                    }
                                    break;
                                case 4:
                                    costo = 55;
                                    break;
                                case 5:
                                    costo = 20;
                                    break;
                                case 6:
                                    costo = 60;
                                    break;
                                case 7:
                                    costo = 90;
                                    break;
                                case 8:
                                    costo = 85;
                                    break;
                                case 9:
                                    costo = 25;
                                    break;
                                case 10:
                                    costo = 15;
                                    break;
                                case 11:
                                    costo = 10;
                                    break;
                                default:
                                    break;
                            }

                            costoTotal = costoTotal + costo;
                            
                            if( costo == 100 ) // Jarabe generico
                            {
                                ticket = ticket + " Medicina: Jarabe \n";
                                ticket = ticket + " Costo: $100\n";
                                ticket = ticket + " Producto genérico\n";
                                jarabes--;
                            } else if( costo == 200 ) // Jarabe generico
                            {
                                ticket = ticket + " Medicina: Jarabe \n";
                                ticket = ticket + " Costo: $200\n";
                                ticket = ticket + " Producto patente\n";
                                jarabes--;
                            } else if( costo == 220 ) // Jarabe generico
                            {
                                ticket = ticket + " Medicina: Omeprazol \n";
                                ticket = ticket + " Costo: $220\n";
                                ticket = ticket + " Producto genérico\n";
                                omeprazoles--;
                            } else if( costo == 340 ) // Jarabe generico
                            {
                                ticket = ticket + " Medicina: Omeprazol \n";
                                ticket = ticket + " Costo: $340\n";
                                ticket = ticket + " Producto patente\n";
                                omeprazoles--;
                            } else if( costo == 180 ) // Jarabe generico
                            {
                                ticket = ticket + " Medicina: Naproxeno \n";
                                ticket = ticket + " Costo: $180\n";
                                ticket = ticket + " Producto genérico\n";
                                naproxenos--;
                            } else if( costo == 640 ) // Jarabe generico
                            {
                                ticket = ticket + " Medicina: Naproxeno \n";
                                ticket = ticket + " Costo: $640\n";
                                ticket = ticket + " Producto patente\n";
                                naproxenos--;
                            } else if( costo == 55 )
                            {
                                ticket = ticket + " Medicina: Shampoo \n";
                                ticket = ticket + " Costo: $55\n";
                                shampoos--;
                            } else if( costo == 20 )
                            {
                                ticket = ticket + " Medicina: Jabón de baño \n";
                                ticket = ticket + " Costo: $20\n";
                                jabon--;
                            } else if( costo == 60 )
                            {
                                ticket = ticket + " Medicina: Toallas femeninas \n";
                                ticket = ticket + " Costo: $60\n";
                                toallas--;
                            } else if( costo == 90 )
                            {
                                ticket = ticket + " Medicina: Labial \n";
                                ticket = ticket + " Costo: $90\n";
                                labial--;
                            } else if( costo == 85 )
                            {
                                ticket = ticket + " Medicina: Maquillaje \n";
                                ticket = ticket + " Costo: $85\n";
                                maquillaje--;
                            } else if( costo == 25 )
                            {
                                ticket = ticket + " Medicina: Gasas \n";
                                ticket = ticket + " Costo: $25\n";
                                gasas--;
                            } else if( costo == 15 )
                            {
                                ticket = ticket + " Medicina: Jeringas \n";
                                ticket = ticket + " Costo: $15\n";
                                jeringas--;
                            } else if( costo == 10 )
                            {
                                ticket = ticket + " Medicina: Cubrebocas \n";
                                ticket = ticket + " Costo: $10\n";
                                cubrebocas--;
                            }
                        }while( opcion != 12 );
                        
                        ticket = ticket + " Costo Total: " + costoTotal ;
                        vCliente.mostrarMensaje("\n\nPor favor pase con el cajero para que le cobren.");
                        vCliente.mostrarMensaje("Termina interaccion con el vendedor.");
                        vCliente.mostrarMensaje("\n\nCliente va con el cajero");
                        
                        try {
                            Thread.sleep(tiempoEspera);
                        } catch (InterruptedException ex) {
                            // Nada
                        }
                        
                        vCliente.setCajero();
                        vServidor.mostrarMensaje("\nEl cliente llegó con el cajero.");
                        vCliente.setMensaje("\nEl cliente llegó con el cajero.");
                        vCliente.setEnviaMsj(1);
                        
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException ex) {
                            
                        }
                        
                        vServidor.mostrarMensaje("\nCajero revisa costo y le informa al cliente.");
                        vCliente.setMensaje("\nCajero revisa costo y le informa al cliente.");
                        vCliente.setEnviaMsj(1);
                        vCliente.mostrarMensaje("\nCajero dice que el costo es de " + costoTotal );
                        
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException ex) {
                            
                        }
                        
                        vCliente.setMensaje("\nCajero dice que el costo es de " + costoTotal );
                        vCliente.setEnviaMsj(1);
                        
                        vCliente.mostrarMensaje("Por favor ingrese la cantidad para pagar (20 50 100 200 500 1000).");
                        
                        do{
                            esperaRespuesta();
                            vCliente.setDioEnter(0); // Renueva el que no se ha dado enter para una siguiente nueva entrada
                            if( vCliente.getOpcion() < costoTotal || vCliente.getOpcion() > 10000 )
                            {
                                JOptionPane.showMessageDialog( null, vCliente.getOpcion() + " no está en el rango permitido, vuelva a intentar.", "Error", JOptionPane.ERROR_MESSAGE );
                            }
                        }while( vCliente.getOpcion() < costoTotal || vCliente.getOpcion() > 10000 );

                        System.out.println("\nCajero recibe " + vCliente.getOpcion() );
                        vCliente.mostrarMensaje("\nCajero recibe: $" + vCliente.getOpcion() );
                        vServidor.mostrarMensaje("\nCajero recibe: $" + vCliente.getOpcion() );
                        vCliente.setMensaje("\nCajero recibe: $" + vCliente.getOpcion() );
                        vCliente.setEnviaMsj(1);
                        
                        cambio = vCliente.getOpcion() - costoTotal;
                        
                        
                        vCliente.mostrarMensaje("Cliente paga $" + costoTotal + " al cajero.");
                        vCliente.mostrarMensaje("Cliente recibe $" + cambio + " de cambio.");
                        
                        vServidor.mostrarMensaje("\nCajero recibe pago del ciente por " + costoTotal );
                        vCliente.setMensaje("\nCajero recibe pago del ciente por " + costoTotal );
                        vCliente.setEnviaMsj(1);
                        vCliente.mostrarMensaje("\nCajero da productos al cliente.");
                        
                        try {
                            Thread.sleep(tiempoEspera);
                        } catch (InterruptedException ex) {
                            // Nada
                        }
                        
                        vCliente.mostrarMensaje("\nSe le da ticket al cliente:\n\n");
                        vServidor.mostrarMensaje("\nCajero dio productos y ticket al cliente.");
                        vCliente.setMensaje("\nCajero dio productos y ticket al cliente.");
                        vCliente.setEnviaMsj(1);
                        
                        //vCliente.setEtiqueta( " Total = $ " + costoTotal );
                        vCliente.setTicket();
                        vCliente.mostrarMensaje(ticket);
                    }
                }
                
                try {
                    generarPDF.genera(ticket, "Ticket", ruta );
                } catch (FileNotFoundException | DocumentException ex) {
                    System.out.println("-----Error al generar ticket ----- ");
                }
                
                
                /*File file = new File("c:/newfile.txt");

                try {
                    if( file.createNewFile() ){
                        System.out.println("Fichero creado correctamente");
                    } else {
                        System.out.println("El fichero ya existe");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(QuintoAgente.class.getName()).log(Level.SEVERE, null, ex);
                }*/

                
                vCliente.mostrarMensaje("\nSe ha generado el ticket del cliente en PDF.\n");
                vServidor.mostrarMensaje("\nSe ha generado el ticket del cliente en PDF.");
                vCliente.setMensaje("\nSe ha generado el ticket del cliente en PDF.");
                        vCliente.setEnviaMsj(1);
                
                // Verifica si ya se van a acabar los productos para comprar mas
                if( jarabes <= 1 )
                {
                    productoFalta = "jarabes";
                    faltan = 1;
                }
                if( omeprazoles <= 1 )
                {
                    productoFalta = "omeprazoles";
                    faltan = 1;
                }
                if( naproxenos <= 1 )
                {
                    productoFalta = "naproxenos";
                    faltan = 1;
                }
                
                // Higiene
                if( shampoos <= 1 )
                {
                    productoFalta = "shampoos";
                    faltan = 1;
                }
                if( jabon <= 1 )
                {
                    productoFalta = "jabones";
                    faltan = 1;
                }
                if( toallas <= 1 )
                {
                    productoFalta = "toallas femenias";
                    faltan = 1;
                }
                
                // Cosmeticos
                if( labial <= 1 )
                {
                    productoFalta = "labiales";
                    faltan = 1;
                }
                if( maquillaje <= 1 )
                {
                    productoFalta = "maquillajes";
                    faltan = 1;
                }
                
                // Salud
                if( gasas <= 1 )
                {
                    productoFalta = "gasas";
                    faltan = 1;
                }
                if( jeringas <= 1 )
                {
                    productoFalta = "jeringas";
                    faltan = 1;
                }
                if( cubrebocas <= 1 )
                {
                    productoFalta = "cubrebocas";
                    faltan = 1;
                }
                
                if( faltan == 1 )
                {
                    vServidor.mostrarMensaje("Están apunto de terminarse l@s " + productoFalta + ".\n");
                    vServidor.mostrarMensaje("Cajero pide más " + productoFalta + " a proveedor.\n");
                    
                    vCliente.setMensaje("Están apunto de terminarse l@s " + productoFalta + ".\n");
                        vCliente.setEnviaMsj(1);
                        
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException ex) {
                        
                    }
                        
                    vCliente.setMensaje("Cajero pide más " + productoFalta + " a proveedor.\n");
                        vCliente.setEnviaMsj(1);
                    
                    AID id = new AID(); // No es el ID del emisor, es el ID del agente al que se va a enviar
                    id.setLocalName("proveedor");

                    // Creación del objeto ACLMessage, es el canal o medio de comunicación para el mensaje
                    ACLMessage mensaje = new ACLMessage(ACLMessage.REQUEST);

                    //Rellenar los campos necesarios del mensaje
                    mensaje.setSender(getAID()); // Al agente emisor se le esta asignando su ID
                    mensaje.setLanguage("Español");
                    mensaje.addReceiver(id); // Al mensaje se le añade el ide del destinatario
                    mensaje.setContent("productos");

                    //Envia el mensaje a los destinatarios
                    send(mensaje);


                    mensaje = blockingReceive();
                    if (mensaje!= null)
                    {
                        // Se reciben productos
                        if( jarabes <= 1 )
                        {
                            productoFalta = "jarabes";
                            jarabes = cantidad;
                        }
                        if( omeprazoles <= 1 )
                        {
                            productoFalta = "omeprazoles";
                            omeprazoles = cantidad;
                        }
                        if( naproxenos <= 1 )
                        {
                            productoFalta = "naproxenos";
                            naproxenos = cantidad;
                        }

                        // Higiene
                        if( shampoos <= 1 )
                        {
                            productoFalta = "shampoos";
                            shampoos = cantidad;
                        }
                        if( jabon <= 1 )
                        {
                            productoFalta = "jabones";
                            jabon = cantidad;
                        }
                        if( toallas <= 1 )
                        {
                            productoFalta = "toallas femenias";
                            toallas = cantidad;
                        }

                        // Cosmeticos
                        if( labial <= 1 )
                        {
                            productoFalta = "labiales";
                            labial = cantidad;
                        }
                        if( maquillaje <= 1 )
                        {
                            productoFalta = "maquillajes";
                            maquillaje = cantidad;
                        }

                        // Salud
                        if( gasas <= 1 )
                        {
                            productoFalta = "gasas";
                            gasas = cantidad;
                        }
                        if( jeringas <= 1 )
                        {
                            productoFalta = "jeringas";
                            jeringas = cantidad;
                        }
                        if( cubrebocas <= 1 )
                        {
                            productoFalta = "cubrebocas";
                            cubrebocas = cantidad;
                        }
                        
                        vServidor.mostrarMensaje("Fueron comprados al proveedor " + productoFalta );
                        vServidor.mostrarMensaje("Stock lleno para " + productoFalta + "\n" );
                        
                        vCliente.setMensaje("Fueron comprados al proveedor " + productoFalta );
                        vCliente.setEnviaMsj(1);
                        
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException ex) {

                        }

                        vCliente.setMensaje("Stock lleno para " + productoFalta + "\n" );
                            vCliente.setEnviaMsj(1);
                        }
                }
                faltan = 0;
                
                vCliente.mostrarMensaje("\nPresione Enter para continuar...");
                vCliente.meterNum(1);
                esperaRespuesta();
                vCliente.setDioEnter(0); // Renueva el que no se ha dado enter para una siguiente nueva entrada
                
                try {
                    Thread.sleep(50); // Espera 50 milisegundos para no calentar el procesador
                } catch (InterruptedException ex) {
                    // Nada
                }
            } while( true );
        }
        
        private void esperaRespuesta()
        {
            do{
                try {
                    Thread.sleep(50); // Espera 50 milisegundos para no calentar el procesador
                } catch (InterruptedException ex) {
                    // Nada
                }
            } while( vCliente.getDioEnter() == 0 );
        }
        
        @Override
        public boolean done()
        {
            return true;
        }
        
        
        
        public long leerInt( String dato )
        {
            int recibeNum = 0;
            try
            {
                recibeNum = Integer.parseInt( dato );
            }catch( NumberFormatException e)
            {

            }
            return recibeNum;
        }
    }
    
    public void init()
    {
        vServidor.setTitle("Venta de Ropa Agentes-Servidor");                   //Pone titulo
        vServidor.setSize(650, 550);                                            //Define tamaño
        vServidor.setVisible(true);                                             //Lo pone visible
        vServidor.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );       //No hace nada si se cierra
        vServidor.setLocationRelativeTo(null);                                  //Lo pone al centro
        vServidor.ejecutarServidor();                                           //Ejecuta el servidor
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyexpertos;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Medico extends Agent{
    
    private String mensajeString;
    private String medicina;
    
    // Inicialización del agente
    @Override
    protected void setup(){
        // Añade un comportamiento
        addBehaviour(new Consulta());
    }
    
    private class Consulta extends Behaviour
    {
            @Override
            public void action()
            {
                do
                {
                    System.out.println("\n Preparandose para dar consulta\n");

                    //Obtiene el primer mensaje de la cola de mensajes
                    //ACLMessage mensaje = receive();
                    ACLMessage mensaje = blockingReceive();
                    if (mensaje!= null)
                    {
                        mensajeString = mensaje.getContent();
                        System.out.println("Mensaje para el médico. Se solicita: " + mensajeString );
                    }
                    
                    AID id = new AID(); // No es el ID del emisor, es el ID del agente al que se va a enviar
                    id.setLocalName("quinto");
                    
                    // Creación del objeto ACLMessage, es el canal o medio de comunicación para el mensaje
                    mensaje = new ACLMessage(ACLMessage.REQUEST);
                    
                    //Rellenar los campos necesarios del mensaje
                    mensaje.setSender(getAID()); // Al agente emisor se le esta asignando su ID
                    mensaje.setLanguage("Español");
                    mensaje.addReceiver(id); // Al mensaje se le añade el ide del destinatario
                    mensaje.setContent("sintomas");
                    
                    //Envia el mensaje a los destinatarios
                    send(mensaje);
                    
                    
                    // Espera ver que sintoma es
                    mensaje = blockingReceive();
                    if (mensaje!= null)
                    {
                        mensajeString = mensaje.getContent();
                        if( mensajeString.equals("1") ) // Tos y es gripa
                        {
                            //medicina = "Usted tiene tos, tome Jarabe";
                            medicina = "camisas";
                        } else if( mensajeString.equals("2") ) // Gastritis
                        {
                            //medicina = "Usted tiene gastritis, tome Omeprazol";
                            medicina = "pantalones";
                        } else // colicos
                        {
                            //medicina = "Usted tiene inflamación que le causa dolor, tome Naproxeno";
                            medicina = "zapatos";
                        }
                        
                        id = new AID(); // No es el ID del emisor, es el ID del agente al que se va a enviar
                        id.setLocalName("quinto");

                        // Creación del objeto ACLMessage, es el canal o medio de comunicación para el mensaje
                        mensaje = new ACLMessage(ACLMessage.REQUEST);

                        //Rellenar los campos necesarios del mensaje
                        mensaje.setSender(getAID()); // Al agente emisor se le esta asignando su ID
                        mensaje.setLanguage("Español");
                        mensaje.addReceiver(id); // Al mensaje se le añade el ide del destinatario
                        mensaje.setContent(medicina);

                        //Envia el mensaje a los destinatarios
                        send(mensaje);
                    }
                    
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        // Nada
                    }
                } while(true);
            }
            
            @Override
            public boolean done()
            {
                return true;
            }
    }
}

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

public class Cajero extends Agent
{
    private String msjString;
    private int costo;
    
     // Inicialización del agente
    @Override
    protected void setup(){
        // Añade un comportamiento
        addBehaviour(new VaVender());
    }
    
    private class VaVender extends Behaviour
    {
        @Override
        public void action()
        {
            do
            {
                // System.out.println("\n Cajero esta listo.");
                ACLMessage mensaje = blockingReceive();
                if (mensaje!= null)
                {
                    msjString = mensaje.getContent();
                    if( !msjString.equals("comprar") ) // no fue compra, es de consulta
                    {
                        if( msjString.endsWith("Jarabe1") )
                        {
                            costo = 100;
                        } else if( msjString.endsWith("Jarabe2") )
                        {
                            costo = 200;
                        } else if( msjString.endsWith("Omeprazol1") )
                        {
                            costo = 220;
                        } else if( msjString.endsWith("Omeprazol2") )
                        {
                            costo = 340;
                        } else if( msjString.endsWith("Naproxeno1") )
                        {
                            costo = 180;
                        } else if( msjString.endsWith("Naproxeno2") )
                        {
                            costo = 640;
                        }

                        AID id = new AID(); // No es el ID del emisor, es el ID del agente al que se va a enviar
                        id.setLocalName("quinto");

                    // Creación del objeto ACLMessage, es el canal o medio de comunicación para el mensaje
                        mensaje = new ACLMessage(ACLMessage.REQUEST);

                    //Rellenar los campos necesarios del mensaje
                        mensaje.setSender(getAID()); // Al agente emisor se le esta asignando su ID
                        mensaje.setLanguage("Español");
                        mensaje.addReceiver(id); // Al mensaje se le añade el ide del destinatario
                        mensaje.setContent("" + costo );

                        //Envia el mensaje a los destinatarios
                        send(mensaje); // Le manda mensaje diciendole cuanto es
                    } else
                    {
                        // Otro mensaje
                    }
                }
                
                    
                
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    // Nada
                }
            } while( true );
        }
        
        @Override
        public boolean done()
        {
            return true;
        }
    }
}

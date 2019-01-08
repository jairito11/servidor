package proyexpertos;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;


public class VServidor extends JFrame implements ActionListener {

    private JTextArea areaPantalla; // muestra la información al usuario
    private Font fuente = new Font("Century", Font.BOLD, 14);
    private JLabel ticket;
    private JButton btnSalir;

    private ObjectOutputStream salida; // flujo de salida hacia el cliente
    private ObjectInputStream entrada; // flujo de entrada del cliente
    private ServerSocket servidor; // socket servidor
    private Socket conexion; // conexión al cliente
    private String datos;  // recibe y guarda los datos del sistema cliente
    private int contador = 1; // contador del número de conexiones
    private Conexion conexionBD;

    public VServidor() {
                                                                                //1er Modulo
        conexionBD = new Conexion();                                            //Crea un objeto de tipo conexion
        btnSalir = new JButton("Terminar");                                     //Pone en memoria un boton
        add(btnSalir, BorderLayout.NORTH);                                      //La agrega al principio
        btnSalir.addActionListener(this);                                       //Agrega listener

        areaPantalla = new JTextArea();                                         // crea objeto JTextArea
        add(new JScrollPane(areaPantalla), BorderLayout.CENTER);                //Le agrega un JscrollPane
        areaPantalla.setEditable(false);                                        //No permite editar el area de pantalla
        areaPantalla.setFont(fuente);                                           //Agrega el estilo de fuente

        ImageIcon img = new ImageIcon(this.getClass().getResource("/imagenes/venta.jpg"));//Obtine la imgaen
        img = new ImageIcon(img.getImage().getScaledInstance(200, 500, java.awt.Image.SCALE_DEFAULT));//Escala la imagen

        ticket = new JLabel(img, SwingConstants.CENTER);                        //Ingresa la imagen a un JLabel
        ticket.setFont(fuente);                                                 //Le pone fuente
        add(ticket, BorderLayout.WEST);                                         //Lo agrega a la izquierda
    }

    public class HiloServidor extends Thread {
                                                                                //3er modulo Hilo
        @Override
        public void run() {
            try                                                                 // establece el servidor
            {
                servidor = new ServerSocket(12345, 100);                        // crea objeto ServerSocket
                while (true) {
                    try {
                        esperarConexion();                                      //Espera una conexión
                        obtenerFlujos();                                        //Flujos entrada y salida
                        procesarConexion();                                     //Procesa la conexión
                    } // fin de try
                    catch (EOFException excepcionEOF) {
                        System.out.println("Servidor termino la conexion");
                    } // fin de catch
                    finally {
                        cerrarConexion(); // cierra la conexión
                        contador++;
                    } // fin de finally
                } // fin de while
            } // fin de try
            catch (IOException exepcionES) {
                exepcionES.printStackTrace();
            } // fin de catch
        }
    }

    // establece y ejecuta el servidor
    public void ejecutarServidor() {
                                                                                //2do Modulo
        HiloServidor hiloServidor = new HiloServidor();                         //Crea un objeto de la clase HiloServidor
        hiloServidor.start();                                                   //Inicializa la ejecucion
    } 


    private void esperarConexion() throws IOException {
                                                                                //4to modulo Espera conexiones
        System.out.println("Esperando una conexion\n");                         //imprime
        conexion = servidor.accept();                                           //acepta la conexión
        System.out.println("Conexion recibida de: "+ conexion.getInetAddress().getHostName());//Verifica cliente
    }

    private void obtenerFlujos() throws IOException {
                                                                                //5to modulo Obtiene flujos
        salida = new ObjectOutputStream(conexion.getOutputStream());            //Obtiene la salida
        salida.flush();                                                         //Vacía el búfer de salida
        entrada = new ObjectInputStream(conexion.getInputStream());             //Obtiene la entrada
        System.out.println("Se obtuvieron los flujos de red de E/S\n");         //imprime
    } 

    private void procesarConexion() throws IOException {
                                                                                //6to Modulo Procesa la conexion
        String mensaje = "Conexion exitosa";                                    //imprime
        enviarDatos(mensaje);                                                   //envía mensaje de conexión exitosa  

        do                                                                      //Recibe mensajes del cliente
        {
            try {
                datos = (String) entrada.readObject();                          //Lee el nuevo mensaje del cliente
                if (!datos.startsWith("Jarabe")) {                              //Si datos comienza diferente de jarabe 
                    mostrarMensaje(datos);
                } else {
                    try {
                        System.out.println("*-*-*--*-*-*"); 
                        conexionBD.conectar();
                        conexionBD.reiniciarTodo(); //
                        conexionBD.cerrar();
                    } catch (Exception e) {
                        System.err.println("Error al conectar BD");
                    }
                    System.out.println("CONTENIDO DE datos:");
                    System.out.println(datos);
                    
                    String registro = ""; // registro individual a registrar
                    for (int i = 0; i < datos.length(); i++) {
                        if (!(datos.charAt(i) == '@')) {
                            registro = registro + datos.charAt(i);
                        } else if (i != datos.length() - 1) {
                            // registrarlo
                            String producto = "", cant = "", vend = "";
                            int coma = 0; // coma que va al recorrer
                            for (int j = 0; j < registro.length(); j++) {
                                // producto
                                if (!(registro.charAt(j) == ',') && coma == 0) {
                                    producto = producto + registro.charAt(j);
                                } else if(coma == 0){
                                    coma = 1;
                                }

                                // cantidad
                                if (!(registro.charAt(j) == ',') && coma == 1) {
                                    //cant = cant + registro.charAt(j);
                                } else if(coma == 1) {
                                    coma = 2;
                                    j++;
                                    cant = cant + registro.charAt(j);
                                    j++;
                                }

                                // cantidad
                                if (!(registro.charAt(j) == ',') && coma == 2) {
                                    vend = vend + registro.charAt(j);
                                }
                            }
                            System.out.println("PARA INSERTAR: REGISTRO, PRODUCTO, CANT, VEND");
                            System.out.println(registro);
                            System.out.println(producto);
                            System.out.println(cant);
                            System.out.println(vend);
                            System.out.println("-----*-*****-------");
                            
                            registro = "";
                            
                            int cantidad, vendidos;
                            cantidad = Integer.parseInt(cant);
                            vendidos = Integer.parseInt(vend);
                            conexionBD.insertar(producto, cantidad, vendidos);
                        }
                    }
                    System.out.println("IMPRIMIR TODO IMPORTADO");
                    conexionBD.select();
                }
                System.out.println("Datos recibidos del cliente.");
            } catch (Exception excep) {
            }

        } while (!mensaje.equalsIgnoreCase("Cliente::: TERMINAR"));
    } // fin del método procesarConexion

    private void cerrarConexion() {
                                                                                //8vo Modulo CierraConexion                                                                                //
        System.out.println("Terminando conexion\n");                            //imprime
        try {
            try {
                salida.close();                                                 // cierra flujo de salida
                entrada.close();                                                // cierra flujo de entrada
                conexion.close();                                               // cierra el socket
            } 
            catch (IOException exepcionES) {
                exepcionES.printStackTrace();
            } 
        } catch (Exception e) {
            
        }
        System.out.println("Conexión terminada");                               //imprime
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                    //Cierra el programa
    } 

    public void enviarDatos(String mensaje) {
                                                                                //7mo Modulo Envia datos al cliente
        try 
        {
            salida.writeObject("Servidor::: " + mensaje);                       //Toma el mensaje y lo envia
            salida.flush();                                                     //Envía toda la salida al cliente
            System.out.println("Servidor::: " + mensaje);                       //imprime en consola
        } 
        catch (IOException exepcionES) {
            
            try {
                cerrarConexion();                                               //Si hubo un error de igual manera cierra conexion
            } catch (Exception e) {
                
            }
        } 
    } 

    public void mostrarMensaje(final String mensajeAMostrar) {
                                                                                //9no Modulo Muestra en pantalla un mensaje
        SwingUtilities.invokeLater( new Runnable() {
            public void run()
            {
                areaPantalla.append("\n" + mensajeAMostrar);                    //Muestra el mensaje con todo lo anterior    
            } 
        } 
        );
    } 

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == btnSalir) {
            mostrarMensaje("\nCerrando...");
            System.out.println("Cerrando");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                    
            }
            System.exit(0);
            
        }
    }
}

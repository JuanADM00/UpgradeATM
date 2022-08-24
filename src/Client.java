import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {

    public static void main(String[] args) {

        try {
            Scanner sn = new Scanner(System.in);
            sn.useDelimiter("\n");

            Socket sc = new Socket("127.0.0.1", 5000);

            DataInputStream in = new DataInputStream(sc.getInputStream());
            DataOutputStream out = new DataOutputStream(sc.getOutputStream());

            // Leer mensaje del servidor
            String mensaje = in.readUTF();
            System.out.println(mensaje);

            // Escribe el nombre y lo envía al servidor
            String numerodecuenta = sn.nextLine();
            out.writeUTF(numerodecuenta);

            // Ejecución del hilo
            mensaje = in.readUTF();
            System.out.println(mensaje);
            if (!mensaje.equals("Fallamos")) {
                ClientThread hilo = new ClientThread(in, out);
                hilo.start();
                hilo.join();
                if (hilo.isInterrupted()) {
                    sn.close();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
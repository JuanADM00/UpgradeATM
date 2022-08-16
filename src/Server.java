import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    protected static ConexionDB instancia;

    public static void main(String[] args) {

        try {
            ServerSocket server = new ServerSocket(5000);
            Socket sc;

            System.out.println("Servidor iniciado");
            while (true) {
                // Espera la conexion del cliente
                sc = server.accept();

                DataInputStream in = new DataInputStream(sc.getInputStream());
                DataOutputStream out = new DataOutputStream(sc.getOutputStream());

                // Pide el número de cuenta
                out.writeUTF("Provee tu cuenta");
                String accountNumber = in.readUTF();
                instancia = ConexionDB.getInstance();
                if (instancia.login(accountNumber)) {
                    out.writeUTF("Creada la conexion con la cuenta " + accountNumber);
                    // Inicio el hilo
                    ServerThread hilo = new ServerThread(sc, in, out, accountNumber);
                    hilo.start();
                }else{
                    out.writeUTF("Fallamos");
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

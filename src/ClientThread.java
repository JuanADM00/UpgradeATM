import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientThread extends Thread {

    private DataInputStream in;
    private DataOutputStream out;

    public ClientThread(DataInputStream in, DataOutputStream out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {

        Scanner sn = new Scanner(System.in);

        String mensaje;
        int opcion = 0;
        boolean salir = false;

        while (!salir) {

            try {
                System.out.println("1) Abono.\n2) Retiro.\n3) Consulta de saldo.\n4) Transferencia.\n0) Salir.");

                opcion = Integer.parseInt(sn.nextLine());
                out.writeInt(opcion);

                switch (opcion) {
                    case 1:
                        mensaje = in.readUTF();
                        System.out.println(mensaje);
                        out.writeInt(Integer.parseInt(sn.nextLine()));
                        mensaje = in.readUTF();
                        System.out.println(mensaje);
                        out.writeUTF(sn.nextLine());
                        mensaje = in.readUTF();
                        System.out.println(mensaje);
                        break;
                    case 2:
                        mensaje = in.readUTF();
                        System.out.println(mensaje);
                        out.writeInt(Integer.parseInt(sn.nextLine()));
                        mensaje = in.readUTF();
                        System.out.println(mensaje);
                        out.writeUTF(sn.nextLine());
                        mensaje = in.readUTF();
                        System.out.println(mensaje);
                        break;
                    case 3:
                        mensaje = in.readUTF();
                        System.out.println(mensaje);
                        out.writeUTF(sn.nextLine());
                        mensaje = in.readUTF();
                        System.out.println(mensaje);
                        break;
                    case 4:
                        mensaje = in.readUTF();
                        System.out.println(mensaje);
                        out.writeUTF(sn.nextLine());
                        mensaje = in.readUTF();
                        System.out.println(mensaje);
                        out.writeInt(Integer.parseInt(sn.nextLine()));
                        mensaje = in.readUTF();
                        System.out.println(mensaje);
                        out.writeUTF(sn.nextLine());
                        mensaje = in.readUTF();
                        System.out.println(mensaje);
                        break;
                    case 0:
                        this.interrupt();
                        sn.close();
                        salir = true;
                        break;
                    default:
                        mensaje = in.readUTF();
                        System.out.println(mensaje);

                }
            } catch (IOException ex) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NumberFormatException ex) {
                System.out.println("Formato inválido. Inténtalo de nuevo.");
            }

        }

    }
}

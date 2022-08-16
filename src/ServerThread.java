import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerThread extends Thread {

    protected static ConexionDB instancia;
    private Socket sc;
    private DataInputStream in;
    private DataOutputStream out;
    private String nombreCliente;

    public ServerThread(Socket sc, DataInputStream in, DataOutputStream out, String nombreCliente) {
        this.sc = sc;
        this.in = in;
        this.out = out;
        this.nombreCliente = nombreCliente;
    }

    @Override
    public void run() {

        int opcion, monto;
        String contraseña;
        instancia = ConexionDB.getInstance();
        boolean salir = false;
        while (!salir) {

            try {
                opcion = in.readInt();
                switch (opcion) {
                    case 1:
                        out.writeUTF("Ingresa cantidad a abonar");
                        monto = in.readInt();
                        out.writeUTF("CONTRASEÑA:");
                        contraseña = in.readUTF();
                        if (monto > -1
                                && instancia.payment(instancia.getCuenta().getAccountNumber(), contraseña, monto)) {
                            out.writeUTF("Abono exitoso");
                        } else {
                            out.writeUTF("Fallo en la transacción. Intentar nuevamente.");
                        }
                        break;

                    case 2:
                        out.writeUTF("Ingresa cantidad a retirar");
                        monto = in.readInt();
                        out.writeUTF("CONTRASEÑA:");
                        contraseña = in.readUTF();
                        if (monto > -1
                                && instancia.withdrawal(instancia.getCuenta().getAccountNumber(), contraseña, monto)) {
                            out.writeUTF("Retiro exitoso");
                        } else {
                            out.writeUTF("Fallo en la transacción. Intentar nuevamente.");
                        }
                        break;

                    case 3:
                        out.writeUTF("CONTRASEÑA:");
                        contraseña = in.readUTF();
                        int saldo = instancia.getSaldo(instancia.getCuenta().getAccountNumber(), contraseña);
                        if (saldo > -1) {
                            out.writeUTF("Saldo actual: $" + saldo);
                        } else {
                            out.writeUTF("Fallo en la consulta. Intentar nuevamente.");
                        }
                        break;

                    case 4:
                        out.writeUTF("Ingresa cuenta destinataria");
                        String receiver = in.readUTF();
                        out.writeUTF("Ingresa cantidad a transferir");
                        monto = in.readInt();
                        out.writeUTF("CONTRASEÑA:");
                        contraseña = in.readUTF();
                        if (monto > -1
                                && instancia.transaction(instancia.getCuenta().getAccountNumber(), contraseña, receiver,
                                        monto)) {
                            out.writeUTF("Transferencia exitoso");
                        } else {
                            out.writeUTF("Fallo en la transacción. Intentar nuevamente.");
                        }
                        break;
                    case 0:
                        salir = true;
                        break;
                    default:
                        out.writeUTF("Sólo del 0 al 4");
                }

            } catch (IOException ex) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        try {
            // Cierro el socket
            sc.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Conexion cerrada con el cliente " + nombreCliente);
    }
}
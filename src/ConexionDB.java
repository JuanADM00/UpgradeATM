import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionDB {
    protected Connection conn;
    protected Statement sentencia;
    protected ResultSet resultSet;
    protected static final String driver = "com.mysql.cj.jdbc.Driver", user = "root", password = "Jpad18UPB*", url = "jdbc:mysql://localhost:3306/BANCOAMAYA";
    protected Account cuenta;
    private static ConexionDB instancia;
    
    private ConexionDB() {
        conn = null;
        try {
            cuenta = new Account("", "");
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("CONEXION ESTABLECIDA");
            }
            conn.setAutoCommit(false);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Connection getConn() {
        return conn;
    }
    
    public Statement getSentencia() {
        return sentencia;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public Account getCuenta() {
        return cuenta;
    }

    private void commit() {

        try {
            conn.commit();
        } catch (SQLException ex) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*public void rollback() {

        try {
            conn.rollback();
        } catch (SQLException ex) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/

    private void cerrarResult() {
        try {
            resultSet.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void cerrarSentencia() {
        try {
            sentencia.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cerrarConexion() {
        try {
            commit();
            if (resultSet != null) {
                cerrarResult();
            }
            if (sentencia != null) {
                cerrarSentencia();
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void ejecutarConsulta(String consulta) {
        try {
            sentencia = conn.createStatement();
            resultSet = sentencia.executeQuery(consulta);
        } catch (SQLException ex) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private boolean existeValor(String valor, String columna, String tabla) {

        boolean existe = false;
        Statement sentenciaAux;

        try {
            sentenciaAux = conn.createStatement();
            ResultSet aux = sentenciaAux.executeQuery("SELECT COUNT(*) FROM " + tabla + " WHERE UPPER(" + columna + ") ='" + valor.toUpperCase() + "'");
            aux.next();
            if (aux.getInt(1) >= 1) {
                existe = true;
            }
            aux.close();
            sentenciaAux.close();

        } catch (SQLException ex) {
            Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return existe;
    }

    private boolean isCountable(String cadena) {
        for (char caracter : cadena.toCharArray()) {
            if (caracter + 0 < 48 || caracter + 0 > 57) {
                return false;
            }
        }

        return true;
    }

    public int getSaldo(String account, String password) {
        if (check(password)) {
            String query = "SELECT SALDO FROM ACCOUNTS WHERE NUMBER = '" + account + "';";
            try {
                ejecutarConsulta(query);
                if (resultSet.next()) {
                    ResultSet rs = getResultSet();
                    return rs.getInt("SALDO");
                }
            } catch (Exception e) {
                return -1;
            }
        }
        return -1;
    }

    public boolean payment(String account, String password, int value) {
        if (check(password)) {
            int res = value + getSaldo(account, password);
            try {
                String query = "UPDATE ACCOUNTS SET SALDO = " + String.valueOf(res) + " WHERE NUMBER = '" + account + "';";
                PreparedStatement s = this.getConn().prepareStatement(query);
                s.executeUpdate();
                commit();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public boolean transaction(String account, String password, String receiver, int value) {
        if (check(password) == true && existeValor(receiver, "NUMBER", "ACCOUNTS") && withdrawal(account, password, value) == true && payment(receiver, password, value) == true) {
            commit();
            return true;
        }
        return false;
    }

    public boolean withdrawal(String account, String password, int value) {
        if (check(password)) {
            int res = getSaldo(account, password) - value;
            if (res < 0) {
                return false;
            } else {
                try {
                    String query = "UPDATE ACCOUNTS SET SALDO = " + String.valueOf(res) + " WHERE NUMBER = '" + account + "';";
                    PreparedStatement s = this.getConn().prepareStatement(query);
                    s.executeUpdate();
                    commit();
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return false;
    }

    public boolean login(String account) {
        if (isCountable(account) && existeValor(account, "NUMBER", "ACCOUNTS")) {
            String query = "SELECT NUMBER, PASSWORD FROM ACCOUNTS WHERE NUMBER ='" + account + "';";
            try {
                ejecutarConsulta(query);
                if (resultSet.next()) {
                    ResultSet rs = getResultSet();
                    cuenta.setAccountNumber(rs.getString("NUMBER"));
                    cuenta.setAccountPassword(rs.getString("PASSWORD"));
                }
                return true;

            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    private boolean check(String password) {
        if (cuenta.getAccountPassword().equals(password) == true) {
            return true;
        }
        return false;
    }

    public static ConexionDB getInstance(){
        if(instancia == null){
            instancia = new ConexionDB();
        }
        return instancia;
    }
}

import java.sql.SQLException;

public interface IController {
    public int getSaldo(String account, String password) throws SQLException;
    public boolean payment(String account, String password, int value);
    public boolean transaction(String account, String password, String receiver, int value);
    public boolean withdrawal(String account, String password, int value);
    public boolean login(String account);
}

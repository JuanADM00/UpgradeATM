public class Account {
    private String accountPassword, accountNumber;

    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Account(String accountNumber, String accountPassword) {
        this.accountNumber = accountNumber;
        this.accountPassword = accountPassword;
    }

    
}

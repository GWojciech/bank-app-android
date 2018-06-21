package pl.kielce.tu.pai2.mobile.bank.bankapplication.model;

public class Transaction {

    private String date;
    private String recipient;
    private String sender;
    private String title;
    private String amount;
    private String balance;

    public Transaction(String date, String recipient, String sender, String title, String amount, String balance) {
        this.date = date;
        this.recipient = recipient;
        this.sender = sender;
        this.title = title;
        this.amount = amount;
        this.balance = balance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return date + " " + recipient + " " + sender + " " + title + " " + amount + " " + balance;
    }
}

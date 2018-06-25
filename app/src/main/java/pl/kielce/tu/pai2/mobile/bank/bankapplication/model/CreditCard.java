package pl.kielce.tu.pai2.mobile.bank.bankapplication.model;

public class CreditCard {
    private Integer idCreditCard;
    private String name;
    private String creditCardNumber;
    private String pinCode;
    private String state;
    private String expirationDate;
    private String type;
    private Double dayLimit;
    private Double monthLimit;

    public CreditCard(Integer idCreditCard, String name, String creditCardNumber, String pinCode, String state, String expirationDate, String type, Double dayLimit, Double monthLimit) {
        this.idCreditCard = idCreditCard;
        this.name = name;
        this.creditCardNumber = creditCardNumber;
        this.pinCode = pinCode;
        this.state = state;
        this.expirationDate = expirationDate;
        this.type = type;
        this.dayLimit = dayLimit;
        this.monthLimit = monthLimit;
    }

    public Integer getIdCreditCard() {
        return idCreditCard;
    }

    public void setIdCreditCard(Integer idCreditCard) {
        this.idCreditCard = idCreditCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getDayLimit() {
        return dayLimit;
    }

    public void setDayLimit(Double dayLimit) {
        this.dayLimit = dayLimit;
    }

    public Double getMonthLimit() {
        return monthLimit;
    }

    public void setMonthLimit(Double monthLimit) {
        this.monthLimit = monthLimit;
    }
}

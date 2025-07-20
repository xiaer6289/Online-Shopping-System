import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Customer {
    private static final String CUSTOMER_FILE = "database/customers.txt";

    private String cusID;
    private String cusName;
    private String cusEmail;
    private String cusPhoneNumber;
    private String cusAddress;
    private String cusPassword;

    public Customer(String cusName, String cusEmail, String cusPhoneNumber, String cusAddress, String cusPassword) {
        this.cusID = TestCustomer.generateCustomerId();
        this.cusName = cusName;
        this.cusEmail = cusEmail;
        this.cusPhoneNumber = cusPhoneNumber;
        this.cusAddress = cusAddress;
        this.cusPassword = cusPassword;
    }

    public Customer() {
        try (BufferedReader br = new BufferedReader(new FileReader(CUSTOMER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("250327001")) { // Replace with logic to match the correct customer ID
                    String[] data = line.split("\\|");
                    this.cusID = data[1].trim();
                    this.cusName = data[2].trim();
                    this.cusEmail = data[3].trim();
                    this.cusPhoneNumber = data[4].trim();
                    this.cusAddress = data[5].trim();
                    this.cusPassword = data[6].trim();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getter
    public String getCusID() {
        return cusID;
    }

    public String getCusName() {
        return cusName;
    }

    public String getCusEmail() {
        return cusEmail;
    }

    public String getCusPhoneNumber() {
        return cusPhoneNumber;
    }

    public String getCusAddress() {
        return cusAddress;
    }

    public String getCusPassword() {
        return cusPassword;
    }

    // Setter
    public void setCusID(String cusID) {
        this.cusID = cusID;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public void setCusEmail(String cusEmail) {
        this.cusEmail = cusEmail;
    }

    public void setCusPhoneNumber(String cusPhoneNumber) {
        this.cusPhoneNumber = cusPhoneNumber;
    }

    public void setCusAddress(String cusAddress) {
        this.cusAddress = cusAddress;
    }

    public void setCusPassword(String cusPassword) {
        this.cusPassword = cusPassword;
    }

    

    @Override
    public String toString() {
        return "Customer{" +
                "customerId='" + cusID + '\n' +
                ", name='" + cusName + '\n' +
                ", email='" + cusEmail + '\n' +
                ", phoneNumber='" + cusPhoneNumber + '\n' +
                ", address='" + cusAddress + '\n' +
                ", Password='" + cusPassword + '\n' +
                '}';
    }
}

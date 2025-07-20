
public class Admin {
    private int adminId;
    private String adminPassword;
    private String adminName;
    private String adminEmail;
    private String adminPhone;

//Constructor
    public Admin() {
    }

    public Admin(int adminId, String adminPassword, String adminName,String adminEmail,String adminPhone) {
        this.adminId = adminId;
        this.adminPassword = adminPassword.trim();
        this.adminName = adminName.trim();
        this.adminEmail = adminEmail.trim();
        this.adminPhone = adminPhone.trim();
        
    }

   
//Getter
    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getAdminPassword() {
        return adminPassword;
    }


    public String getAdminName() {
        return adminName;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public String getAdminPhone() {
        return adminPhone;
    }
//Setter
    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    } 

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public void setAdminphone(String adminPhone) {
        this.adminPhone = adminPhone;
    }

    @Override
    public String toString() {
        return "Admin Information:\n" +
               "ID: " + adminId + "\n" +
               "Name: " + adminName + "\n" +
               "Password: " + "*".repeat(adminPassword.length()) + "\n" +
               "Email: "+adminEmail+
               "Phone Number: " + adminPhone;
    }
}

public class Login {

    private String username;
    private String password;
    private String cellPhoneNumber;
    private String firstName;
    private String lastName;

    private String loginUsername;
    private String loginPassword;

    public Login(String username, String password, String cellPhoneNumber,
                 String firstName, String lastName) {

        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Checks whether the username contains an underscore
    // and is no more than 5 characters long.
    public boolean checkUserName() {
        return username.contains("_") && username.length() <= 5;
    }

    // Checks password complexity.
    public boolean checkPasswordComplexity() {

        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecialCharacter = false;

        if (password.length() < 8) {
            return false;
        }

        for (int i = 0; i < password.length(); i++) {

            char character = password.charAt(i);

            if (Character.isUpperCase(character)) {
                hasCapital = true;
            }

            if (Character.isDigit(character)) {
                hasNumber = true;
            }

            if (!Character.isLetterOrDigit(character)) {
                hasSpecialCharacter = true;
            }
        }

        return hasCapital && hasNumber && hasSpecialCharacter;
    }

    /**
 * Checks the South African cellphone number using a regular expression.
 *
 * Regex research/reference:
 * Oracle (2026) Pattern - Java Regular Expression Documentation.
 * Available at:
 * https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/regex/Pattern.html
 * Accessed: 13 September 2026.
 */
    // The number starts with +27 followed by 9 digits.
    public boolean checkCellPhoneNumber() {
        return cellPhoneNumber.matches("^\\+27\\d{9}$");
    }

    // Returns registration messages.
    public String registerUser() {

        if (!checkUserName()) {
            return "Username is not correctly formatted; please ensure that "
                    + "your username contains an underscore and is no more "
                    + "than five characters in length.";
        }

        if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted; please ensure that "
                    + "the password contains at least eight characters, "
                    + "a capital letter, a number, and a special character.";
        }

        if (!checkCellPhoneNumber()) {
            return "Cell phone number incorrectly formatted or does not "
                    + "contain international code.";
        }

        return "User registered successfully.";
    }

    // Stores the details entered during login.
    public void setLoginDetails(String loginUsername, String loginPassword) {
        this.loginUsername = loginUsername;
        this.loginPassword = loginPassword;
    }

    // Checks whether login details match registration details.
    public boolean loginUser() {
        return username.equals(loginUsername)
                && password.equals(loginPassword);
    }

    // Returns the required login status message.
    public String returnLoginStatus() {

        if (loginUser()) {
            return "Welcome " + firstName + ", " + lastName
                    + " it is great to see you again.";
        }

        return "Username or password incorrect, please try again.";
    }
}
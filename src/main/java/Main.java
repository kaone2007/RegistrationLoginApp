import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.println("===== USER REGISTRATION =====");

        System.out.print("Enter your first name: ");
        String firstName = input.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = input.nextLine();

        System.out.print("Enter a username: ");
        String username = input.nextLine();

        System.out.print("Enter a password: ");
        String password = input.nextLine();

        System.out.print("Enter your South African cell phone number (+27): ");
        String cellPhoneNumber = input.nextLine();

        Login user = new Login(
                username,
                password,
                cellPhoneNumber,
                firstName,
                lastName
        );

        // Username feedback
        if (user.checkUserName()) {
            System.out.println("Username successfully captured.");
        } else {
            System.out.println(
                    "Username is not correctly formatted; please ensure that "
                    + "your username contains an underscore and is no more "
                    + "than five characters in length."
            );
        }

        // Password feedback
        if (user.checkPasswordComplexity()) {
            System.out.println("Password successfully captured.");
        } else {
            System.out.println(
                    "Password is not correctly formatted; please ensure that "
                    + "the password contains at least eight characters, "
                    + "a capital letter, a number, and a special character."
            );
        }

        // Cell phone feedback
        if (user.checkCellPhoneNumber()) {
            System.out.println("Cell phone number successfully added.");
        } else {
            System.out.println(
                    "Cell phone number incorrectly formatted or does not "
                    + "contain international code."
            );
        }

        System.out.println();
        System.out.println(user.registerUser());

        // Only continue to login if registration details are valid.
        if (user.checkUserName()
                && user.checkPasswordComplexity()
                && user.checkCellPhoneNumber()) {

            System.out.println();
            System.out.println("===== LOGIN =====");

            System.out.print("Enter your username: ");
            String loginUsername = input.nextLine();

            System.out.print("Enter your password: ");
            String loginPassword = input.nextLine();

            user.setLoginDetails(loginUsername, loginPassword);

            System.out.println(user.returnLoginStatus());
        }

        input.close();
    }
}
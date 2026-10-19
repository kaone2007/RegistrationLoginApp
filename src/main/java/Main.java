import quickchat.Message;
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
                username, password, cellPhoneNumber,
                firstName, lastName
        );

        System.out.println();

        if (user.checkUserName()) {
            System.out.println("Username successfully captured.");
        } else {
            System.out.println(
                    "Username is not correctly formatted; please ensure that "
                    + "your username contains an underscore and is no more "
                    + "than five characters in length."
            );
        }

        if (user.checkPasswordComplexity()) {
            System.out.println("Password successfully captured.");
        } else {
            System.out.println(
                    "Password is not correctly formatted; please ensure that "
                    + "the password contains at least eight characters, "
                    + "a capital letter, a number, and a special character."
            );
        }

        if (user.checkCellPhoneNumber()) {
            System.out.println("Cell phone number successfully added.");
        } else {
            System.out.println(
                    "Cell phone number incorrectly formatted or does not "
                    + "contain international code."
            );
        }

        System.out.println(user.registerUser());

        if (user.checkUserName()
                && user.checkPasswordComplexity()
                && user.checkCellPhoneNumber()) {

            System.out.println("\n===== LOGIN =====");

            System.out.print("Enter your username: ");
            String loginUsername = input.nextLine();

            System.out.print("Enter your password: ");
            String loginPassword = input.nextLine();

            user.setLoginDetails(loginUsername, loginPassword);

            System.out.println(user.returnLoginStatus());

            if (user.loginUser()) {
                startQuickChat(input);
            }

        } else {
            System.out.println(
                    "Registration failed. Please correct your details."
            );
        }

        input.close();
    }

    public static void startQuickChat(Scanner input) {

        System.out.println("\nWelcome to QuickChat.");

        int maximumMessages = 0;

        while (maximumMessages <= 0) {
            System.out.print(
                    "How many messages would you like to enter? "
            );

            try {
                maximumMessages = Integer.parseInt(input.nextLine());

                if (maximumMessages <= 0) {
                    System.out.println(
                            "Please enter a number greater than 0."
                    );
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }

        int messagesProcessed = 0;
        boolean running = true;

        while (running) {

            System.out.println("\n===== QUICKCHAT =====");
            System.out.println("1. Send Messages");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Quit");
            System.out.print("Select an option: ");

            String option = input.nextLine();

            switch (option) {

                case "1":

                    if (messagesProcessed >= maximumMessages) {
                        System.out.println(
                                "You have reached the set number of messages."
                        );
                        break;
                    }

                    System.out.print("Enter recipient cell number: ");
                    String recipient = input.nextLine();

                    System.out.print("Enter your message: ");
                    String messageText = input.nextLine();

                    Message newMessage = new Message(
                            recipient,
                            messageText,
                            messagesProcessed
                    );

                    System.out.println(
                            "Message ID generated: "
                            + newMessage.getMessageID()
                    );

                    String recipientResult =
                            newMessage.checkRecipientCell();

                    System.out.println(recipientResult);

                    if (!recipientResult.equals(
                            "Cell phone number successfully captured.")) {
                        break;
                    }

                    String lengthResult =
                            newMessage.checkMessageLength();

                    if (messageText.length() > 250) {
                        System.out.println(
                                "Please enter a message of less than 250 characters."
                        );
                        System.out.println(lengthResult);
                        break;
                    }

                    System.out.println(lengthResult);

                    System.out.println(
                            "Message Hash: "
                            + newMessage.getMessageHash()
                    );

                    String result = newMessage.SentMessage();
                    System.out.println(result);

                    if (result.equals("Message successfully sent.")) {
                        System.out.println("\n===== MESSAGE DETAILS =====");
                        System.out.println(newMessage.getMessageDetails());
                    }

                    messagesProcessed++;
                    break;

                case "2":

                    System.out.println("Coming Soon.");
                    break;

                case "3":

                    running = false;

                    System.out.println("\nTotal number of messages sent: "
                            + Message.returnTotalMessages());

                    System.out.println("Thank you for using QuickChat.");
                    break;

                default:

                    System.out.println(
                            "Invalid option. Please select 1, 2 or 3."
                    );
                    break;
            }
        }
    }
}

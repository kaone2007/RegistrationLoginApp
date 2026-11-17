
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

        System.out.print(
                "Enter your South African cell phone number (+27): ");
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
                startQuickChat(input, cellPhoneNumber);
            }

        } else {
            System.out.println(
                    "Registration failed. Please correct your details."
            );
        }

        input.close();
    }

    // Keep the original method available
    public static void startQuickChat(Scanner input) {
        startQuickChat(input, "Unknown");
    }

    public static void startQuickChat(
            Scanner input, String sender) {

        System.out.println("\nWelcome to QuickChat.");

        // Load previously stored messages from JSON
        Message.loadStoredMessages();

        int maximumMessages = 0;

        while (maximumMessages <= 0) {

            System.out.print(
                    "How many messages would you like to enter? "
            );

            try {
                maximumMessages =
                        Integer.parseInt(input.nextLine());

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
            System.out.println("4. Stored Messages");
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

                    System.out.print(
                            "Enter recipient cell number: ");
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

                    System.out.println(lengthResult);

                    if (!lengthResult.equals(
                            "Message ready to send.")) {
                        break;
                    }

                    System.out.println(
                            "Message Hash: "
                            + newMessage.getMessageHash()
                    );

                    String result = newMessage.SentMessage();
                    System.out.println(result);

                    if (result.equals(
                            "Message successfully sent.")) {

                        System.out.println(
                                "\n===== MESSAGE DETAILS =====");
                        System.out.println(
                                newMessage.getMessageDetails());
                    }

                    if (!result.equals("No option selected.")) {
                        messagesProcessed++;
                    }

                    break;

                case "2":

                    // Retained from Part 2
                    System.out.println("Coming Soon.");
                    break;

                case "3":

                    running = false;

                    System.out.println(
                            "\nTotal number of messages sent: "
                            + Message.returnTotalMessages());

                    System.out.println(
                            "Thank you for using QuickChat.");
                    break;

                case "4":

                    storedMessagesMenu(input, sender);
                    break;

                default:

                    System.out.println(
                            "Invalid option. Please select 1, 2, 3 or 4."
                    );
                    break;
            }
        }
    }

    // Part 3: Stored Messages menu
    public static void storedMessagesMenu(
            Scanner input, String sender) {

        boolean back = false;

        while (!back) {

            System.out.println(
                    "\n===== STORED MESSAGES MENU =====");
            System.out.println(
                    "1. Display sender and recipient");
            System.out.println(
                    "2. Display longest stored message");
            System.out.println(
                    "3. Search message by Message ID");
            System.out.println(
                    "4. Search messages by recipient");
            System.out.println(
                    "5. Delete message using Message Hash");
            System.out.println(
                    "6. Display full message report");
            System.out.println(
                    "7. Return to main menu");
            System.out.print("Select an option: ");

            String choice = input.nextLine();

            switch (choice) {

                case "1":

                    System.out.println(
                            "\n===== SENDER AND RECIPIENT =====");

                    System.out.println(
                            Message.displaySenderAndRecipient(sender));
                    break;

                case "2":

                    System.out.println(
                            "\n===== LONGEST STORED MESSAGE =====");

                    System.out.println(
                            Message.displayLongestMessage());
                    break;

                case "3":

                    System.out.print(
                            "Enter the Message ID to search: ");

                    String id = input.nextLine().trim();

                    System.out.println(
                            Message.searchMessageID(id));
                    break;

                case "4":

                    System.out.print(
                            "Enter recipient cell number: ");

                    String recipient = input.nextLine().trim();

                    System.out.println(
                            Message.searchRecipient(recipient));
                    break;

                case "5":

                    System.out.print(
                            "Enter Message Hash to delete: ");

                    String hash = input.nextLine().trim();

                    System.out.println(
                            Message.deleteMessageByHash(hash));
                    break;

                case "6":

                    System.out.println(
                            "\n===== QUICKCHAT MESSAGE REPORT =====");

                    System.out.println(
                            Message.displayReport());
                    break;

                case "7":

                    back = true;
                    System.out.println(
                            "Returning to QuickChat main menu.");
                    break;

                default:

                    System.out.println(
                            "Invalid option. Please select 1 to 7.");
                    break;
            }
        }
    }
}

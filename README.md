# RegistrationLoginApp – QuickChat Part 3

## Project Description

QuickChat is a Java-based registration, login, and messaging application developed using Apache NetBeans IDE.

The project demonstrates Java programming concepts, object-oriented programming, user authentication, message management, JSON file storage, and automated unit testing using JUnit.

QuickChat was developed in three parts, with each part introducing additional functionality.

## Part 1: User Registration and Login

The application allows users to:

- Register a username, password, and South African cellphone number.
- Validate username requirements.
- Validate password complexity.
- Validate cellphone numbers using the international +27 format.
- Log in using registered credentials.
- Receive appropriate success or error messages.

## Part 2: Sending Messages

After successfully logging in, users can access QuickChat.

The application allows users to:

- Enter the number of messages they want to create.
- Enter a recipient cellphone number and message text.
- Validate recipient cellphone numbers.
- Validate messages against the 250-character limit.
- Generate a unique 10-digit message ID.
- Generate a message hash automatically.
- Choose to send, disregard, or store a message.
- Save stored messages in a JSON file.
- Display the total number of sent messages.

## Part 3: Message Storage and Reporting

Part 3 extends QuickChat with additional message-management functionality.

### Message Arrays

The application maintains collections for:

- Sent messages
- Disregarded messages
- Stored messages
- Message hashes
- Message IDs

Stored messages can be loaded from the `storedMessages.json` file.

### Stored Messages Menu

The Stored Messages menu provides the following features:

1. **Display Sender and Recipient:** Displays the sender and recipient cellphone numbers for stored messages.
2. **Display Longest Message:** Finds and displays the longest stored message.
3. **Search by Message ID:** Searches for a message using its unique ID and displays the recipient and message content.
4. **Search by Recipient:** Displays messages associated with a specified recipient.
5. **Delete Message by Hash:** Finds and deletes a stored message using its message hash.
6. **Display Full Message Report:** Displays stored message details, including message ID, message hash, recipient, and message content.
7. **Return to Main Menu:** Returns to the QuickChat main menu.

## Technologies Used

- Java
- Apache NetBeans IDE
- Apache Maven
- JUnit 5
- JSON file storage
- Git and GitHub
- GitHub Actions

## Project Structure

```text
RegistrationLoginApp/
├── .github/
│   └── workflows/
│       └── main.yml
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── Login.java
│   │       ├── Main.java
│   │       └── quickchat/
│   │           └── Message.java
│   └── test/
│       └── java/
│           ├── LoginTest.java
│           └── quickchat/
│               └── MessageTest.java
├── pom.xml
├── README.md
└── storedMessages.json
```

## Unit Testing

The application uses JUnit 5 for automated testing.

### Part 1 Tests

The `LoginTest.java` class tests registration and login validation functionality.

### Part 2 Tests

The `MessageTest.java` class includes tests for:

- Valid message length
- Invalid message length
- Valid recipient cellphone number
- Invalid recipient cellphone number
- Message ID generation
- Message hash generation

### Part 3 Tests

Additional JUnit tests verify:

- Sent messages collection
- Longest stored message
- Searching by message ID
- Searching by recipient
- Successful deletion using a message hash
- Full message report

The deletion test uses a temporary JSON file to protect the application's normal stored messages.

## How to Run the Application

1. Download or clone the GitHub repository.
2. Open Apache NetBeans IDE.
3. Select **File → Open Project**.
4. Open the `RegistrationLoginApp` project.
5. Allow Maven to load the project dependencies.
6. Open `Main.java` under Source Packages.
7. Run `Main.java`.
8. Follow the instructions to register, log in, and access QuickChat.

## How to Run Unit Tests

1. Open the project in NetBeans.
2. Right-click `RegistrationLoginApp`.
3. Select **Test**.
4. Wait for Maven and JUnit to complete.
5. Check the Output window for the test results.

Alternatively, run the following command from the project directory:

```bash
mvn test
```

## GitHub Actions

The repository includes a GitHub Actions workflow for automated testing.

The workflow helps verify that the project builds and that the automated tests pass.

## Conclusion

QuickChat Part 3 demonstrates the integration of Java programming, message management, JSON storage, automated testing, and version control.

The project combines the registration and login features from Part 1, the messaging features from Part 2, and the message storage and reporting functionality introduced in Part 3.

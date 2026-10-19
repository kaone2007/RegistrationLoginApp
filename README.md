# RegistrationLoginApp – QuickChat Part 2

## Project Description

This project is a Java-based registration, login, and messaging application developed using Apache NetBeans.

The application allows users to register, log in, and access QuickChat to create and manage messages.

## Features

### Part 1: Registration and Login
- Username validation
- Password validation
- South African cellphone number validation
- User registration
- User login
- Login status messages

### Part 2: QuickChat Messaging
- QuickChat welcome message and menu
- Unique 10-digit message ID generation
- Recipient cellphone number validation using international format
- Message length validation (maximum 250 characters)
- Message hash generation
- Send Message option
- Disregard Message option
- Store Message option
- JSON message storage
- Tracking of sent messages

## Technologies Used

- Java
- Apache NetBeans
- Maven
- JUnit 5
- Git
- GitHub
- GitHub Actions
- JSON

## Project Structure

- `Login.java` – Contains registration and login validation methods.
- `Main.java` – Runs the application and handles user input and the QuickChat menu.
- `LoginTest.java` – Contains JUnit tests for registration and login.
- `Message.java` – Handles message creation, validation, message IDs, hashes, and storage.
- `MessageTest.java` – Contains JUnit tests for messaging functionality.
- `storedMessages.json` – Stores messages in JSON format.
- `pom.xml` – Contains Maven project configuration and dependencies.

## How the Application Works

1. The user registers by entering their personal details.
2. The application validates the registration information.
3. The user logs in using the registered username and password.
4. The QuickChat menu appears after successful login.
5. The user selects the option to create a message.
6. The application validates the recipient and message length.
7. A unique message ID and message hash are generated.
8. The user chooses to send, disregard, or store the message.
9. Stored messages are saved in `storedMessages.json`.
10. The user can exit the application using the Quit option.

## Testing

JUnit 5 was used to test the application's functionality.

The tests cover:
- Username validation
- Password validation
- Cellphone number validation
- Login functionality
- Message length validation
- Recipient validation
- Message ID generation
- Message hash generation

**Test Results:**
- LoginTest.java: 13 tests passed
- MessageTest.java: 6 tests passed
- Total: 19 tests passed

The project completed Clean and Build successfully in Apache NetBeans.

## GitHub Actions

GitHub Actions is configured to run automated Maven build checks.

The latest workflow completed successfully with a green tick.

## Conclusion

QuickChat Part 2 demonstrates Java programming concepts such as classes, objects, methods, loops, conditional statements, input validation, file handling, and unit testing.

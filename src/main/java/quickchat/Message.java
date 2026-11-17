
package quickchat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Message {

    private final String messageID;
    private final String recipient;
    private final String message;
    private final int messageNumber;
    private final String messageHash;

    private static int totalMessages = 0;

    // Part 3: Required arrays
    private static final ArrayList<Message> sentMessages =
            new ArrayList<>();

    private static final ArrayList<Message> disregardedMessages =
            new ArrayList<>();

    private static final ArrayList<Message> storedMessages =
            new ArrayList<>();

    private static final ArrayList<String> messageHashes =
            new ArrayList<>();

    private static final ArrayList<String> messageIDs =
            new ArrayList<>();

    private static final HashSet<String> usedMessageIDs =
            new HashSet<>();

    private static final Random random = new Random();

    private static Path JSON_FILE =
            Paths.get("storedMessages.json");

    // Constructor used by Part 2
    public Message(String recipient, String message,
                   int messageNumber) {

        this(generateMessageID(), recipient, message,
                messageNumber, null);
    }

    // Constructor for messages loaded from JSON
    private Message(String id, String recipient,
                    String message, int messageNumber,
                    String savedHash) {

        this.messageID = id;
        this.recipient = recipient;
        this.message = message;
        this.messageNumber = messageNumber;

        this.messageHash = savedHash == null
                ? createMessageHash() : savedHash;

        usedMessageIDs.add(id);
        messageIDs.add(id);
        messageHashes.add(this.messageHash);
    }

    private static synchronized String generateMessageID() {

        String id;

        do {
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < 10; i++) {
                builder.append(random.nextInt(10));
            }

            id = builder.toString();

        } while (usedMessageIDs.contains(id));

        usedMessageIDs.add(id);
        return id;
    }

    public boolean checkMessageID() {
        return messageID.matches("\\d{10}");
    }

    public String checkRecipientCell() {

        if (recipient != null
                && recipient.matches("\\+27\\d{9}")) {

            return "Cell phone number successfully captured.";
        }

        return "Cell phone number is incorrectly formatted "
                + "or does not contain an international code. "
                + "Please correct the number and try again.";
    }

    public String checkMessageLength() {

        if (message == null) {
            return "Please enter a message.";
        }

        if (message.length() <= 250) {
            return "Message ready to send.";
        }

        int extraCharacters = message.length() - 250;

        return "Message exceeds 250 characters by "
                + extraCharacters
                + ", please reduce the size.";
    }

    public String createMessageHash() {

        String firstTwoDigits = messageID.substring(0, 2);

        String cleanedMessage =
                message == null ? "" : message.trim();

        if (cleanedMessage.isEmpty()) {
            return (firstTwoDigits + ":"
                    + messageNumber + ":").toUpperCase();
        }

        String[] words = cleanedMessage.split("\\s+");

        return (firstTwoDigits + ":"
                + messageNumber + ":"
                + words[0]
                + words[words.length - 1]).toUpperCase();
    }

    // Part 2: Message options
    public String SentMessage() {

        String[] options = {
            "Send Message",
            "Disregard Message",
            "Store Message"
        };

        JFrame popupFrame = new JFrame();

        popupFrame.setAlwaysOnTop(true);
        popupFrame.setLocationRelativeTo(null);

        int choice;

        try {
            choice = JOptionPane.showOptionDialog(
                    popupFrame,
                    "Choose what you want to do:",
                    "QuickChat - Message Options",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );
        } finally {
            popupFrame.dispose();
        }

        if (choice == 0) {

            sentMessages.add(this);
            totalMessages++;

            return "Message successfully sent.";
        }

        if (choice == 1) {

            disregardedMessages.add(this);

            return "Press 0 to delete the message.";
        }

        if (choice == 2) {

            if (storeMessageToJson()) {
                storedMessages.add(this);
                return "Message successfully stored.";
            }

            return "Unable to store message.";
        }

        return "No option selected.";
    }

    public static String printMessages() {

        if (sentMessages.isEmpty()) {
            return "No messages have been sent.";
        }

        StringBuilder output = new StringBuilder();

        for (Message msg : sentMessages) {
            output.append(msg.getMessageDetails())
                    .append("\n\n");
        }

        return output.toString();
    }

    public static int returnTotalMessages() {
        return totalMessages;
    }

    // Kept for compatibility with Part 2
    public static int returnTotalMessagess() {
        return returnTotalMessages();
    }

    // Part 3: Get sent messages
    public static ArrayList<Message> getSentMessages() {
        return new ArrayList<>(sentMessages);
    }

    public static ArrayList<Message> getDisregardedMessages() {
        return new ArrayList<>(disregardedMessages);
    }

    public static ArrayList<Message> getStoredMessages() {
        return new ArrayList<>(storedMessages);
    }

    public static ArrayList<String> getMessageHashes() {
        return new ArrayList<>(messageHashes);
    }

    public static ArrayList<String> getMessageIDs() {
        return new ArrayList<>(messageIDs);
    }

    // Part 3: Display sender and recipient
    public static String displaySenderAndRecipient(
            String sender) {

        if (storedMessages.isEmpty()) {
            return "No stored messages found.";
        }

        StringBuilder result = new StringBuilder();

        for (Message msg : storedMessages) {

            result.append("Sender: ")
                    .append(sender)
                    .append("\nRecipient: ")
                    .append(msg.recipient)
                    .append("\n\n");
        }

        return result.toString();
    }

    // Part 3: Find longest stored message
    public static String displayLongestMessage() {

        if (storedMessages.isEmpty()) {
            return "No stored messages found.";
        }

        Message longest = storedMessages.get(0);

        for (Message msg : storedMessages) {

            if (msg.message.length()
                    > longest.message.length()) {

                longest = msg;
            }
        }

        return longest.message;
    }

    // Part 3: Search using message ID
    public static String searchMessageID(String id) {

        for (Message msg : getSentAndStoredMessages()) {

            if (msg.messageID.equals(id)) {

                return "Recipient: " + msg.recipient
                        + "\nMessage: " + msg.message;
            }
        }

        return "Message ID not found.";
    }

    // Part 3: Search using recipient
    public static String searchRecipient(String recipient) {

        StringBuilder result = new StringBuilder();

        for (Message msg : getSentAndStoredMessages()) {

            if (msg.recipient.equals(recipient)) {

                result.append("Message: ")
                        .append(msg.message)
                        .append("\n\n");
            }
        }

        if (result.length() == 0) {
            return "No messages found for this recipient.";
        }

        return result.toString();
    }

    private static ArrayList<Message>
            getSentAndStoredMessages() {

        ArrayList<Message> all = new ArrayList<>(sentMessages);

        for (Message msg : storedMessages) {

            boolean exists = false;

            for (Message existing : all) {

                if (existing.messageID.equals(msg.messageID)) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                all.add(msg);
            }
        }

        return all;
    }

    // Part 3: Delete message using hash
    public static String deleteMessageByHash(String hash) {

        Message found = null;

        for (Message msg : storedMessages) {

            if (msg.messageHash.equalsIgnoreCase(hash)) {
                found = msg;
                break;
            }
        }

        if (found == null) {
            return "Message hash not found.";
        }

        ArrayList<Message> updated =
                new ArrayList<>(storedMessages);

        updated.remove(found);

        if (!saveStoredMessages(updated)) {
            return "Unable to delete message.";
        }

        storedMessages.clear();
        storedMessages.addAll(updated);

        messageIDs.remove(found.messageID);
        messageHashes.remove(found.messageHash);

        return "Message: \"" + found.message
                + "\" successfully deleted.";
    }

    // Part 3: Full report
    public static String displayReport() {

        if (storedMessages.isEmpty()) {
            return "No stored messages found.";
        }

        StringBuilder report = new StringBuilder();

        report.append("QUICKCHAT MESSAGE REPORT\n")
                .append("========================\n\n");

        for (Message msg : storedMessages) {

            report.append("Message ID: ")
                    .append(msg.messageID)
                    .append("\nMessage Hash: ")
                    .append(msg.messageHash)
                    .append("\nRecipient: ")
                    .append(msg.recipient)
                    .append("\nMessage: ")
                    .append(msg.message)
                    .append("\n------------------------\n");
        }

        return report.toString();
    }

    // Convert a message to JSON
    private static String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private String toJson() {

        return "  {\n"
                + "    \"messageID\": \""
                + escapeJson(messageID) + "\",\n"
                + "    \"messageHash\": \""
                + escapeJson(messageHash) + "\",\n"
                + "    \"recipient\": \""
                + escapeJson(recipient) + "\",\n"
                + "    \"message\": \""
                + escapeJson(message) + "\",\n"
                + "    \"messageNumber\": "
                + messageNumber + "\n"
                + "  }";
    }

    public void storeMessage() {
        if (storeMessageToJson()
                && !storedMessages.contains(this)) {
            storedMessages.add(this);
        }
    }

    private boolean storeMessageToJson() {

        ArrayList<Message> updated =
                new ArrayList<>(storedMessages);

        updated.add(this);

        // Load previous JSON messages before replacing file
        try {
            if (Files.exists(JSON_FILE)) {

                String content = Files.readString(
                        JSON_FILE, StandardCharsets.UTF_8);

                ArrayList<Message> existing =
                        parseJsonMessages(content);

                for (Message msg : existing) {

                    boolean duplicate = false;

                    for (Message item : updated) {

                        if (item.messageID.equals(msg.messageID)) {
                            duplicate = true;
                            break;
                        }
                    }

                    if (!duplicate) {
                        updated.add(msg);
                    }
                }
            }

            return saveStoredMessages(updated);

        } catch (IOException | IllegalArgumentException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Error storing message: " + e.getMessage());

            return false;
        }
    }

    private static boolean saveStoredMessages(
            ArrayList<Message> messages) {

        Path temp = JSON_FILE.resolveSibling(
                JSON_FILE.getFileName().toString() + ".tmp");

        StringBuilder json = new StringBuilder("[\n");

        for (int i = 0; i < messages.size(); i++) {

            json.append(messages.get(i).toJson());

            if (i < messages.size() - 1) {
                json.append(",");
            }

            json.append("\n");
        }

        json.append("]");

        try {

            Files.writeString(temp, json.toString(),
                    StandardCharsets.UTF_8);

            Files.move(temp, JSON_FILE,
                    StandardCopyOption.REPLACE_EXISTING);

            return true;

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Error saving JSON: " + e.getMessage());

            return false;
        }
    }

    // Read JSON into the stored messages array
    public static void loadStoredMessages() {

        if (!Files.exists(JSON_FILE)) {
            return;
        }

        try {

            String json = Files.readString(
                    JSON_FILE, StandardCharsets.UTF_8);

            ArrayList<Message> loaded =
                    parseJsonMessages(json);

            storedMessages.clear();
            storedMessages.addAll(loaded);

        } catch (IOException | IllegalArgumentException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Error loading messages: " + e.getMessage());
        }
    }

    // JSON parser for the fields saved by QuickChat
    private static ArrayList<Message> parseJsonMessages(
            String json) {

        ArrayList<Message> result = new ArrayList<>();

        if (json == null || json.trim().isEmpty()) {
            return result;
        }

        String trimmed = json.trim();

        if (!trimmed.startsWith("[")
                || !trimmed.endsWith("]")) {
            throw new IllegalArgumentException(
                    "Invalid JSON array.");
        }

        java.util.regex.Pattern objectPattern =
                java.util.regex.Pattern.compile(
                        "\\{(.*?)\\}",
                        java.util.regex.Pattern.DOTALL);

        java.util.regex.Matcher matcher =
                objectPattern.matcher(trimmed);

        while (matcher.find()) {

            String object = matcher.group(1);

            String id = extractJsonString(object, "messageID");
            String hash = extractJsonString(object, "messageHash");
            String recipient = extractJsonString(object, "recipient");
            String text = extractJsonString(object, "message");

            String numberText =
                    extractJsonNumber(object, "messageNumber");

            int number = numberText.isEmpty()
                    ? result.size() + 1
                    : Integer.parseInt(numberText);

            if (id.isEmpty()) {
                throw new IllegalArgumentException(
                        "Stored message is missing an ID.");
            }

            result.add(new Message(id, recipient,
                    text, number, hash));
        }

        return result;
    }

    private static String extractJsonString(
            String object, String key) {

        java.util.regex.Pattern pattern =
                java.util.regex.Pattern.compile(
                        "\"" + key
                        + "\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"");

        java.util.regex.Matcher matcher =
                pattern.matcher(object);

        if (!matcher.find()) {
            return "";
        }

        String value = matcher.group(1);

        return value.replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
    }

    private static String extractJsonNumber(
            String object, String key) {

        java.util.regex.Pattern pattern =
                java.util.regex.Pattern.compile(
                        "\"" + key + "\"\\s*:\\s*(\\d+)");

        java.util.regex.Matcher matcher =
                pattern.matcher(object);

        return matcher.find() ? matcher.group(1) : "";
    }

    public String getMessageDetails() {

        return "Message ID: " + messageID
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient
                + "\nMessage: " + message;
    }

    public String getMessageID() {
        return messageID;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }
    
    // Helper method for testing sent messages
    public static void addSentMessageForTest(Message msg) {
        sentMessages.add(msg);
    }

    // Helper method for testing stored messages
    public static void addStoredMessageForTest(Message msg) {
        storedMessages.add(msg);
    }


    // Point JSON storage to a temporary test file.
    // Only call this from JUnit tests, not the main application.
    public static void useJsonFileForTest(Path testFile) {
        if (testFile == null) {
            throw new IllegalArgumentException("Test file cannot be null.");
        }
        JSON_FILE = testFile;
    }

    // Restore the normal application JSON file after a test.
    public static void restoreJsonFileAfterTest() {
        JSON_FILE = Paths.get("storedMessages.json");
    }

    // Clear in-memory message data between JUnit tests.
    // Does not delete or modify storedMessages.json.
    public static void resetMessagesForTest() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
        usedMessageIDs.clear();
        totalMessages = 0;
    }
}

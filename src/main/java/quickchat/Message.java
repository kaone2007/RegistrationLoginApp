
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

    private static final ArrayList<Message> sentMessages =
            new ArrayList<>();

    private static final HashSet<String> usedMessageIDs =
            new HashSet<>();

    private static final Random random = new Random();

    public Message(String recipient, String message,
                   int messageNumber) {

        this.recipient = recipient;
        this.message = message;
        this.messageNumber = messageNumber;

        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
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

        String firstWord = words[0];
        String lastWord = words[words.length - 1];

        return (firstTwoDigits + ":"
                + messageNumber + ":"
                + firstWord + lastWord).toUpperCase();
    }

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

            return "Press 0 to delete the message.";
        }

        if (choice == 2) {

            if (storeMessageToJson()) {
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

    public static int returnTotalMessagess() {
        return returnTotalMessages();
    }

    private String escapeJson(String value) {

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
                + escapeJson(message) + "\"\n"
                + "  }";
    }

    public void storeMessage() {
        storeMessageToJson();
    }

    private boolean storeMessageToJson() {

        Path file = Paths.get("storedMessages.json");
        Path temp = Paths.get("storedMessages.json.tmp");

        try {

            String content = "[]";

            if (Files.exists(file)) {

                content = Files.readString(
                        file,
                        StandardCharsets.UTF_8
                ).trim();
            }

            if (!content.startsWith("[")
                    || !content.endsWith("]")) {

                throw new IOException(
                        "Existing file is not a JSON array."
                );
            }

            String inside = content.substring(
                    1, content.length() - 1
            ).trim();

            String updated;

            if (inside.isEmpty()) {

                updated = "[\n" + toJson() + "\n]";

            } else {

                updated = "[\n"
                        + inside + ",\n"
                        + toJson() + "\n]";
            }

            Files.writeString(
                    temp,
                    updated,
                    StandardCharsets.UTF_8
            );

            Files.move(
                    temp,
                    file,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return true;

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Error storing message: "
                            + e.getMessage()
            );

            return false;
        }
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
}

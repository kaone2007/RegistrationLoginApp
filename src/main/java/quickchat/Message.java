
package quickchat;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Message {

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String message;
    private String messageHash;

    private static int totalMessages = 0;
    private static final ArrayList<Message> sentMessages =
            new ArrayList<>();

    public Message(String recipient, String message,
                   int messageNumber) {
        this.messageID = generateMessageID();
        this.recipient = recipient;
        this.message = message;
        this.messageNumber = messageNumber;
        this.messageHash = createMessageHash();
    }

    private String generateMessageID() {
        Random random = new Random();
        StringBuilder id = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            id.append(random.nextInt(10));
        }

        return id.toString();
    }

    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
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

        int extra = message.length() - 250;

        return "Message exceeds 250 characters by "
                + extra + ", please reduce the size.";
    }

    public String createMessageHash() {
        String firstTwo = messageID.substring(0, 2);
        String[] words = message.trim().split("\\s+");

        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 0
                ? words[words.length - 1] : "";

        return (firstTwo + ":" + messageNumber + ":"
                + firstWord + lastWord).toUpperCase();
    }

    public String SentMessage() {
        final String[] options = {
            "Send Message",
            "Disregard Message",
            "Store Message"
        };

        final int[] choice = {-1};

        Runnable showDialog = () -> {
            choice[0] = JOptionPane.showOptionDialog(
                    null,
                    "Choose what you want to do:",
                    "QuickChat - Message Options",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );
        };

        try {
            if (SwingUtilities.isEventDispatchThread()) {
                showDialog.run();
            } else {
                SwingUtilities.invokeAndWait(showDialog);
            }
        } catch (Exception e) {
            return "Unable to display message options: "
                    + e.getMessage();
        }

        if (choice[0] == 0) {
            sentMessages.add(this);
            totalMessages++;
            return "Message successfully sent.";
        }

        if (choice[0] == 1) {
            return "Press 0 to delete the message.";
        }

        if (choice[0] == 2) {
            storeMessage();
            return "Message successfully stored.";
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

    private String escapeJson(String value) {
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    public void storeMessage() {
        try (FileWriter writer =
                     new FileWriter("storedMessages.json", true)) {

            writer.write("{\n");
            writer.write("  \"messageID\": \""
                    + escapeJson(messageID) + "\",\n");
            writer.write("  \"messageHash\": \""
                    + escapeJson(messageHash) + "\",\n");
            writer.write("  \"recipient\": \""
                    + escapeJson(recipient) + "\",\n");
            writer.write("  \"message\": \""
                    + escapeJson(message) + "\"\n");
            writer.write("}\n");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error storing message: " + e.getMessage()
            );
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

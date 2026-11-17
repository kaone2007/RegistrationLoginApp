
package quickchat;

import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @TempDir
    Path temporaryFolder;

    @BeforeEach
    public void setUp() {
        Message.restoreJsonFileAfterTest();
        Message.resetMessagesForTest();
    }

    @AfterEach
    public void tearDown() {
        Message.restoreJsonFileAfterTest();
        Message.resetMessagesForTest();
    }

    // ========================================
    // PART 2 TESTS
    // ========================================

    @Test
    public void testMessageLengthSuccess() {
        Message message = new Message(
                "+27718693002",
                "Hi Mike, can you join us for dinner tonight?",
                0
        );

        assertEquals(
                "Message ready to send.",
                message.checkMessageLength()
        );
    }

    @Test
    public void testMessageLengthFailure() {
        String longMessage = "A".repeat(260);

        Message message = new Message(
                "+27718693002",
                longMessage,
                0
        );

        assertEquals(
                "Message exceeds 250 characters by 10, please reduce the size.",
                message.checkMessageLength()
        );
    }

    @Test
    public void testRecipientNumberSuccess() {
        Message message = new Message(
                "+27718693002",
                "Hi Mike, can you join us for dinner tonight?",
                0
        );

        assertEquals(
                "Cell phone number successfully captured.",
                message.checkRecipientCell()
        );
    }

    @Test
    public void testRecipientNumberFailure() {
        Message message = new Message(
                "08575975889",
                "Hi Keegan, did you receive the payment?",
                1
        );

        assertEquals(
                "Cell phone number is incorrectly formatted or does not contain "
                + "an international code. Please correct the number and try again.",
                message.checkRecipientCell()
        );
    }

    @Test
    public void testMessageIDCreated() {
        Message message = new Message(
                "+27718693002",
                "Hello Mike",
                0
        );

        assertNotNull(message.getMessageID());
        assertEquals(10, message.getMessageID().length());
        assertTrue(message.checkMessageID());
    }

    @Test
    public void testMessageHash() {
        Message message = new Message(
                "+27718693002",
                "Hi Mike, can you join us for dinner tonight",
                0
        );

        String id = message.getMessageID().substring(0, 2);
        String expected = id + ":0:HITONIGHT";

        assertEquals(expected, message.getMessageHash());
    }

    // ========================================
    // PART 3 TESTS
    // ========================================

    @Test
    public void testSentMessagesArray() {
        Message message1 = new Message(
                "+27834557896",
                "Did you get the cake?",
                1
        );

        Message message4 = new Message(
                "+27838884567",
                "It is dinner time!",
                4
        );

        Message.addSentMessageForTest(message1);
        Message.addSentMessageForTest(message4);

        assertEquals(2, Message.getSentMessages().size());

        assertEquals(
                "Did you get the cake?",
                Message.getSentMessages().get(0).getMessage()
        );

        assertEquals(
                "It is dinner time!",
                Message.getSentMessages().get(1).getMessage()
        );
    }

    @Test
    public void testLongestMessage() {
        Message message2 = new Message(
                "+27838884567",
                "Where are you? You are late! I have asked you to be on time.",
                2
        );

        Message message5 = new Message(
                "+27838884567",
                "Ok, I am leaving without you.",
                5
        );

        Message.addStoredMessageForTest(message2);
        Message.addStoredMessageForTest(message5);

        assertEquals(
                message2.getMessage(),
                Message.displayLongestMessage()
        );
    }

    @Test
    public void testSearchMessageID() {
        Message message4 = new Message(
                "+27838884567",
                "It is dinner time!",
                4
        );

        Message.addSentMessageForTest(message4);

        String expected =
                "Recipient: " + message4.getRecipient()
                + "\nMessage: " + message4.getMessage();

        assertEquals(
                expected,
                Message.searchMessageID(message4.getMessageID())
        );
    }

    @Test
    public void testSearchRecipient() {
        Message message2 = new Message(
                "+27838884567",
                "Where are you? You are late! I have asked you to be on time.",
                2
        );

        Message message5 = new Message(
                "+27838884567",
                "Ok, I am leaving without you.",
                5
        );

        Message.addStoredMessageForTest(message2);
        Message.addStoredMessageForTest(message5);

        String expected =
                "Message: " + message2.getMessage() + "\n\n"
                + "Message: " + message5.getMessage() + "\n\n";

        assertEquals(
                expected,
                Message.searchRecipient("+27838884567")
        );
    }

    @Test
    public void testDeleteMessageByHash() {
        // Use a temporary JSON file, not the real one.
        Path testFile = temporaryFolder.resolve("testMessages.json");
        Message.useJsonFileForTest(testFile);

        Message message2 = new Message(
                "+27838884567",
                "Where are you? You are late! I have asked you to be on time.",
                2
        );

        Message message5 = new Message(
                "+27838884567",
                "Ok, I am leaving without you.",
                5
        );

        message2.storeMessage();
        message5.storeMessage();

        assertEquals(2, Message.getStoredMessages().size());

        String expected =
                "Message: \"" + message2.getMessage()
                + "\" successfully deleted.";

        assertEquals(
                expected,
                Message.deleteMessageByHash(message2.getMessageHash())
        );

        assertEquals(1, Message.getStoredMessages().size());

        assertEquals(
                message5.getMessageID(),
                Message.getStoredMessages().get(0).getMessageID()
        );

        // Check that the deleted message is also gone
        // after reloading from the temporary JSON file.
        Message.resetMessagesForTest();
        Message.loadStoredMessages();

        assertEquals(1, Message.getStoredMessages().size());

        assertEquals(
                message5.getMessageID(),
                Message.getStoredMessages().get(0).getMessageID()
        );
    }

    @Test
    public void testDisplayReport() {
        Message message2 = new Message(
                "+27838884567",
                "Where are you? You are late! I have asked you to be on time.",
                2
        );

        Message message5 = new Message(
                "+27838884567",
                "Ok, I am leaving without you.",
                5
        );

        Message.addStoredMessageForTest(message2);
        Message.addStoredMessageForTest(message5);

        String report = Message.displayReport();

        assertTrue(report.contains(message2.getMessageID()));
        assertTrue(report.contains(message2.getMessageHash()));
        assertTrue(report.contains(message2.getRecipient()));
        assertTrue(report.contains(message2.getMessage()));

        assertTrue(report.contains(message5.getMessageID()));
        assertTrue(report.contains(message5.getMessageHash()));
        assertTrue(report.contains(message5.getRecipient()));
        assertTrue(report.contains(message5.getMessage()));
    }
}

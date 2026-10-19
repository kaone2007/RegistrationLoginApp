package quickchat;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

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

        assertEquals(
                10,
                message.getMessageID().length()
        );

        assertTrue(
                message.checkMessageID()
        );
    }

    @Test
    public void testMessageHash() {

        Message message = new Message(
                "+27718693002",
                "Hi Mike, can you join us for dinner tonight",
                0
        );

        String id =
                message.getMessageID().substring(0, 2);

        String expected =
                id + ":0:HITONIGHT";

        assertEquals(
                expected,
                message.getMessageHash()
        );
    }
}
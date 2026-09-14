import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    @Test
    public void testUsernameCorrectlyFormatted() {
        Login login = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertTrue(login.checkUserName());
    }

    @Test
    public void testUsernameIncorrectlyFormatted() {
        Login login = new Login(
                "kyle!!!!!!!",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertFalse(login.checkUserName());
    }

    @Test
    public void testPasswordMeetsComplexityRequirements() {
        Login login = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertTrue(login.checkPasswordComplexity());
    }

    @Test
    public void testPasswordDoesNotMeetComplexityRequirements() {
        Login login = new Login(
                "kyl_1",
                "password",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertFalse(login.checkPasswordComplexity());
    }

    @Test
    public void testCellPhoneNumberCorrectlyFormatted() {
        Login login = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertTrue(login.checkCellPhoneNumber());
    }

    @Test
    public void testCellPhoneNumberIncorrectlyFormatted() {
        Login login = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "08966553",
                "Kyle",
                "Smith"
        );

        assertFalse(login.checkCellPhoneNumber());
    }

    @Test
    public void testLoginSuccessful() {
        Login login = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        login.setLoginDetails("kyl_1", "Ch&&sec@ke99!");

        assertTrue(login.loginUser());
    }

    @Test
    public void testLoginFailed() {
        Login login = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        login.setLoginDetails("wrongUser", "wrongPassword");

        assertFalse(login.loginUser());
    }

    @Test
    public void testCorrectUsernameMessage() {
        Login login = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        String expected = "Welcome Kyle, Smith it is great to see you again.";

        login.setLoginDetails("kyl_1", "Ch&&sec@ke99!");

        assertEquals(expected, login.returnLoginStatus());
    }

    @Test
    public void testIncorrectUsernameMessage() {
        Login login = new Login(
                "kyle!!!!!!!",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        String expected =
                "Username is not correctly formatted; please ensure that "
                + "your username contains an underscore and is no more "
                + "than five characters in length.";

        assertEquals(expected, login.registerUser());
    }

    @Test
    public void testPasswordSuccessfullyCapturedMessage() {
        Login login = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertTrue(login.checkPasswordComplexity());
    }

    @Test
    public void testPasswordIncorrectlyFormattedMessage() {
        Login login = new Login(
                "kyl_1",
                "password",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        String expected =
                "Password is not correctly formatted; please ensure that "
                + "the password contains at least eight characters, "
                + "a capital letter, a number, and a special character.";

        assertEquals(expected, login.registerUser());
    }

    @Test
    public void testSuccessfulRegistration() {
        Login login = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertEquals(
                "User registered successfully.",
                login.registerUser()
        );
    }
}
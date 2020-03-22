package skipBo.userExceptions;

/**
 * Exception thrown when a user chooses an invalid name. Invalid names are: less than 3 or more than 13 characters,
 * anything containing anything else than letters and digits.
 */
public class NoNameException extends Exception {

    public NoNameException(String message) {
        super(message);
    }
}

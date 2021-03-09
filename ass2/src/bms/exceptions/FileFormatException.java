package bms.exceptions;

/**
 * Exception thrown when a save file
 * containing a list of building data is invalid.
 *
 * @ass2
 */
public class FileFormatException extends Exception{
    /**
     * Constructs a normal FileFormatException
     * with no error message or cause.
     *
     * @see Exception#Exception()
     * @ass2
     */
    public FileFormatException() {
        super();
    }

    /**
     * Constructs a FileFormatException that contains a helpful
     * message detailing why the exception occurred,
     * and an underlying cause of the exception.
     *
     * Note: implementing this constructor is optional. It has only
     * been included in the Javadoc to ensure your code will compile
     * if you give your exception a message and a cause when throwing it.
     * This practice can be useful for debugging purposes.
     *
     * Important: do not write JUnit tests that expect a valid implementation
     * of the assignment to have a certain error message, as the official
     * solution will use different messages to those you are expecting,
     * if any at all.
     *
     * @param message detail message
     * @see Exception#Exception(String)
     * @ass2
     */
    public FileFormatException(String message) {
        super(message);
    }
}

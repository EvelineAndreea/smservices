package sm.exceptions;


import static sm.utils.Constants.ERROR_INVALID_PROBLEM_TYPE;
import static sm.utils.Constants.XML_ERROR_PREFIX;

public class ValidationException extends RuntimeException {

    public ValidationException() {
        super(XML_ERROR_PREFIX + ERROR_INVALID_PROBLEM_TYPE);
    }

    public ValidationException(String errorMessage) {
        super(XML_ERROR_PREFIX + errorMessage);
    }
}

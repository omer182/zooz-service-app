package exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UniqueConstraintException extends RuntimeException {
    public UniqueConstraintException(DataIntegrityViolationException e) {
        super(GetConstraints(e));
    }

    public UniqueConstraintException(Exception e) {
        super(GetConstraints(e));
    }

    /**
     * get a string representation of the exception
     *
     * @param e exception
     * @return a string representation of the exception
     */
    private static String GetConstraints(Exception e) {
        String s = e.getCause().getMessage();
        return s.substring(0, s.length() - 39);
    }

    /**
     * get a string representation of the exception
     *
     * @param e exception
     * @return a string representation of the exception
     */
    private static String GetConstraints(DataIntegrityViolationException e) {
        String s = e.getCause().getCause().getMessage();
        return s.substring(0, s.length() - 39);
    }
}

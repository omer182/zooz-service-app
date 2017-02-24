package exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import javax.validation.ConstraintViolationException;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NullFieldException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NullFieldException(ConstraintViolationException e) {
        super(GetConstraints(e)); }

    public NullFieldException(String msg){
        super(msg);
    }

    /**
     * get a string representation of the exception
     *
     * @param e exception
     * @return a string representation of the exception
     */
    private static String GetConstraints(ConstraintViolationException e) {
        StringBuilder sb = new StringBuilder();
        e.getConstraintViolations().forEach(c -> sb.append(String.format("exception in field '%s', reason: '%s'. ",
                c.getPropertyPath(),c.getMessage())));
        return sb.toString();
    }
}

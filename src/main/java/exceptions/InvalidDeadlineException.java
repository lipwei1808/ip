package exceptions;

public class InvalidDeadlineException extends DukeException {
    public InvalidDeadlineException() {
        super("invalid deadline");
    }

    @Override
    public String toString() {
        return super.toString() + " Please enter a deadline with the '/by' annotation";
    }
}

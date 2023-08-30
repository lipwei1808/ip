import java.time.LocalDateTime;

public class Deadline extends Task {
    private LocalDateTime by;

    public Deadline(String description, LocalDateTime by, boolean marked) {
        super(description, "deadline", marked);
        this.by = by;
    }

    @Override
    public String getOriginalMessage() {
        return "deadline " + this.getDescription() + " /by " + this.stringifyDate(this.by);
    }

    @Override

    public String toString() {
        return super.toString() + " (by: " + this.formatDate(this.by) + ")";
    }
}

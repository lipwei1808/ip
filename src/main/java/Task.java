public class Task {
    private Line line = new Line();
    private String description;
    private boolean isDone = false;

    public Task(String description) {
        this.description = description;
    }

    public void mark(boolean val) {
        this.isDone = val;
        System.out.println(line);
        if (val) {
            System.out.println("    Nice! I've marked this task as done:");
        } else {
            System.out.println("    OK, I've marked this task as not done yet:");
        }
        System.out.println("      " + this);
        System.out.println(line);
    }

    private String getStatusIcon() {
        return (isDone ? "X" : " "); //return tick or X symbols
    }

    @Override

    public String toString() {
        String s = String.format("[%s] %s", this.getStatusIcon(), this.description);
        return s;
    }
}

import exceptions.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Tasks {
    public Line line = new Line();
    private String filepath;
    private List<Task> tasks = new ArrayList<>();

    public Tasks(String filepath) {
        this.filepath = filepath;
    }


    public void handle(String text, boolean isRestoring) {
        try {
            // Check if input text is valid
            String[] parsedText = checkValid(text);
            String action = parsedText[0];
            String restOfText = parsedText[1];
            switch (action) {
                case "mark":
                    this.handleMark(restOfText, true);
                    break;
                case "unmark":
                    this.handleMark(restOfText, false);
                    break;
                case "list":
                    this.listTasks();
                    break;
                case "delete":
                    this.handleDelete(restOfText);
                    break;
                case "todo":
                case "deadline":
                case "event":
                    this.addTask(action, restOfText, isRestoring);
                    break;
                default:
                    System.out.println(line);
                    System.out.println("    Internal error...");
                    System.out.println(line);
                    break;
            }
        } catch (DukeException e) {
            System.out.println(line);
            System.out.println(e);
            System.out.println(line);
        }
    }

    private void handleDelete(String id) {
        try {
            int number = Integer.parseInt(id);
            Task task = this.getTask(number);
            if (task != null) {
                Task t = this.tasks.remove(number - 1);
                this.saveTasks();
                System.out.println(line);
                System.out.println("    Noted. I've removed this task:");
                System.out.println("    " + t);
                System.out.println("    Now you have " + this.tasks.size() + " tasks in the list.");
                System.out.println(line);
            } else {
                System.out.println(line);
                System.out.println("    Unknown task number! Please try again :-)");
                System.out.println(line);
            }
        } catch (NumberFormatException | StringIndexOutOfBoundsException ex) {
            System.out.println(line);
            System.out.println("    Please enter a number for the task! Please try again :-)");
            System.out.println(line);
        }
    }

    private void listTasks() {

        System.out.println(line);
        if (tasks.size() == 0) {
            System.out.println("    You do not have any tasks currently!");
        } else {
            System.out.println("    Here are the tasks in your list:");
            for (int i = 0; i < this.tasks.size(); i++) {
                Task task = this.tasks.get(i);
                String s = String.format("    %s.%s", i + 1, task);
                System.out.println(s);
            }
        }
        System.out.println(line);
    }

    private Task getTask(int id) {
        if (id > this.tasks.size() || id <= 0) {
            return null;
        }

        return this.tasks.get(id - 1);
    }

    private void handleMark(String id, boolean val) {
        try {
            int number = Integer.parseInt(id);
            Task task = this.getTask(number);
            if (task != null) {
                task.mark(val, false);
                this.saveTasks();
            } else {
                System.out.println(line);
                System.out.println("    Unknown task number! Please try again :-)");
                System.out.println(line);
            }
        } catch (NumberFormatException | StringIndexOutOfBoundsException ex) {
            System.out.println(line);
            System.out.println("    Please enter a number for the task! Please try again :-)");
            System.out.println(line);
        }
    }

    private void addTask(String action, String text, boolean isRestoring) {
        // Check if input text is valid
        Task task;
        boolean marked = false;
        if (isRestoring) {
            String marker = text.substring(text.length() - 1);
            marked = marker.equals("1");
            text = text.substring(0, text.length() - 1);
        }
        try {
            switch (action) {
                case "todo":
                    task = new Todo(text, marked);
                    break;
                case "deadline":
                    task = checkDeadline(text, marked);
                    break;
                case "event":
                    task = checkEvent(text, marked);
                    break;
                default:
                    System.out.println("    You shouldn't be here, something went wrong...");
                    System.exit(1);
                    return;
            }
        } catch (DukeException e) {
            System.out.println(line);
            System.out.println(e);
            System.out.println(line);
            return;
        }

        this.tasks.add(task);
        this.saveTasks();
        if (!isRestoring) {
            System.out.println(line);
            System.out.println("    Got it. I've added this task:");
            System.out.println("      " + task);
            System.out.println("    Now you have " + tasks.size() + " tasks in the list.");
            System.out.println(line);
        }
    }

    private void saveTasks() {
        // Delete everything in
        try {
            PrintWriter writer = new PrintWriter(this.filepath);
            writer.print("");
            writer.close();
        } catch (IOException ex) {
            System.out.println("    Error saving file... exiting");
            System.exit(1);
        }

        // Rewrite everything
        for (int i = 0; i < tasks.size(); i++) {
            tasks.get(i).save(this.filepath);
        }
    }

    private String[] checkValid(String text) throws DukeException {
        String[] words = text.split(" ");
        String action = words[0];
        if (!action.equals("todo")
                && !action.equals("deadline")
                && !action.equals("event")
                && !action.equals("mark")
                && !action.equals("unmark")
                && !action.equals("delete")
                && !action.equals("list")) {
            throw new InvalidCommandException();
        }
        String[] remaining = Arrays.copyOfRange(words, 1, words.length);
        String restOfText = String.join(" ", remaining);

        if (!action.equals("list") && restOfText.equals("")) {
            throw new EmptyTaskException(action);
        }

        return new String[] {action, restOfText};
    }

    private LocalDateTime parseDateTime(String text) {
        String[] datetime = text.split(" ");
        LocalDateTime parsedDateTime;
        try {
            if (datetime.length == 2) {
                String dateTimeString = datetime[0] + "T" + datetime[1];
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy'T'HH:mm");
                parsedDateTime = LocalDateTime.parse(dateTimeString, formatter);
            } else {
                return null;
            }
        } catch (DateTimeParseException ex) {
            return null;
        }

        return parsedDateTime;
    }

    private Deadline checkDeadline(String text, boolean marked) throws InvalidDeadlineException {
        String[] deadline = text.split(" /by ");
        if (deadline.length != 2) {
            throw new InvalidDeadlineException();
        }

        LocalDateTime parsedDateTime = this.parseDateTime(deadline[1]);
        if (parsedDateTime == null) {
            throw new InvalidDeadlineException();
        }

        return new Deadline(deadline[0], parsedDateTime, marked);
    }

    private Event checkEvent(String text, boolean marked) throws InvalidEventException {
        String[] first = text.split(" /from ");
        if (first.length != 2) {
            throw new InvalidEventException();
        }
        String[] second = first[1].split(" /to ");
        if (second.length != 2) {
            throw new InvalidEventException();
        }
        LocalDateTime fromDate = this.parseDateTime(second[0]);
        LocalDateTime toDate = this.parseDateTime(second[1]);

        if (fromDate == null || toDate == null) {
            throw new InvalidEventException();
        }

        return new Event(first[0], fromDate, toDate, marked);
    }
}

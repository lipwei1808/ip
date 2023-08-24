import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;

public class Duke {
    private static Line line = new Line();
    public static void main(String[] args) {
        printGreetings();
        Scanner s = new Scanner(System.in);
        Tasks tasks = new Tasks();

        while (true) {
            String text = s.nextLine();
            if (text.equals("")) {
                System.out.println(line);
                System.out.println("    Please enter something :-)");
                System.out.println(line);
                continue;
            }

            if (text.equals("bye")) {
                break;
            }

            tasks.handle(text);
        }
        printExit();
    }

    private static void printGreetings() {
        System.out.println(line);
        System.out.println("    Hello I'm lynn the koala <3");
        System.out.println("    What can I do for you?");
        System.out.println(line);
    }

    private static void printExit() {
        System.out.println(line);
        System.out.println("    Bye. Hpoe to see you again soon!");
        System.out.println(line);
    }
}

public class AddCommand extends Command {
    public static final String COMMAND_WORD_D = "deadline";
    public static final String COMMAND_WORD_E = "event";
    public static final String COMMAND_WORD_T = "todo";

    private Task task;

    public AddCommand(Task task) {
        this.task = task;
    }
    public void execute(Tasks tasks, Ui ui, Storage storage, boolean isRestoring) {
        tasks.add(this.task);
        storage.save(tasks);
        if (!isRestoring) {
            ui.showAddTask(this.task, tasks.size());
        }
    }
}

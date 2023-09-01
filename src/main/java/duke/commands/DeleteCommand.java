package duke.commands;

import duke.storage.Storage;
import duke.tasks.Task;
import duke.tasks.Tasks;
import duke.ui.Ui;
import duke.exceptions.DukeException;
import duke.exceptions.InvalidCommandException;

/**
 * Represents a DeleteCommand where this command should delete a Task.
 */
public class DeleteCommand extends Command {
    public static final String COMMAND_WORD = "delete";
    public int target;

    /**
     * Public constructor for DeleteCommand
     *
     * @param target the targeted index of the Task to be deleted
     */
    public DeleteCommand(int target) {
        this.target = target;
    }

    @Override
    public void execute(Tasks tasks, Ui ui, Storage storage, boolean isRestoring) throws DukeException {
        Task task = tasks.getTaskByIndex(this.target);
        if (task == null) {
            throw new InvalidCommandException();
        }
        Task t = tasks.remove(this.target - 1);
        storage.save(tasks);
        if (!isRestoring) {
            ui.showSuccessDelete(t, tasks.size());
        }
    }
}

public class ExitCommand extends Command {
    public static final String COMMAND_WORD = "bye";
    public void execute(Tasks tasks, Ui ui, Storage storage, boolean isRestoring) {
        ui.printExit();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}

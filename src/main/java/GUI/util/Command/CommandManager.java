package GUI.util.Command;

import java.util.Deque;
import java.util.LinkedList;

/**
 *
 */
public class CommandManager {
    private Deque<Command> history = new LinkedList<>();
    private static CommandManager instance;

    public static CommandManager getInstance(){
        if(instance==null)
            instance = new CommandManager();
        return instance;
    }

    private CommandManager() {
    }

    public void addCommand(Command command){
        history.offer(command);
    }

    public Command getPrevious(){
       return history.peek();
    }
}

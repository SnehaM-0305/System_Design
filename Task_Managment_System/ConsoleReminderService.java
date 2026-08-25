import java.time.LocalDateTime;

public class ConsoleReminderService implements ReminderService {
    @Override
    public void schedule(Task task , LocalDateTime remindertime){
        System.out.println("[Reminder Schedule] Task \"" + task.getTitle() + "\"will remind at " +remindertime);
    }
}

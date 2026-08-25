import java.time.LocalDateTime; 

public interface ReminderService {
    void schedule(Task task , LocalDateTime reminderTime) ; 
}

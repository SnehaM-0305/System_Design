import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Main just wires everything together and exercises each requirement once,
 * so you can see the whole system working end to end. Read this top to
 * bottom — it's written in the same order as the 7 requirements.
 */
public class Main {

    public static void main(String[] args) {

        // The ONE place we choose which ReminderService implementation to
        // use. Swap ConsoleReminderService for a future EmailReminderService
        // here and nothing else in the codebase changes.
        ReminderService reminderService = new ConsoleReminderService();
        TaskManager manager = new TaskManager(reminderService);

        // --- Set up two users ---
        User alice = manager.createUser("Alice", "alice@example.com");
        User bob = manager.createUser("Bob", "bob@example.com");

        // --- Requirement 1 & 2: create a task with title/description/due date/priority ---
        Task task1 = alice.create(
                "Write project proposal",
                "Draft the Q3 project proposal for review",
                LocalDate.now().plusDays(3),
                Priority.HIGH
        );
        System.out.println("Created: " + task1);

        // --- Requirement 3: assign the task to another user, and set a reminder ---
        alice.assign(task1.getId(), bob.getId());
        manager.setReminder(task1.getId(), LocalDateTime.now().plusDays(2));
        System.out.println("After assignment: " + task1);

        // --- Create a couple more tasks so search/filter has something to work with ---
        Task task2 = bob.create(
                "Fix login bug",
                "Users report login failing on mobile",
                LocalDate.now().plusDays(1),
                Priority.HIGH
        );
        bob.assign(task2.getId(), alice.getId());

        Task task3 = alice.create(
                "Update documentation",
                "Refresh the onboarding docs",
                LocalDate.now().plusDays(10),
                Priority.LOW
        );

        // --- Requirement 4: search/filter ---
        List<Task> highPriorityTasks = manager.searchTasks(
                new SearchCriteria().withPriority(Priority.HIGH)
        );
        System.out.println("\nHigh priority tasks: " + highPriorityTasks);

        List<Task> bobsTasks = manager.searchTasks(
                new SearchCriteria().withAssignedTo(bob.getId())
        );
        System.out.println("Tasks assigned to Bob: " + bobsTasks);

        // --- Requirement 5: mark completed, then view history ---
        bob.complete(task2.getId());
        System.out.println("\nAfter completing task2: " + task2);
        System.out.println("Status history for task2: " + task2.getHistory());

        System.out.println("Bob's completed tasks: " + bob.getCompletedTasks());
        System.out.println("Alice's full history: " + manager.history(alice.getId()));

        // --- Requirement 6: concurrency / optimistic locking demo ---
        int versionBeforeEdit = task3.getVersion();
        // Someone else updates task3 first...
        manager.updateTask(task3.getId(), versionBeforeEdit,
                "Update documentation (rev 2)", task3.getDescription(),
                task3.getDueDate(), Priority.MEDIUM);

        // ...now our original caller tries to save using the STALE version
        // they read earlier. This should be rejected.
        try {
            manager.updateTask(task3.getId(), versionBeforeEdit,
                    "My conflicting edit", task3.getDescription(),
                    task3.getDueDate(), Priority.HIGH);
        } catch (IllegalStateException e) {
            System.out.println("\nOptimistic lock correctly rejected a stale update: "
                    + e.getMessage());
        }

        // --- Requirement 7: extensibility already demonstrated above —
        // ReminderService was injected, not hardcoded.
    }
}
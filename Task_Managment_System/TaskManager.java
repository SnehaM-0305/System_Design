import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class TaskManager {

    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final Map<String, Task> tasks = new ConcurrentHashMap<>();

    // AtomicInteger, not a plain int: incrementing a plain int (`counter++`)
    // is actually TWO steps (read, then write) and can race between threads.
    // AtomicInteger.incrementAndGet() does both steps as one atomic operation.
    private final AtomicInteger userIdCounter = new AtomicInteger(0);
    private final AtomicInteger taskIdCounter = new AtomicInteger(0);

    private final ReminderService reminderService;

    /**
     * The ReminderService is passed IN rather than created inside this
     * class ("dependency injection"). This is what makes requirement #7
     * (extensibility) real: to switch from console reminders to email
     * reminders, you change ONE line where TaskManager is constructed —
     * nothing inside TaskManager itself needs to change.
     */
    public TaskManager(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    // ---------------------------------------------------------------
    // User management
    // ---------------------------------------------------------------

    public User createUser(String name, String email) {
        String id = "u" + userIdCounter.incrementAndGet();
        User user = new User(id, name, email, this);
        users.put(id, user);
        return user;
    }

    public User getUser(String userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new IllegalArgumentException("No such user: " + userId);
        }
        return user;
    }

    // ---------------------------------------------------------------
    // Task CRUD — requirement #1
    // ---------------------------------------------------------------

    public Task createTask(String creatorUserId, String title, String description,
                            LocalDate dueDate, Priority priority) {
        getUser(creatorUserId); // throws if the creator doesn't exist — fail fast
        String id = "t" + taskIdCounter.incrementAndGet();
        Task task = new Task(id, title, description, dueDate, priority, creatorUserId);
        tasks.put(id, task);
        return task;
    }

    /**
     * This overload demonstrates OPTIMISTIC LOCKING in practice (see the
     * long comment in Task.java about `version`). The caller must supply
     * the version they last read; if someone else changed the task in the
     * meantime, the version will have moved on and we reject the update
     * rather than silently overwrite the other change.
     */
    public void updateTask(String taskId, int expectedVersion, String title,
                            String description, LocalDate dueDate, Priority priority) {
        Task task = getTask(taskId);
        if (task.getVersion() != expectedVersion) {
            throw new IllegalStateException(
                    "Task " + taskId + " was modified by someone else - reload and retry.");
        }
        task.update(title, description, dueDate, priority);
    }

    public void deleteTask(String taskId) {
        tasks.remove(taskId);
    }

    public Task getTask(String taskId) {
        Task task = tasks.get(taskId);
        if (task == null) {
            throw new IllegalArgumentException("No such task: " + taskId);
        }
        return task;
    }



    public void assignTask(String taskId, String userId) {
        getUser(userId);        // validate the target user exists
        Task task = getTask(taskId);
        task.assignTo(userId);
    }

    public void setReminder(String taskId, LocalDateTime reminderTime) {
        Task task = getTask(taskId);
        task.setReminderTime(reminderTime);
        reminderService.schedule(task, reminderTime); // delegate to the interface
    }



    public void completeTask(String taskId) {
        getTask(taskId).markCompleted();
    }


    public List<Task> searchTasks(SearchCriteria criteria) {
        List<Task> result = new ArrayList<>();
        for (Task task : tasks.values()) {
            if (criteria.matches(task)) {
                result.add(task);
            }
        }
        return result;
    }

    public List<Task> history(String userId) {
        List<Task> result = new ArrayList<>();
        for (Task task : tasks.values()) {
            boolean isCreator = task.getCreatedBy().equals(userId);
            boolean isAssignee = userId.equals(task.getAssignee());
            if (isCreator || isAssignee) {
                result.add(task);
            }
        }
        return result;
    }
}
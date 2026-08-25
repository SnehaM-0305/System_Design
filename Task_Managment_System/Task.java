import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Task is the central entity in the whole system — every requirement
 * touches it in some way. Walk through the fields with requirement #2 open
 * in front of you and you'll see each one is there because the spec asked
 * for it, not because "more fields = more professional".
 *
 * THREAD SAFETY (requirement #6):
 * Two different users could try to update the SAME task at the same
 * moment (e.g. one marks it complete while another reassigns it). We
 * handle that here with two techniques used TOGETHER:
 *
 *   1. `synchronized` on every method that changes state. This means:
 *      if Thread A is inside markCompleted(), Thread B calling assignTo()
 *      on the SAME Task object must wait its turn. Only one thread can be
 *      inside ANY synchronized method of a given Task instance at once.
 *      This is called a "monitor lock" — every object in Java has one
 *      built in for free.
 *
 *   2. A `version` counter, incremented on every change. This isn't for
 *      preventing the race itself (synchronized already does that) — it's
 *      for a DIFFERENT problem: "optimistic locking" across a longer
 *      workflow. Imagine a user loads a task into a web form, thinks for
 *      five minutes, then hits Save — meanwhile someone else already
 *      changed it. The version number lets TaskManager detect "the copy
 *      you're saving is stale" and reject the write instead of silently
 *      overwriting someone else's change. See TaskManager.updateTask(...)
 *      for where this check actually happens.
 */
public class Task {

    private final String id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private Priority priority;
    private Status status;

    private final String createdBy;   // userId of whoever created this task
    private String assignee;          // userId of whoever it's assigned to (nullable)

    private LocalDateTime reminderTime; // nullable — not every task has a reminder

    private int version;
    private final List<StatusChange> history = new ArrayList<>();

    public Task(String id, String title, String description, LocalDate dueDate,
                Priority priority, String createdBy) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.createdBy = createdBy;
        this.status = Status.PENDING; // every task starts life as PENDING
        this.version = 0;
    }

    // ----- read-only getters: no synchronization needed for simple reads
    // of immutable or rarely-changing fields, but we mark the mutable-field
    // getters synchronized too, so a reader never sees a half-written state. -----

    public String getId() {
        return id;
    }

    public synchronized String getTitle() {
        return title;
    }

    public synchronized String getDescription() {
        return description;
    }

    public synchronized LocalDate getDueDate() {
        return dueDate;
    }

    public synchronized Priority getPriority() {
        return priority;
    }

    public synchronized Status getStatus() {
        return status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public synchronized String getAssignee() {
        return assignee;
    }

    public synchronized LocalDateTime getReminderTime() {
        return reminderTime;
    }

    public synchronized int getVersion() {
        return version;
    }

    /** Returns an unmodifiable view so nobody can tamper with history from outside. */
    public synchronized List<StatusChange> getHistory() {
        return Collections.unmodifiableList(new ArrayList<>(history));
    }

    // ----- mutating methods: every one of these is synchronized -----

    /**
     * Requirement #1: update a task's editable fields.
     * We don't let callers change `id` or `createdBy` — those are facts
     * about the task's origin and should never change after creation.
     */
    public synchronized void update(String title, String description,
                                     LocalDate dueDate, Priority priority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.version++;
    }

    /** Requirement #3: assign this task to another user. */
    public synchronized void assignTo(String userId) {
        this.assignee = userId;
        this.version++;
    }

    /** Requirement #3: attach a reminder time to this task. */
    public synchronized void setReminderTime(LocalDateTime reminderTime) {
        this.reminderTime = reminderTime;
        this.version++;
    }

    /**
     * Requirement #5: mark completed. Every status change is recorded into
     * `history`, which is how "view task history" gets answered later.
     */
    public synchronized void markCompleted() {
        changeStatus(Status.COMPLETED);
    }

    public synchronized void changeStatus(Status newStatus) {
        history.add(new StatusChange(this.status, newStatus, LocalDateTime.now()));
        this.status = newStatus;
        this.version++;
    }

    /**
     * Requirement #2 (implied): a task is overdue if its due date has
     * passed AND it isn't already done. A completed task is never
     * "overdue" — that distinction matters for any UI badge/warning logic.
     */
    public synchronized boolean isOverdue() {
        return dueDate != null
                && LocalDate.now().isAfter(dueDate)
                && status != Status.COMPLETED;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (priority=%s, status=%s, assignee=%s, due=%s)",
                id, title, priority, status, assignee, dueDate);
    }
}
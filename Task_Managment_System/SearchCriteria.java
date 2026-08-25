import java.time.LocalDate;



public class SearchCriteria {

    private Priority priority;
    private LocalDate dueBefore;
    private String assignedTo;


    public SearchCriteria withPriority(Priority priority) {
        this.priority = priority;
        return this;
    }

    public SearchCriteria withDueBefore(LocalDate dueBefore) {
        this.dueBefore = dueBefore;
        return this;
    }

    public SearchCriteria withAssignedTo(String userId) {
        this.assignedTo = userId;
        return this;
    }


    public boolean matches(Task task) {
        if (priority != null && task.getPriority() != priority) {
            return false;
        }
        if (dueBefore != null
                && (task.getDueDate() == null || !task.getDueDate().isBefore(dueBefore))) {
            return false;
        }
        if (assignedTo != null && !assignedTo.equals(task.getAssignee())) {
            return false;
        }
        return true;
    }
}
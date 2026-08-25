import java.time.LocalDate;
import java.util.List;

public class User {
    private final String id ; 
    private final String name  ; 
    private final String email ; 
    private final TaskManager taskManager ; 

    public User(String id , String name , String email , TaskManager tm){
        this.id=id ; 
        this.name=name ; 
        this.email=email ; 
        this.taskManager = tm;
    }

    public String getId(){
  return id ; 
    }
      
    
    public String getName(){
        return name ; 

    }
    public String getEmail(){
        return email ; 
    }

    public Task create(String title , String description , LocalDate dueDate , Priority p){
        return taskManager.createTask(this.id,title , description,dueDate,p) ;
    }

    public void assign(String taskId , String TargetUserId){
        taskManager.assignTask(taskId,TargetUserId) ; 
    }

    public void complete(String taskId){
        taskManager.completeTask(taskId) ; 
    }

    public List<Task> getAssignedTasks(){
        return taskManager.searchTasks(new SearchCriteria().withAssignedTo(this.id));

    }

     /** Requirement #5: tasks I've completed, pulled from my full history. */
    public List<Task> getCompletedTasks() {
        return taskManager.history(this.id).stream()
                .filter(t -> t.getStatus() == Status.COMPLETED)
                .toList();
    }
 
    @Override
    public String toString() {
        return name + " <" + email + ">";
    }
    
}

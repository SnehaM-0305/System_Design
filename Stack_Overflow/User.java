import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class User {
    private final String id;
    private final String name;
    private final String email;
    private final AtomicInteger reputation;

    public User(String name, String email) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.reputation = new AtomicInteger(0);
    }

    // Only touches its OWN field -> stays on the entity, not the service
    public void updateReputation(ReputationType type) {
        reputation.addAndGet(type.getPoints());
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public int getReputation() { return reputation.get(); }
}

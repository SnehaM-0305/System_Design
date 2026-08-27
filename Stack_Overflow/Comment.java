import java.util.Date;
import java.util.UUID;

public class Comment {
    private final String id;
    private final String content;
    private final User author;
    private final Date creationDate;

    public Comment(User author, String content) {
        this.id = UUID.randomUUID().toString();
        this.author = author;
        this.content = content;
        this.creationDate = new Date();
    }

    public String getId() { return id; }
    public String getContent() { return content; }
    public User getAuthor() { return author; }
    public Date getCreationDate() { return creationDate; }
}

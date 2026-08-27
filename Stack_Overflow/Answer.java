import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Answer implements Votable, Commentable {
    private final String id;
    private final String content;
    private final User author;
    private final Question question;
    private final Date creationDate;
    private boolean isAccepted;
    private final List<Comment> comments;
    private final Map<String, VoteType> voteRecord;

    public Answer(User author, Question question, String content) {
        this.id = UUID.randomUUID().toString();
        this.author = author;
        this.question = question;
        this.content = content;
        this.creationDate = new Date();
        this.isAccepted = false;
        this.comments = new CopyOnWriteArrayList<>();
        this.voteRecord = new ConcurrentHashMap<>();
    }

    // Only touches its OWN boolean flag -> stays on Answer
    public void markAsAccepted() {
        this.isAccepted = true;
    }

    @Override
    public synchronized void vote(User voter, VoteType type) {
        VoteType previous = voteRecord.get(voter.getId());
        if (previous == type) return;
        voteRecord.put(voter.getId(), type);
    }

    @Override
    public int getVoteCount() {
        int sum = 0;
        for (VoteType v : voteRecord.values()) sum += v.getValue();
        return sum;
    }

    @Override
    public void addComment(Comment comment) {
        comments.add(comment);
    }

    @Override
    public List<Comment> getComments() {
        return Collections.unmodifiableList(comments);
    }

    public String getId() { return id; }
    public String getContent() { return content; }
    public User getAuthor() { return author; }
    public Question getQuestion() { return question; }
    public boolean isAccepted() { return isAccepted; }
}

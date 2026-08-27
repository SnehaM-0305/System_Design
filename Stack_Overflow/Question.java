import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Question implements Votable, Commentable {
    private final String id;
    private final String title;
    private final String content;
    private final User author;
    private final Date creationDate;
    private final List<Tag> tags;
    private final List<Answer> answers;
    private final List<Comment> comments;
    // per-user vote record prevents duplicate voting
    private final Map<String, VoteType> voteRecord;
    private Answer acceptedAnswer;

    public Question(User author, String title, String content, List<Tag> tags) {
        this.id = UUID.randomUUID().toString();
        this.author = author;
        this.title = title;
        this.content = content;
        this.tags = new ArrayList<>(tags);
        this.creationDate = new Date();
        this.answers = new CopyOnWriteArrayList<>();
        this.comments = new CopyOnWriteArrayList<>();
        this.voteRecord = new ConcurrentHashMap<>();
    }

    // Only touches ITS OWN answers list -> stays on Question, not the service
    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public synchronized void acceptAnswer(Answer answer) {
        this.acceptedAnswer = answer;
        answer.markAsAccepted();
    }

    @Override
    public synchronized void vote(User voter, VoteType type) {
        VoteType previous = voteRecord.get(voter.getId());
        if (previous == type) return; // no-op: same vote repeated
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
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public User getAuthor() { return author; }
    public List<Tag> getTags() { return tags; }
    public List<Answer> getAnswers() { return answers; }
    public Answer getAcceptedAnswer() { return acceptedAnswer; }
}

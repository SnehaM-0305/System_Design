import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class StackOverflowService {
    private static volatile StackOverflowService instance;

    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final Map<String, Question> questions = new ConcurrentHashMap<>();
    private final Map<String, Answer> answers = new ConcurrentHashMap<>();
    private final Map<String, Tag> tags = new ConcurrentHashMap<>();

    private final SearchService searchService = new SearchService();
    private final ReputationManager reputationManager = new ReputationManager();

    private StackOverflowService() {}

    public static StackOverflowService getInstance() {
        if (instance == null) {
            synchronized (StackOverflowService.class) {
                if (instance == null) {
                    instance = new StackOverflowService();
                }
            }
        }
        return instance;
    }

    public User createUser(String name, String email) {
        User user = new User(name, email);
        users.put(user.getId(), user);
        return user;
    }

    public Question postQuestion(String userId, String title, String content, List<String> tagNames) {
        User author = users.get(userId);
        if (author == null) throw new IllegalArgumentException("User not found");

        List<Tag> resolvedTags = new ArrayList<>();
        for (String name : tagNames) {
            Tag tag = tags.computeIfAbsent(name.toLowerCase(), k -> new Tag(name));
            resolvedTags.add(tag);
        }

        Question question = new Question(author, title, content, resolvedTags);
        questions.put(question.getId(), question);
        searchService.indexQuestion(question);
        return question;
    }

    public Answer postAnswer(String userId, String questionId, String content) {
        User author = users.get(userId);
        Question question = questions.get(questionId);
        if (author == null || question == null) throw new IllegalArgumentException("Invalid user or question");

        Answer answer = new Answer(author, question, content);
        answers.put(answer.getId(), answer);
        question.addAnswer(answer); // delegate the "own list" update to Question
        return answer;
    }

    public Comment addComment(String userId, Commentable target, String content) {
        User author = users.get(userId);
        if (author == null) throw new IllegalArgumentException("User not found");
        Comment comment = new Comment(author, content);
        target.addComment(comment); // polymorphic: works for Question or Answer
        return comment;
    }

    public void vote(String userId, Votable target, VoteType type) {
        User voter = users.get(userId);
        if (voter == null) throw new IllegalArgumentException("User not found");
        target.vote(voter, type);

        // decide which reputation rule applies based on target type
        if (target instanceof Question) {
            ReputationType repType = (type == VoteType.UPVOTE)
                    ? ReputationType.QUESTION_UPVOTE : ReputationType.QUESTION_DOWNVOTE;
            reputationManager.apply(((Question) target).getAuthor(), repType);
        } else if (target instanceof Answer) {
            ReputationType repType = (type == VoteType.UPVOTE)
                    ? ReputationType.ANSWER_UPVOTE : ReputationType.ANSWER_DOWNVOTE;
            reputationManager.apply(((Answer) target).getAuthor(), repType);
        }
    }

    public void acceptAnswer(String questionId, String answerId) {
        Question question = questions.get(questionId);
        Answer answer = answers.get(answerId);
        if (question == null || answer == null) throw new IllegalArgumentException("Invalid question or answer");

        question.acceptAnswer(answer);
        reputationManager.apply(answer.getAuthor(), ReputationType.ANSWER_ACCEPTED);
    }

    public List<Question> searchQuestions(String query) {
        return searchService.searchByKeyword(query);
    }

    public List<Question> getQuestionsByTag(String tagName) {
        return searchService.searchByTag(tagName);
    }

    public List<Question> getQuestionsByUser(String userId) {
        return searchService.searchByUser(userId);
    }
}

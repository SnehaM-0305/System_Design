import java.util.List;

// This is the entry point. Compile all files, then run this one.
//   javac *.java
//   java Demo
public class Demo {
    public static void main(String[] args) {
        StackOverflowService service = StackOverflowService.getInstance();

        User alice = service.createUser("Alice", "alice@example.com");
        User bob = service.createUser("Bob", "bob@example.com");

        Question q = service.postQuestion(
                alice.getId(),
                "How does ConcurrentHashMap work?",
                "I want to understand internal bucket locking.",
                List.of("java", "concurrency")
        );

        Answer a = service.postAnswer(bob.getId(), q.getId(),
                "It uses segment-level locking internally for thread safety.");

        service.addComment(alice.getId(), q, "Great question, following this!");
        service.addComment(bob.getId(), a, "Thanks, happy to elaborate more.");

        service.vote(alice.getId(), a, VoteType.UPVOTE);
        service.acceptAnswer(q.getId(), a.getId());

        System.out.println("Bob's reputation: " + bob.getReputation()); // 10 (upvote) + 15 (accepted) = 25
        System.out.println("Answer vote count: " + a.getVoteCount());   // 1
        System.out.println("Is answer accepted: " + a.isAccepted());    // true

        List<Question> results = service.searchQuestions("ConcurrentHashMap");
        System.out.println("Search results: " + results.size());       // 1

        List<Question> byTag = service.getQuestionsByTag("java");
        System.out.println("Questions tagged 'java': " + byTag.size()); // 1
    }
}

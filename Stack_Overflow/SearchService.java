import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class SearchService {
    private final Map<String, List<Question>> tagIndex = new ConcurrentHashMap<>();
    private final Map<String, List<Question>> userIndex = new ConcurrentHashMap<>();
    private final List<Question> allQuestions = new CopyOnWriteArrayList<>();

    public void indexQuestion(Question q) {
        allQuestions.add(q);
        for (Tag tag : q.getTags()) {
            tagIndex.computeIfAbsent(tag.getName().toLowerCase(), k -> new CopyOnWriteArrayList<>()).add(q);
        }
        userIndex.computeIfAbsent(q.getAuthor().getId(), k -> new CopyOnWriteArrayList<>()).add(q);
    }

    public List<Question> searchByKeyword(String query) {
        String lower = query.toLowerCase();
        List<Question> result = new ArrayList<>();
        for (Question q : allQuestions) {
            if (q.getTitle().toLowerCase().contains(lower) || q.getContent().toLowerCase().contains(lower)) {
                result.add(q);
            }
        }
        return result;
    }

    public List<Question> searchByTag(String tagName) {
        return tagIndex.getOrDefault(tagName.toLowerCase(), Collections.emptyList());
    }

    public List<Question> searchByUser(String userId) {
        return userIndex.getOrDefault(userId, Collections.emptyList());
    }
}

public class ReputationManager {
    // single place that maps "what happened" -> "how many points"
    public void apply(User user, ReputationType type) {
        user.updateReputation(type);
    }
}

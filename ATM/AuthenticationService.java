public class AuthenticationService {

    public boolean validateAccount(Account account) {
        // In a real system: check with the bank's backend/database
        // For our LLD scope: assume the account object being non-null and valid means it's legitimate
        if (account == null) {
            return false;
        }
        System.out.println("Account validated for: " + account);
        return true;
    }
}
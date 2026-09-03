public class TransactionProcessor {

    public void processTransaction(Transaction transaction) {
        // In a real system: send this transaction to the bank's backend for settlement/recording
        transaction.updateStatus(TransactionStatus.SUCCESS);
        System.out.println("Transaction processed: " + transaction.getStatus());
    }
}
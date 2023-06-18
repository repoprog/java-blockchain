package blockchain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 7L;
    private String sender;
    private String recipient;
    private int amount;

    public Transaction(String sender, String recipient, int amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public int getAmount() {
        return amount;
    }


    @Override
   public String toString() {
        return sender + " sent " + amount + " VC to " + recipient;
    }

    public static Transaction generateRandomTransaction() {
//        Transaction transaction1 = new Transaction("user1", "user2", 50);
//        Transaction transaction2 = new Transaction("user3", "user4", 30);
//        Transaction transaction3 = new Transaction("user2", "user1", 20);
//        Transaction transaction4 = new Transaction("user4", "user3", 10);
//
//        List<Transaction> transactions = new ArrayList<>();
//        transactions.add(transaction1);
//        transactions.add(transaction2);
//        transactions.add(transaction3);
//        transactions.add(transaction4);
//        Random random = new Random();
//        int index = random.nextInt(transactions.size());
//        return transactions.get(index);
//    }
//
        Random random = new Random();
        String sender = "user" + random.nextInt(50);
        String recipient = "user" + random.nextInt(50);
        int amount = random.nextInt(100);

        return new Transaction(sender, recipient, amount);
    }
}
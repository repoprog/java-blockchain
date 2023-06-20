package blockchain;

import java.io.Serializable;
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
        Random random = new Random();
        String sender = "user" + random.nextInt(50);
        String recipient = "user" + random.nextInt(50);
        int amount = random.nextInt(100);

        return new Transaction(sender, recipient, amount);
    }
}
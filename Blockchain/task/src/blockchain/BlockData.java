package blockchain;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.List;


public class BlockData implements Serializable {
    private static final long serialVersionUID = 7L;
    private List<Transaction> transactions;
    private int id;
    private  PublicKey publicKey;
    private byte[] signature;
    private transient AsymmetricCryptography cipher;


    public BlockData(AsymmetricCryptography cipher) {
        this(1, List.of(new Transaction("Block", "Someone", 100)), cipher);
    }

    public BlockData(int id,List<Transaction> transactions, AsymmetricCryptography cipher) {
        this.id = id;
        this.transactions = transactions;
        this.cipher = cipher;
        this.publicKey = cipher.getPublicKey();
    }

    public List<Transaction> getTransactions() {

        return transactions;
    }

    public int getId() {
        return id;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void signTransactions(List<Transaction> transactions) {
            signature = cipher.sign(transactions);
    }
}

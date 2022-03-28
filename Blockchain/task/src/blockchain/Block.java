package blockchain;

import java.util.Date;

public class Block {
    private int id;
    private Long timeStamp;
    private String previousHash;
    private String hash;

    public Block(int id, String previousHash) {
        this.id = id;
        this.timeStamp = new Date().getTime();
        this.previousHash = previousHash;
        this.hash = calculateBlockHash();
    }

    public int getId() {
        return id;
    }
    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String calculateBlockHash() {
        String dataToHash = previousHash
                + timeStamp
                + id;
        return StringUtil.applySha256(dataToHash);
    }

    public String toString() {
       return "Block:\n" +
                "Id:" + id + "\n" +
                "Timestamp: " + timeStamp + "\n" +
                "Hash of the previous block: \n" + previousHash + "\n" +
                "Hash of the block: \n" +  hash + "\n";
    }
}

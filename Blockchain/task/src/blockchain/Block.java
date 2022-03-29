package blockchain;

import java.io.Serializable;
import java.util.Date;

public class Block implements Serializable {
    private static final Long serialVersionUID = 7L;
    private final int id;
    private final Long timeStamp;
    private long magicNumber;
    private final String previousHash;
    private String hash;
    private Long genTime;

    public Block(int id, String previousHash) {
        this.id = id;
        this.timeStamp = new Date().getTime();
        this.previousHash = previousHash;
        this.hash = calculateBlockHash();
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setMagicNumber(long magicNumber) {
        this.magicNumber = magicNumber;
    }

    public void setGenTime(Long genTime) {
        this.genTime = genTime;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String calculateBlockHash() {
        String dataToHash = previousHash
                + timeStamp
                + id
                + magicNumber;
        return StringUtil.applySha256(dataToHash);
    }

    public String toString() {
        return "Block:\n" +
                "Id:" + id + "\n" +
                "Timestamp: " + timeStamp + "\n" +
                "Magic number:" + magicNumber + "\n" +
                "Hash of the previous block: \n" + previousHash + "\n" +
                "Hash of the block: \n" + hash + "\n" +
                "Block was generating for " + genTime
                + " seconds\n";
    }
}

package blockchain;

import java.io.Serializable;
import java.util.Date;

public class Block implements Serializable {
    private static final long serialVersionUID = 7L;
    private long minerID;
    private final int id;
    private final long timeStamp;
    private long magicNumber;
    private final String previousHash;
    private String hash;
    private final BlockData blockData;
    private long genTime;
    private String diffNMsg;


    public Block(AsymmetricCryptography cipher) {
        this(1, "0", new BlockData(cipher));
    }

    public Block(int id, String previousHash, BlockData blockData) {
        this.id = id;
        this.timeStamp = new Date().getTime();
        this.previousHash = previousHash;
        this.hash = calculateBlockHash();
        this.blockData = blockData;
    }

    public int getId() {
        return  id;
    }

    public long getGenTime() {
        return genTime;
    }

    public void setMinerID(long minerID) {
        this.minerID = minerID;
    }

    public void setDiffNMsg(String diffNMsg) {
        this.diffNMsg = diffNMsg;
    }

    public String getHash() {
        return hash;
    }
    public void setHash(String hash) {
        this.hash = hash;
    }


    public long getTimeStamp() {
        return timeStamp;
    }

    public void setMagicNumber(long magicNumber) {
        this.magicNumber = magicNumber;
    }

    public void setGenTime(long genTime) {
        this.genTime = genTime;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public BlockData getBlockData() {
      return   blockData ;
    }

    public String calculateBlockHash() {
        String dataToHash = previousHash
                + timeStamp
                + id
                + magicNumber
                + blockData;
        return StringUtil.applySha256(dataToHash);
    }

    public String toString() {
        return "Block:\n" +
                "Created by miner # " + minerID + "\n" +
                "Id: " + id + "\n" +
                "Timestamp: " + timeStamp + "\n" +
                "Magic number: " + magicNumber + "\n" +
                "Hash of the previous block: \n" + previousHash + "\n" +
                "Hash of the block: \n" + hash + "\n" +
                "Block data: \n" + blockData.getMessage() + "\n" +
                "Block was generating for " + genTime
                + " seconds\n" +
                "N " + diffNMsg + "\n";
    }
}

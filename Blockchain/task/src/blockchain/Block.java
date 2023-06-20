package blockchain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Block implements Serializable {
    private static final long serialVersionUID = 7L;
    private long minerID;
    private Transaction rewardTransaction;
    private final int id;
    private final long timeStamp;
    private long magicNumber;
    private final String previousHash;
    private String hash;
    private final BlockData blockData;
    private long genTime;
    private String diffNMsg;

    public Block(int id, String previousHash, BlockData blockData) {
        this.id = id;
        this.timeStamp = new Date().getTime();
        this.previousHash = previousHash;
        this.hash = calculateBlockHash();
        this.blockData = blockData;
    }

    public int getId() {
        return id;
    }

    public long getGenTime() {
        return genTime;
    }


    public void setMinerID(long minerID) {
        this.minerID = minerID;
    }

    public long getMinerID() {
        return minerID;
    }

    public void setRewardTransaction(Transaction reward) {
        this.rewardTransaction = reward;
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
        return blockData;
    }

    public String calculateBlockHash() {
        String dataToHash = rewardTransaction
                + previousHash
                + timeStamp
                + id
                + magicNumber
                + blockData;
        return StringUtil.applySha256(dataToHash);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Block:\n");
        sb.append("Created by: miner").append(minerID).append("\n");
        sb.append(rewardTransaction.getRecipient()).append(" gets ").append(rewardTransaction.getAmount()).append(" VC\n");
        sb.append("Id: ").append(id).append("\n");
        sb.append("Timestamp: ").append(timeStamp).append("\n");
        sb.append("Magic number: ").append(magicNumber).append("\n");
        sb.append("Hash of the previous block: \n").append(previousHash).append("\n");
        sb.append("Hash of the block: \n").append(hash).append("\n");
        sb.append("Block data: \n");
        List<Transaction> transactions = blockData.getTransactions();
        for (Transaction transaction : transactions) {
            sb.append(transaction.toString()).append("\n");
        }
        sb.append("Block was generating for ").append(genTime).append(" seconds\n");
        sb.append("N ").append(diffNMsg).append("\n");
        return sb.toString();
    }
}

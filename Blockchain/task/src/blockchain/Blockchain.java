package blockchain;

import java.io.Serializable;
import java.util.*;

public class Blockchain implements Serializable {

    private static final long serialVersionUID = 7L;
    private final List<Block> blocks = new ArrayList<>();
    private String proofNumber;

    public void setProofNumber(int numOfZeros) {
        proofNumber = // numOfZeros == 0 ? "0" :
                new String(new char[numOfZeros]).replace("\0", "0");
    }

    public void createNewBlock() {
        Block newBlock = blocks.isEmpty() ? new Block(1, "0")
                : new Block(blocks.size() + 1, blocks.get(blocks.size() - 1).getHash());

        while (!newBlock.getHash().startsWith(proofNumber)) {
            newBlock.setMagicNumber(generateMagicNumber());
            newBlock.setHash(newBlock.calculateBlockHash());
        }
        Long generationTime = (new Date().getTime() - newBlock.getTimeStamp()) / 1000;
        newBlock.setGenTime(generationTime);
        blocks.add(newBlock);
    }

    public Long generateMagicNumber() {
        return (long) (Math.random() * 100000000000L);
    }

    public boolean isChainValid() {

        for (int i = 0; i < blocks.size(); i++) {
            String previousHash = i == 0 ? "0" : blocks.get(i - 1).getHash();
            if (!blocks.get(i).getHash().equals(blocks.get(i).calculateBlockHash())
                    && previousHash.equals(blocks.get(i).getPreviousHash())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Block b : blocks) {
            builder.append(b).append("\n");
        }
        return builder.toString();
    }
}

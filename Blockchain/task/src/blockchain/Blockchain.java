package blockchain;

import java.io.Serializable;
import java.util.*;

public class Blockchain implements Serializable {

    private static final long serialVersionUID = 7L;
    private final List<Block> blocks = new ArrayList<>();

    public List<Block> getBlocks() {
        return blocks;
    }

    public Block getBlock(int index) {
        return blocks.get(index);
    }

    public void addBlock(Block block) {
        blocks.add(block);
    }

    public int getSize() {
        return blocks.size();
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

package blockchain;

import java.util.*;

public class Blockchain {

    private final List<Block> blocks = new ArrayList<>();

    public void createNewBlock() {
        Block nextBlock = blocks.isEmpty() ? new Block(1, "0")
                : new Block(blocks.size() + 1, blocks.get(blocks.size() - 1).getHash());
        blocks.add(nextBlock);
    }


    public boolean isValid() {

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

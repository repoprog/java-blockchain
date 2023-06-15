package blockchain;

import java.io.Serializable;
import java.util.*;

public class Blockchain implements Serializable {

    private static final long serialVersionUID = 7L;
    private final List<Block> blocks = new ArrayList<>();
    private int dataID;

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

    public int getDataID() {
        return getSize() + 1;
    }

    public boolean isChainValid(AsymmetricCryptography cipher) {

            Block previousBlock = blocks.get(0);

            for (int i = 1; i < blocks.size(); i++) {
                Block currentBlock = blocks.get(i);

                // Verify previous block's hash
                if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                    System.out.println("Invalid blockchain: Hash mismatch for block " + currentBlock.getId());
                    return false;
                }

                // Verify block's hash
                String calculatedHash = currentBlock.calculateBlockHash();
                if (!currentBlock.getHash().equals(calculatedHash)) {
                    System.out.println("Invalid blockchain: Invalid block hash for block " + currentBlock.getId());
                    return false;
                }

                // Verify block data signature
                BlockData blockData = currentBlock.getBlockData();
                boolean isValidSignature = false;
                    isValidSignature = cipher.verifySignature(
                            blockData.getMessage(),
                            blockData.getSignature(),
                            blockData.getPublicKey()
                    );
                if (!isValidSignature) {
                    System.out.println("Invalid blockchain: Invalid signature for block " + currentBlock.getId());
                    return false;
                }

                // Verify block data identifier
                if (blockData.getId() <= previousBlock.getBlockData().getId()) {
                    System.out.println("Invalid blockchain: Invalid block data identifier for block " + currentBlock.getId());
                    return false;
                }

                previousBlock = currentBlock;
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

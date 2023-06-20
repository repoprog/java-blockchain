package blockchain;


import java.io.Serializable;
import java.util.*;

public class Blockchain implements Serializable {

    private static final long serialVersionUID = 7L;
    private final List<Block> blocks = new ArrayList<>();
    Map<String, Integer> accountBalances = new HashMap<>();
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

    public boolean isChainValid(AsymmetricCryptography cipher, boolean verifyTransaction) {

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
                    blockData.getTransactions(),
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

            // Verify transactions in the block
            if (verifyTransaction && !validateBlockTransactions(currentBlock.getBlockData().getTransactions())) {
                System.out.println("Invalid blockchain: Invalid transactions in block " + currentBlock.getId());
                return false;
            }

            previousBlock = currentBlock;
        }
        return true;
    }

    private boolean validateBlockTransactions(List<Transaction> transactions) {
        // Fill accountBalances with initial balances

//        System.out.println("Wylosowane transakcje " + transactions);
//        int count = 1;
        for (Transaction transaction : transactions) {
            String sender = transaction.getSender();
            String recipient = transaction.getRecipient();
            int amount = transaction.getAmount();

            // Update sender's balance, For testing purpose give all new users 100 VC
            accountBalances.put(sender, accountBalances.getOrDefault(sender, 100) - amount);

            // Update recipient's balance
            accountBalances.put(recipient, accountBalances.getOrDefault(recipient, 100) + amount);
//            accountBalances.forEach((k, v) -> System.out.println(k +" " +v));
//            System.out.println("Koniec " + count++ + " transakcji w blou");
        }

        for (Transaction transaction : transactions) {
            // Validate sender and recipient
            String sender = transaction.getSender();
            String recipient = transaction.getRecipient();

            if (!accountBalances.containsKey(sender) || !accountBalances.containsKey(recipient)) {
                System.out.println("Invalid transaction: Invalid sender or recipient");
                return false;
            }

            // Validate sender's balance
            int amount = transaction.getAmount();

            // Check if sender has sufficient balance using streams and map computation
            int senderBalance = accountBalances.compute(sender, (key, balance) -> {
                if (balance >= amount) {
                    return balance - amount;
                } else {
                    System.out.println("Invalid transaction: Insufficient balance for sender " + sender);
                    return balance;
                }
            });

            // Validate transaction uniqueness (no double-spending)
//            if (isTransactionDuplicate(transaction)) {
//                System.out.println("Invalid transaction: Duplicate transaction");
//                return false;
//            }

            // Update recipient's balance
            accountBalances.compute(recipient, (key, balance) -> balance + amount);
        }
        return true;
    }
//    private boolean isTransactionDuplicate(Transaction transaction) {
//        return blocks.stream()
//                .flatMap(block -> block.getBlockData().getTransactions().stream())
//                .anyMatch(blockTransaction -> blockTransaction.equals(transaction));
//    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Block b : blocks) {
            builder.append(b).append("\n");
        }
        return builder.toString();
    }
}

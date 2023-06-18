package blockchain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

public class BlocksMine {
    private final int NUM_THREADS = 4;
    private final ExecutorService msgExecutor = Executors.newFixedThreadPool(NUM_THREADS);
    private final static Object LOCK = new Object();
    private String proofNumber;
    private BlockData nextBlockData;

    public void setProofNumber(int numOfZeros) {
        proofNumber = // numOfZeros == 0 ? "0" :
                new String(new char[numOfZeros]).replace("\0", "0");
    }

    public void createNewBlock(Blockchain chain, AsymmetricCryptography cipher) {
        //Create new List of futures with creation of every block
        List<Future<Transaction>> futuresTrans = generateRandomTrans();
        synchronized (LOCK) {

            // create new block and add random asynchronous generated trans to each block (after first)
            Block newBlock = chain.getBlocks().isEmpty() ? new Block(cipher) : new Block(chain.getSize() + 1,
                    chain.getBlock(chain.getSize() - 1).getHash(), nextBlockData);
//            System.out.println("made block" + newBlock.getId() + " by " + Thread.currentThread().getName());
            newBlock.setMinerID(Thread.currentThread().getId());
            rewardMiner(newBlock);
            generateMagicNumber(newBlock);
            newBlock.setGenTime(getGenerationTime(newBlock));
            adjustMiningDifficulty(newBlock);
            chain.addBlock(newBlock);
//            chain.isChainValid(cipher);
            generateBlockData(chain, cipher, futuresTrans);
        }
    }

    public void rewardMiner(Block newBLock) {
        int BLOCK_REWARD = 100;
        Transaction reward = new Transaction("Blockchain", "miner"+ newBLock.getMinerID(), BLOCK_REWARD);
        newBLock.setRewardTransaction(reward);
    }

    public static long getGenerationTime(Block newBlock) {
        // for stage 6 changed to milliseconds seconds = 1000
        return (new Date().getTime() - newBlock.getTimeStamp());
    }

    public List<Future<Transaction>> generateRandomTrans() {
        List<Callable<Transaction>> callables = new ArrayList<>();
        for (int i = 0; i < NUM_THREADS; i++) {
            callables.add(Transaction::generateRandomTransaction);
        }
        try {
            return msgExecutor.invokeAll(callables);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

//    public void generateMagicNumber(Block newBlock) {
//        long magicNumber = 0;
//        String hash;
//
//        do {
//            newBlock.setMagicNumber(magicNumber);
//            hash = newBlock.calculateBlockHash();
//            magicNumber++;
//        } while (!hash.startsWith(proofNumber));
//
//        newBlock.setHash(hash);
//    }


    public void generateMagicNumber(Block newBlock) {
        while (!newBlock.getHash().startsWith(proofNumber)) {
            newBlock.setMagicNumber((long) (Math.random() * 100000000000L));
            newBlock.setHash(newBlock.calculateBlockHash());
        }
    }

    public synchronized void adjustMiningDifficulty(Block block) {
        int difficulty;
        if (block.getGenTime() < 15) {
            setProofNumber(proofNumber.length() + 1);
            difficulty = proofNumber.length();
            block.setDiffNMsg("was increased to " + difficulty);
        } else if (block.getGenTime() >= 15 && block.getGenTime() <= 60) {
            block.setDiffNMsg("stays the same");
        } else {
            setProofNumber(proofNumber.length() - 1);
            block.setDiffNMsg("was decreased by 1");
        }
    }

    public void generateBlockData(Blockchain chain, AsymmetricCryptography cipher, List<Future<Transaction>> futuresTran) {
        List<Transaction> transactions = syncTransactions(futuresTran);
        BlockData data = new BlockData(chain.getDataID(), transactions, cipher);
        data.signTransactions(transactions);
        nextBlockData = data;
    }

    public List<Transaction> syncTransactions(List<Future<Transaction>> futuresTrans) {
        List<Transaction> transactions = new ArrayList<>();
        for (Future<Transaction> f : futuresTrans) {
            try { // current thread (from block executor) wait for messages
                transactions.add(f.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return transactions;
    }
}


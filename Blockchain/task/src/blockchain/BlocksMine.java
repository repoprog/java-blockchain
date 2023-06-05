package blockchain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

public class BlocksMine {
    private List<Future<String>> future;
    private final int NUM_THREADS = 4;
    private  ExecutorService msgExecutor = Executors.newFixedThreadPool(NUM_THREADS);
    private final static Object LOCK = new Object();
    private String proofNumber;
    private static String blockData;

    public void setProofNumber(int numOfZeros) {
        proofNumber = // numOfZeros == 0 ? "0" :
                new String(new char[numOfZeros]).replace("\0", "0");
    }

    public void createNewBlock(Blockchain chain) {
        generateRandomMsg();
        synchronized (LOCK) {
            // create new block and add random asynchronous generated msg to each block (after first)
            Block newBlock = chain.getBlocks().isEmpty() ? new Block() : new Block(chain.getSize() + 1,
                    chain.getBlock(chain.getSize() - 1).getHash(), blockData);
            clearDataForNextBlock();
            generateMagicNumber(newBlock);
            newBlock.setGenTime(getGenerationTime(newBlock));
            newBlock.setMinerID(Thread.currentThread().getId());
            adjustMiningDifficulty(newBlock);
            chain.addBlock(newBlock);
            syncDataForNextBlock();
        }
    }

    public static void clearDataForNextBlock() {
        blockData = null;
    }

    public static long getGenerationTime(Block newBlock) {
        return (new Date().getTime() - newBlock.getTimeStamp()) / 1000;
    }

    public void generateRandomMsg() {
        List<Callable<String>> callables = new ArrayList<>();
        for (int i = 0; i < NUM_THREADS; i++) {
            callables.add(() -> new RandomDataGenerator().generate());
        }
        try {
            future = msgExecutor.invokeAll(callables);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void generateMagicNumber(Block newBlock) {
        while (!newBlock.getHash().startsWith(proofNumber)) {
            newBlock.setMagicNumber((long) (Math.random() * 100000000000L));
            newBlock.setHash(newBlock.calculateBlockHash());
        }
    }

    public synchronized void adjustMiningDifficulty(Block block) {
        int N;
        if (block.getGenTime() < 15) {
            setProofNumber(proofNumber.length() + 1);
            N = proofNumber.length();
            block.setDiffNMsg("was increased to " + N);
        } else if (block.getGenTime() >= 15 && block.getGenTime() <= 60) {
            block.setDiffNMsg("stays the same");
        } else {
            setProofNumber(proofNumber.length() - 1);
            block.setDiffNMsg("was decreased by 1");
        }
    }

    public void syncDataForNextBlock() {
        StringBuilder sb = new StringBuilder();

        for (Future<String> f : future) {
            try { // current thread (from block executor) wait for messages
                sb.append(f.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            blockData = sb.toString();
        }
    }
}


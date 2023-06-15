package blockchain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

public class BlocksMine {
    private List<Future<String>> future;
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
        List<Future<String>> futuresMsg = generateRandomMsg();
        synchronized (LOCK) {

            // create new block and add random asynchronous generated msg to each block (after first)
            Block newBlock = chain.getBlocks().isEmpty() ? new Block(cipher) : new Block(chain.getSize() + 1,
                    chain.getBlock(chain.getSize() - 1).getHash(), nextBlockData);
//            System.out.println("made block" + newBlock.getId() + " by " + Thread.currentThread().getName());

            generateMagicNumber(newBlock);
            newBlock.setGenTime(getGenerationTime(newBlock));
            newBlock.setMinerID(Thread.currentThread().getId());
            adjustMiningDifficulty(newBlock);
            chain.addBlock(newBlock);
            chain.isChainValid(cipher);
            generateBlockData(chain, cipher, futuresMsg);
        }
    }

    public static long getGenerationTime(Block newBlock) {
        return (new Date().getTime() - newBlock.getTimeStamp()) / 1000;
    }

    public List<Future<String>> generateRandomMsg() {
        List<Callable<String>> callables = new ArrayList<>();
        for (int i = 0; i < NUM_THREADS; i++) {
            callables.add(() -> new RandomMsgGenerator().generate());
        }
        try {
            return msgExecutor.invokeAll(callables);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

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

    public void generateBlockData(Blockchain chain, AsymmetricCryptography cipher, List<Future<String>> futureMsg) {
        List<String> message = synchronizeMsg(futureMsg);
        BlockData data = new BlockData(chain.getDataID(), message, cipher);
        data.signMessage(message);
        nextBlockData = data;
//        System.out.println("Generate data method " + nextBlockData.getId() + " by " + Thread.currentThread().getName());
    }


    public List<String> synchronizeMsg(List<Future<String>> futureMsg) {
        List<String> message = new ArrayList<>();
        for (Future<String> f : futureMsg) {
            try { // current thread (from block executor) wait for messages
                message.add(f.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return message;
    }
}


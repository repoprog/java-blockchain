package blockchain;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        final int NUMBER_OF_BLOCKS = 5;

        File file = new File("blockchain.txt");
        // delete blockchain.txt from project first
        Blockchain blockchain;
        if (file.exists()) {
            // for this stage file content is not needed and makes impossible
            //  to pass tests normally chainFromFile = blockchain
           Blockchain chainFromFile = loadFromFile(file);
            chainFromFile.isChainValid();
        }
        blockchain = new Blockchain();
        BlocksMine mine = new BlocksMine();
        mine.setProofNumber(0);

        ExecutorService blockExecutor = Executors.newFixedThreadPool(4);
        for (int i = 0; i < NUMBER_OF_BLOCKS; i++) {
            blockExecutor.submit(() -> mine.createNewBlock(blockchain));
            blockchain.isChainValid();
        }
        blockExecutor.shutdown();
        saveToFile(blockchain, file);
        System.out.println(blockchain);
    }

    public static void saveToFile(Blockchain blockchain, File file) {
        SerializationUtils.serialize(blockchain, file);
    }

    public static Blockchain loadFromFile(File file) {
        return SerializationUtils.deserialize(file);
    }

}

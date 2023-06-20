package blockchain;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws Exception {
        final int NUMBER_OF_BLOCKS = 15;

        File file = new File("blockchain.txt");
        AsymmetricCryptography cipher = generateKeys();
        // delete blockchain.txt from project first
        Blockchain blockchain;
//        if (file.exists()) {
//            // for this stage file content is not needed and makes impossible
//            //  to pass tests normally chainFromFile = blockchain
//            Blockchain chainFromFile = loadBlockchainFromFile(file);
//            if(chainFromFile != null) {
//                chainFromFile.isChainValid(cipher);
//            }
//        }
        blockchain = new Blockchain();
        BlocksMine mine = new BlocksMine();
        mine.setProofNumber(0);

        ExecutorService blockExecutor = Executors.newFixedThreadPool(4);
        for (int i = 0; i < NUMBER_OF_BLOCKS; i++) {
            blockExecutor.submit(() -> mine.createNewBlock(blockchain, cipher));
        }

        blockExecutor.shutdown();
        while (!blockExecutor.isTerminated()) {
            if (blockExecutor.awaitTermination(1, TimeUnit.SECONDS)) {
                break;  // All tasks have completed, exit the loop
            }
        }
        //Main thread waits for blockchain creation
        blockchain.isChainValid(cipher, false);
        saveBlockchainToFile(blockchain, file);
        System.out.println(blockchain);
    }

    public static void saveBlockchainToFile(Blockchain blockchain, File file) {
        SerializationUtils.serialize(blockchain, file);
    }

    public static Blockchain loadBlockchainFromFile(File file) {
        return SerializationUtils.deserialize(file);
    }

    public static AsymmetricCryptography generateKeys() {
        AsymmetricCryptography cipher = null;
        try {
            cipher = new AsymmetricCryptography(1024);
            cipher.createKeys();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            System.err.println(e.getMessage());
        }
        return cipher;
    }

}

package blockchain;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java                                                                                                                                                                                  .util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        File file = new File("blockchain.txt");

        if (file.exists()) {
            Blockchain blockchain = new Blockchain();
            blockchain = loadFromFile(blockchain, file);
            blockchain.isChainValid();
        }
        BlocksMine mine = new BlocksMine();
        mine.setProofNumber(0);

        Blockchain blockchain = new Blockchain();
        ExecutorService executor = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 5; i++) {
            executor.submit(() -> mine.createNewBlock(blockchain));
            saveToFile(blockchain, file);
        }
        executor.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println(blockchain);
    }

    public static void saveToFile(Blockchain blockchain, File file) {
        SerializationUtils.serialize(blockchain, file);
    }

    public static Blockchain loadFromFile(Blockchain blockchain, File file) {
        blockchain = SerializationUtils.deserialize(blockchain, file);
        return blockchain;
    }
}

package blockchain;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        File file = new File("blockchain.txt");

        if (file.exists()) {
            Blockchain blockchain = new Blockchain();
            blockchain = loadFromFile(blockchain, file);
            blockchain.isChainValid();
        }
        Blockchain blockchain = new Blockchain();
        System.out.println("Enter how many zeros the hash must start with:");
        blockchain.setProofNumber(scanner.nextInt());

        for (int i = 0; i < 5; i++) {
            blockchain.createNewBlock();
            saveToFile(blockchain, file);
        }
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

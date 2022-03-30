package blockchain;

import java.io.*;

public class SerializationUtils {

    public static void serialize(Blockchain blockchain, File file) {

        try (ObjectOutputStream out = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(file)))) {
            out.writeObject(blockchain);
        } catch (IOException e) {
            System.out.println("Filed write blockChain into a file " + e);
        }
    }

    public static Blockchain deserialize(Blockchain blockchain, File file) {
        try (ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(file)))) {
            blockchain = (Blockchain) in.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("File not found " + e);
        } catch (IOException e) {
            System.out.println("Failed to read object from file " + e);
        } catch (ClassNotFoundException e) {
            System.out.println("Unknown serialised type " + e);
        }
        return blockchain;
    }
}

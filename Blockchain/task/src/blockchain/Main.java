package blockchain;

public class Main {
    public static void main(String[] args)  {
        Blockchain blockchain = new Blockchain();
        for (int i = 0; i < 5; i++) {
            blockchain.createNewBlock();
        }
        if (blockchain.isValid()) {
            System.out.println(blockchain);
        }
    }
}

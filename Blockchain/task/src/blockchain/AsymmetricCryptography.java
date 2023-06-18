package blockchain;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.*;
import java.util.List;


public class AsymmetricCryptography {
    private KeyPairGenerator keyGen;
    private KeyPair pair;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public AsymmetricCryptography(int keyLength) throws NoSuchAlgorithmException, NoSuchProviderException {
        this.keyGen = KeyPairGenerator.getInstance("RSA");
        this.keyGen.initialize(keyLength);
    }

    public void createKeys() {
        this.pair = this.keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public byte[] sign(List<Transaction> transactions) {
        try {
            Signature rsa = Signature.getInstance("SHA1withRSA");
            rsa.initSign(privateKey);
            byte[] transactionBytes = getTransactionsAsBytes(transactions);
            rsa.update(transactionBytes);
            return rsa.sign();
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // make private
    public boolean verifySignature(List<Transaction> transactions, byte[] signature, PublicKey pubKeyFromBlock) {
        try {
            Signature sig = Signature.getInstance("SHA1withRSA");
            sig.initVerify(pubKeyFromBlock);
            byte[] transactionBytes = getTransactionsAsBytes(transactions);
            sig.update(transactionBytes);
            return sig.verify(signature);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Algorithm not available: " + e.getMessage());
        } catch (InvalidKeyException e) {
            System.err.println("Invalid public key: " + e.getMessage());
        } catch (SignatureException e) {
            System.err.println("Error during signature verification: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public byte[] getTransactionsAsBytes(List<Transaction> transactions) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(transactions);
        oos.flush();
        return baos.toByteArray();
    }
}

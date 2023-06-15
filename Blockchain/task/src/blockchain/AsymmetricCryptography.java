package blockchain;

import java.io.Serializable;
import java.security.*;


public class AsymmetricCryptography  {
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

    public byte[] sign(String data) {
        try {
            Signature rsa = Signature.getInstance("SHA1withRSA");
            rsa.initSign(privateKey);
            rsa.update(data.getBytes());
            return rsa.sign();
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

     // make private
    public boolean verifySignature(byte[] message, byte[] signature, PublicKey pubKeyFromBlock) throws Exception {
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initVerify(pubKeyFromBlock);
        sig.update(message);

        return sig.verify(signature);
    }
}

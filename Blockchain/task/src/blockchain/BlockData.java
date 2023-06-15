package blockchain;

import java.io.Serializable;
import java.security.PublicKey;


public class BlockData implements Serializable {
    private static final long serialVersionUID = 7L;
    private String message;
    private int id;
    private  PublicKey publicKey;
    private byte[] signature;
    private transient AsymmetricCryptography cipher;


    public BlockData(AsymmetricCryptography cipher) {
        this(1, "no message", cipher);
    }

    public BlockData(int id,String message, AsymmetricCryptography cipher) {
        this.id = id;
        this.message = message;
        this.cipher = cipher;
        this.publicKey = cipher.getPublicKey();
    }

    public String getMessage() {

        return message;
    }

    public int getId() {
        return id;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void signMessage(String msg) {
            signature = cipher.sign(msg);
    }

}

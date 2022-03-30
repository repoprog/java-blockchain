package blockchain;

import java.util.Date;

public class BlocksMine {

   private final static Object LOCK = new Object();
    private String proofNumber;

    public void setProofNumber(int numOfZeros) {
        proofNumber = // numOfZeros == 0 ? "0" :
                new String(new char[numOfZeros]).replace("\0", "0");
    }

    public void createNewBlock(Blockchain chain) {
        synchronized (LOCK) {
            Block newBlock = chain.getBlocks().isEmpty() ? new Block(1, "0")
                    : new Block(chain.getSize() + 1, chain.getBlock(chain.getSize() - 1).getHash());

            while (!newBlock.getHash().startsWith(proofNumber)) {
                newBlock.setMagicNumber(generateMagicNumber());
                newBlock.setHash(newBlock.calculateBlockHash());
            }
            Long generationTime = (new Date().getTime() - newBlock.getTimeStamp()) / 1000;
            newBlock.setGenTime(generationTime);
            newBlock.setMinerId(Thread.currentThread().getId());
            chain.addBlock(newBlock);
            adjustMiningDifficulty(newBlock);
        }
    }

    public void adjustMiningDifficulty(Block block) {
        int N;
        if (block.getGenTime() < 15) {
            setProofNumber(proofNumber.length() + 1);
            N = proofNumber.length();
            block.setDiffNMsg("was increased to " + N);
        } else if (block.getGenTime() >= 15 && block.getGenTime() <= 60) {
            block.setDiffNMsg("stays the same");
        } else {
            setProofNumber(proofNumber.length() - 1);
            block.setDiffNMsg("was decreased by 1");
        }
    }

    public Long generateMagicNumber() {
        return (long) (Math.random() * 100000000000L);
    }

}

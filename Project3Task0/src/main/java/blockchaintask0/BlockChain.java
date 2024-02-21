/**
 * Name : Ruth Peter
 * Andrew id : rpeter
 */

package blockchaintask0;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BlockChain {

    private List<Block> blockList;

    private String chainHash;

    private int hashesPerSecond;

    Gson gson = new Gson();

    public BlockChain() {
        this.blockList = new ArrayList<>();
        this.chainHash = "";
        this.hashesPerSecond = 0;

    }

    /**
     * @return the chain hash
     */
    public String getChainHash() {
        return chainHash;
    }

    /**
     * @return the current system time
     */
    public Timestamp getTime() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * a reference to the most recently added Block
     *
     * @return
     */
    public Block getLatestBlock() {
        return blockList.get(blockList.size() - 1);
    }

    public int getChainSize() {
        return blockList.size();
    }

    /**
     * This method computes exactly 2 million hashes and times how long that process takes
     */
    public void computeHashesPerSecond() throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        int i = 0;
        Timestamp startTime = getTime();
        while (i < 2000000) {
            byte[] encodedHash = md.digest(
                    "00000000".getBytes(StandardCharsets.UTF_8));
            i += 1;
        }
        Timestamp endTime = getTime();
        this.hashesPerSecond = 2000000 / (endTime.compareTo(startTime));

    }

    /**
     * get hashes per second
     *
     * @return the instance variable approximating the number of hashes per second
     */
    public int getHashesPerSecond() {
        return this.hashesPerSecond;
    }

    /**
     * Method to add new block to blockChain
     * block's previous hash must hold the hash of the most recently added block
     * the new block becomes the most recently added block on the BlockChain
     * The SHA256 hash of every block must exhibit proof of work
     *
     * @param newBlock
     * @throws NoSuchAlgorithmException
     */
    public void addBlock(Block newBlock) throws NoSuchAlgorithmException {
        if (this.blockList.size() > 0)
            newBlock.setPreviousHash(getChainHash());
        else
            newBlock.setPreviousHash("");
        blockList.add(newBlock);
        this.chainHash = newBlock.proofOfWork();
    }


    /**
     * For difficulty 2
     * Time to add block = 5ms
     * Verify blockchain = 1ms
     * Repair chain = 5ms
     *
     * For difficulty 3
     * Time to add block = 24ms
     * Verify blockchain = 1ms
     * Repair chain = 1ms
     *
     * For difficulty 4
     * Time to add block = 51ms
     * Verify blockchain = 1ms
     * Repair chain = 6ms
     *
     *
     * As the difficulty of the block increases, the time taken to add block also increases exponentially
     * Verification of the block still approximately takes the same time
     * Repairing the block chain also approximately takes the same time
     *
     * The maximum time is taken during adding the block as it computes the proof of work for the difficulty given ,
     * and keeps increasing nonce till an appropriate hash is found , the higher the difficulty the more computations are
     * required
     *
     *
     */
    public static void main(String args[]) throws NoSuchAlgorithmException {

        BlockChain chain = new BlockChain();

        //adding genesis block to chain

        Block genesis = new Block(0, chain.getTime(), "Genesis", 2);
        //first block , previous hash is blank
        genesis.setPreviousHash("");
        //calculate pow
        genesis.proofOfWork();

        chain.computeHashesPerSecond();

        //add 1st block to the main chain
        chain.addBlock(genesis);


        Scanner sc = new Scanner(System.in);
        int option = 0;

        while (true) {
            System.out.println("0. View basic blockchain status.\n" +
                    "1. Add a transaction to the blockchain.\n" +
                    "2. Verify the blockchain.\n" +
                    "3. View the blockchain.\n" +
                    "4. Corrupt the chain.\n" +
                    "5. Hide the corruption by repairing the chain.\n" +
                    "6. Exit");

            option = sc.nextInt();

            switch (option) {
                //Status of blockchain
                case 0: {
                    System.out.println("Current size of chain:  " + chain.getChainSize());
                    System.out.println("Difficulty of most recent block: " + chain.getLatestBlock().getDifficulty());
                    System.out.println("Total difficulty for all blocks:  " + chain.getTotalDifficulty());
                    System.out.println("Approximate hashes per second on this machine : " + chain.getHashesPerSecond());
                    System.out.println("Expected total hashes required for the whole chain: " + chain.getTotalExpectedHashes());
                    System.out.println("Nonce for most recent block:  " + chain.getLatestBlock().getNonce());
                    System.out.println("Chain hash:  " + chain.getChainHash() + "\n");
                    break;
                }

                // adding block to blockchain
                case 1: {
                    System.out.println("Enter difficulty > 0 \n");
                    int difficulty = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Enter transaction:  \n");
                    String transaction = sc.nextLine();

                    Timestamp start = chain.getTime();

                    //add new block to the chain
                    Block newBlock = new Block(chain.getChainSize(), chain.getTime(), transaction, difficulty);
                    newBlock.setPreviousHash(chain.getChainHash());
                    newBlock.proofOfWork();

                    chain.addBlock(newBlock);

                    Timestamp end = chain.getTime();


                    System.out.println("Total execution time to add this block was  " + (end.getTime() - start.getTime()) + " milliseconds");
                    break;

                }

                //verify blockchain
                case 2: {
                    Timestamp start = chain.getTime();
                    System.out.println("Chain verification: " + chain.isChainValid());
                    Timestamp end = chain.getTime();

                    System.out.println("Total execution time to verify the chain was  " + (end.getTime() - start.getTime()) + " milliseconds");
                    break;
                }

                //view blockchain
                case 3: {
                    System.out.println("View the Blockchain");
                    System.out.println(chain.toString());
                    break;

                }

                //corrupt blockchain
                case 4: {
                    System.out.println("corrupt the Blockchain");
                    System.out.println("Enter block ID of block to corrupt");
                    int index = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Enter new data for block " + index);
                    String transaction = sc.nextLine();
                    //setting data to the selected block in the chain
                    chain.getBlock(index).setData(transaction);
                    System.out.println("Block " + index + " now holds " + transaction);
                    break;
                }

                // repair blockchain
                case 5: {
                    Timestamp start = chain.getTime();
                    chain.repairChain();
                    Timestamp end = chain.getTime();

                    System.out.println("Total execution time required to repair the chain was " + (end.getTime() - start.getTime()) + " milliseconds");
                    break;
                }

                //exit the program
                case 6:
                    System.exit(0);
            }
        }
    }

    /**
     * This method uses the toString method defined on each individual block
     *
     * @return a String representation of the entire chain is returned
     */
    @Override
    public String toString() {

        JsonArray jsonArray = new JsonArray();

        for (Block b : blockList) {
            jsonArray.add(gson.fromJson(b.toString(), JsonElement.class));
        }

        return jsonArray.toString();

    }

    /**
     * return block at position i
     *
     * @param i
     * @return
     */
    public Block getBlock(int i) {
        return blockList.get(i);
    }


    /**
     * Compute and return the total difficulty of all blocks on the chain. Each block knows its own difficulty
     *
     * @return totalDifficulty
     */
    public int getTotalDifficulty() {
        int totalDifficulty = 0;
        for (Block block : blockList) {
            totalDifficulty += block.getDifficulty();
        }
        return totalDifficulty;
    }

    /**
     * Compute and return the expected number of hashes required for the entire chain.
     *
     * @return totalExpectedHashes
     */
    public double getTotalExpectedHashes() {
        double totalExpectedHashes = 0;
        for (Block block : blockList)
            totalExpectedHashes += Math.pow(16, block.getDifficulty());  // 16 (16 hex characters) ^ difficulty of block
        return totalExpectedHashes;
    }

    /**
     * checks if the hash has requisite number of leftmost zeroes as specified in difficulty for the entire chain
     *
     * @return "TRUE" if the chain is valid, otherwise return a string with an appropriate error message
     * @throws NoSuchAlgorithmException
     */
    public String isChainValid() throws NoSuchAlgorithmException {
        //chain contains only 1 block , i.e. genesis
        if (blockList.size() == 1) {
            Block genesisBlock = this.blockList.get(0);
            String hash = genesisBlock.calculateHash();
            //calculate prefix based on difficulty, number of leading zeroes based on the difficulty value
            String prefix = new String(new char[genesisBlock.getDifficulty()]).replace("\0", "0");
            if (!hash.substring(0, genesisBlock.getDifficulty()).equals(prefix)) {
                return "FALSE \n Improper hash on genesis node";
            } else if (!chainHash.equals(hash)) {
                return "FALSE \n Chain hash and computed hash do not match";
            } else {
                return "TRUE";
            }
        }

        //more than 1 block
        if (blockList.size() > 1) {
            for (int i = 1; i < blockList.size(); i++) {

                Block currentBlock = this.blockList.get(i);
                Block previousBlock = this.blockList.get(i - 1);


                String hash = currentBlock.calculateHash();
                String hashPointer = currentBlock.getPreviousHash();
                //calculate prefix based on difficulty, number of leading zeroes based on the difficulty value
                String prefix = new String(new char[currentBlock.getDifficulty()]).replace("\0", "0");

                if (!hash.substring(0, currentBlock.getDifficulty()).equals(prefix))
                    return "FALSE \n Improper hash on node " + i + " Does not begin with " + prefix;
                    //check proof of work / leading zeros
                else if (!hashPointer.equals(previousBlock.calculateHash()))
                    return "FALSE \n Improper previous hash";
            }
        }

        //chain hash , check the last element added to to the blocklist
        if (!chainHash.equals(blockList.get(blockList.size() - 1).calculateHash())) {
            return "Chain hash error";
        }

        return "TRUE";
    }

    /**
     * This routine repairs the chain. It checks the hashes of each block and ensures that any illegal hashes are recomputed.
     * After this routine is run, the chain will be valid. The routine does not modify any difficulty values.
     * It computes new proof of work based on the difficulty specified in the Block.
     *
     * @throws NoSuchAlgorithmException
     */
    public void repairChain() throws NoSuchAlgorithmException {

        //genesis block
        if (blockList.size() == 1) {
            //Reset previous hash and recompute proof of work
            blockList.get(0).setPreviousHash("");
            blockList.get(0).proofOfWork();
        }


        if (blockList.size() > 1) {
            for (int i = 1; i < blockList.size(); i++) {
                //reset previous hash and recompute proof of work
                blockList.get(i).setPreviousHash(blockList.get(i - 1).calculateHash());
                blockList.get(i).proofOfWork();
            }

            //reset chain hash
            this.chainHash = blockList.get(blockList.size() - 1).calculateHash();
        }
    }

}



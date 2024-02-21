/**
 * Name : Ruth Peter
 * Andrew id : rpeter
 */

package blockchaintask1;

import com.google.gson.JsonObject;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

public class Block {

    // the position of the block on the chain
    private int index;

    //time of the block's creation
    private Timestamp timestamp;

    //block's single transaction details
    private String data;

    //the SHA256 hash of a block's parent
    private String previousHash;

    //determined by POW routine
    private BigInteger nonce;

    //minimum number of left most hex digits needed by a proper hash
    private int difficulty;

    public Block(int index, Timestamp timestamp, String data, int difficulty) {
        this.index = index;
        this.timestamp = timestamp;
        this.data = data;
        this.difficulty = difficulty;
        this.nonce = BigInteger.ZERO;
    }


    /**
     * This method computes a hash of the concatenation of the index,
     * timestamp, data, previousHash, nonce, and difficulty.
     *
     * @return a String holding Hexadecimal characters
     */
    public String calculateHash() throws NoSuchAlgorithmException {
        String parentString = String.valueOf(this.index) + this.timestamp + this.data + this.previousHash + this.nonce + this.difficulty;
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = md.digest(
                parentString.getBytes(StandardCharsets.UTF_8));
        return BlockHelper.bytesToHex(encodedHash);
    }


    /**
     * This method returns the nonce for this block
     *
     * @return a BigInteger representing the nonce for this block
     */
    public BigInteger getNonce() {
        return nonce;
    }

    /**
     * Simple getter method
     *
     * @return difficulty
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Simple setter method
     *
     * @param difficulty - determines how much work is required to produce a proper hash
     */
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }


    /**
     * Simple getter method
     *
     * @return index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Simple getter method
     *
     * @return timestamp of this block
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }


    /**
     * Simple getter method
     *
     * @return this block's transaction
     */
    public String getData() {
        return data;
    }

    /**
     * Simple getter method
     *
     * @return previous hash
     */
    public String getPreviousHash() {
        return previousHash;
    }


    /**
     * Simple setter method
     *
     * @param index - the index of this block in the chain
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Simple setter method
     *
     * @param timestamp - of when this block was created
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Simple setter method
     *
     * @param data - represents the transaction held by this block
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Simple setter method
     *
     * @param previousHash - a hashpointer to this block's parent
     */
    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }


    /**
     * @return A JSON representation of all of this block's data is returned
     */
    @Override
    public String toString() {

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("index", index);
        jsonObject.addProperty("timestamp", String.valueOf(timestamp));
        jsonObject.addProperty("tx", data);
        jsonObject.addProperty("previousHash", previousHash);
        jsonObject.addProperty("nonce", nonce);
        jsonObject.addProperty("difficulty", difficulty);
        return jsonObject.toString();
    }

    /**
     * The proof of work methods finds a good hash.
     *
     * @return a String with a hash that has the appropriate number of leading hex zeroes.
     */
    public String proofOfWork() throws NoSuchAlgorithmException {

        String hexHash = calculateHash();

        String matchString = "";
        //this.difficulty

        for (int i = 0; i < this.difficulty; i++) {
            matchString = matchString + "0";
        }

        while(!hexHash.substring(0, this.difficulty).equalsIgnoreCase(matchString)){
            this.nonce = this.nonce.add(BigInteger.ONE);
            hexHash = calculateHash();
        }

        return hexHash;

    }

}

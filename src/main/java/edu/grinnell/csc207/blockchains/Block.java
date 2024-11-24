package edu.grinnell.csc207.blockchains;

/**
 * Blocks to be stored in blockchains.
 *
 * Each block contains a block number, a transaction, the hash of the previous block, a nonce,
 * and its own hash.
 * The hash is computed by including the block number, transaction, previous hash, and nonce.
 * The nonce is determined by mining, using a provided validator to check validity.
 * 
 * @author 
 */
public class Block {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The number of this block in the blockchain.
   */
  private final int num;

  /**
   * The transaction data stored in this block.
   */
  private final Transaction transaction;

  /**
   * The hash of the previous block in the chain.
   */
  private final Hash prevHash;

  /**
   * The nonce, determined by mining, for this block.
   */
  private final long nonce;

  /**
   * The hash of this block, computed using its fields.
   */
  private final Hash hash;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new block from the specified block number, transaction, and
   * previous hash, mining to choose a nonce that meets the requirements of the validator.
   *
   * @param num         The number of the block.
   * @param transaction The transaction for the block.
   * @param prevHash    The hash of the previous block.
   * @param check       The validator used to check the block.
   */
  public Block(int num, Transaction transaction, Hash prevHash, HashValidator check) {
    this.num = num;
    this.transaction = transaction;
    this.prevHash = prevHash;
    long tempNonce = 0L;

    Hash tempHash;
    do { // while
      tempNonce++;
      tempHash = computeHash(this.num, this.transaction, this.prevHash, tempNonce);
    } while (!check.isValid(tempHash)); // while

    this.nonce = tempNonce;
    this.hash = tempHash;
  } // Block(int, Transaction, Hash, HashValidator)

  /**
   * Create a new block, computing the hash for the block.
   *
   * @param num         The number of the block.
   * @param transaction The transaction for the block.
   * @param prevHash    The hash of the previous block.
   * @param nonce       The nonce of the block.
   */
  public Block(int num, Transaction transaction, Hash prevHash, long nonce) {
    this.num = num;
    this.transaction = transaction;
    this.prevHash = prevHash;
    this.nonce = nonce;
    this.hash = computeHash(this.num, this.transaction, this.prevHash, this.nonce);
  } // Block(int, Transaction, Hash, long)

  // +---------+-----------------------------------------------------
  // | Helpers |
  // +---------+

  /**
   * Compute the hash of the block given all the other info already stored in the block.
   *
   * @param num         The block number.
   * @param transaction The transaction data.
   * @param prevHash    The hash of the previous block.
   * @param nonce       The nonce for the block.
   * @return The computed hash for the block.
   */
  private static Hash computeHash(int num, Transaction transaction, Hash prevHash, long nonce) {
    String data = num + transaction.toString() + prevHash.toString() + nonce;
    return new Hash(data.getBytes(java.nio.charset.StandardCharsets.UTF_8));
  } // computeHash(int, Transaction, Hash, long)

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Get the number of the block.
   *
   * @return the number of the block.
   */
  public int getNum() {
    return this.num;
  } // getNum()

  /**
   * Get the transaction stored in this block.
   *
   * @return the transaction.
   */
  public Transaction getTransaction() {
    return this.transaction;
  } // getTransaction()

  /**
   * Get the nonce of this block.
   *
   * @return the nonce.
   */
  public long getNonce() {
    return this.nonce;
  } // getNonce()

  /**
   * Get the hash of the previous block.
   *
   * @return the hash of the previous block.
   */
  public Hash getPrevHash() {
    return this.prevHash;
  } // getPrevHash()

  /**
   * Get the hash of the current block.
   *
   * @return the hash of the current block.
   */
  public Hash getHash() {
    return this.hash;
  } // getHash()

  /**
   * Get a string representation of the block.
   *
   * @return a string representation of the block.
   */
  @Override
  public String toString() {
    return "Block #" + this.num
        + ", Transaction: " + this.transaction
        + ", Nonce: " + this.nonce
        + ", Hash: " + this.hash.toString();
  } // toString()
} // class Block

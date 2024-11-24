package edu.grinnell.csc207.blockchains;

/**
 * Blocks to be stored in blockchains.
 *
 * @author Your Name Here
 * @author Samuel A. Rebelsky
 */
public class Block {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

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
  public Block(int num, Transaction transaction, Hash prevHash,
      HashValidator check) {
    // STUB
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
    // STUB
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
  static void computeHash() {
    // STUB
  } // computeHash()

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Get the number of the block.
   *
   * @return the number of the block.
   */
  public int getNum() {
    return 0;   // STUB
  } // getNum()

  /**
   * Get the transaction stored in this block.
   *
   * @return the transaction.
   */
  public Transaction getTransaction() {
    return new Transaction("Here", "There", 0); // STUB
  } // getTransaction()

  /**
   * Get the nonce of this block.
   *
   * @return the nonce.
   */
  public long getNonce() {
    return 0;   // STUB
  } // getNonce()

  /**
   * Get the hash of the previous block.
   *
   * @return the hash of the previous block.
   */
  Hash getPrevHash() {
    return new Hash(new byte[] {0});  // STUB
  } // getPrevHash

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
    return "";  // STUB
  } // toString()
} // class Block

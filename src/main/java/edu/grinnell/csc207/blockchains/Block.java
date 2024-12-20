package edu.grinnell.csc207.blockchains;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Represents a block to be stored in blockchains.
 * Each block contains a block number, a transaction, the hash of the previous block, a nonce,
 * and its own hash. The hash is computed using these fields.
 *
 * The nonce is determined by mining with a validator to check validity.
 *
 * @author Samantha Schmidt
 * @author Princess Alexander
 * @author Samuel Rebelsky
 */
public class Block {

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The number of this block in the blockchain.
   */
  private int num; // The block number

  /**
   * The transaction data stored in this block.
   * Changed to package-private for test access.
   */
  Transaction transaction; // Package-private for testing

  /**
   * The hash of the previous block in the chain.
   */
  private Hash prevHash;

  /**
   * The nonce, determined by mining, for this block.
   * Changed to package-private for test access.
   */
  long nonce; // Package-private for testing

  /**
   * The hash of this block, computed using its fields.
   */
  private Hash hash;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Creates a new block, mining to determine a valid nonce.
   *
   * @param num The number of the block.
   * @param transaction The transaction stored in the block.
   * @param prevHash The hash of the previous block in the chain.
   * @param check The validator used to check the block's hash.
   */
  public Block(int num, Transaction transaction, Hash prevHash, HashValidator check) {
    this.num = num;
    this.transaction = transaction;
    this.prevHash = prevHash;

    long maxAttempts = 1_000_000; // Prevent infinite mining
    long tempNonce = 0;
    Hash tempHash;

    while (maxAttempts-- > 0) { // Mine for a valid nonce
      tempHash = computeHash(num, transaction, prevHash, tempNonce);
      if (check.isValid(tempHash)) {
        this.nonce = tempNonce;
        this.hash = tempHash;
        return;
      } // if
      tempNonce++;
    } // while

    throw new IllegalStateException("Failed to mine a valid block after 1,000,000 attempts");
  } // Block(int, Transaction, Hash, HashValidator)

  /**
   * Creates a new block with a predefined nonce, computing the hash.
   *
   * @param num The number of the block.
   * @param transaction The transaction stored in the block.
   * @param prevHash The hash of the previous block in the chain.
   * @param nonce The predefined nonce for this block.
   */
  public Block(int num, Transaction transaction, Hash prevHash, long nonce) {
    this.num = num; // Set block number
    this.transaction = transaction; // Set transaction
    this.prevHash = prevHash; // Set previous hash
    this.nonce = nonce; // Set nonce
    this.hash = computeHash(num, transaction, prevHash, nonce); // Compute and set hash
  } // Block(int, Transaction, Hash, long)

  // +---------+-----------------------------------------------------
  // | Helpers |
  // +---------+

  /**
   * Computes the hash of the block using its fields.
   *
   * @param num The block number.
   * @param transaction The transaction data.
   * @param prevHash The hash of the previous block.
   * @param nonce The nonce for the block.
   * @return The computed hash for the block.
   */
  public static Hash computeHash(int num, Transaction transaction, Hash prevHash, long nonce) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");

      // Hash the block number
      digest.update(ByteBuffer.allocate(Integer.BYTES).putInt(num).array());

      // Serialize transaction data and hash it
      digest.update(transaction.getSource().getBytes(java.nio.charset.StandardCharsets.UTF_8));
      digest.update(transaction.getTarget().getBytes(java.nio.charset.StandardCharsets.UTF_8));
      digest.update(ByteBuffer.allocate(Integer.BYTES).putInt(transaction.getAmount()).array());

      // Hash the previous hash
      digest.update(prevHash.getBytes());

      // Hash the nonce
      digest.update(ByteBuffer.allocate(Long.BYTES).putLong(nonce).array());

      // Compute and return the final hash
      return new Hash(digest.digest());
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("SHA-256 algorithm not found", e);
    } // try/catch
  } // computeHash(int, Transaction, Hash, long)

  // +---------+-----------------------------------------------------
  // | Getters |
  // +---------+

  /**
   * Gets the number of the block.
   *
   * @return The block number.
   */
  public int getNum() {
    return this.num;
  } // getNum()

  /**
   * Gets the transaction stored in the block.
   *
   * @return The transaction.
   */
  Transaction getTransaction() {
    return this.transaction;
  } // getTransaction()

  /**
   * Gets the nonce of the block.
   *
   * @return The nonce.
   */
  public long getNonce() {
    return this.nonce;
  } // getNonce()

  /**
   * Gets the hash of the previous block.
   *
   * @return The previous block's hash.
   */
  public Hash getPrevHash() {
    return this.prevHash;
  } // getPrevHash()

  /**
   * Gets the hash of the current block.
   *
   * @return The current block's hash.
   */
  public Hash getHash() {
    return this.hash;
  } // getHash()

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Returns a string representation of the block.
   *
   * @return A string representation of the block.
   */
  @Override
  public String toString() {
    return String.format(
      "Block #%d [Transaction: %s, Nonce: %d, Hash: %s, PrevHash: %s]",
      this.num, this.transaction, this.nonce, this.hash, this.prevHash
    );
  } // toString()

} // class Block


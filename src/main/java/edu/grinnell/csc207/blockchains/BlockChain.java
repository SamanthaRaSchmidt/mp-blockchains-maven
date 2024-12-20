package edu.grinnell.csc207.blockchains;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A blockchain implemented using a Node-based linked list.
 *
 * Each block is linked to the next block in the chain through a Node structure.
 * The chain supports operations like mining, appending, removing, and validating blocks.
 *
 * @author Samuel A. Rebelsky
 * @author Princess Alexander
 *
 */
public class BlockChain implements Iterable<Transaction> {

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The head (first node) of the blockchain.
   */
  private Node head; // head

  /**
   * The tail (last node) of the blockchain.
   */
  private Node tail; // tail

  /**
   * The size of the blockchain.
   */
  private int size; // size

  /**
   * The validator used for mining and checking hashes.
   */
  private final HashValidator validator; // validator

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new blockchain using a validator to check elements.
   *
   * @param validator The validator used to check elements.
   */
  public BlockChain(HashValidator validator) {
    this.validator = validator;
    Transaction genesisTransaction = new Transaction("", "", 0); // No magic numbers
    Block genesisBlock = new Block(0, genesisTransaction, new Hash(new byte[0]), validator);
    this.head = this.tail = new Node(genesisBlock); // Initialize head and tail
    this.size = 1;
  } // BlockChain(HashValidator)

  // +---------+-----------------------------------------------------
  // | Helpers |
  // +---------+

  /**
   * A node class for the linked list structure.
   */
  private static class Node {
    /**
     * The block in the node.
     */
    private final Block block; // block

    /**
     * The next node in the chain.
     */
    private Node next; // next

    /**
     * Constructor to create a new node with a block.
     *
     * @param block The block to store in the node.
     */
    Node(Block block) {
      this.block = block;
    } // Node(Block)
  } // Node

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Mine for a new valid block for the end of the chain.
   *
   * @param transaction The transaction that goes in the block.
   * @return a new block with the correct number, hashes, and such.
   */
  public Block mine(Transaction transaction) {
    return new Block(size, transaction, tail.block.getHash(), validator);
  } // mine(Transaction)

  /**
   * Get the number of blocks currently in the chain.
   *
   * @return the number of blocks in the chain, including the initial block.
   */
  public int getSize() {
    return size;
  } // getSize()

/**
 * Append a block to the end of the blockchain.
 *
 * @param blk The block to append.
 * @throws IllegalArgumentException if the block is invalid.
 */
  public void append(Block blk) {
    Transaction t = blk.getTransaction();

    // Validate balance for source
    if (!t.getSource().isEmpty() && balance(t.getSource()) < t.getAmount()) {
      throw new IllegalArgumentException("Insufficient balance for transaction.");
    } // if

    // Ensure positive transaction amount
    if (t.getAmount() < 0) {
      throw new IllegalArgumentException("Transaction amount cannot be negative.");
    } // if

    // Check if block's previous hash matches the chain's tail hash
    if (!blk.getPrevHash().equals(tail.block.getHash())) {
      throw new IllegalArgumentException("Invalid previous hash in block.");
    } // if

    // Ensure the block's hash matches its contents
    Hash recomputedHash = Block.computeHash(
        blk.getNum(),
        blk.getTransaction(),
        blk.getPrevHash(),
        blk.getNonce()
    );
    if (!blk.getHash().equals(recomputedHash)) {
      throw new IllegalArgumentException("Block hash does not match its contents.");
    } // if

    // Ensure the block's hash is valid
    if (!validator.isValid(blk.getHash())) {
      throw new IllegalArgumentException("Invalid hash for block.");
    } // if

    // Append the block
    Node newNode = new Node(blk);
    tail.next = newNode;
    tail = newNode;
    size++;
  } // append(Block)

  /**
   * Remove the last block from the chain.
   *
   * @return false if the chain has only one block; true otherwise.
   */
  public boolean removeLast() {
    if (head == tail) {
      return false; // Cannot remove the genesis block
    } // if

    Node current = head;
    while (current.next != tail) {
      current = current.next;
    } // while

    current.next = null;
    tail = current;
    size--;
    return true;
  } // removeLast()

  /**
   * Get the hash of the last block in the chain.
   *
   * @return the hash of the last block.
   */
  public Hash getHash() {
    return tail.block.getHash();
  } // getHash()

  /**
   * Validate the blockchain.
   *
   * @return true if the blockchain is valid; false otherwise.
   */
  public boolean isCorrect() {
    Node current = head;
    java.util.Map<String, Integer> balances = new java.util.HashMap<>();

    while (current != null) { // while
      Block currentBlock = current.block;
      Transaction transaction = currentBlock.getTransaction();

      // Verify the block's hash matches its contents
      Hash recomputedHash = Block.computeHash(
          currentBlock.getNum(),
          currentBlock.getTransaction(),
          currentBlock.getPrevHash(),
          currentBlock.getNonce()
      );
      if (!currentBlock.getHash().equals(recomputedHash)) {
        return false; // Block hash mismatch
      } // if

      // Validate hash with the validator
      if (!validator.isValid(currentBlock.getHash())) {
        return false; // Hash is invalid
      } // if

      // Ensure hash linkage
      if (current.next != null
          && !current.next.block.getPrevHash().equals(currentBlock.getHash())) {
        return false; // Invalid hash linkage
      } // if

      // Validate balances
      balances.putIfAbsent(transaction.getSource(), 0);
      balances.putIfAbsent(transaction.getTarget(), 0);

      if (!transaction.getSource().isEmpty()) {
        int sourceBalance = balances.get(transaction.getSource());
        if (sourceBalance < transaction.getAmount()) {
          return false; // Insufficient balance
        } // if
        balances.put(transaction.getSource(), sourceBalance - transaction.getAmount());
      } // if

      balances.put(transaction.getTarget(),
          balances.get(transaction.getTarget()) + transaction.getAmount());

      current = current.next; // Move to the next node
    } // while

    return true; // Blockchain is valid
  } // isCorrect()

  /**
   * Check if the blockchain is correct. Throws an exception if not.
   *
   * @throws Exception if the blockchain is invalid.
   */
  public void check() throws Exception {
    if (!isCorrect()) {
      throw new Exception("Blockchain is invalid.");
    } // if
  } // check()

  /**
   * Return an iterator of all the people who participated in the system.
   *
   * @return an iterator of all the people in the system.
   */
  public Iterator<String> users() {
    return new Iterator<>() {
      private Node current = head;
      private final java.util.HashSet<String> userSet = new java.util.HashSet<>();
      private final java.util.Iterator<String> iterator;

      {
        // Populate the userSet with unique users
        while (current != null) {
          Transaction t = current.block.getTransaction();
          if (!t.getSource().isEmpty()) {
            userSet.add(t.getSource());
          } // if
          if (!t.getTarget().isEmpty()) {
            userSet.add(t.getTarget());
          } // if
          current = current.next;
        } // while
        iterator = userSet.iterator();
      } // Initializer block

      @Override
      public boolean hasNext() {
        return iterator.hasNext();
      } // hasNext()

      @Override
      public String next() {
        return iterator.next();
      } // next()
    }; // Iterator
  } // users()

  /**
   * Find a user's balance.
   *
   * @param user The user whose balance we want to find.
   * @return that user's balance (or 0, if the user is not in the system).
   */
  public int balance(String user) {
    int balance = 0;
    Node current = head;

    while (current != null) {
      Transaction t = current.block.getTransaction();
      if (t.getTarget().equals(user)) {
        balance += t.getAmount();
      } else if (t.getSource().equals(user)) {
        balance -= t.getAmount();
      } // if
      current = current.next;
    } // while

    return balance;
  } // balance(String)

  /**
   * Get an iterator for all the blocks in the chain.
   *
   * @return an iterator for all the blocks in the chain.
   */
  public Iterator<Block> blocks() {
    return new Iterator<>() {
      private Node current = head; // Iterator position

      @Override
      public boolean hasNext() {
        return current != null;
      } // hasNext()

      @Override
      public Block next() {
        if (!hasNext()) {
          throw new NoSuchElementException();
        } // if
        Block blk = current.block;
        current = current.next;
        return blk;
      } // next()
    }; // Iterator
  } // blocks()

  /**
   * Get an iterator for all the transactions in the chain.
   *
   * @return an iterator for all the transactions in the chain.
   */
  @Override
  public Iterator<Transaction> iterator() {
    return new Iterator<>() {
      private Node current = head; // Iterator position

      @Override
      public boolean hasNext() {
        return current != null;
      } // hasNext()

      @Override
      public Transaction next() {
        if (!hasNext()) {
          throw new NoSuchElementException();
        } // if
        Transaction tx = current.block.getTransaction();
        current = current.next;
        return tx;
      } // next()
    }; // Iterator
  } // iterator()

} // class BlockChain
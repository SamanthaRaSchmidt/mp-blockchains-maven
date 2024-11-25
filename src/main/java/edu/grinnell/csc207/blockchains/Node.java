package edu.grinnell.csc207.blockchains;

/**
 * Creates nodes.
 * 
 * @author Sam Schmidt
 */
public class Node<Block> {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The previous node.
   */
  Node<Block> prev;

  /**
   * The stored block.
   */
  Block blk;

  /**
   * The next node.
   */
  Node<Block> next;


  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new node.
   *
   * @param prevNode
   *   The previous node in the list (or null, if it's the front of the list).
   * @param val
   *   The value to be stored in the node.
   * @param nextNode
   *   The next node in the list (or null, if it's the end of the list).
   */
  public Node(Node<Block> prevNode, Block val, Node<Block> nextNode) {
    this.prev = prevNode;
    this.blk = val;
    this.next = nextNode;
  } // Node(Node<Block>, Block, Node<Block>)

  /**
   * Create a new node with no previous link (e.g., the front
   * of some kinds of lists).
   *
   * @param val
   *   The value to be stored in the node.
   * @param nextNode
   *   The next node in the list (or null, if it's the end of the list).
   */
  public Node(Block val, Node<Block> nextNode) {
    this(null, val, nextNode);
  } // Node(Block, Node<Block>)

  /**
   * Create a new node with no next link (e.g., if it's at the end of
   * the list). Included primarily for symmetry.
   *
   * @param prevNode
   *   The previous node in the list (or null, if it's the front of the list).
   * @param val
   *   The value to be stored in the node.
   */
  public Node(Node<Block> prevNode, Block val) {
    this(prevNode, val, null);
  } // Node(Node<Block>, Block)

  /**
   * Create a new node with no links.
   *
   * @param val
   *   The value to be stored in the node.
   */
  public Node(Block val) {
    this(null, val, null);
  } // Node2(T)

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

   /**
   * Insert a new value after this node.
   *
   * @param val
   *   The value to insert.
   * @return
   *   The newly created node that contains the value.
   */
  Node<Block> insertAfter(Block val) {
    Node<Block> tmp = new Node<Block>(this, val, this.next);
    if (this.next != null) {
      this.next.prev = tmp;
    } // if
    this.next = tmp;
    return tmp;
  } // insertAfter

  /**
   * Insert a new value before this node.
   *
   * @param val
   *   The value to insert.
   * @return
   *   The newly created node that contains the value.
   */
  Node<Block> insertBefore(Block val) {
    Node<Block> tmp = new Node<Block>(this.prev, val, this);
    if (this.prev != null) {
      this.prev.next = tmp;
    } // if
    this.prev = tmp;
    return tmp;
  } // insertBefore

  /**
   * Remove this node.
   */
  void remove() {
    if (this.prev != null) {
      this.prev.next = this.next;
    } // if
    if (this.next != null) {
      this.next.prev = this.prev;
    } // if
    this.prev = null;
    this.next = null;
  } // remove()
} // class Node


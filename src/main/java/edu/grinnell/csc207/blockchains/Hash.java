package edu.grinnell.csc207.blockchains;

/**
 * Encapsulated hashes.
 *
 * @author Princess Alexander
 * @author Samuel A. Rebelsky
 */
public class Hash {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The data stored in this hash.
   */
  private final byte[] data;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new encapsulated hash.
   *
   * @param data The data to copy into the hash.
   */
  public Hash(byte[] data) {
    if (data == null) {
      throw new IllegalArgumentException("Hash data cannot be null.");
    } // if
    this.data = data.clone();
  } // Hash(byte[])

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Determine how many bytes are in the hash.
   *
   * @return the number of bytes in the hash.
   */
  public int length() {
    return this.data.length;
  } // length()

  /**
   * Get the ith byte.
   *
   * @param i The index of the byte to get, between 0 (inclusive) and length() (exclusive).
   * @return the ith byte.
   * @throws IndexOutOfBoundsException if the index is out of range.
   */
  public byte get(int i) {
    if (i < 0 || i >= this.data.length) {
      throw new IndexOutOfBoundsException("Index " + i + " is out of range.");
    } // if
    return this.data[i];
  } // get(int)

  /**
   * Get a copy of the bytes in the hash.
   *
   * @return a copy of the bytes in the hash.
   */
  public byte[] getBytes() {
    return this.data.clone();
  } // getBytes()

  /**
   * Convert to a hex string.
   *
   * @return the hash as a hex string.
   */
  @Override
  public String toString() {
    StringBuilder hexString = new StringBuilder();
    for (byte b : this.data) {
      hexString.append(String.format("%02X", b)); // Uppercase hexadecimal
    } // for
    return hexString.toString();
  } //toString

  /**
   * Determine if this is equal to another object.
   *
   * @param other The object to compare to.
   * @return true if the two objects are conceptually equal and false otherwise.
   */
  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    } // if
    if (other == null || getClass() != other.getClass()) {
      return false;
    } // if
    Hash otherHash = (Hash) other;
    return java.util.Arrays.equals(this.data, otherHash.data);
  } // equals(Object)

  /**
   * Get the hash code of this object.
   *
   * @return the hash code.
   */
  @Override
  public int hashCode() {
    return java.util.Arrays.hashCode(this.data);
  } // hashCode()
} // class Hash

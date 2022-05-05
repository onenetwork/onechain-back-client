package com.onenetwork.backchain.client.eth;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;

/**
 * Helper class for Ethereum
 */
final class EthereumHelper {

  /**
   * @throws InstantiationException 
   *
   */
  private EthereumHelper() throws InstantiationException {
    throw new InstantiationException("Cannot create object of Singleton class");
  }

  @FunctionalInterface
  interface Supplier<T> {
    T get() throws Exception;
  }

  public static <T> T await(Supplier<T> s) {
    try {
      return s.get();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static Bytes32 hashStringToBytes32(String hash) throws DecoderException {
    return new Bytes32(
      Hex.decodeHex((hash.toUpperCase().startsWith("0X") ? hash.substring(2, hash.length()) : hash).toCharArray()));
  }

  public static byte[] hashStringToBytes(String hash) {
    try {
      return Hex.decodeHex((hash.toUpperCase().startsWith("0X") ? hash.substring(2, hash.length()) : hash).toCharArray());
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String hashBytesToString(Bytes32 bytes) throws DecoderException {
    return bytes == null ? null : "0x" + new String(Hex.encodeHex(bytes.getValue()));
  }

  public static String hashBytesToString(byte[] bytes) {
    try {
      return bytes == null ? null : "0x" + new String(Hex.encodeHex(bytes));
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String newHash() throws Exception {
    return "0x" + Hex
      .encodeHexString(DIGEST.digest(("" + new Date().getTime() + Math.random()).getBytes()));
  }

  @SuppressWarnings("unchecked")
  public static DynamicArray<Bytes32> convertAndGetBytes32DA(String[] hashes) throws DecoderException {
    DynamicArray<Bytes32> dynamicBytes32;
    Bytes32[] bytes32Array = new Bytes32[hashes == null ? 0 : hashes.length];
    for (int i = 0; i < bytes32Array.length; i++) {
      bytes32Array[i] = hashStringToBytes32(hashes[i]);
    }

    if (bytes32Array.length > 0) {
      dynamicBytes32 = new DynamicArray<>(bytes32Array);
    }
    else {
      dynamicBytes32 = DynamicArray.empty(Bytes32.TYPE_NAME + "32[]");
    }
    return dynamicBytes32;
  }

  @SuppressWarnings("unchecked")
  public static List<byte[]> convertAndGetBytes(String[] hashes) {
    List<byte[]> byteLists = new ArrayList<>();
    for (int i = 0; i < hashes.length; i++) {
      byteLists.add(hashStringToBytes(hashes[i]));
    }

    return byteLists;
  }

  @SuppressWarnings("unchecked")
  public static DynamicArray<Address> convertAndGetAddressDA(String[] addressArray) {
    DynamicArray<Address> addressDA;
    Address[] addresses = new Address[addressArray == null ? 0 : addressArray.length];
    for (int i = 0; i < addresses.length; i++) {
      addresses[i] = new Address(addressArray[i]);
    }

    if (addresses.length > 0) {
      addressDA = new DynamicArray<>(addresses);
    }
    else {
      addressDA = DynamicArray.empty(Address.TYPE_NAME + "[]");
    }
    return addressDA;
  }

  @SuppressWarnings("unchecked")
  public static <T extends Enum<T>> DynamicArray<Uint256> convertAndGetUint256DA(T[] enumValues) {
    DynamicArray<Uint256> uint256DA;
    Uint256[] uint256Array = new Uint256[enumValues == null ? 0 : enumValues.length];
    for (int i = 0; i < uint256Array.length; i++) {
      uint256Array[i] = new Uint256(enumValues[i].ordinal());
    }
    if (uint256Array.length > 0) {
      uint256DA = new DynamicArray<>(uint256Array);
    }
    else {
      uint256DA = DynamicArray.empty(Uint256.TYPE_NAME + "256[]");
    }
    return uint256DA;
  }

  public static <T> boolean isNullOrEmpty(T param) {
    if (param == null) {
      return true;
    }
    if (param instanceof String) {
      return ((String) param).trim().length() <= 0;
    }
    return false;
  }

  public static Uint256 getTimeInUint256(Calendar cal) {
    return new Uint256(cal == null ? 0 : cal.getTimeInMillis());
  }

  private static MessageDigest DIGEST;
  static {
    try {
      DIGEST = MessageDigest.getInstance("SHA-256");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}

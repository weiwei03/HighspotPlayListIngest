package com.highspot.playlist.exception;

/**
 * exception indicate an object is already exist.
 */
public class AlreadyExistException extends Exception {
  /**
   * constructor.
   * @param action action info.
   * @param errorMsg error message.
   */
  public AlreadyExistException(final String action, final String errorMsg) {
    super(String.format("%s failed, %s", action, errorMsg));
  }
}

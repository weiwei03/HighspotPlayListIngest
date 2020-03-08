package com.highspot.playlist.exception;

/**
 * Exception indicates a object does not exists.
 */
public class NotFoundException extends Exception {
  /**
   * constructor.
   * @param action action info
   * @param errorMsg error message
   */
  public NotFoundException(final String action, final String errorMsg) {
    super(String.format("%s failed, %s", action, errorMsg));
  }
}

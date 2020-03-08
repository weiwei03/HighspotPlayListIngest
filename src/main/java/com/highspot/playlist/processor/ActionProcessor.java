package com.highspot.playlist.processor;

import com.highspot.playlist.action.Action;
import com.highspot.playlist.model.MixTape;

/**
 * process ingestion actions with MixTape file.
 */
public interface ActionProcessor {
  /**
   * execute actions with MixTape file.
   * @param mixTape the content of MixTape.json
   * @param actionList action to be execute on MixTape file.
   * @throws Exception exceptions
   */
  void process(
    final MixTape mixTape,
    final Action[] actionList) throws Exception;
}

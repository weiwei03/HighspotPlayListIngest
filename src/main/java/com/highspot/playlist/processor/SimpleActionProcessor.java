package com.highspot.playlist.processor;

import com.highspot.playlist.action.Action;
import com.highspot.playlist.model.MixTape;

/**
 * A simple action processor to execute actions with MixTape file.
 * It executes the actions in sequence.
 * If one action failed, it will fail too.
 */
public class SimpleActionProcessor implements ActionProcessor {
  @Override
  public void process(final MixTape mixTape,
                      final Action[] actionList) throws Exception {
    for (Action action : actionList) {
      action.execute(mixTape);
    }
  }
}

package com.highspot.playlist.action;

import lombok.Data;

/**
 * abstract call for Actions.
 */
@Data
public abstract class AbstractAction implements Action {
  private String action;
}

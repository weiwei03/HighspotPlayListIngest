package com.highspot.playlist.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.highspot.playlist.exception.NotFoundException;
import com.highspot.playlist.model.MixTape;
import com.highspot.playlist.model.PlayList;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Remove an existing play list from MixTape object.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonTypeName("remove_playlist")
public class RemovePlayList extends AbstractAction {
  @JsonProperty("playlist_id")
  private String playListId;

  /**
   * Remove an existing play list from MixTape object.
   * @param mixTape a MixTape object representing content of MixTape.json.
   * @throws Exception NotFoundException when the play list does not exist.
   */
  @Override
  public void execute(final MixTape mixTape) throws Exception {
    final PlayList playList = mixTape.getPlayListById(playListId);
    //play list doesn't exist
    if (playList == null) {
      final String actionMsg = String.format("%s %s", getAction(), playListId);
      throw new NotFoundException(actionMsg, "playlist does not exist");
    }

    mixTape.getPlayLists().remove(playList);
  }
}

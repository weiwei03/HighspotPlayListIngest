package com.highspot.playlist.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.highspot.playlist.exception.AlreadyExistException;
import com.highspot.playlist.exception.NotFoundException;
import com.highspot.playlist.model.MixTape;
import com.highspot.playlist.model.PlayList;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;

/**
 * add a new playlist, the playlist should contain at list one song.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonTypeName("add_playlist")
@Slf4j
public class AddPlayList extends AbstractAction {
  @JsonProperty("playlist")
  private PlayList playList;

  /**
   * Add a new play list to MixTape file.
   * It will fail in the following situation:
   *  1. the playlist doesn't have any songs.
   *  2. the playlist id already exists.
   *  3. the userId in the playlist does not exist in MixTape.
   *  4. some song id does not exist in MixTape.
   * @param mixTape a MixTape object representing content of MixTape object.
   * @throws Exception it will throw three type of exceptions:
   *  1. IllegalArgumentException the playlist does not have any songs.
   *  2. AlreadyExistException the playlist already exists.
   *  3. NotFoundException the user id or some song id does not exist in MixTape object.
   */
  @Override
  public void execute(final MixTape mixTape) throws Exception {
    final String actionMsg = String.format("%s %s", getAction(), playList.getId());
    //new playlist should contain songs
    Validate.notEmpty(playList.getSongIds(),
                      String.format("%s, playlist has no songs", actionMsg));

    //playlist id already exists
    if (mixTape.getPlayListById(playList.getId()) != null) {
      throw new AlreadyExistException(actionMsg, "playlist already exists");
    }

    //the userId doesn't exist in mixTape
    if (mixTape.getUserById(playList.getUserId()) == null) {
      throw new NotFoundException(actionMsg, "user id does not exist");
    }

    //check if all the song id exist
    for (String songId : playList.getSongIds()) {
      //songId doesn't exist in mixTap
      if (mixTape.getSongById(songId) == null) {
        throw new NotFoundException(actionMsg, String.format("song id %s does not exist", songId));
      }
    }

    mixTape.getPlayLists().add(playList);
  }
}

package com.highspot.playlist.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.highspot.playlist.exception.AlreadyExistException;
import com.highspot.playlist.exception.NotFoundException;
import com.highspot.playlist.model.MixTape;
import com.highspot.playlist.model.PlayList;
import com.highspot.playlist.model.Song;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Add an existing song to an existing play list.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonTypeName("add_song")
public class AddSongToPlayList extends AbstractAction {
  @JsonProperty("song_id")
  private String songId;

  @JsonProperty("playlist_id")
  private String playlistId;

  /**
   * Add an existing song to an existing play list.
   * It will fail in the following situation:
   *  1. The song does not exist.
   *  2. The play list does not exist.
   *  3. The play list already contains the song.
   * @param mixTape a MixTape object representing content of MixTape.json.
   * @throws Exception
   *  1. NotFoundException the song or the play list does not exist.
   *  2. AlreadyExistException the song already belongs to the play list.
   */
  @Override
  public void execute(final MixTape mixTape) throws Exception {
    final String actionMsg = String.format("%s song id %s, playlist id %s",
                    getAction(), songId, playlistId);

    final Song song = mixTape.getSongById(songId);
    //song doesn't exist in mixTape
    if (song == null) {
      throw new NotFoundException(actionMsg, "song does not exist");
    }

    final PlayList playList = mixTape.getPlayListById(playlistId);
    //playList doesn't exist in mixTape
    if (playList == null) {
      throw new NotFoundException(actionMsg, "play list does not exist");
    }

    //playList already contains the song
    if (playList.getSongIds().contains(songId)) {
      throw new AlreadyExistException(actionMsg, "song id already exist in playlist");
    }

    playList.getSongIds().add(songId);
  }
}

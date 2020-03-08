package com.highspot.playlist.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Date model for MixTape file.
 */
@Data
public class MixTape {
  private List<User> users;
  @JsonProperty("playlists")
  private List<PlayList> playLists;
  private List<Song> songs;

  /**
   * find Song by song id.
   * @param songId song id.
   * @return a Song object or null.
   */
  public Song getSongById(final String songId) {
    return findObjectFromList(songs, song -> song.getId().equals(songId));
  }

  /**
   * find PlayList by play list id.
   * @param playListId play list id.
   * @return a PlayList object or null
   */
  public PlayList getPlayListById(final String playListId) {
    return findObjectFromList(playLists,
                              playList -> playList.getId().equals(playListId));
  }

  /**
   * find user by user id.
   * @param userId user id.
   * @return a user object or null.
   */
  public User getUserById(final String userId) {
    return findObjectFromList(users,
                              user -> user.getId().equals(userId));
  }

  private static <T> T findObjectFromList(final List<T> list,
                          final Predicate<? super T> predicate) {
   final Optional<T> optObj =
     list.stream()
     .filter(predicate)
     .findFirst();

   return optObj.orElse(null);
  }
}

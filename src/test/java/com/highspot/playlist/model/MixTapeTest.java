package com.highspot.playlist.model;

import org.junit.Before;
import org.junit.Test;
import java.util.Collections;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class MixTapeTest {
  private static final String SONG_ID = "randomSongId";
  private final Song song = new Song(SONG_ID, "randomArtist", "randomTitle");

  private static final String USER_ID = "randomUserId";
  private final User user = new User(USER_ID, "randomUserName");

  private static final String PLAY_LIST_ID = "randomPlayListId";
  private final PlayList playList = new PlayList(PLAY_LIST_ID, "randomUserId",
                                                 Collections.singletonList("randomSongId1"));

  private MixTape mixTape;

  @Before
  public void setup() {
    mixTape = new MixTape();
    mixTape.setSongs(Collections.singletonList(song));
    mixTape.setUsers(Collections.singletonList(user));
    mixTape.setPlayLists(Collections.singletonList(playList));
  }

  @Test
  public void testGetUserById_UserExist() {
    assertNotNull(mixTape.getUserById(USER_ID));
  }

  @Test
  public void testGetUserById_UserNotExist() {
    assertNull(mixTape.getUserById("randomUserIdX"));
  }

  @Test
  public void testGetPlayListById_PlayListExist() {
    assertNotNull(mixTape.getPlayListById(PLAY_LIST_ID));
  }

  @Test
  public void testGetSongById_SongExist() {
    assertNotNull(mixTape.getSongById(SONG_ID));
  }
}

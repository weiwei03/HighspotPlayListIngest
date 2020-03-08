package com.highspot.playlist.action;

import com.highspot.playlist.exception.AlreadyExistException;
import com.highspot.playlist.exception.NotFoundException;
import com.highspot.playlist.model.MixTape;
import com.highspot.playlist.model.PlayList;
import com.highspot.playlist.model.Song;
import com.highspot.playlist.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddPlayListTest {
  private static final String PLAY_LIST_ID = "randomPlayListId";
  private static final String USER_ID = "randomUserId";
  private static final List<String> SONG_IDS =
    Arrays.asList("randomSongId1", "randomSongId2", "randomSongId3");

  @Mock
  private MixTape mockMixTape;

  private AddPlayList addPlayList;
  private PlayList playList;

  @Before
  public void setup() {
    playList = new PlayList(PLAY_LIST_ID, USER_ID, SONG_IDS);

    addPlayList = new AddPlayList();
    addPlayList.setAction("add_playlist");
    addPlayList.setPlayList(playList);
  }

  @Test(expected = AlreadyExistException.class)
  public void testExecute_PlayListExists() throws Exception {
    when(mockMixTape.getPlayListById(PLAY_LIST_ID)).thenReturn(playList);

    addPlayList.execute(mockMixTape);
  }

  @Test(expected = NotFoundException.class)
  public void testExecute_UserIdNotExist() throws Exception {
    when(mockMixTape.getPlayListById(PLAY_LIST_ID)).thenReturn(null);
    when(mockMixTape.getUserById(USER_ID)).thenReturn(null);

    addPlayList.execute(mockMixTape);
  }

  @Test(expected = NotFoundException.class)
  public void testExecute_SongIdNotExist() throws Exception {
    when(mockMixTape.getPlayListById(PLAY_LIST_ID)).thenReturn(null);

    final User mockUser = mock(User.class);
    when(mockMixTape.getUserById(USER_ID)).thenReturn(mockUser);

    when(mockMixTape.getSongById(anyString())).thenReturn(null);

    addPlayList.execute(mockMixTape);
  }

  @Test
  public void testExecute_AddPlayListSucceed() throws Exception {
    when(mockMixTape.getPlayListById(PLAY_LIST_ID)).thenReturn(null);

    final User mockUser = mock(User.class);
    when(mockMixTape.getUserById(USER_ID)).thenReturn(mockUser);

    final Song mockSong = mock(Song.class);
    when(mockMixTape.getSongById(anyString())).thenReturn(mockSong);

    final List<PlayList> playLists = new ArrayList<>();
    when(mockMixTape.getPlayLists()).thenReturn(playLists);
    addPlayList.execute(mockMixTape);

    assertTrue(playLists.contains(playList));
  }
}

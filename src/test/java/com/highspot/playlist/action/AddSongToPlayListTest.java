package com.highspot.playlist.action;

import com.highspot.playlist.exception.AlreadyExistException;
import com.highspot.playlist.exception.NotFoundException;
import com.highspot.playlist.model.MixTape;
import com.highspot.playlist.model.PlayList;
import com.highspot.playlist.model.Song;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddSongToPlayListTest {
  private static final String SONG_ID = "32";
  private static final String PLAYLIST_ID = "3";

  @Mock
  private MixTape mockMixTape;

  private AddSongToPlayList addSongToPlayList;

  @Before
  public void setup() {
    addSongToPlayList = new AddSongToPlayList();
    addSongToPlayList.setAction("add_song");
    addSongToPlayList.setSongId(SONG_ID);
    addSongToPlayList.setPlaylistId(PLAYLIST_ID);
  }

  @Test(expected = NotFoundException.class)
  public void testExecute_SongNotExist() throws Exception {
    when(mockMixTape.getSongById(SONG_ID)).thenReturn(null);

    addSongToPlayList.execute(mockMixTape);
  }

  @Test(expected = NotFoundException.class)
  public void testExecute_PlayListNotExist() throws Exception {
    final Song mockSong = mock(Song.class);
    when(mockMixTape.getSongById(SONG_ID)).thenReturn(mockSong);
    when(mockMixTape.getPlayListById(PLAYLIST_ID)).thenReturn(null);

    addSongToPlayList.execute(mockMixTape);
  }

  @Test(expected = AlreadyExistException.class)
  public void testExecute_SongAlreadyExistInPlayList() throws Exception {
    final Song mockSong = mock(Song.class);
    when(mockMixTape.getSongById(SONG_ID)).thenReturn(mockSong);

    final PlayList mockPlayList = mock(PlayList.class);
    when(mockMixTape.getPlayListById(PLAYLIST_ID)).thenReturn(mockPlayList);

    final List<String> songIds = Arrays.asList(SONG_ID, "11");
    when(mockPlayList.getSongIds()).thenReturn(songIds);

    addSongToPlayList.execute(mockMixTape);
  }

  @Test
  public void testExecute_SongAddedToPlayList() throws Exception {
    final Song mockSong = mock(Song.class);
    when(mockMixTape.getSongById(SONG_ID)).thenReturn(mockSong);

    final PlayList mockPlayList = mock(PlayList.class);
    when(mockMixTape.getPlayListById(PLAYLIST_ID)).thenReturn(mockPlayList);

    final List<String> songIds = new LinkedList<>();
    when(mockPlayList.getSongIds()).thenReturn(songIds);

    addSongToPlayList.execute(mockMixTape);

    assertTrue(songIds.contains(SONG_ID));
  }
}

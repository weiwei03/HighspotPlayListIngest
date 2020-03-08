package com.highspot.playlist.action;

import com.highspot.playlist.exception.NotFoundException;
import com.highspot.playlist.model.MixTape;
import com.highspot.playlist.model.PlayList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RemovePlayListTest {
  private static final String PLAYLIST_ID = "3";

  @Mock
  private MixTape mockMixTape;

  private RemovePlayList removePlayList;

  @Before
  public void setup() throws Exception {
    removePlayList = new RemovePlayList();
    removePlayList.setAction("remove_playlist");
    removePlayList.setPlayListId(PLAYLIST_ID);
  }

  @Test (expected = NotFoundException.class)
  public void testExecute_PlayListNotExist() throws Exception {
    when(mockMixTape.getPlayListById(PLAYLIST_ID)).thenReturn(null);
    removePlayList.execute(mockMixTape);
  }

  @Test
  public void testExecute_PlayListRemoved() throws Exception {
    final PlayList mockPlayList = mock(PlayList.class);
    when(mockMixTape.getPlayListById(PLAYLIST_ID)).thenReturn(mockPlayList);

    final List<PlayList> playlists = new ArrayList<>();
    playlists.add(mockPlayList);
    when(mockMixTape.getPlayLists()).thenReturn(playlists);

    removePlayList.execute(mockMixTape);
    assertFalse(playlists.contains(mockPlayList));
  }
}

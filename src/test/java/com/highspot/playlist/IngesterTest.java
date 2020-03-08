package com.highspot.playlist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.highspot.playlist.action.Action;
import com.highspot.playlist.action.RemovePlayList;
import com.highspot.playlist.exception.NotFoundException;
import com.highspot.playlist.model.MixTape;
import com.highspot.playlist.model.PlayList;
import com.highspot.playlist.processor.SimpleActionProcessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IngesterTest {
  @Mock
  private File mockInputFile;

  @Mock
  private File mockChangeFile;

  @Mock
  private File mockOutputFile;

  private static final String PLAY_LIST_ID = "randomPlayListId";
  private PlayList playList = new PlayList(PLAY_LIST_ID, "randomUserId",
                                           Collections.singletonList("randomSongId"));
  private MixTape mixTape = new MixTape();
  private Action[] actions = new Action[1];

  @Mock
  private ObjectMapper mockObjectMapper;

  @Mock
  private ObjectWriter mockObjectWriter;

  @Mock
  private SimpleActionProcessor mockActionProcessor;

  private Ingester ingester;

  @Before
  public void setup() {
    ingester = new Ingester(mockObjectMapper, mockActionProcessor);
    ingester.setInputFile(mockInputFile);
    ingester.setChangeFile(mockChangeFile);
    ingester.setOutputFile(mockOutputFile);

    final List<PlayList> playLists = new ArrayList<>();
    playLists.add(playList);
    mixTape.setPlayLists(playLists);

    final RemovePlayList removePlayList = new RemovePlayList();
    removePlayList.setPlayListId(PLAY_LIST_ID);
    actions[0] = removePlayList;

    when(mockObjectMapper.writerWithDefaultPrettyPrinter()).thenReturn(mockObjectWriter);
  }

  @Test
  public void testCall_LoadMixTapeFileFailed() throws Exception {
    when(mockObjectMapper.readValue(mockInputFile, MixTape.class)).thenThrow(new IOException("load file error"));
    final int status = ingester.call();
    assertNotEquals( 0, status);
  }

  @Test
  public void testCall_LoadChangesFileFailed() throws Exception {
    when(mockObjectMapper.readValue(mockInputFile, MixTape.class)).thenReturn(mixTape);
    when(mockObjectMapper.readValue(mockChangeFile, Action[].class)).thenThrow(new IOException("load file error"));

    final int status = ingester.call();
    assertNotEquals( 0, status);
  }

  @Test
  public void testCall_ProcessActionsFailed() throws Exception {
    when(mockObjectMapper.readValue(mockInputFile, MixTape.class)).thenReturn(mixTape);
    when(mockObjectMapper.readValue(mockChangeFile, Action[].class)).thenReturn(actions);
    doThrow(new NotFoundException("remove_playlist", "error"))
      .when(mockActionProcessor).process(mixTape, actions);

    final int status = ingester.call();
    assertNotEquals( 0, status);
  }

  @Test
  public void testCall_SaveResultFailed() throws Exception {
    when(mockObjectMapper.readValue(mockInputFile, MixTape.class)).thenReturn(mixTape);
    when(mockObjectMapper.readValue(mockChangeFile, Action[].class)).thenReturn(actions);
    doThrow(new IOException("io error")).when(mockObjectWriter)
                                        .writeValue(mockOutputFile, mixTape);

    final int status = ingester.call();
    assertNotEquals( 0, status);
  }

  @Test
  public void testCall_Succeed() throws Exception {
    when(mockObjectMapper.readValue(mockInputFile, MixTape.class)).thenReturn(mixTape);
    when(mockObjectMapper.readValue(mockChangeFile, Action[].class)).thenReturn(actions);
    doCallRealMethod().when(mockActionProcessor).process(mixTape, actions);
    doNothing().when(mockObjectWriter)
               .writeValue(mockOutputFile, mixTape);

    final int status = ingester.call();
    assertNull(mixTape.getPlayListById(PLAY_LIST_ID));
    assertEquals( 0, status);
  }

  private File getResource(final String fileName) throws Exception {
    final ClassLoader classLoader = getClass().getClassLoader();
    final URL resource = classLoader.getResource(fileName);
    assertNotNull(resource);
    return new File(resource.toURI());
  }
}

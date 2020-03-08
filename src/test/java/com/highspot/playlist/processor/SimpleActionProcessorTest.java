package com.highspot.playlist.processor;

import com.highspot.playlist.action.Action;
import com.highspot.playlist.model.MixTape;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SimpleActionProcessorTest {
  @Mock
  private MixTape mockMixTape;

  @Mock
  private Action mockAction;

  private Action[] actions = new Action[1];

  private SimpleActionProcessor actionProcessor = new SimpleActionProcessor();

  @Before
  public void setup() {
    actions[0] = mockAction;
  }

  @Test
  public void testProcess_succeed() throws Exception {
    doNothing().when(mockAction).execute(mockMixTape);
    actionProcessor.process(mockMixTape, actions);
    verify(mockAction).execute(mockMixTape);
  }
}

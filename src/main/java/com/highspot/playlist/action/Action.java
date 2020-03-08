package com.highspot.playlist.action;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.highspot.playlist.model.MixTape;

/**
 * interface for Action which will be applied on MixTape object.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "action")
@JsonSubTypes({
  @JsonSubTypes.Type(value = AddPlayList.class, name = "add_playlist"),
  @JsonSubTypes.Type(value = RemovePlayList.class, name = "remove_playlist"),
  @JsonSubTypes.Type(value = AddSongToPlayList.class, name = "add_song")
})
public interface Action {
  /**
   * execute action to change mixTape file.
   * @param mixTape a MixTape object representing content of MixTape.json.
   * @throws Exception exception.
   */
  void execute(final MixTape mixTape) throws Exception;
}

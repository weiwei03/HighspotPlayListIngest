package com.highspot.playlist.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Date model for Play List.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayList {
  private String id;
  @JsonProperty("user_id")
  private String userId;
  @JsonProperty("song_ids")
  private List<String> songIds;
}

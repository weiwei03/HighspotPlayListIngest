package com.highspot.playlist.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Date model for Song.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Song {
  private String id;
  private String artist;
  private String title;
}

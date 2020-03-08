package com.highspot.playlist.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Date model for User.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
  private String id;
  private String name;
}

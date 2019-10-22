package com.waes.compare.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comparison {

  @Id
  private String id;
  private String left;
  private String right;

  public Comparison(String id) {
    this.id = id;
  }
}

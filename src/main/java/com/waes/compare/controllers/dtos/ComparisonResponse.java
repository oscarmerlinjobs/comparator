package com.waes.compare.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ComparisonResponse {
  @JsonProperty("result")
  private String comparisonResult;

  @JsonInclude(Include.NON_NULL)
  @JsonProperty("diffs")
  private List<ComparisonDetailResponse> details;
}


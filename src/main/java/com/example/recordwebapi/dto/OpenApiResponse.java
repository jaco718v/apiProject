package com.example.recordwebapi.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class OpenApiResponse {
  public String id;
  public String object;
  public long created;
  public String model;
  public List<Choice> choices;
  public Usage usage;

  public static class Choice {
    public String text;
    public int index;
    public int logprobs;
    public String finish_reason;
  }

  public static class Usage {
    @JsonProperty("prompt_tokens")
    public int promptTokens;
    @JsonProperty("completion_tokens")
    public int completionTokens;
    @JsonProperty("total_tokens")
    public int totalTokens;
  }
}
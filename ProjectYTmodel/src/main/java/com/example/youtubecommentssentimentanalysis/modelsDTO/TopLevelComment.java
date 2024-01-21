package com.example.youtubecommentssentimentanalysis.modelsDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TopLevelComment {

   @JsonProperty("snippet")
   private SnippetLevelComment snippetLevelComment;
}

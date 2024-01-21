package com.example.youtubecommentssentimentanalysis.modelsDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseModel {

    @JsonProperty("items")
    private List<Items> items;

}

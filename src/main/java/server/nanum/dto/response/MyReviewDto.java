package server.nanum.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public record MyReviewDto(
        Long id,
        String name,
        String imgUrl,
        Float rating,
        String comment){

}

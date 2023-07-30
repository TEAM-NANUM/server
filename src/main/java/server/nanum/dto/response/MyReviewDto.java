package server.nanum.dto.response;

public record MyReviewDto(
        Long id,
        String name,
        String imgUrl,
        Float rating,
        String comment){

}

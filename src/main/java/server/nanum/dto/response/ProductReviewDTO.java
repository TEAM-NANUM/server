package server.nanum.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE) // 유틸리티 클래스이기 때문에 인스턴스화 방지
public class ProductReviewDTO {
    @Builder
    @Getter
    @JsonPropertyOrder({"id","username","rating","comment"})
    public static class ReviewListItem {
        @Schema(example = "1",description = "리뷰 번호")
        private Long id;
        @Schema(example = "나눔이",description = "리뷰 작성자명")
        private String username;
        @Schema(example = "5.0",description = "상품 별점")
        private Float rating;
        @Schema(example = "음식이 맛있어요",description = "상품 후기")
        private String comment;
        @Schema(example = "9999-99-99 99:99:99.999999",description = "생성 날짜")
        @JsonProperty("created_at")
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    public static class ReviewList {
        @Schema(description = "리뷰 정보")
        private List<ReviewListItem> reviews;
    }

}

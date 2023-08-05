package server.nanum.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE) // 유틸리티 클래스이기 때문에 인스턴스화 방지
public class ProductReviewDTO {
    @Builder
    @Getter
    public static class ReviewListItem {
        private Long id;
        private String username;
        private Float rating;
        private String comment;
    }

    @Builder
    @Getter
    public static class ReviewList {
        private List<ReviewListItem> reviews;
    }

}

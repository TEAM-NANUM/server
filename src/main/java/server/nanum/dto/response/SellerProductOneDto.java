package server.nanum.dto.response;

public record SellerProductOneDto(
        Long productId,
        String name,
        String imgUrl,
        Integer unit,
        Integer price) {
}

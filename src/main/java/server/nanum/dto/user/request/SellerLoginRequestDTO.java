package server.nanum.dto.user.request;

public record SellerLoginRequestDTO(String email, String password)
        implements UserLoginRequestDTO{
}

package server.nanum.annotation;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@AuthenticationPrincipal(expression = "#this instanceof T(server.nanum.security.custom.SellerWrapper) ? #this.getSeller() : (#this instanceof T(server.nanum.security.custom.UserWrapper) ? #this.getUser() : (#this != 'anonymousUser' ? #this : null))")
public @interface CurrentUser {
}



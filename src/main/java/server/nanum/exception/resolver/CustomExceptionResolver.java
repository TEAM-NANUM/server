package server.nanum.exception.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import server.nanum.utils.WebhookExceptionHandler;


@RequiredArgsConstructor
public class CustomExceptionResolver implements HandlerExceptionResolver {
    private final WebhookExceptionHandler webhookExceptionHandler;

    @Override
    public ModelAndView resolveException(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex) {

        webhookExceptionHandler.sendExceptionWithDiscord(ex);

        return null;
    }
}
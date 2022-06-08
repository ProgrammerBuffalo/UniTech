package az.unibank.unitech.filter;

import az.unibank.unitech.entity.UserToken;
import az.unibank.unitech.exception.constant.ErrorConstants;
import az.unibank.unitech.exception.RestException;
import az.unibank.unitech.repository.TokenRepository;
import az.unibank.unitech.util.FilterExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Component
public class TokenFilter extends OncePerRequestFilter {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private FilterExceptionUtil filterExceptionUtil;

    private final String[] whiteList = new String[] {
            "/h2-console/**",
            "/authentication/**"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenString = request.getHeader("token");
        UUID tokenUUID = UUID.fromString(tokenString);

        try {

            UserToken userToken = tokenRepository.findById(tokenUUID)
                    .orElseThrow(() -> RestException.of(ErrorConstants.TOKEN_NOT_FOUND));

            if (userToken.getRevoked())
                throw RestException.of(ErrorConstants.TOKEN_IS_REVOKED);

            if (LocalDateTime.now().isAfter(userToken.getExpireAt()))
                throw RestException.of(ErrorConstants.TOKEN_IS_EXPIRED);
        }
        catch (RestException ex) {
            filterExceptionUtil.prepareException(response, ex);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        return Arrays.stream(whiteList)
                .anyMatch(s -> antPathMatcher.match(s, request.getRequestURI()));
    }
}

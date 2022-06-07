package az.unibank.unitech.filter;

import az.unibank.unitech.entity.UserToken;
import az.unibank.unitech.repository.TokenRepository;
import az.unibank.unitech.util.FilterExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.Optional;
import java.util.UUID;

@Component
public class TokenFilter extends OncePerRequestFilter {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private FilterExceptionUtil filterExceptionUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenString = request.getHeader("token");
        UUID tokenUUID = UUID.fromString(tokenString);

        try {

            UserToken userToken = tokenRepository.findById(tokenUUID)
                    .orElseThrow(() -> new Exception());

            if (userToken.getRevoked())
                throw new Exception();

            if (LocalDateTime.now().isAfter(userToken.getExpireAt()))
                throw new Exception();
        }
        catch (Exception ex) {
            filterExceptionUtil.prepareException(response, HttpStatus.BAD_REQUEST, ex);
            return;
        }

        filterChain.doFilter(request, response);
    }

}

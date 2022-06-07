package az.unibank.unitech.util;

import az.unibank.unitech.dto.base.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class FilterExceptionUtil {

    @Autowired
    private ObjectMapper objectMapper;

    public void prepareException(HttpServletResponse response, HttpStatus status, Throwable throwable) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String responseAsString = objectMapper.writeValueAsString(BaseResponse.fault(throwable.getMessage(), status.value()));

        response.getWriter().write(responseAsString);
    }
}

package az.unibank.unitech.util;

import az.unibank.unitech.dto.base.BaseResponse;
import az.unibank.unitech.exception.RestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class FilterExceptionUtil {

    @Autowired
    private ObjectMapper objectMapper;

    public void prepareException(HttpServletResponse response, RestException exception) throws IOException {
        response.setStatus(exception.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String responseAsString = objectMapper.writeValueAsString(BaseResponse.fault(exception.getMessage(), response.getStatus()));

        response.getWriter().write(responseAsString);
    }
}

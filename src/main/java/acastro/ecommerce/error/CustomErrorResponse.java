package acastro.ecommerce.error;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomErrorResponse {
    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    public CustomErrorResponse(HttpStatus httpStatus, String message, WebRequest path) {
        this.timestamp=LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        this.status=httpStatus.value();
        this.error=httpStatus.getReasonPhrase();
        this.message=message;
        this.path=path.getDescription(false).substring(4);

    }
}

package response;

import exception.CommonException;
import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String code;
    private final String content;

    public ErrorResponse(CommonException exception){
        this.code = exception.getCode();
        this.content = exception.getContent();
    }
}

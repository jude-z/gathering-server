package exception;

import lombok.Getter;

@Getter
public class CommonException extends RuntimeException{
    private final String code;
    private final String content;

    public CommonException(Status status){
        this.code = status.getCode();
        this.content = status.getContent();
    }
}

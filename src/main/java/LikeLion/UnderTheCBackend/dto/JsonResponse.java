package LikeLion.UnderTheCBackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JsonResponse {
    private String message;
    private String status;
    private Object data;

    public JsonResponse(String message, String status, Object data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }
}

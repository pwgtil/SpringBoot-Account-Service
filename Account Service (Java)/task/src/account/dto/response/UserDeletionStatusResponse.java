package account.dto.response;

public class UserDeletionStatusResponse {
    public String user;

    public String status;

    public UserDeletionStatusResponse(String user, String status) {
        this.user = user;
        this.status = status;
    }
}

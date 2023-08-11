package account.dto.response;

public class UserDeletionStatusDTO {
    public String user;

    public String status;

    public UserDeletionStatusDTO(String user, String status) {
        this.user = user;
        this.status = status;
    }
}

package account.service;

public interface UserServiceChangeAccess {
    void changeAccess(String username, String operation);

    int getFailedLoginAttempts(String username);

    void setFailedLoginAttempts(int failedAttempts, String username);
}

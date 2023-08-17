package account.entity.enums;

public enum ActionType {
    CREATE_USER,        // A user has been successfully registered                          -> UserService.signUp()
    CHANGE_PASSWORD,    // A user has changed the password successfully                     -> UserService.changePass()
    ACCESS_DENIED,      // A user is trying to access a resource without access rights      -> CustomAccessDeniedHandler.handle()
    LOGIN_FAILED,       // Failed authentication                                            -> RestAuthenticationEntryPoint.commence()
    GRANT_ROLE,         // A role is granted to a user                                      -> UserService.changeRole()
    REMOVE_ROLE,        // A role has been revoked                                          -> UserService.changeRole()
    LOCK_USER,          // The Administrator has locked the user                            -> UserService.changeAccess()
    UNLOCK_USER,        // The Administrator has unlocked a user                            -> UserService.changeAccess()
    DELETE_USER,        // The Administrator has deleted a user                             -> UserService.deleteUser()
    BRUTE_FORCE         // A user has been blocked on suspicion of a brute force attack     -> TODO()
}

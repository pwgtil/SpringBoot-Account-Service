package account.presentation;

import java.time.LocalDateTime;

public record CustomErrorMessage(LocalDateTime timestamp,
                                 int status,
                                 String error,
                                 String message,
                                 String path) { }

package account.presentation.custom_messages;

import java.time.LocalDateTime;

public record CustomErrorMessage(LocalDateTime timestamp,
                                 int status,
                                 String error,
                                 String message,
                                 String path) { }

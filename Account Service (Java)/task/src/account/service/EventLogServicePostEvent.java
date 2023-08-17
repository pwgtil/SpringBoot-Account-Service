package account.service;

import account.entity.enums.ActionType;
import jakarta.validation.constraints.NotEmpty;

public interface EventLogServicePostEvent {
    void postEvent(ActionType action, @NotEmpty String subject, @NotEmpty String object, String path);
}

package account.controller;

import account.service.EventLogService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventLogController {
    private final EventLogService eventLogService;

    public EventLogController(EventLogService eventLogService) {
        this.eventLogService = eventLogService;
    }
}

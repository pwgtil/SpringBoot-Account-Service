package account.controller;

import account.controller.routing.Events;
import account.entity.EventLog;
import account.service.EventLogService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EventLogController {
    private final EventLogService eventLogService;

    public EventLogController(EventLogService eventLogService) {
        this.eventLogService = eventLogService;
    }

    @GetMapping(Events.PATH)
    @ResponseStatus(HttpStatus.OK)
    public List<EventLog> getEvents() {
        // todo(
        //  1. Actually get the list of events )
        return List.of();
    }
}

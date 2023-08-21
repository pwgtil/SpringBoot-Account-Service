package account.service;

import account.entity.EventLog;
import account.entity.enums.ActionType;
import account.repository.EventLogRepository;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventLogService implements EventLogServicePostEvent, EventLogServiceGetAllEvents {

    private final EventLogRepository eventLogRepository;

    public EventLogService(EventLogRepository eventLogRepository) {
        this.eventLogRepository = eventLogRepository;
    }

    @Override
    public void postEvent(@NotEmpty ActionType action, @NotEmpty String subject, @NotEmpty String object, String path) {

        if (path == null || path.isEmpty()) {
            path = ServletUriComponentsBuilder.fromCurrentRequest().toUriString()
                    .substring(ServletUriComponentsBuilder.fromCurrentServletMapping().toUriString().length());
            if (object == null || object.isEmpty()) {
                object = path;
            }
        }

        EventLog eventLog = EventLog.builder()
                .date(LocalDateTime.now())
                .action(action)
                .subject(subject)
                .object(object)
                .path(path)
                .build();

        eventLogRepository.save(eventLog);
    }

    @Override
    public List<EventLog> getAllEvents() {
        return eventLogRepository.findAll();
    }
}

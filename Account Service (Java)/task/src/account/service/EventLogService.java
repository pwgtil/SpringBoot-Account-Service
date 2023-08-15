package account.service;

import account.entity.EventLog;
import account.entity.enums.ActionType;
import account.repository.EventLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EventLogService {
    private final EventLogRepository eventLogRepository;


    public EventLogService(EventLogRepository eventLogRepository) {
        this.eventLogRepository = eventLogRepository;
//        EventLog log = new EventLog(LocalDateTime.now(), ActionType.ACCESS_DENIED, "Test1", "Test2");
//        eventLogRepository.save(log);
    }
}

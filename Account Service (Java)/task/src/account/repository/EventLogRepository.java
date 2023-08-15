package account.repository;

import account.entity.EventLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventLogRepository extends CrudRepository<EventLog, Long> {
}

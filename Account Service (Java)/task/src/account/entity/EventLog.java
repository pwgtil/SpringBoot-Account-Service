package account.entity;

import account.entity.enums.ActionType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "event_logs")
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EventLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date", columnDefinition = "timestamp(0)")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime date; // when

    @Column(name = "action")
    @Enumerated(EnumType.STRING)
    private ActionType action; // what happened

    @Column(name = "subject")
    private String subject = "Anonymous"; // who did that (if possible to determine, otherwise "Anonymous")

    @Column(name = "object")
    private String object; // the object on which the action was performed

    @Column(name = "path")
    private String path; // api called


    public EventLog(Long id, LocalDateTime date, ActionType action, String subject, String object, String path) {
        this.id = id;
        this.date = date;
        this.action = action;
        this.subject = subject;
        this.object = object;
        this.path = path;
    }
}

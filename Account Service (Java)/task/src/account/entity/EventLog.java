package account.entity;

import account.entity.enums.ActionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "event_logs")
@NoArgsConstructor
@Getter
@Setter
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


    public EventLog(LocalDateTime date, ActionType action, String subject, String object, String path) {
        this.date = date;
        this.action = action;
        this.subject = subject;
        this.object = object;
        this.path = path;
    }

    public EventLog(LocalDateTime date, ActionType action, String object, String path) {
        this.date = date;
        this.action = action;
        this.object = object;
        this.path = path;
    }
}

package study.batch.springbatchtutorial.core.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MigrationProcess {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "STATUS", length = 30)
    @Enumerated(EnumType.STRING)
    private MigrationStatus status;

    private String jobId;

    private String errorLog;


    @Getter
    @AllArgsConstructor
    public enum MigrationStatus {
        SUCCESS,
        FAIL
    }
}

package study.batch.springbatchtutorial.core.domain.process;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@ToString
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MigrationResult {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "STATUS", length = 30)
    @Enumerated(EnumType.STRING)
    private MigrationStatus status;

    private Long resourceId;

    @Column(columnDefinition = "TEXT")
    private String errorLog;

    private Date txStartTime;

    private Date txEndTime;

    public void reset() {
        this.status = MigrationStatus.CREATED;
        this.txStartTime = new Date();
        this.txEndTime = null;
    }


    @Getter
    @AllArgsConstructor
    private enum MigrationStatus {
        SUCCESS,
        FAIL,
        CREATED,
    }

    public static MigrationResult create(Long resourceId){
        MigrationResult migrationResult = new MigrationResult();
        migrationResult.status = MigrationStatus.CREATED;
        migrationResult.resourceId = resourceId;
        return migrationResult;
    }

    public void isFail(String errorLog) {
        this.status = MigrationStatus.FAIL;
        this.errorLog = errorLog;
        this.txEndTime = new Date();
    }

    public void isSuccess() {
        this.status = MigrationStatus.SUCCESS;
        this.txEndTime = new Date();
    }

}

package study.batch.springbatchtutorial.core.domain.process;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@ToString
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MigrationProcess {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "STATUS", length = 30)
    @Enumerated(EnumType.STRING)
    private MigrationStatus status;

    private Long resourceId;

    private String errorLog;

    private Date txStartTime;

    private Date txEndTime;

    private String jobId;


    @Getter
    @AllArgsConstructor
    private enum MigrationStatus {
        SUCCESS,
        FAIL,
        CREATED,
    }

    public static MigrationProcess create(Long resourceId){
        MigrationProcess migrationProcess = new MigrationProcess();
        migrationProcess.status = MigrationStatus.CREATED;
        migrationProcess.resourceId = resourceId;
        migrationProcess.txStartTime = new Date();
        return migrationProcess;
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

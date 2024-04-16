package study.batch.springbatchtutorial.job.migration.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MigrationChunkListener implements ChunkListener {
    @Override
    public void beforeChunk(ChunkContext context) {
        StepContext stepContext = context.getStepContext();
        StepExecution stepExecution = stepContext.getStepExecution();
        log.info("###### beforeChunk read count: {}", stepExecution.getReadCount());
    }

    @Override
    public void afterChunk(ChunkContext context) {
        StepContext stepContext = context.getStepContext();
        StepExecution stepExecution = stepContext.getStepExecution();
        log.info("###### afterChunk read count: {}", stepExecution.getReadCount());
        log.info("###### afterChunk commit count: {}", stepExecution.getCommitCount());
    }

    @Override
    public void afterChunkError(ChunkContext context) {
        StepContext stepContext = context.getStepContext();
        StepExecution stepExecution = stepContext.getStepExecution();
        log.warn("###### afterChunkError read count: {}", stepExecution.getReadCount());
        log.warn("###### afterChunkError rollback Count : {}", stepExecution.getRollbackCount());
    }
}

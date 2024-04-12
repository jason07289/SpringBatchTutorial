package study.batch.springbatchtutorial.job.validatedparam.joblistener;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.batch.springbatchtutorial.job.validatedparam.joblistener.listener.JobLoggerListener;

/**
 * desc: 기본
 * program args: --spring.batch.job.names=jobListenerJob
 */
@Configuration
@RequiredArgsConstructor
public class JobListenerJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job jobListenerJob() {
        return jobBuilderFactory.get("jobListenerJob")
                .incrementer(new RunIdIncrementer())
                .listener(new JobLoggerListener())
                .start(jobListenerStep())
                .build();
    }

    @JobScope
    @Bean
    public Step jobListenerStep(){
        return stepBuilderFactory.get("jobListenerStep")
                .tasklet(jobListenerTasklet())
                .build();
    }

    @StepScope
    @Bean
    public Tasklet jobListenerTasklet() {
        return (stepContribution, chunkContext) -> {
            System.out.println("==========================");
            System.out.println("jobListener");
            System.out.println("==========================");
            return RepeatStatus.FINISHED;
//            throw new RuntimeException("job is failed");
        };
    }
}

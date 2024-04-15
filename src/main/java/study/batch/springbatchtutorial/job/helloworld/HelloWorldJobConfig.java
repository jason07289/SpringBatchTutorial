package study.batch.springbatchtutorial.job.helloworld;

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

/**
 * desc: 기본
 * program args: --spring.batch.job.names=helloWorldJob
 */
@Configuration
@RequiredArgsConstructor
public class HelloWorldJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job hellowWorldJob() {
        return jobBuilderFactory.get("helloWorldJob")
                .incrementer(new RunIdIncrementer())
                .start(helloWorldStep())
                .build();
    }

    @JobScope
    @Bean
    public Step helloWorldStep(){
        return stepBuilderFactory.get("helloWorldStep")
                .tasklet(helloWorldTasklet())
                .build();
    }

    @StepScope
    @Bean
    public Tasklet helloWorldTasklet() {
        return (stepContribution, chunkContext) -> {
            System.out.println("==========================");
            System.out.println("hello world spring batch");
            System.out.println("==========================");

            return RepeatStatus.FINISHED;
        };
    }
}

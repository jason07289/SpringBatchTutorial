package study.batch.springbatchtutorial.job.migration.step.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import study.batch.springbatchtutorial.core.domain.accounts.Accounts;
import study.batch.springbatchtutorial.core.domain.orders.Orders;
@Component
@RequiredArgsConstructor
public class OrdersProcessorStep {
    @StepScope
    @Bean
    public ItemProcessor<Orders, Accounts> jpaOrdersProcessor(){
        //읽어온 Orders를 Accounts로 매핑
        return Accounts::create;
    }
}

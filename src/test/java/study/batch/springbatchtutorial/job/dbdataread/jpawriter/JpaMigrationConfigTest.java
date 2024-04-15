package study.batch.springbatchtutorial.job.dbdataread.jpawriter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import study.batch.springbatchtutorial.SpringBatchTestConfig;
import study.batch.springbatchtutorial.core.domain.accounts.AccountsRepository;
import study.batch.springbatchtutorial.core.domain.orders.Orders;
import study.batch.springbatchtutorial.core.domain.orders.OrdersRepository;
import study.batch.springbatchtutorial.job.dbdataread.jpawriter.JpaMigrationConfig;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBatchTest
@SpringBootTest(classes = {SpringBatchTestConfig.class, JpaMigrationConfig.class})
class JpaMigrationConfigTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    @AfterEach
    public void cleanDB(){
        ordersRepository.deleteAll();
        accountsRepository.deleteAll();
    }

    @Test
    public void success_noData() throws Exception {
        //when
        JobExecution execution = jobLauncherTestUtils.launchJob();

        //then
        Assertions.assertEquals(execution.getExitStatus(), ExitStatus.COMPLETED);
        Assertions.assertEquals(0, accountsRepository.count());

    }

    @Test
    public void success_whenExistData() throws Exception {
        //given
        Orders orders1 = new Orders(null, "kakao gift", 15000, new Date());
        Orders orders2 = new Orders(null, "naver gift", 15000, new Date());
        List<Orders> ordersList = new ArrayList<>();
        ordersList.add(orders1);
        ordersList.add(orders2);

        ordersRepository.saveAll(ordersList);

        // when
        JobExecution execution = jobLauncherTestUtils.launchJob();

        //then
        Assertions.assertEquals(execution.getExitStatus(), ExitStatus.COMPLETED);
        Assertions.assertEquals(ordersList.size(), accountsRepository.count());
    }
}
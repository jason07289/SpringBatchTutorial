package study.batch.springbatchtutorial.core.domain.accounts;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
@Getter
@Entity
@ToString
public class Accounts {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String orderItem;
    private Integer price;
    private Date orderDate;
    private Date accountDate;
}
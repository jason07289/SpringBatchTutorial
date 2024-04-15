package study.batch.springbatchtutorial.core.domain.accounts;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import study.batch.springbatchtutorial.core.domain.orders.Orders;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
@Getter
@Entity
@ToString
@NoArgsConstructor
public class Accounts {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String orderItem;
    private Integer price;
    private Date orderDate;
    private Date accountDate;

    public static Accounts create(Orders orders){
        Accounts accounts = new Accounts();
        accounts.id = orders.getId();
        accounts.orderItem = orders.getOrderItem();
        accounts.price = orders.getPrice();
        accounts.orderDate = orders.getOrderDate();
        accounts.accountDate = new Date();
        return accounts;
    }
}

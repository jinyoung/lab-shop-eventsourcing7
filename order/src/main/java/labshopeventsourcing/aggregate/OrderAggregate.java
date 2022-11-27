package labshopeventsourcing.aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.*;

import java.math.BigDecimal;
import java.util.List;
import labshopeventsourcing.command.*;
import labshopeventsourcing.event.*;
import lombok.Data;
import lombok.ToString;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

//<<< DDD / Aggregate Root
@Aggregate
@Data
@ToString
public class OrderAggregate {

    @AggregateIdentifier
    private Long id;

    private String productId;
    private Integer qty;
    private String customerId;
    private BigDecimal amount;
    private String status;
    private String address;

    public OrderAggregate() {}

    @CommandHandler
    public OrderAggregate(OrderCommand command) {
        OrderPlacedEvent event = new OrderPlacedEvent();
        BeanUtils.copyProperties(command, event);

        event.setId(createUUID());

        apply(event);
    }

    private Long createUUID() {
        return System.currentTimeMillis();
    }

    @CommandHandler
    public void handle(CancelCommand command) {
        OrderCancelledEvent event = new OrderCancelledEvent();
        BeanUtils.copyProperties(command, event);

        apply(event);
    }

    @CommandHandler
    public void handle(UpdateStatusCommand command) {
        OrderDeliveryStartedEvent event = new OrderDeliveryStartedEvent();
        BeanUtils.copyProperties(command, event);

        apply(event);
    }

    //<<< EDA / Event Sourcing

    @EventSourcingHandler
    public void on(OrderPlacedEvent event) {
        BeanUtils.copyProperties(event, this);
    }

    //<<< EDA / Event Sourcing

    @EventSourcingHandler
    public void on(OrderCancelledEvent event) {
        BeanUtils.copyProperties(event, this);
    }

    //<<< EDA / Event Sourcing

    @EventSourcingHandler
    public void on(OrderDeliveryStartedEvent event) {
        BeanUtils.copyProperties(event, this);
    }
    //>>> EDA / Event Sourcing

}
//>>> DDD / Aggregate Root

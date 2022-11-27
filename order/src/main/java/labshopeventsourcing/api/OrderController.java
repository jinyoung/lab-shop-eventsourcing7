package labshopeventsourcing.api;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import org.springframework.beans.BeanUtils;


import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;

import java.util.concurrent.CompletableFuture;


import labshopeventsourcing.aggregate.*;
import labshopeventsourcing.command.*;

@RestController
public class OrderController {

  private final CommandGateway commandGateway;
  private final QueryGateway queryGateway;

  public OrderController(CommandGateway commandGateway, QueryGateway queryGateway) {
      this.commandGateway = commandGateway;
      this.queryGateway = queryGateway;
  }

  @RequestMapping(value = "/orders",
        method = RequestMethod.POST
      )
  public CompletableFuture order(@RequestBody OrderCommand orderCommand)
        throws Exception {
      System.out.println("##### /order/order  called #####");

      // send command
      return commandGateway.send(orderCommand)            
            .thenApply(
            id -> {
                  OrderAggregate resource = new OrderAggregate();
                  BeanUtils.copyProperties(orderCommand, resource);

                  resource.setId(id);
                  
                  EntityModel<OrderAggregate> model = EntityModel.of(resource);
                  model
                        .add(Link.of("/orders/" + resource.getId()).withSelfRel());

                  return new ResponseEntity<>(model, HttpStatus.OK);
            }
      );

  }


  @RequestMapping(value = "/orders",
        method = RequestMethod.DELETE
      )
  public CompletableFuture cancel(@RequestBody CancelCommand cancelCommand)
        throws Exception {
      System.out.println("##### /order/cancel  called #####");

      // send command
      return commandGateway.send(cancelCommand)            
            .thenApply(
            id -> {
                  OrderAggregate resource = new OrderAggregate();
                  BeanUtils.copyProperties(cancelCommand, resource);

                  resource.setId(id);
                  
                  EntityModel<OrderAggregate> model = EntityModel.of(resource);
                  model
                        .add(Link.of("/orders/" + resource.getId()).withSelfRel());

                  return new ResponseEntity<>(model, HttpStatus.OK);
            }
      );

  }



  @RequestMapping(value = "/updatestatus",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8")
  public CompletableFuture updateStatus(@RequestBody UpdateStatusCommand updateStatusCommand)
        throws Exception {
      System.out.println("##### /order/updateStatus  called #####");

      // send command
      return commandGateway.send(updateStatusCommand);
  }
}

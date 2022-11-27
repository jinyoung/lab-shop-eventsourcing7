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
public class DeliveryController {

  private final CommandGateway commandGateway;
  private final QueryGateway queryGateway;

  public DeliveryController(CommandGateway commandGateway, QueryGateway queryGateway) {
      this.commandGateway = commandGateway;
      this.queryGateway = queryGateway;
  }


  @RequestMapping(value = "/returndelivery",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8")
  public CompletableFuture returnDelivery(@RequestBody ReturnDeliveryCommand returnDeliveryCommand)
        throws Exception {
      System.out.println("##### /delivery/returnDelivery  called #####");

      // send command
      return commandGateway.send(returnDeliveryCommand);
  }

  @RequestMapping(value = "/deliveries",
        method = RequestMethod.POST
      )
  public CompletableFuture addToDeliveryList(@RequestBody AddToDeliveryListCommand addToDeliveryListCommand)
        throws Exception {
      System.out.println("##### /delivery/addToDeliveryList  called #####");

      // send command
      return commandGateway.send(addToDeliveryListCommand)            
            .thenApply(
            id -> {
                  DeliveryAggregate resource = new DeliveryAggregate();
                  BeanUtils.copyProperties(addToDeliveryListCommand, resource);

                  resource.setId(id);
                  
                  EntityModel<DeliveryAggregate> model = EntityModel.of(resource);
                  model
                        .add(Link.of("/deliveries/" + resource.getId()).withSelfRel());

                  return new ResponseEntity<>(model, HttpStatus.OK);
            }
      );

  }

}

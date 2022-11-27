package labshopeventsourcing.query;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "orderStatuses_", collectionResourceRel = "orderStatuses_")
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {

    @Override
    @RestResource(exported = false)
    void delete(OrderStatus entity);

    @Override
    @RestResource(exported = false)
    void deleteAll();

    @Override
    @RestResource(exported = false)
    void deleteById(Long id);

    @Override
    @RestResource(exported = false)
     <S extends OrderStatus> S save(S entity);


    

    
}

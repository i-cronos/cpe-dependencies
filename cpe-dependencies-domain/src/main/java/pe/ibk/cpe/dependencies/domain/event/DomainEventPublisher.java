package pe.ibk.cpe.dependencies.domain.event;

public interface DomainEventPublisher<T extends DomainEvent> {

    boolean publish(T domainEvent);
}

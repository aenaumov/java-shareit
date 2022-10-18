package ru.practicum.shareit.request;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "requests")
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> getItemRequestsByRequestor_Id(Long requestorId, Sort sort);

    List<ItemRequest> getItemRequestsByRequestorIdIsNot(Long requestorId, Pageable pageable);

}

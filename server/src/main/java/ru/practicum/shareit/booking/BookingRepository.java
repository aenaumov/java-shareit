package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "booking")
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> getBookingsByBooker_Id(Long bookerId, Pageable pageable);

    List<Booking> getBookingsByBooker_IdAndStatusEquals(Long ownerId,
                                                        BookingStatus status,
                                                        Pageable pageable);

    @Query(value = "SELECT b FROM Booking as b where b.booker.id = :userId AND b.start < :time AND b.end > :time")
    List<Booking> getCurrentBookingsForBooker(@Param("userId") Long bookerId,
                                              @Param("time") LocalDateTime timeNow,
                                              Pageable pageable);

    List<Booking> getBookingsByBooker_IdAndStartAfter(Long bookerId,
                                                      LocalDateTime timeNow,
                                                      Pageable pageable);

    List<Booking> getBookingsByBooker_IdAndEndBefore(Long bookerId,
                                                     LocalDateTime timeNow,
                                                     Pageable pageable);

    List<Booking> getBookingsByItem_Owner_Id(Long ownerId, Pageable pageable);

    List<Booking> getBookingsByItem_Owner_IdAndStatusEquals(Long ownerId,
                                                            BookingStatus status,
                                                            Pageable pageable);

    @Query(value = "SELECT b FROM Booking as b where b.item.owner.id = :userId AND b.start < :time AND b.end > :time")
    List<Booking> getCurrentBookingsForOwner(@Param("userId") Long bookerId,
                                             @Param("time") LocalDateTime timeNow,
                                             Pageable pageable);

    List<Booking> getBookingsByItem_Owner_IdAndStartAfter(Long bookerId,
                                                          LocalDateTime timeNow,
                                                          Pageable pageable);

    List<Booking> getBookingsByItem_Owner_IdAndEndBefore(Long bookerId,
                                                         LocalDateTime timeNow,
                                                         Pageable pageable);

    Optional<Booking> findFirstByItem_IdAndStartIsBefore(Long itemId, LocalDateTime start, Sort sort);

    Optional<Booking> findFirstByItem_IdAndStartIsAfter(Long itemId, LocalDateTime start, Sort sort);

    @Query(value = "SELECT b FROM Booking as b where b.booker.id = :bookerId " +
            "AND b.item.id = :itemId " +
            "AND b.status = 'APPROVED' " +
            "AND b.end < :timeNow")
    List<Booking> findApprovedBookingsInPastByUser(@Param("bookerId") Long bookerId,
                                                   @Param("itemId") Long itemId,
                                                   @Param("timeNow") LocalDateTime time);

    @Query(value = "SELECT b FROM Booking as b where b.item.id = :itemId " +
            "AND b.status = 'APPROVED' " +
            "AND b.end >= :startTime ")
    List<Booking> getBookingsByItemIdAndEndTimeIsAfter(@Param("itemId") Long itemId,
                                                       @Param("startTime") LocalDateTime startTime);

}
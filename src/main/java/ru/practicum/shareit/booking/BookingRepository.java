package ru.practicum.shareit.booking;

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

    @Query(value = "SELECT b FROM Booking as b where b.booker.id = :userId")
    List<Booking> getBookingsByBooker(@Param("userId") Long bookerId,
                                      Sort sort);

    @Query(value = "SELECT b FROM Booking as b where b.booker.id = :userId AND b.status = :status")
    List<Booking> getBookingsByBookerAndStatus(@Param("userId") Long ownerId,
                                               @Param("status") BookingStatus status,
                                               Sort sort);

    @Query(value = "SELECT b FROM Booking as b where b.booker.id = :userId AND b.start < :time AND b.end > :time")
    List<Booking> getCurrentBookingsForBooker(@Param("userId") Long bookerId,
                                              @Param("time") LocalDateTime timeNow,
                                              Sort sort);

    @Query(value = "SELECT b FROM Booking as b where b.booker.id = :userId AND b.start > :time")
    List<Booking> getFutureBookingsForBooker(@Param("userId") Long bookerId,
                                             @Param("time") LocalDateTime timeNow,
                                             Sort sort);

    @Query(value = "SELECT b FROM Booking as b where b.booker.id = :userId AND b.end < :time")
    List<Booking> getPastBookingsForBooker(@Param("userId") Long bookerId,
                                           @Param("time") LocalDateTime timeNow,
                                           Sort sort);

    @Query(value = "SELECT b FROM Booking as b where b.item.owner.id = :userId")
    List<Booking> getBookingsByOwner(@Param("userId") Long ownerId,
                                     Sort sort);

    @Query(value = "SELECT b FROM Booking as b where b.item.owner.id = :userId AND b.status = :status")
    List<Booking> getBookingsByItemBYOwnerAndStatus(@Param("userId") Long ownerId,
                                                    @Param("status") BookingStatus status,
                                                    Sort sort);

    @Query(value = "SELECT b FROM Booking as b where b.item.owner.id = :userId AND b.start < :time AND b.end > :time")
    List<Booking> getCurrentBookingsForOwner(@Param("userId") Long bookerId,
                                             @Param("time") LocalDateTime timeNow,
                                             Sort sort);

    @Query(value = "SELECT b FROM Booking as b where b.item.owner.id = :userId AND b.start > :time")
    List<Booking> getFutureBookingsForOwner(@Param("userId") Long bookerId,
                                            @Param("time") LocalDateTime timeNow,
                                            Sort sort);

    @Query(value = "SELECT b FROM Booking as b where b.item.owner.id = :userId AND b.end < :time")
    List<Booking> getPastBookingsForOwner(@Param("userId") Long bookerId,
                                          @Param("time") LocalDateTime timeNow,
                                          Sort sort);

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
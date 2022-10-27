package ru.practicum.shareit.booking;


import java.util.Optional;

/**
 * Параметр state
 * ALL (англ. «все»).
 * CURRENT (англ. «текущие»),
 * **PAST** (англ. «завершённые»),
 * FUTURE (англ. «будущие»),
 * WAITING (англ. «ожидающие подтверждения»),
 * REJECTED (англ. «отклонённые»).
 */

public enum BookingState {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    static Optional<BookingState> from(String state) {
        for (BookingState value : BookingState.values()) {
            if (value.name().equals(state)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }
}

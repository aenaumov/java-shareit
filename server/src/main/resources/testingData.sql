INSERT INTO USERS (USER_ID, USER_NAME, EMAIL)
VALUES (1L, 'user_1', 'user_1@email.com');
INSERT INTO USERS (USER_ID, USER_NAME, EMAIL)
VALUES (2L, 'user_2', 'user_2@email.com');
INSERT INTO USERS (USER_ID, USER_NAME, EMAIL)
VALUES (3L, 'user_3', 'user_3@email.com');

INSERT INTO ITEMS (ITEM_ID, ID_OWNER, ITEM_NAME, ITEM_DESCRIPTION, ITEM_AVAILABLE, REQUEST_ID)
VALUES (1L, 1L, 'item_1', 'description_1', true, null);
INSERT INTO ITEMS (ITEM_ID, ID_OWNER, ITEM_NAME, ITEM_DESCRIPTION, ITEM_AVAILABLE, REQUEST_ID)
VALUES (2L, 1L, 'item_1', 'tryToFindMe', true, null);

INSERT INTO BOOKINGS (BOOKING_ID, START_BOOKING, END_BOOKING, ITEM_ID, USER_ID, STATUS)
VALUES (1L, CURRENT_TIMESTAMP(0) - 86400, CURRENT_TIMESTAMP(0) + 86400, 1L, 2L, 'APPROVED');
INSERT INTO BOOKINGS (BOOKING_ID, START_BOOKING, END_BOOKING, ITEM_ID, USER_ID, STATUS)
VALUES (2L, CURRENT_TIMESTAMP(0) - 86400, CURRENT_TIMESTAMP(0) - 80000, 1L, 3L, 'APPROVED');
INSERT INTO BOOKINGS (BOOKING_ID, START_BOOKING, END_BOOKING, ITEM_ID, USER_ID, STATUS)
VALUES (3L, CURRENT_TIMESTAMP(0) + 86000, CURRENT_TIMESTAMP(0) + 86400, 1L, 3L, 'APPROVED');
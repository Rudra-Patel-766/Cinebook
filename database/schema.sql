
CREATE DATABASE IF NOT EXISTS movie_booking_db;
USE movie_booking_db;

-- ── 1. User ──────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS User (
    UserID   INT AUTO_INCREMENT PRIMARY KEY,
    Name     VARCHAR(100) NOT NULL,
    Email    VARCHAR(100) UNIQUE NOT NULL,
    Phone    VARCHAR(15),
    Password VARCHAR(100) NOT NULL,
    Role     ENUM('ADMIN','CUSTOMER') DEFAULT 'CUSTOMER'
);

-- ── 2. Theatre ───────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS Theatre (
    TheatreID INT AUTO_INCREMENT PRIMARY KEY,
    Name      VARCHAR(100) NOT NULL,
    Location  VARCHAR(150)
);

-- ── 3. Screen ────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS Screen (
    ScreenID     INT AUTO_INCREMENT PRIMARY KEY,
    ScreenNumber INT NOT NULL,
    TheatreID    INT,
    FOREIGN KEY (TheatreID) REFERENCES Theatre(TheatreID) ON DELETE CASCADE
);

-- ── 4. Seat ──────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS Seat (
    SeatID     INT AUTO_INCREMENT PRIMARY KEY,
    SeatNumber VARCHAR(10) NOT NULL,
    SeatType   ENUM('STANDARD','PREMIUM','RECLINER') DEFAULT 'STANDARD',
    ScreenID   INT,
    FOREIGN KEY (ScreenID) REFERENCES Screen(ScreenID) ON DELETE CASCADE
);

-- ── 5. Movie ─────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS Movie (
    MovieID  INT AUTO_INCREMENT PRIMARY KEY,
    Title    VARCHAR(150) NOT NULL,
    Genre    VARCHAR(50),
    Duration INT,          -- in minutes
    Language VARCHAR(50)
);

-- ── 6. Shows ─────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS Shows (
    ShowID      INT AUTO_INCREMENT PRIMARY KEY,
    ShowDate    DATE NOT NULL,
    ShowTime    TIME NOT NULL,
    TicketPrice DECIMAL(8,2) NOT NULL,
    MovieID     INT,
    ScreenID    INT,
    FOREIGN KEY (MovieID)   REFERENCES Movie(MovieID)   ON DELETE CASCADE,
    FOREIGN KEY (ScreenID)  REFERENCES Screen(ScreenID) ON DELETE CASCADE
);

-- ── 7. Booking ───────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS Booking (
    BookingID   INT AUTO_INCREMENT PRIMARY KEY,
    BookingDate DATE NOT NULL,
    Status      ENUM('CONFIRMED','CANCELLED','PENDING') DEFAULT 'PENDING',
    TotalAmount DECIMAL(10,2),
    UserID      INT,
    ShowID      INT,
    SeatID      INT,
    FOREIGN KEY (UserID)  REFERENCES User(UserID)   ON DELETE SET NULL,
    FOREIGN KEY (ShowID)  REFERENCES Shows(ShowID)  ON DELETE CASCADE,
    FOREIGN KEY (SeatID)  REFERENCES Seat(SeatID)   ON DELETE CASCADE
);

-- ── 8. Payment ───────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS Payment (
    PaymentID       INT AUTO_INCREMENT PRIMARY KEY,
    Mode            ENUM('CARD','UPI','NETBANKING') NOT NULL,
    PaymentStatus   ENUM('SUCCESS','FAILED','PENDING','REFUND_INITIATED') DEFAULT 'PENDING',
    TransactionDate DATE,
    BookingID       INT,
    FOREIGN KEY (BookingID) REFERENCES Booking(BookingID) ON DELETE CASCADE
);

-- ── 9. DigitalInvoice ────────────────────────────────────────
CREATE TABLE IF NOT EXISTS DigitalInvoice (
    InvoiceID INT AUTO_INCREMENT PRIMARY KEY,
    Date      DATE,
    Tax       DECIMAL(8,2),
    PaymentID INT,
    FOREIGN KEY (PaymentID) REFERENCES Payment(PaymentID) ON DELETE CASCADE
);

-- ── 10. Cancellation ─────────────────────────────────────────
CREATE TABLE IF NOT EXISTS Cancellation (
    CancelID         INT AUTO_INCREMENT PRIMARY KEY,
    Reason           VARCHAR(200),
    CancellationDate DATE,
    BookingID        INT,
    FOREIGN KEY (BookingID) REFERENCES Booking(BookingID) ON DELETE CASCADE
);

-- ── 11. Refund ───────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS Refund (
    RefundID     INT AUTO_INCREMENT PRIMARY KEY,
    RefundStatus ENUM('PENDING','APPROVED','REJECTED') DEFAULT 'PENDING',
    RefundAmount DECIMAL(10,2),
    PaymentID    INT,
    CancelID     INT,
    FOREIGN KEY (PaymentID) REFERENCES Payment(PaymentID)       ON DELETE CASCADE,
    FOREIGN KEY (CancelID)  REFERENCES Cancellation(CancelID)   ON DELETE CASCADE
);

-- ============================================================
-- SAMPLE DATA
-- ============================================================

-- Users (1 Admin + 3 Customers)
INSERT INTO User (Name, Email, Phone, Password, Role) VALUES
('Admin User',    'admin@moviebooking.com',   '9000000001', 'admin123',    'ADMIN'),
('Parwez Khan',   'parwez@gmail.com',         '9000000002', 'parwez123',   'CUSTOMER'),
('Prachi Jagota', 'prachi@gmail.com',         '9000000003', 'prachi123',   'CUSTOMER'),
('Prisha Gupta',  'prisha@gmail.com',         '9000000004', 'prisha123',   'CUSTOMER');

-- Theatres
INSERT INTO Theatre (Name, Location) VALUES
('PVR Cinemas',   'Pune, Koregaon Park'),
('INOX Multiplex','Pune, Wakad'),
('Cinepolis',     'Pune, Kharadi');

-- Screens
INSERT INTO Screen (ScreenNumber, TheatreID) VALUES
(1, 1), (2, 1),
(1, 2), (2, 2),
(1, 3);

-- Seats (Screen 1 of Theatre 1 – 10 seats)
INSERT INTO Seat (SeatNumber, SeatType, ScreenID) VALUES
('A1','STANDARD',1), ('A2','STANDARD',1), ('A3','STANDARD',1),
('B1','PREMIUM',1),  ('B2','PREMIUM',1),  ('B3','PREMIUM',1),
('C1','RECLINER',1), ('C2','RECLINER',1),
('A1','STANDARD',2), ('B1','PREMIUM',2);

-- Movies
INSERT INTO Movie (Title, Genre, Duration, Language) VALUES
('Kalki 2898-AD',   'Sci-Fi',  180, 'Telugu'),
('Stree 2',         'Horror',  135, 'Hindi'),
('Fighter',         'Action',  167, 'Hindi'),
('Animal',          'Drama',   201, 'Hindi'),
('Dune Part Two',   'Sci-Fi',  166, 'English');

-- Shows
INSERT INTO Shows (ShowDate, ShowTime, TicketPrice, MovieID, ScreenID) VALUES
('2026-04-05', '10:00:00', 250.00, 1, 1),
('2026-04-05', '14:00:00', 300.00, 1, 2),
('2026-04-05', '10:30:00', 200.00, 2, 3),
('2026-04-06', '18:00:00', 350.00, 3, 1),
('2026-04-06', '20:00:00', 400.00, 5, 2);

-- Sample Booking
INSERT INTO Booking (BookingDate, Status, TotalAmount, UserID, ShowID, SeatID) VALUES
('2026-04-03', 'CONFIRMED', 250.00, 2, 1, 1);

-- Sample Payment
INSERT INTO Payment (Mode, PaymentStatus, TransactionDate, BookingID) VALUES
('UPI', 'SUCCESS', '2026-04-03', 1);

-- Sample Invoice
INSERT INTO DigitalInvoice (Date, Tax, PaymentID) VALUES
('2026-04-03', 45.00, 1);

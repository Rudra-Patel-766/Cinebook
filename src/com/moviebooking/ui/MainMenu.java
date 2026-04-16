package com.moviebooking.ui;

import com.moviebooking.dao.*;
import com.moviebooking.db.DBConnection;
import com.moviebooking.exception.*;
import com.moviebooking.model.*;
import com.moviebooking.service.BookingService;
import com.moviebooking.util.ValidationUtil;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Main CLI entry point for the Online Movie Ticket Booking System.
 * Demonstrates all OOP concepts and JDBC operations.
 */
public class MainMenu {

    private static final Scanner sc      = new Scanner(System.in);
    private static final BookingService  bookingSvc  = new BookingService();
    private static final MovieDAO        movieDAO    = new MovieDAO();
    private static final UserDAO         userDAO     = new UserDAO();
    private static final ShowDAO         showDAO     = new ShowDAO();
    private static final TheatreDAO      theatreDAO  = new TheatreDAO();
    private static final BookingDAO      bookingDAO  = new BookingDAO();
    private static final CancellationDAO cancelDAO   = new CancellationDAO();
    private static final PaymentDAO      paymentDAO  = new PaymentDAO();

    private static User loggedInUser = null;

    public static void main(String[] args) {
        printBanner();
        try {
            // Test DB connection on startup
            DBConnection.getConnection();
        } catch (DatabaseConnectionException e) {
            System.err.println("[FATAL] Cannot connect to database: " + e.getMessage());
            System.err.println("Ensure MySQL is running and credentials in DBConnection.java are correct.");
            return;
        }

        boolean running = true;
        while (running) {
            if (loggedInUser == null) {
                running = showAuthMenu();
            } else if ("ADMIN".equals(loggedInUser.getRole())) {
                running = showAdminMenu();
            } else {
                running = showCustomerMenu();
            }
        }

        DBConnection.closeConnection();
        System.out.println("\nThank you for using Movie Booking System. Goodbye!");
    }

    // ── AUTH MENU ─────────────────────────────────────────────────────────
    private static boolean showAuthMenu() {
        System.out.println("\n╔══════════════════════════════╗");
        System.out.println("║      MOVIE BOOKING SYSTEM    ║");
        System.out.println("╠══════════════════════════════╣");
        System.out.println("║  1. Login                    ║");
        System.out.println("║  2. Register as New Customer ║");
        System.out.println("║  0. Exit                     ║");
        System.out.println("╚══════════════════════════════╝");
        System.out.print("Choice: ");

        int choice = readInt();
        switch (choice) {
            case 1: doLogin();    break;
            case 2: doRegister(); break;
            case 0: return false;
            default: System.out.println("[!] Invalid choice.");
        }
        return true;
    }

    // ── CUSTOMER MENU ─────────────────────────────────────────────────────
    private static boolean showCustomerMenu() {
        System.out.println("\n╔══════════════════════════════════╗");
        System.out.println("║  Welcome, " + padRight(loggedInUser.getName(), 20) + "  ║");
        System.out.println("╠══════════════════════════════════╣");
        System.out.println("║  1. Browse Movies                ║");
        System.out.println("║  2. View Shows for a Movie       ║");
        System.out.println("║  3. View Available Seats         ║");
        System.out.println("║  4. Book Ticket                  ║");
        System.out.println("║  5. My Bookings                  ║");
        System.out.println("║  6. View Booking Details         ║");
        System.out.println("║  7. Cancel Booking               ║");
        System.out.println("║  8. View All Theatres            ║");
        System.out.println("║  9. Logout                       ║");
        System.out.println("║  0. Exit                         ║");
        System.out.println("╚══════════════════════════════════╝");
        System.out.print("Choice: ");

        int choice = readInt();
        try {
            switch (choice) {
                case 1: browseMovies();            break;
                case 2: viewShowsForMovie();        break;
                case 3: viewAvailableSeats();       break;
                case 4: bookTicket();               break;
                case 5: myBookings();               break;
                case 6: viewBookingDetails();       break;
                case 7: cancelBooking();            break;
                case 8: viewTheatres();             break;
                case 9: loggedInUser = null; System.out.println("[OK] Logged out."); break;
                case 0: return false;
                default: System.out.println("[!] Invalid choice.");
            }
        } catch (DatabaseConnectionException e) {
            System.err.println("[DB Error] " + e.getMessage());
        } catch (MovieBookingException e) {
            System.err.println("[Error] " + e.getMessage());
        }
        return true;
    }

    // ── ADMIN MENU ────────────────────────────────────────────────────────
    private static boolean showAdminMenu() {
        System.out.println("\n╔══════════════════════════════════╗");
        System.out.println("║    ADMIN DASHBOARD               ║");
        System.out.println("╠══════════════════════════════════╣");
        System.out.println("║  1.  Add Movie                   ║");
        System.out.println("║  2.  Update Movie                ║");
        System.out.println("║  3.  Delete Movie                ║");
        System.out.println("║  4.  View All Movies             ║");
        System.out.println("║  5.  Add Show                    ║");
        System.out.println("║  6.  Update Show Price           ║");
        System.out.println("║  7.  Delete Show                 ║");
        System.out.println("║  8.  Add Theatre                 ║");
        System.out.println("║  9.  View All Bookings           ║");
        System.out.println("║  10. View All Users              ║");
        System.out.println("║  11. View Pending Refunds        ║");
        System.out.println("║  12. Approve Refund              ║");
        System.out.println("║  13. View All Payments           ║");
        System.out.println("║  14. Logout                      ║");
        System.out.println("║  0.  Exit                        ║");
        System.out.println("╚══════════════════════════════════╝");
        System.out.print("Choice: ");

        int choice = readInt();
        try {
            switch (choice) {
                case 1:  adminAddMovie();         break;
                case 2:  adminUpdateMovie();      break;
                case 3:  adminDeleteMovie();      break;
                case 4:  browseMovies();           break;
                case 5:  adminAddShow();           break;
                case 6:  adminUpdateShowPrice();   break;
                case 7:  adminDeleteShow();        break;
                case 8:  adminAddTheatre();        break;
                case 9:  adminAllBookings();       break;
                case 10: adminAllUsers();          break;
                case 11: adminPendingRefunds();    break;
                case 12: adminApproveRefund();     break;
                case 13: adminAllPayments();       break;
                case 14: loggedInUser = null; System.out.println("[OK] Logged out."); break;
                case 0:  return false;
                default: System.out.println("[!] Invalid choice.");
            }
        } catch (MovieBookingException e) {
            System.err.println("[Error] " + e.getMessage());
        }
        return true;
    }

    // ══════════════════════════════════════════════════════════════════════
    // AUTH OPERATIONS
    // ══════════════════════════════════════════════════════════════════════

    private static void doLogin() {
        System.out.print("Email   : "); String email = sc.nextLine().trim();
        System.out.print("Password: "); String pass  = sc.nextLine().trim();
        try {
            User user = userDAO.login(email, pass);
            if (user != null) {
                loggedInUser = user;
                System.out.println("[OK] Login successful! Welcome, " + user.getName() + " (" + user.getRole() + ")");
            } else {
                System.out.println("[!] Invalid email or password.");
            }
        } catch (DatabaseConnectionException e) {
            System.err.println("[DB Error] " + e.getMessage());
        }
    }

    private static void doRegister() {
    System.out.println("--- Customer Registration ---");

    System.out.print("Name    : "); 
    String name  = sc.nextLine().trim();

    String email;
    while (true) {
        System.out.print("Email   : "); 
        email = sc.nextLine().trim();

        if (ValidationUtil.isValidEmail(email)) {
            break;
        } else {
            System.out.println("[!] Invalid Email! Example: abc@gmail.com");
        }
    }

    String phone;
    while (true) {
        System.out.print("Phone   : "); 
        phone = sc.nextLine().trim();

        if (ValidationUtil.isValidPhone(phone)) {
            break;
        } else {
            System.out.println("[!] Phone must be exactly 10 digits!");
        }
    }

    System.out.print("Password: "); 
    String pass  = sc.nextLine().trim();

    User user = new User(0, name, email, phone, pass, "CUSTOMER");

    try {
        if (userDAO.registerUser(user)) {
            System.out.println("[OK] Registered successfully! Please login.");
        }
    } catch (DatabaseConnectionException e) {
        System.err.println("[DB Error] " + e.getMessage());
    }
}

    // ══════════════════════════════════════════════════════════════════════
    // CUSTOMER OPERATIONS
    // ══════════════════════════════════════════════════════════════════════

    private static void browseMovies() throws DatabaseConnectionException {
        List<Movie> movies = movieDAO.getAllMovies();
        if (movies.isEmpty()) { System.out.println("[!] No movies available."); return; }
        System.out.println("\n====== MOVIES ======");
        movies.forEach(System.out::println);
    }

    private static void viewShowsForMovie() throws DatabaseConnectionException {
        System.out.print("Enter MovieID: ");
        int movieId = readInt();
        List<Show> shows = showDAO.getShowsByMovie(movieId);
        if (shows.isEmpty()) { System.out.println("[!] No shows for this movie."); return; }
        System.out.println("\n====== SHOWS ======");
        shows.forEach(System.out::println);
    }

    private static void viewAvailableSeats() throws DatabaseConnectionException {
        System.out.print("Enter ShowID : "); int showId   = readInt();
        System.out.print("Enter ScreenID: "); int screenId = readInt();
        List<Seat> seats = theatreDAO.getAvailableSeats(showId, screenId);
        if (seats.isEmpty()) { System.out.println("[!] No seats available."); return; }
        System.out.println("\n====== AVAILABLE SEATS ======");
        seats.forEach(System.out::println);
    }

    private static void bookTicket() throws MovieBookingException, DatabaseConnectionException {
    System.out.print("Enter ShowID: "); int showId = readInt();
    System.out.print("Enter SeatID: "); int seatId = readInt();

    // Get show price
    List<Show> shows = showDAO.getAllShows();
    double price = 0.0;
    for (Show s : shows) {
        if (s.getShowId() == showId) { price = s.getTicketPrice(); break; }
    }

    if (price == 0.0) {
        System.out.println("[!] Show not found.");
        return;
    }

    // Create booking WITH correct price
    java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
    Booking booking = new Booking(0, today, "CONFIRMED", price, loggedInUser.getUserId(), showId, seatId);

    int bookingId = bookingDAO.addBooking(booking);
    if (bookingId == -1) {
        System.out.println("[!] Booking failed.");
        return;
    }
    System.out.println("[Booking] BookingID " + bookingId + " confirmed for UserID " + loggedInUser.getUserId());

    // Payment
    System.out.println("\nSelect Payment Mode:");
    System.out.println("  1. Card");
    System.out.println("  2. UPI");
    System.out.println("  3. Net Banking");
    System.out.print("Choice: ");
    int payChoice = readInt();
    String mode;
    String extra = "";
    switch (payChoice) {
        case 2:  mode = "UPI";
                 System.out.print("Enter UPI ID: "); extra = sc.nextLine().trim();
                 break;
        case 3:  mode = "NETBANKING";
                 System.out.print("Enter Bank Name: "); extra = sc.nextLine().trim();
                 break;
        default: mode = "CARD";
                 System.out.print("Enter last 4 digits of card: "); extra = sc.nextLine().trim();
                 break;
    }

    bookingSvc.processPayment(bookingId, price, mode, extra);
}

    
private static void myBookings() throws DatabaseConnectionException, MovieBookingException {
    List<Booking> list = bookingDAO.getBookingsByUser(loggedInUser.getUserId());
    if (list.isEmpty()) { System.out.println("[!] No bookings found."); return; }
    System.out.println("\n====== MY BOOKINGS ======");
    for (Booking b : list) {
        // Get invoice to show total amount with GST
        Payment p = paymentDAO.getPaymentByBooking(b.getBookingId());
        if (p != null) {
            DigitalInvoice inv = paymentDAO.getInvoiceByPayment(p.getPaymentId());
            if (inv != null) {
                double totalWithGST = b.getTotalAmount() + inv.getTax();
                System.out.printf("BookingID: %d | Date: %s | Status: %-10s | Amount: %.2f (incl. GST) | ShowID: %d%n",
                        b.getBookingId(), b.getBookingDate(), b.getStatus(),
                        totalWithGST, b.getShowId());
            } else {
                System.out.println(b);
            }
        } else {
            System.out.println(b);
        }
    }
}

    private static void viewBookingDetails() throws MovieBookingException {
        System.out.print("Enter BookingID: ");
        int id = readInt();
        bookingSvc.viewBookingDetails(id);
    }

    private static void cancelBooking() throws MovieBookingException {
        System.out.print("Enter BookingID to cancel: ");
        int id = readInt();
        bookingSvc.cancel(id);
    }

    private static void viewTheatres() throws DatabaseConnectionException {
        List<Theatre> list = theatreDAO.getAllTheatres();
        if (list.isEmpty()) { System.out.println("[!] No theatres found."); return; }
        System.out.println("\n====== THEATRES ======");
        list.forEach(System.out::println);
    }

    // ══════════════════════════════════════════════════════════════════════
    // ADMIN OPERATIONS
    // ══════════════════════════════════════════════════════════════════════

    private static void adminAddMovie() throws DatabaseConnectionException {
        System.out.println("--- Add Movie ---");
        System.out.print("Title   : "); String title    = sc.nextLine().trim();
        System.out.print("Genre   : "); String genre    = sc.nextLine().trim();
        System.out.print("Duration (mins): "); int dur  = readInt();
        System.out.print("Language: "); String lang     = sc.nextLine().trim();
        movieDAO.addMovie(new Movie(0, title, genre, dur, lang));
    }

    private static void adminUpdateMovie() throws DatabaseConnectionException {
        System.out.print("Enter MovieID to update: "); int id = readInt();
        Movie m = movieDAO.getMovieById(id);
        if (m == null) { System.out.println("[!] Movie not found."); return; }
        System.out.println("Current: " + m);
        System.out.print("New Title (Enter to keep): ");   String t = sc.nextLine().trim();
        System.out.print("New Genre (Enter to keep): ");   String g = sc.nextLine().trim();
        System.out.print("New Duration (0 to keep): ");    int d    = readInt();
        System.out.print("New Language (Enter to keep): ");String l = sc.nextLine().trim();
        if (!t.isEmpty()) m.setTitle(t);
        if (!g.isEmpty()) m.setGenre(g);
        if (d > 0)        m.setDuration(d);
        if (!l.isEmpty()) m.setLanguage(l);
        movieDAO.updateMovie(m);
    }

    private static void adminDeleteMovie() throws DatabaseConnectionException {
        System.out.print("Enter MovieID to delete: "); int id = readInt();
        movieDAO.deleteMovie(id);
    }

    private static void adminAddShow() throws DatabaseConnectionException {
        System.out.println("--- Add Show ---");
        System.out.print("MovieID  : ");  int mid   = readInt();
        System.out.print("ScreenID : ");  int sid   = readInt();
        System.out.print("Date (YYYY-MM-DD): "); String ds = sc.nextLine().trim();
        System.out.print("Time (HH:MM:SS)  : "); String ts = sc.nextLine().trim();
        System.out.print("Ticket Price : ");      double p  = readDouble();

        Show show = new Show(0, Date.valueOf(ds),
                java.sql.Time.valueOf(ts), p, mid, sid);
        showDAO.addShow(show);
    }

    private static void adminUpdateShowPrice() throws DatabaseConnectionException {
        System.out.print("Enter ShowID: "); int sid = readInt();
        System.out.print("New Price   : "); double p = readDouble();
        showDAO.updateShowPrice(sid, p);
    }

    private static void adminDeleteShow() throws DatabaseConnectionException {
        System.out.print("Enter ShowID to delete: "); int id = readInt();
        showDAO.deleteShow(id);
    }

    private static void adminAddTheatre() throws DatabaseConnectionException {
        System.out.print("Theatre Name    : "); String name = sc.nextLine().trim();
        System.out.print("Theatre Location: "); String loc  = sc.nextLine().trim();
        theatreDAO.addTheatre(new Theatre(0, name, loc));
    }

    private static void adminAllBookings() throws DatabaseConnectionException {
        List<Booking> list = bookingDAO.getAllBookings();
        if (list.isEmpty()) { System.out.println("[!] No bookings found."); return; }
        System.out.println("\n====== ALL BOOKINGS ======");
        list.forEach(System.out::println);
    }

    private static void adminAllUsers() throws DatabaseConnectionException {
        List<User> list = userDAO.getAllUsers();
        System.out.println("\n====== ALL USERS ======");
        list.forEach(u -> System.out.println(u.getDetails()));
    }

    private static void adminPendingRefunds() throws DatabaseConnectionException {
        List<Refund> list = cancelDAO.getAllRefunds();
        System.out.println("\n====== REFUNDS ======");
        if (list.isEmpty()) { System.out.println("No refunds found."); return; }
        list.forEach(System.out::println);
    }

    private static void adminApproveRefund() throws DatabaseConnectionException {
        System.out.print("Enter RefundID to approve: "); int id = readInt();
        cancelDAO.updateRefundStatus(id, "APPROVED");
    }

    private static void adminAllPayments() throws DatabaseConnectionException {
        List<Payment> list = paymentDAO.getAllPayments();
        System.out.println("\n====== ALL PAYMENTS ======");
        list.forEach(p -> System.out.println(p.getPaymentSummary()));
    }

    // ══════════════════════════════════════════════════════════════════════
    // UTILITY HELPERS
    // ══════════════════════════════════════════════════════════════════════

    private static void printBanner() {
        // System.out.println("╔══════════════════════════════════════════════╗");
        // System.out.println("║   Online Movie Ticket Booking System         ║");
        // System.out.println("║   SIT Pune – DBMS + Java Mini Project        ║");
        // System.out.println("╚══════════════════════════════════════════════╝");
    }

    private static int readInt() {
        try {
            String line = sc.nextLine().trim();
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid number, using 0.");
            return 0;
        }
    }

    private static double readDouble() {
        try {
            return Double.parseDouble(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid number, using 0.0.");
            return 0.0;
        }
    }

    private static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }
}

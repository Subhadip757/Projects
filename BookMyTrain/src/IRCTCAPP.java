import java.util.*;
import java.util.Scanner;

public class IRCTCAPP {
    private final Scanner scanner = new Scanner(System.in);
    private final UserService userService = new UserService();
    private final BookingService bookingService = new BookingService();

    public static void main(String[] args) {
        new IRCTCAPP().start();
    }

    public void start(){
        while(true){
            System.out.println("------------Welcome to IRCTC app----------");
            if(!userService.isLoggedIn()){
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.println("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice){
                    case 1 -> register();
                    case 2 -> login();
                    case 3 -> exitApp();
                    default -> System.out.println("Invalid choice");
                }
            }
        }
    }

    public void register(){
        System.out.println("Enter username: ");
        String username = scanner.next();
        System.out.println("Enter password: ");
        String password = scanner.next();
        System.out.println("Enter full name: ");
        scanner.nextLine();
        String fullname = scanner.nextLine();
        System.out.println("Enter contact: ");
        String contact = scanner.next();

        userService.registerUser(username, password, fullname, contact);
    }

    public void login(){
        System.out.println("Enter username: ");
        String username = scanner.next();
        System.out.println("Enter password");
        String password = scanner.next();
        if(userService.login(username, password)){
            showUserMenu();
        }
    }

    public void showUserMenu(){
        while(userService.isLoggedIn()){
            System.out.println("--------------User Menu--------------");
            System.out.println("1. Search Trains");
            System.out.println("2. Book Tickets");
            System.out.println("3. View my Tickets");
            System.out.println("4. Cancel Tickets");
            System.out.println("5. View all Trains");
            System.out.println("6. Logout");

            System.out.println("Enter your choice: ");
            int choice = scanner.nextInt();
            switch (choice){
                case 1 -> searchTrains();
                case 2 -> bookTickets();
                case 3 -> viewMyTicket();
                case 4 -> cancelTicket();
                case 5 -> bookingService.listAllTrains();
                case 6 -> userService.logout();
                default -> System.out.println("Invalid Choice");
            }
        }
    }

    private void searchTrains(){
        System.out.println("Enter source station: ");
        String source = scanner.next();
        System.out.println("Enter Destination station: ");
        String destination = scanner.next();

        List<Train> trains = bookingService.searchTrain(source, destination);
        if(trains.isEmpty()){
            System.out.println("No Trains found....");
            return;
        }
        System.out.println("Train Found.....");
        System.out.println(trains);

        System.out.println("Do you want to book ticket? (yes / no) ");
        String choice = scanner.next();
        if(choice.equalsIgnoreCase("yes")){
            System.out.println("Enter train id to book");
            int trainID = scanner.nextInt();
            System.out.println("Enter number of seats to book: ");
            int seats = scanner.nextInt();

            Ticket ticket = bookingService.bookTicket(userService.getCurrentUser(), trainID, seats);
            if(ticket != null){
                System.out.println("Booking Successfull");
                System.out.println(ticket);
            }
            else{
                System.out.println("Returning to user Menu");
            }
        }
    }

    private void bookTickets(){
        System.out.println("Enter source station: ");
        String source = scanner.next();
        System.out.println("Enter Destination station: ");
        String destination = scanner.next();

        List<Train> trains = bookingService.searchTrain(source, destination);
        if(trains.isEmpty()){
            System.out.println("No Trains Available for booking....");
            return;
        }

        System.out.println("Available Trains --> ");
        for(Train train : trains){
            System.out.println(train);
        }

        System.out.println("Enter train id to book");
        int trainID = scanner.nextInt();
        System.out.println("Enter number of seats to book: ");
        int seats = scanner.nextInt();

        Ticket ticket = bookingService.bookTicket(userService.getCurrentUser(), trainID, seats);
        if(ticket != null){
            System.out.println("Booking Successfull");
            System.out.println(ticket);
        }
        else{
            System.out.println("Returning to user Menu");
        }
    }

    private void viewMyTicket(){
        List<Ticket> ticket = bookingService.getTicketByUser(userService.getCurrentUser());
        if(ticket.isEmpty()){
            System.out.println("No Ticket found.....");
        }
        else{
            System.out.println("Your Tickets");
            for(Ticket t : ticket){
                System.out.println(t);
            }
        }
    }

    private void cancelTicket(){
        System.out.println("Enter the ticket id to cancel: ");
        int ticketId = scanner.nextInt();

        bookingService.cancelTicket(ticketId, userService.getCurrentUser());
    }

    private void exitApp(){
        System.out.println("Thank You for using APP.........");
        System.exit(0);
    }
}

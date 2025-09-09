import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BookingService {

    private List<Train> trainList = new ArrayList<>();

    private List<Ticket> ticketList = new ArrayList<>();

    public BookingService(){
        trainList.add(new Train(101, "Rajdhani Express", "Delhi", "Howrah", 250));
        trainList.add(new Train(102, "Rajdhani Express", "Delhi", "Mumbai", 100));
        trainList.add(new Train(103, "Rajdhani Express", "Howrah", "Kashmir", 150));
        trainList.add(new Train(104, "Vande Bharat Express", "Howrah", "Puri", 300));
        trainList.add(new Train(105, "Satapdi Express", "Delhi", "Lucknow", 200));
    }

    public List<Train> searchTrain(String source, String destination){
        List<Train> res = new ArrayList<>();

        for(Train train : trainList){
            if(train.getSource().equalsIgnoreCase(source) && train.getDestination().equalsIgnoreCase(destination)){
                res.add(train);
            }
        }
        return res;
    }

    public Ticket bookTicket(User user, int trainId, int seatCount){
        for(Train train : trainList){
            if(train.getId() == trainId){
                if(train.bookSeats(seatCount)){
                    Ticket ticket = new Ticket(user, train, seatCount);
                    ticketList.add(ticket);
                    return ticket;
                }
                else{
                    System.out.println("Not enough seats available");
                    return null;
                }
            }
        }
        System.out.println("Train id not found");
        return null;
    }

    public List<Ticket> getTicketByUser(User user){
        List<Ticket> res= new ArrayList<>();

        for(Ticket ticket : ticketList){
            if(ticket.getUser().getName().equalsIgnoreCase(user.getName())){
                res.add(ticket);
            }
        }
        return res;
    }

    public boolean cancelTicket(int ticketId, User user){
        Iterator<Ticket> iterator = ticketList.listIterator();
        while(iterator.hasNext()){
            Ticket ticket = iterator.next();
            if(ticket.getId() == ticketId && ticket.getUser().getName().equalsIgnoreCase(user.getName())){
                Train train = ticket.getTrain();
                train.cancelSeats(ticket.getSeatBooked());
                iterator.remove();
                System.out.println("Ticket " + ticketId + "Cancelled successfully");
                return true;
            }
        }
        System.out.println("Ticket Not found or does not belong to the user");
        return false;
    }

    public void listAllTrains(){
        for(Train t : trainList){
            System.out.println(t);
        }
    }

}

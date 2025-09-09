public class Ticket
{
    private static int counter = 1001;
    private final int id;
    private User user;

    private Train train;
    private int seatBooked;

    public static int getCounter() {
        return counter;
    }

    public static void setCounter(int counter) {
        Ticket.counter = counter;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public int getSeatBooked() {
        return seatBooked;
    }

    public void setSeatBooked(int seatBooked) {
        this.seatBooked = seatBooked;
    }

    @Override
    public String toString() {
        return "Ticket id: " + id + " | Train: "+train.getName()
                + " | Route: " + train.getSource() + " to " + train.getDestination() +
                " | Available Seats: " + seatBooked + " | Booked by: " + user.getFullName();
    }

    public Ticket(User user, Train train, int seatBooked) {
        this.id = counter++;
        this.user = user;
        this.train = train;
        this.seatBooked = seatBooked;
    }

}

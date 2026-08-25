import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ParkingLotSystem {

    // new singleton object

    private static final ParkingLotSystem INSTANCE = new ParkingLotSystem();
    private final List<ParkingFloor> floors = new ArrayList<>();
    private final Map<String, ParkingTicket> activeTickets = new ConcurrentHashMap<>();
    private ParkingStrategy st = new NearestFirstStrategy();

    private ParkingLotSystem() {

    }

    public static ParkingLotSystem getInstance() {
        return INSTANCE;
    }

    public void addFloor(ParkingFloor floor) {
        floors.add(floor);
    }

    public void setStrategy(ParkingStrategy st) {
        this.st = st;
    }

    public ParkingTicket parkVehicle(Vehicle vehicle) {

        ParkingSpot spot = st.findSpot(floors, vehicle);

        if (spot == null) {
            return null;
        }

        spot.park(vehicle);

        ParkingTicket ticket = new ParkingTicket(vehicle, spot);

        activeTickets.put(ticket.getTicketId(), ticket);

        return ticket;
    }


    public Long unparkVehicle(String ticketId){
        ParkingTicket ticket = activeTickets.remove(ticketId) ; 
        if(ticket==null){
            return null ; 
        }
        ticket.getSpot().unpark(); 
        ticket.close();
        return ticket.getDuration();
    }

    public void displayAvailability(){
        for(ParkingFloor floor :floors){
            floor.displayAvailability();
        }
    }
}

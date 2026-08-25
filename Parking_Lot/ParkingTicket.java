import java.time.Duration;
import java.time.LocalDateTime;

public class ParkingTicket {

    private final String ticketId;
    private final Vehicle vehicle;
    private final ParkingSpot spot;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;

    public ParkingTicket(Vehicle v, ParkingSpot spot) {
        this.vehicle = v;
        this.spot = spot;
        this.ticketId = "T" + System.currentTimeMillis();
        this.entryTime = LocalDateTime.now();

    }

    public void close(){
        this.exitTime = LocalDateTime.now() ; 
    }

    public long getDuration(){
        LocalDateTime end = (exitTime!=null)? exitTime:LocalDateTime.now() ; 
        return Duration.between(entryTime, end).toMinutes() ; 
    }

    public String getTicketId(){
        return ticketId ; 
    }
    public Vehicle getVehicle(){
        return vehicle ; 
    }

    public ParkingSpot getSpot(){
        return spot ; 

    }

    public LocalDateTime getEntryTime(){
        return entryTime; 
    }

    public LocalDateTime getExitTime(){
        return exitTime ; 
    }

}

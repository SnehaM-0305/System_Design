import java.util.Optional;
import java.util.* ; 

public interface ParkingStrategy {
    ParkingSpot findSpot(List<ParkingFloor> floors , Vehicle vehicle) ; 
}

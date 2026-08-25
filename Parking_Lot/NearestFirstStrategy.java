import java.util.List;
import java.util.Optional;

public class NearestFirstStrategy implements ParkingStrategy {
    
    @Override
    public ParkingSpot findSpot(List<ParkingFloor> floors, Vehicle vehicle) {

        for(ParkingFloor floor :floors){
            for(ParkingSpot spot : floor.getSpots()){
                if(spot.canFitVehicle(vehicle)){
                    return spot ; 
                }
            }
        }
        
        return null ; 
    }
}

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ParkingFloor {
    private final int floorNumber ; 
    private final List<ParkingSpot> spots = new CopyOnWriteArrayList<>() ; 

    public ParkingFloor(int floorNumber){
        this.floorNumber = floorNumber ; 

    }

    public void addSpot(ParkingSpot spot){
        spots.add(spot) ; 

    }

    public List<ParkingSpot> getSpots(){
        return spots ;
    }

    public int getfloorNumber(){
        return floorNumber ; 
    }

    public long getavailableCount(VehicleSize sz){
        return spots.stream().filter(s->s.isAvailable() && s.getSpotSize()==sz).count();
    }

    public void displayAvailability(){
        for(VehicleSize size :VehicleSize.values()){
            System.out.println("Floor" + floorNumber + "-"+size + ":" +getavailableCount(size) +"available");
        }
    }
}

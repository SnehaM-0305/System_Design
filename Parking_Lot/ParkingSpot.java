public class ParkingSpot {
    private  String spotId ; 
    private VehicleSize spotSize ; 
    private boolean isOccupied ; 
    private Vehicle parkedVehicle ;

    public ParkingSpot(String id , VehicleSize sz ){
        this.spotId = id ; 
        this.spotSize = sz ; 
        
     
    }

    //can a vehicle fit there ? 

    public boolean canFitVehicle(Vehicle v){
        if(isOccupied){
            return false ; 
        }
        if(spotSize==v.getVehicleSize()){
            return true ; 
        }
        return false ; 
    }

    //parking the vehicle here(synchronized)

    public synchronized boolean park(Vehicle  v){

        if(!canFitVehicle(v)){
            return false ; 
        }

        parkedVehicle = v ; 
        isOccupied = true ; 
        return true ; 

    }

    public synchronized void unpark(){
        isOccupied=false ; 
        parkedVehicle = null ; 
    }

    public boolean isAvailable(){
       return !isOccupied ; 
    }

    public String getSpotId(){
        return spotId ; 
    }

    public VehicleSize getSpotSize(){
        return spotSize ; 
    }

    public Vehicle getParkedVehicle(){
        return parkedVehicle;
    }
    
}

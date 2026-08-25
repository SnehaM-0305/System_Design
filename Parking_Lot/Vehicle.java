public abstract class Vehicle {

    private final String licencePlate ; 
    private final VehicleSize size ; 

    protected Vehicle(String lp , VehicleSize size){
        this.licencePlate = lp ; 
        this.size = size ; 
    }

    public String getLicensePlat(){
        return licencePlate ; 
    }

    public VehicleSize getVehicleSize(){
return size ; 
    }

    @Override
    public String toString(){
        return "License plate number of vehicle is["+licencePlate+"]" ; 
    }


    
}

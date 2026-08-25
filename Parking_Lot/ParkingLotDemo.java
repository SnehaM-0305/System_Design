
import java.util.Optional;

public class ParkingLotDemo {

    public static void main(String[] args) throws InterruptedException {

        // Step 1: Get the one and only ParkingLotSystem (it's a Singleton)
        ParkingLotSystem lot = ParkingLotSystem.getInstance();

        // Step 2: Build Floor 1 and add some spots to it
        ParkingFloor floor1 = new ParkingFloor(1);
        floor1.addSpot(new ParkingSpot("F1-S1", VehicleSize.SMALL));
        floor1.addSpot(new ParkingSpot("F1-M1", VehicleSize.MEDIUM));
        floor1.addSpot(new ParkingSpot("F1-L1", VehicleSize.LARGE));
        lot.addFloor(floor1);

        // Step 3: Build Floor 2 and add some spots to it
        ParkingFloor floor2 = new ParkingFloor(2);
        floor2.addSpot(new ParkingSpot("F2-S1", VehicleSize.SMALL));
        floor2.addSpot(new ParkingSpot("F2-M1", VehicleSize.MEDIUM));
        lot.addFloor(floor2);

        System.out.println("Initial availability:");
        lot.displayAvailability();

        // Step 4: Park a car and check if it worked
        Vehicle car = new Car("KA-01-1234");
        ParkingTicket carTicket = lot.parkVehicle(car);

        if (carTicket!=null) {
            System.out.println("Parked " + car + " -> ticket " + carTicket.getTicketId());
        } else {
            System.out.println("No spot available for " + car);
        }

        System.out.println();
        System.out.println("After parking one car:");
        lot.displayAvailability();

        // Step 5: Simulate 3 entry gates trying to park a bike at the SAME time.
        // Only 2 SMALL spots are left, so exactly one gate should fail.
        Thread gateA = new Thread(new GateWorker(lot), "GateA");
        Thread gateB = new Thread(new GateWorker(lot), "GateB");
        Thread gateC = new Thread(new GateWorker(lot), "GateC");

        gateA.start();
        gateB.start();
        gateC.start();

        // Wait for all three gate threads to finish before continuing
        gateA.join();
        gateB.join();
        gateC.join();

        System.out.println();
        System.out.println("After concurrent gate arrivals:");
        lot.displayAvailability();

        // Step 6: Unpark the car and see how long it stayed
        if (carTicket!=null) {
            String ticketId = carTicket.getTicketId();
            Long minutesParked = lot.unparkVehicle(ticketId);

            if (minutesParked!=null) {
                System.out.println();
                System.out.println("Unparked " + car + ", duration minutes: " + minutesParked);
            }
        }

        System.out.println();
        System.out.println("Final availability:");
        lot.displayAvailability();
    }
}

/**
 * A small helper class that represents one gate trying to park one bike.
 * Runnable is Java's basic way of saying "here is a task to run on a thread".
 */
class GateWorker implements Runnable {

    private ParkingLotSystem lot;

    public GateWorker(ParkingLotSystem lot) {
        this.lot = lot;
    }

    @Override
    public void run() {
        String gateName = Thread.currentThread().getName();
        Vehicle bike = new Bike(gateName);

        ParkingTicket ticket = lot.parkVehicle(bike);

        if (ticket!=null) {
            System.out.println(gateName + " parked " + bike + " -> ticket " + ticket.getTicketId());
        } else {
            System.out.println(gateName + " found no spot for " + bike);
        }
    }
}
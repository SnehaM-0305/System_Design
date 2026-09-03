public class ATM {

    private final String location;
    private final ATMService atmService;

    public ATM(String location, ATMService atmService) {
        this.location = location;
        this.atmService = atmService;
    }

    public String getLocation() {
        return location;
    }

    public ATMService getAtmService() {
        return atmService;
    }
}
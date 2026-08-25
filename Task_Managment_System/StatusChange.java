import java.time.LocalDateTime;
public final class StatusChange {
    private final Status from ; 
    private final Status to ; 
    private final LocalDateTime changedat;

    public StatusChange(Status from, Status to, LocalDateTime changedat) {
        this.from = from;
        this.to = to;
        this.changedat = changedat;
    }

    public Status getFrom(){
        return from  ; 
    }
    public Status getTo(){
        return to  ; 
    }
    public LocalDateTime getChangedAt(){
        return changedat  ; 
    }

    @Override
    public String toString() {
        return "StatusChange{" +
                "from=" + from +
                ", to=" + to +
                ", changedat=" + changedat +
                '}';
    }
}

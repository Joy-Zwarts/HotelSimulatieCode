package Model;

public class HotelEvent{

    private int time; // tijd in ms
    private HotelEventType eventType; // type event uit de enum
    private int guestID; // welke gast gaat het om (nu nog random)
    private int data; // data (dit moest erin maar ben vergeten wat het doet)


    // constructor

   public HotelEvent(int time, HotelEventType eventType, int guestID, int data){
        this.time = time;
        this.eventType = eventType;
        this.guestID = guestID;
        this.data = data;
   }

   // getters en setters

    public HotelEventType getHotelEventType() {
        return eventType;
    }
    public int getGuestID() {
       return guestID;
    }
    public int getTime() {
        return time;
    }
    public int getData() {
       return data;
    }
}

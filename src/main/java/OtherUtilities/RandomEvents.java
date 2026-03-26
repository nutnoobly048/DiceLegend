package OtherUtilities;

import java.util.*;
import Event.*;

public class RandomEvents {
    
    private static HashMap<String, ArrayList<Event>> allEvents = new HashMap<>();

    static {

        ArrayList<Event> defaultEvents = createDefaultEvents();

        allEvents.put("mysteriousJungle", createMysteriousJungleEvents(defaultEvents));
        allEvents.put("cryoGard", createCryoGardEvents(defaultEvents));
        allEvents.put("goldenSeason", createGoldenSeasonEvents(defaultEvents));
    }

    public static ArrayList<Event> createDefaultEvents(){

        ArrayList<Event> list = new ArrayList<>();
        // เพิ่ม Event ที่มีทุกด่านตรงนี้
        // list.add(new Event());
        list.add(new ReverseEvent());
        list.add(new MoveFast());
        list.add(new MoveSlow());
        return list;

    }

    public static ArrayList<Event> createMysteriousJungleEvents(ArrayList<Event> base){

        ArrayList<Event> list = new ArrayList<>(base);
        // เพิ่ม Event ของด่าน Mysterious Jungle ตรงนี้
        // list.add(new Event());
        return list;

    }

    public static ArrayList<Event> createCryoGardEvents(ArrayList<Event> base){

        ArrayList<Event> list = new ArrayList<>(base);

        list.add(new FenrirFangEvent());
        return list;

    }

    public static ArrayList<Event> createGoldenSeasonEvents(ArrayList<Event> base){

        ArrayList<Event> list = new ArrayList<>(base);
        // เพิ่ม Event ของด่าน Golden Season ตรงนี้
        // list.add(new Event());
        return list;

    }

    // Method สำหรับส่งผลลัพธ์การสุ่ม วิธีใช้ => RandomEvents.resultRandomEvent();
    public static Event resultRandomEvent(String selectedMap){

        ArrayList<Event> canUseEvents = allEvents.get(selectedMap);
        int index = RandomUtilities.randomInt(canUseEvents.size());
        return canUseEvents.get(index);

    }

}

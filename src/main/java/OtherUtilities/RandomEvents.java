package OtherUtilities;

import java.util.*;
import Event.base.Event;

public class RandomEvents {

    private static HashMap<String, ArrayList<Event>> allEvents = new HashMap<>();

    static {

        // โหลดแต่ละ folder
        ArrayList<Event> defaultEvents = new ArrayList<>(EventLoader.loadEventsFromPackage("Event.defaultEvents"));

        ArrayList<Event> mysteriousEvents = new ArrayList<>(
                EventLoader.loadEventsFromPackage("Event.mysteriousEvents"));

        ArrayList<Event> cryoEvents = new ArrayList<>(EventLoader.loadEventsFromPackage("Event.cryoGradEvents"));

        ArrayList<Event> goldenEvents = new ArrayList<>(EventLoader.loadEventsFromPackage("Event.goldenSeasonEvents"));

        // 👉 รวม default เข้าไปทุก map (กันว่าง)
        allEvents.put("default", defaultEvents);

        allEvents.put("mysteriousJungle", combine(defaultEvents, mysteriousEvents));
        allEvents.put("cryoGrad", combine(defaultEvents, cryoEvents));
        allEvents.put("goldenSeason", combine(defaultEvents, goldenEvents));
    }

    // รวม list
    private static ArrayList<Event> combine(List<Event> base, List<Event> extra) {
        ArrayList<Event> result = new ArrayList<>(base);
        result.addAll(extra);
        return result;
    }

    // 🎲 random event
    public static Event resultRandomEvent(String selectedMap) {

        ArrayList<Event> list = allEvents.get(selectedMap);

        // ✅ fallback กันพัง
        if (list == null || list.isEmpty()) {
            System.out.println("⚠️ No events in map: " + selectedMap + ", fallback to default");
            list = allEvents.get("default");
        }

        if (list == null || list.isEmpty()) {
            throw new RuntimeException("❌ No events available at all!");
        }

        int index = RandomUtilities.randomInt(list.size());
        return list.get(index);
    }

    public static void logging() {
        System.out.println(allEvents);
    }

}
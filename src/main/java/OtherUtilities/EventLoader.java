package OtherUtilities;

import org.reflections.Reflections;
import java.util.*;
import Event.base.Event;

public class EventLoader {

    public static List<Event> loadEventsFromPackage(String packageName) {

        List<Event> list = new ArrayList<>();

        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends Event>> classes = reflections.getSubTypesOf(Event.class);

        for (Class<? extends Event> clazz : classes) {
            try {
                Event event = clazz.getDeclaredConstructor().newInstance();
                list.add(event);
            } catch (Exception e) {
                System.out.println("❌ Cannot load: " + clazz.getName());
                e.printStackTrace();
            }
        }

        return list;
    }
}
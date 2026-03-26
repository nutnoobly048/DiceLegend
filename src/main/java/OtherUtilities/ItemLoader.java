package OtherUtilities;

import org.reflections.Reflections;
import java.util.*;
import Item.base.Item;

public class ItemLoader {

    public static List<Class<? extends Item>> loadItemClasses(String packageName) {

        List<Class<? extends Item>> list = new ArrayList<>();

        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends Item>> classes = reflections.getSubTypesOf(Item.class);

        for (Class<? extends Item> clazz : classes) {
            list.add(clazz);
        }

        return list;
    }
}
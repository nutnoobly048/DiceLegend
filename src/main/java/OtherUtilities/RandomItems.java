package OtherUtilities;

import java.util.*;
import Item.base.Item;

public class RandomItems {

    private static HashMap<String, ArrayList<Class<? extends Item>>> allItems = new HashMap<>();

    static {

        // ✅ default มี 3 ตัวที่คุณต้องการ
        ArrayList<Class<? extends Item>> defaultItems = new ArrayList<>(
                ItemLoader.loadItemClasses("Item.defaultItems"));

        ArrayList<Class<? extends Item>> mysteriousItems = new ArrayList<>(
                ItemLoader.loadItemClasses("Item.mysteriousItems"));

        ArrayList<Class<? extends Item>> cryoItems = new ArrayList<>(ItemLoader.loadItemClasses("Item.cryoGradItems"));

        ArrayList<Class<? extends Item>> goldenItems = new ArrayList<>(
                ItemLoader.loadItemClasses("Item.goldenSeasonItems"));

        // 👉 รวม default เข้าไปทุก map
        allItems.put("default", defaultItems);
        allItems.put("mysteriousJungle", combine(defaultItems, mysteriousItems));
        allItems.put("cryoGard", combine(defaultItems, cryoItems));
        allItems.put("goldenSeason", combine(defaultItems, goldenItems));
    }

    // รวม list
    private static ArrayList<Class<? extends Item>> combine(
            List<Class<? extends Item>> base,
            List<Class<? extends Item>> extra) {
        ArrayList<Class<? extends Item>> result = new ArrayList<>(base);
        result.addAll(extra);
        return result;
    }

    // 🎯 random item (new ใหม่ทุกครั้ง)
    public static Item resultRandomItem(String selectedMap) {

        ArrayList<Class<? extends Item>> list = allItems.get(selectedMap);

        // ✅ กันพัง
        if (list == null || list.isEmpty()) {
            System.out.println("⚠️ No items in map: " + selectedMap + ", fallback to default");
            list = allItems.get("default");
        }

        if (list == null || list.isEmpty()) {
            throw new RuntimeException("❌ No items available!");
        }

        int index = RandomUtilities.randomInt(list.size());

        try {
            return list.get(index).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("❌ Cannot create item", e);
        }
    }

    public static void logging() {
        System.out.println(allItems);
    }

}
package OtherUtilities;

import java.util.*;
import Item.*;

public class RandomItems {

    private static HashMap<String, ArrayList<Item>> allItems = new HashMap<>();
    
    static {

        ArrayList<Item> defaultItems = createDefaultItems();

        allItems.put("mysteriousJungle", createMysteriousJungleItems(defaultItems));
        allItems.put("cryoGard", createCryoGardItems(defaultItems));
        allItems.put("goldenSeason", createGoldenSeasonItems(defaultItems));
    }


    public static ArrayList<Item> createDefaultItems(){

        ArrayList<Item> list = new ArrayList<>();
        // เพิ่ม Item ที่มีทุกด่านตรงนี้
        // list.add(new Item());
        list.add(new DoubleDiceItem());
        list.add(new PullItem());
        return list;

    }

    public static ArrayList<Item> createMysteriousJungleItems(ArrayList<Item> base){

        ArrayList<Item> list = new ArrayList<>(base);
        // เพิ่ม Item ของด่าน Mysterious Jungle ตรงนี้
        // list.add(new Item());
        return list;

    }

    public static ArrayList<Item> createCryoGardItems(ArrayList<Item> base){

        ArrayList<Item> list = new ArrayList<>(base);
        // เพิ่ม Item ของด่าน Cryo-Gard ตรงนี้
        // list.add(new Item());
        list.add(new SpineItem());
        list.add(new BorealisItem());
        list.add(new QuantumGateItem());
        return list;

    }

    public static ArrayList<Item> createGoldenSeasonItems(ArrayList<Item> base){

        ArrayList<Item> list = new ArrayList<>(base);
        // เพิ่ม Item ของด่าน Golden Season ตรงนี้
        // list.add(new Item());
        return list;

    }

    // Method สำหรับส่งผลลัพธ์การสุ่ม วิธีใช้ => RandomItems.resultRandomItem();
    public static Item resultRandomItem(String selectedMap){

        ArrayList<Item> canUseItems = allItems.get(selectedMap);
        int index = RandomUtilities.randomInt(canUseItems.size());
        return canUseItems.get(index);
    }

}

package io.zipcoder;

import io.zipcoder.utils.FileReader;
import io.zipcoder.utils.Item;
import io.zipcoder.utils.ItemParseException;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GroceryReporter {
    ItemParser ip = new ItemParser();

    private final String originalFileText;

    public GroceryReporter(String jerksonFileName) {
        this.originalFileText = FileReader.readFile(jerksonFileName);
    }

    @Override
    public String toString() {
        List<Item> itemList = ip.parseItemList(originalFileText);
        Function<List<Item>,List<String>> getUniqueNames = (arr) -> {
            List<String> result = new ArrayList<>();
            Set<String> set = new HashSet<>();
            for (Item i: arr) {
                if(i.getName().equals("co0kies")) {
                    continue;
                }
                else if(set.add(i.getName())) result.add(i.getName());
            }
            return result;
        };
        BiFunction<String,List<Item>,Integer> getNumOfOcc = (s, arr) -> {
            int counter = 0;
            for (Item i: arr) {
                if(s.equals("cookies") && i.getName().equals("co0kies")) counter++;
                if(i.getName().equals(s)) counter++;
            }return counter;
        };
        List<String> uniqueNames = getUniqueNames.apply(itemList);
        BiFunction<Double,String,Integer> getNumOfOccPrice = (d, s) -> {
            int counter = 0;
            for (Item i: itemList) {
                if(s.equals("cookies") && i.getName().equals("co0kies") && Objects.equals(i.getPrice(), d)) counter++;
                if(Objects.equals(i.getPrice(), d) && i.getName().equals(s)) counter++;
            }
            return counter;
        };
        LinkedHashMap<String,Integer> nameToNumOfOcc = new LinkedHashMap<>();
        for (String name: uniqueNames
             ) {
            nameToNumOfOcc.put(name, getNumOfOcc.apply(name,itemList));
        }
        LinkedHashMap<String,LinkedHashMap<Double,Integer>> nameToPricesOccMap  = new LinkedHashMap<>();
        for (String name: uniqueNames
             ) {
            LinkedHashMap<Double,Integer> priceToNumOfOcc = new LinkedHashMap<>();
            for (Item item: itemList
                 ) {
                if(item.getName().equals(name)){
                    priceToNumOfOcc.put(item.getPrice(),getNumOfOccPrice.apply(item.getPrice(), item.getName()));
                }
            }
            nameToPricesOccMap.put(name,priceToNumOfOcc);
        }
        StringBuilder sb = new StringBuilder();
        int counter;
        String times = "times";
        for (String name: uniqueNames
             ) {
            counter = 1;
            sb.append(String.format("name:%8s \t\t %s: %d %s\n",name.substring(0,1).toUpperCase() + name.substring(1),"seen",nameToNumOfOcc.get(name),"times"));
            sb.append("============= \t \t =============\n");
            for (Double price: nameToPricesOccMap.get(name).keySet()) { //Double price: nameToPricesOccMap.get(name).keySet()
                if(nameToPricesOccMap.get(name).get(price) == 1) times = "time";
                else times = "times";
                if(counter == 2) sb.append("-------------\t\t -------------\n");
                sb.append(String.format("Price: \t%5.2f\t\t %s: %d %s\n",price,"seen",nameToPricesOccMap.get(name).get(price),times));
                counter++;
            }
            if(counter==2) sb.append("-------------\t\t -------------\n");
            sb.append("\n");
        }

        sb.append(String.format("%-15s\t \t %s: %d %s","Errors","seen",ip.getExceptionCounter() - 1 ,"times"));
        sb.append("\n");
        return sb.toString();
    }

}

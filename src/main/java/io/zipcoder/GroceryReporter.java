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
    private final String originalFileText;

    public GroceryReporter(String jerksonFileName) {
        this.originalFileText = FileReader.readFile(jerksonFileName);
    }

    @Override
    public String toString() {
        ItemParser ip = new ItemParser();
        List<Item> itemList = ip.parseItemList(originalFileText);
        Function<List<Item>,List<String>> getUniqueNames = (arr) -> {
            List<String> result = new ArrayList<>();
            Set<String> set = new HashSet<>();
            for (Item i: arr
                 ) {
                if(set.add(i.getName())) result.add(i.getName());
            }
            return result;
        };
        BiFunction<String,List<Item>,Integer> getNumOfOcc = (s, arr) -> {
            int counter = 0;
            for (Item i: arr
                 ) {
                if(i.getName().equals(s)) counter++;
            }
            return counter;
        };
        List<String> uniqueNames = getUniqueNames.apply(itemList);
        LinkedHashMap<String,Integer> nameToNumOfOcc = new LinkedHashMap<>();
        for (String name: uniqueNames
             ) {
            nameToNumOfOcc.put(name, getNumOfOcc.apply(name,itemList));
        }


        Function<List<Item>,List<Double>> getUniquePrices = (list) -> {
            List<Double> result = new ArrayList<>();
            Set<Double> set = new HashSet<>();
            for (Item i: list
            ) {
                if(set.add(i.getPrice())) result.add(i.getPrice());
            }
            return result;
        };
        LinkedHashMap<String,LinkedHashMap<Double,Integer>>  = new LinkedHashMap<>();




        int counter = 1;
        for (Item i: itemList
             ) {
            System.out.println(counter);
            System.out.println( i + "\n");
            counter++;
        }
        System.out.println(ip.getExceptionCounter());
        System.out.println(itemList.size());

        return  null;
    }


    public static void main(String[] args) {

    }
}

package io.zipcoder;

import io.zipcoder.utils.FileReader;
import io.zipcoder.utils.Item;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroceryReporter {
    ItemParser ip = new ItemParser();
    private final String originalFileText;

    public GroceryReporter(String jerksonFileName) {
        this.originalFileText = FileReader.readFile(jerksonFileName);
    }

    @Override
    public String toString() {
        List<Item> items = findAndReplaceCo0kies(ip.parseItemList(originalFileText));;

        //extract the unique names of all items
        List<String> uniqueNames = items.stream()
                .map(Item::getName)
                .distinct()
                .collect(Collectors.toList());

        //group the items in the list by name
        Map<String, List<Item>> itemsByName = items.stream()
                .collect(Collectors.groupingBy(Item::getName));

        //group and map the items in the list by name and price
        Map<String, Map<Double, List<Item>>> itemsByNameAndPrice = items.stream()
                .collect(Collectors.groupingBy(Item::getName, Collectors.groupingBy(Item::getPrice)));
        StringBuilder sb = new StringBuilder();
        int counter;
        for (String name: uniqueNames
             ) {
            counter = 1;
            sb.append(String.format("name:%8s \t\t %s: %d %s\n",name.substring(0,1).toUpperCase() +
                    name.substring(1),"seen",itemsByName.get(name).size(),"times"));
            sb.append("============= \t \t =============\n");
            for (Double price: itemsByNameAndPrice.get(name).keySet()) {
                int priceOccByItem = itemsByNameAndPrice.get(name).get(price).size();
                if(counter % 2 == 0) sb.append("-------------\t\t -------------\n");
                sb.append(String.format("Price: \t%5.2f\t\t %s: %d %s\n", price,"seen",
                        priceOccByItem,priceOccByItem == 1 ? "time" : "times"));
                counter++;
            }
            if(counter % 2 == 0) sb.append("-------------\t\t -------------\n");
            sb.append("\n");
        }
        sb.append(String.format("%-15s\t \t %s: %d %s","Errors","seen",ip.getExceptionCounter() - 1 ,"times"));
        sb.append("\n");
        return sb.toString();
    }
    public List<Item> findAndReplaceCo0kies(List<Item> toFix){
        for (Item item: toFix) {
            if(item.getName().equals("co0kies")) item.setName("cookies");
        }
        return toFix;
    }
}


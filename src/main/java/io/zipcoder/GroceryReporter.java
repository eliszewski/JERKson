package io.zipcoder;

import io.zipcoder.utils.FileReader;
import io.zipcoder.utils.Item;
import io.zipcoder.utils.ItemParseException;

public class GroceryReporter {
    private final String originalFileText;

    public GroceryReporter(String jerksonFileName) {
        this.originalFileText = FileReader.readFile(jerksonFileName);
    }

    @Override
    public String toString() {
        ItemParser ip = new ItemParser();
        int counter = 1;
        for (Item i: ip.parseItemList(originalFileText)
             ) {
            System.out.println(counter);
            System.out.println( i + "\n");
            counter++;
        }
        System.out.println(ip.getExceptionCounter());
        System.out.println(ip.parseItemList(originalFileText).size());

        return  null;
    }

    public static void main(String[] args) {

    }
}

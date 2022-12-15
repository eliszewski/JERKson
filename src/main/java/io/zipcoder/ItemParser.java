package io.zipcoder;

import io.zipcoder.utils.Item;
import io.zipcoder.utils.ItemParseException;
import io.zipcoder.utils.match.Match;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemParser {
    private String [] fieldnames ={"name","price","type","expiration"};
    private Integer exceptionCounter = 0;

    public Integer getExceptionCounter() {
        return exceptionCounter;
    }

    public List<Item> parseItemList(String valueToParse)  {
        List<Item> itemList = new ArrayList<>();
        Pattern pattern = Pattern.compile("(?<=##|^).*?(?=##|$)");
        Matcher matcher = pattern.matcher(valueToParse);
        while(matcher.find()){
            try{
                itemList.add(parseSingleItem(matcher.group()));
            }catch (ItemParseException ignored) {
                exceptionCounter++;
            }
        }
        return itemList;
    }

    public Item parseSingleItem(String singleItem) throws ItemParseException {
        String[] fields = new String[4];
        for (int i = 0; i < fields.length ; i++) {
            Pattern pattern = Pattern.compile("(?<="+fieldnames[i]+"[;:%@*^!]).*?(?=[;:%@*^#!]|$)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(singleItem);
            if (matcher.find()){
                    fields[i] = matcher.group().toLowerCase();
            }
        }
        checkForBadItem(fields);
        double price;
        try{
            price = Double.parseDouble(fields[1]);
        } catch (NumberFormatException e){
            throw new ItemParseException();
        }

        try{
            return new Item(fields[0],price,fields[2],fields[3]);
        } catch (NullPointerException e){
            throw new ItemParseException();
        }
    }
    public void checkForBadItem(String[] fields) throws ItemParseException {
        for (String s: fields
             ) {
            if(s==null || s.trim().equals("") ) throw new ItemParseException();
            else if(s.equalsIgnoreCase("name") || s.equalsIgnoreCase("price")
               || s.equalsIgnoreCase("type") || s.equalsIgnoreCase("expiration"))
                throw new ItemParseException();
        }
    }


}

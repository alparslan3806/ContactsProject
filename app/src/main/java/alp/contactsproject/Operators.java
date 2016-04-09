package alp.contactsproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Toshıba on 21.3.2016.
 */
public class Operators {

    public List<Map<String, String>> getAllNumbers(Map<String, String> numberAndNameMap)
    {
        List<Map<String, String>> listOperator = new ArrayList<>();

        Iterator<Map.Entry<String, String >> map = numberAndNameMap.entrySet().iterator();
        if( map != null )
        {
            while (map.hasNext()) {
                Map.Entry<String, String> entry = map.next();
                Map operatorMap = new HashMap();
                operatorMap.put("userIcon", R.drawable.nouser);
                operatorMap.put("userName", entry.getKey());
                operatorMap.put("userNumber", entry.getValue());
                listOperator.add(operatorMap);
            }
        }
        return listOperator;
    }

    public List<Map<String, String>> getOperator(Map<String, String> numberAndNameMap, char number1) {
        List<Map<String, String>> listOperator = new ArrayList<>();

        Iterator<Map.Entry<String, String>> map = numberAndNameMap.entrySet().iterator();
        Iterator<Map.Entry<String, String>> map2 = numberAndNameMap.entrySet().iterator();
        if (map != null) {
            while (map.hasNext()) {
                Map.Entry<String, String> entry = map.next();
                Map operatorMap = new HashMap();

                if ((entry.getValue().charAt(0) == '+') && entry.getValue().charAt(3) == '5' && (entry.getValue().charAt(4) == number1)) {
                    operatorMap.put("userIcon", R.drawable.nouser);
                    operatorMap.put("userName", entry.getKey());
                    operatorMap.put("userNumber", entry.getValue());
                    listOperator.add(operatorMap);

                } else if (entry.getValue().charAt(2) == number1 && entry.getValue().charAt(1) == '5') {
                    operatorMap.put("userIcon", R.drawable.nouser);
                    operatorMap.put("userName", entry.getKey());
                    operatorMap.put("userNumber", entry.getValue());
                    listOperator.add(operatorMap);
                }
            }

            if (number1 == '5') { /** This part of code is for avea which its number can start with 050... and 055... both.*/
                while (map2.hasNext()) {
                    Map.Entry<String, String> entry = map2.next();
                    Map operatorMap = new HashMap();

                    if ((entry.getValue().charAt(0) == '+') && entry.getValue().charAt(3) == '5' && (entry.getValue().charAt(4) == '0')) {
                        operatorMap.put("userIcon", R.drawable.nouser);
                        operatorMap.put("userName", entry.getKey());
                        operatorMap.put("userNumber", entry.getValue());
                        listOperator.add(operatorMap);

                    } else if (entry.getValue().charAt(2) == '0' && entry.getValue().charAt(1) == '5') {
                        operatorMap.put("userIcon", R.drawable.nouser);
                        operatorMap.put("userName", entry.getKey());
                        operatorMap.put("userNumber", entry.getValue());
                        listOperator.add(operatorMap);
                    }
                }
            }
        }
        return listOperator;
    }
    /** Version 1
     * Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
     * String printNumber = null, printName = null;
     * while (entries.hasNext()) {
     * Map.Entry<String, String> entry = entries.next();
     * printNumber += entry.getKey();
     * printName += entry.getValue();
     */
}

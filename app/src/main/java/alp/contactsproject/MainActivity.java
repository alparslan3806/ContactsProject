package alp.contactsproject;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    private ListView listView;
    private RadioButton aveaRadioButton,turkcellRadioButton,vodafoneRadioButton;
    private List<Map<String, String>> phoneAndNameMap = new ArrayList<>();
    private Map<String, String> item = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list);

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        cursor.moveToFirst();
        int indexName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

        int indexNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);


        item = new HashMap();
        do {
            

            String name = cursor.getString(indexName);
            String number = cursor.getString(indexNumber);

            item.put(name, number);
            phoneAndNameMap.add(item);

        } while (cursor.moveToNext());


        aveaRadioButton = (RadioButton) findViewById(R.id.aveaRadioButton);
        turkcellRadioButton = (RadioButton) findViewById(R.id.turkcellRadioButton);
        vodafoneRadioButton = (RadioButton) findViewById(R.id.vodafoneRadioButton);

    }

    public void printHasMap(List<Map<String, String>> listMap)
    {
        SimpleAdapter adapter = new SimpleAdapter(this, listMap, R.layout.item,
                new String[] {"userIcon", "userName", "userNumber" }, new int[] {R.id.userIcon, R.id.username, R.id.usertext});

        listView.setAdapter(adapter);

        /**
         * Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
         * String printNumber = null, printName = null;
         * while (entries.hasNext()) {
         * Map.Entry<String, String> entry = entries.next();
         * printNumber += entry.getKey();
         * printName += entry.getValue();
         */
    }

    public void radioButtonOnClick(View view)
    {
        int id = view.getId();
        List<Map<String, String>> temp = null;

        switch (id)
        {
            case R.id.aveaRadioButton:
                temp = getOperator(item, '5');
                printHasMap(temp);
                aveaRadioButton.setChecked(true);
                break;
            case R.id.turkcellRadioButton:
                temp = getOperator(item, '3');
                printHasMap(temp);
                turkcellRadioButton.setChecked(true);
                break;
            case R.id.vodafoneRadioButton:
                temp = getOperator(item, '4');
                printHasMap(temp);
                vodafoneRadioButton.setChecked(true);
        }
    }


    public List<Map<String, String>> getOperator(Map<String, String> numberAndNameMap, char number1) {
        List<Map<String, String>> listOperator = new ArrayList<>();


        Iterator<Map.Entry<String, String>> map = numberAndNameMap.entrySet().iterator();
        if(map != null) {
            while (map.hasNext()) {
                Map.Entry<String, String> entry = map.next();
                Map operatorMap = new HashMap();
                if ((entry.getValue().charAt(0) == '+') && (entry.getValue().charAt(7) == number1)) {
                    operatorMap.put("userIcon", R.drawable.nouser);
                    operatorMap.put("userName", entry.getKey());
                    operatorMap.put("userNumber", entry.getValue());
                    listOperator.add(operatorMap);

                } else if (entry.getValue().charAt(3) == number1) {
                    operatorMap.put("userIcon", R.drawable.nouser);
                    operatorMap.put("userName", entry.getKey());
                    operatorMap.put("userNumber", entry.getValue());
                    listOperator.add(operatorMap);
                }
            }
        }
        return listOperator;
    }
}
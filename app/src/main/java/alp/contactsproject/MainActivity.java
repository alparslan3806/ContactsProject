package alp.contactsproject;

import android.app.ListActivity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends ListActivity {

    private ListView listView;
    private TreeMap<String, String> phoneAndNameMap = new TreeMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        cursor.moveToFirst();
        int indexName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

        int indexNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

        do{
            String name = cursor.getString(indexName);
            String number = cursor.getString(indexNumber);

            phoneAndNameMap.put(number, name);

        }while(cursor.moveToNext());

        getAvea(phoneAndNameMap);

        //setListAdapter( phoneAndNameMap);
        listView = (ListView) findViewById(android.R.id.list);
        listView = getListView();
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);



    }

    public TreeMap<String, String> getAvea(TreeMap<String, String> numberAndNameMap)
    {
        TreeMap<String, String> aveaMap = new TreeMap<>();
        Iterator<Map.Entry<String, String>> map = numberAndNameMap.entrySet().iterator();
        while(map.hasNext())
        {
            Map.Entry<String, String> entry = map.next();
            if(entry.getKey().charAt(0) == '+' && entry.getKey().charAt(7) == '5')
            {
                aveaMap.put(entry.getKey(), entry.getValue());

            }else if(entry.getKey().charAt(3) == '5'){
                aveaMap.put(entry.getKey(), entry.getValue());
            }
        }
        return aveaMap;
    }

    public TreeMap<String, String> getTurkcell(TreeMap<String, String> numberAndNameMap)
    {
        TreeMap<String, String> turkcellMap = new TreeMap<>();
        Iterator<Map.Entry<String, String>> map = numberAndNameMap.entrySet().iterator();
        while(map.hasNext())
        {
            Map.Entry<String, String> entry = map.next();
            if(entry.getKey().charAt(0) == '+' && entry.getKey().charAt(7) == '3')
            {
                turkcellMap.put(entry.getKey(), entry.getValue());
            }else if(entry.getKey().charAt(3) == '3'){
                turkcellMap.put(entry.getKey(), entry.getValue());
            }
        }
        return turkcellMap;
    }

    public TreeMap<String, String> getVodafone(TreeMap<String, String> numberAndNameMap)
    {
        TreeMap<String, String> vodafoneMap = new TreeMap<>();
        Iterator<Map.Entry<String, String>> map = numberAndNameMap.entrySet().iterator();
        while(map.hasNext())
        {
            Map.Entry<String, String> entry = map.next();
            if(entry.getKey().charAt(0) == '+' && entry.getKey().charAt(7) == '4')
            {
                vodafoneMap.put(entry.getKey(), entry.getValue());
            }else if(entry.getKey().charAt(3) == '4'){
                vodafoneMap.put(entry.getKey(), entry.getValue());
            }
        }
        return vodafoneMap;
    }


}
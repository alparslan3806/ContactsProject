package alp.contactsproject;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.*;

public class MainActivity extends Activity {

    private ListView listView;
    private RadioButton aveaRadioButton, turkcellRadioButton, vodafoneRadioButton, allNumbers;
    private List<Map<String, String>> phoneAndNameMap = new ArrayList<>();
    private Map<String, String> item = new HashMap<>();
    private Operators operators = new Operators();
    private final String FILENAME = "contactslist";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list);

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
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
        allNumbers = (RadioButton) findViewById(R.id.allNumbers);
    }

    public void printHasMap(List<Map<String, String>> listMap) {
        SimpleAdapter adapter = new SimpleAdapter(this, listMap, R.layout.item,
                new String[]{"userIcon", "userName", "userNumber"}, new int[]{R.id.userIcon, R.id.username, R.id.usertext});

        listView.setAdapter(adapter);
    }

    public void radioButtonOnClick(View view) {
        int id = view.getId();
        List<Map<String, String>> temp = null;
        switch (id) {
            case R.id.aveaRadioButton:
                temp = operators.getOperator(item, '5');
                printHasMap(temp);
                aveaRadioButton.setChecked(true);
                break;
            case R.id.vodafoneRadioButton:
                temp = operators.getOperator(item, '4');
                printHasMap(temp);
                vodafoneRadioButton.setChecked(true);
                break;
            case R.id.turkcellRadioButton:
                temp = operators.getOperator(item, '3');
                printHasMap(temp);
                turkcellRadioButton.setChecked(true);
                break;
            case R.id.allNumbers:
                temp = operators.getAllNumbers(item);
                printHasMap(temp);
                allNumbers.setChecked(true);
                break;
        }
    }

    public void backUpClicked(View view) throws IOException {
        String fpath = "/sdcard/" + FILENAME + ".txt";
        File file = new File(fpath);
        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fwriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fwriter);

        Iterator<Map.Entry<String, String>> entries = item.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            String separator = "\r\n";
            StringBuilder lines = new StringBuilder();

            lines.append(entry.getKey());
            lines.append(":");
            lines.append(entry.getValue());
            lines.append(separator);
            bw.write(lines.toString());
        }
        Toast toast = Toast.makeText(this, "Succesfully Backed Up", Toast.LENGTH_SHORT);
        toast.show();
        bw.close();
        fwriter.close();
    }

    public void btnRestoreClicked(View view) throws IOException {

        Map<String, String> contactsFromFile = new HashMap<>();
        String fpath = "/sdcard/" + FILENAME + ".txt";
        File file = new File(fpath);

        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis);

        BufferedReader br = new BufferedReader(isr);
        String line = "";

        while ((line = br.readLine()) != null) {
            String[] arrayOfString = line.split(":");
            contactsFromFile.put(arrayOfString[0], arrayOfString[1]);
            /** Here I get the contact list which is backed up in file before. */
        }

        int i = 0;
        Iterator<Map.Entry<String, String>> contatcsIterator = contactsFromFile.entrySet().iterator();
        while (contatcsIterator.hasNext()) {
            Map.Entry<String, String> contactIterator = contatcsIterator.next();

            if (!item.containsKey(contactIterator.getKey())) {
                createContact(contactIterator.getKey(), contactIterator.getValue());
                i++;
            }
        }

        if (i > 0) {
            Toast toast = Toast.makeText(this, "Succesfully Recovered!", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Toast toast = Toast.makeText(this, "Contact List is up-to-date", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void createContact(String key, String value) {
        ArrayList<ContentProviderOperation> opsList = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = opsList.size();

        opsList.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        opsList.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, key)
                .build());

        opsList.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(
                        ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, value)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());
        try {
            ContentProviderResult[] res = getContentResolver().applyBatch(ContactsContract.AUTHORITY, opsList);
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
        }
    }
}
package com.example.sqliteexample;

        import androidx.appcompat.app.AppCompatActivity;

        import android.database.Cursor;
        import android.database.SQLException;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ListView;
        import android.widget.SimpleAdapter;
        import android.widget.TextView;

        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Button button;
    TextView textView;

    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDBHelper = new DatabaseHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = "INSERT INTO clients (name, age) VALUES ('19', 'Dmitry Tuchkov')";
                mDb.execSQL(query);
                updateList();
            }
        });
        updateList();
    }

    void updateList() {
        ArrayList<HashMap<String, Object>> clients = new ArrayList<HashMap<String, Object>>();

        HashMap<String, Object> client;

        Cursor cursor = mDb.rawQuery("SELECT * FROM clients", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            client = new HashMap<String, Object>();

            client.put("name",  cursor.getString(1));
            client.put("age",  cursor.getString(2));

            clients.add(client);

            cursor.moveToNext();
        }
        cursor.close();

        String[] from = { "name", "age"};
        int[] to = { R.id.textView, R.id.textView2};

        SimpleAdapter adapter = new SimpleAdapter(this, clients, R.layout.adapter_item, from, to);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }
}

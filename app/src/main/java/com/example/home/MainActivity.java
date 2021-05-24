package com.example.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    EditText etName, etSurname, etSearchname;
    Button btnSearch;
    TextView tvResult;
    ArrayList <Person> persons;
    boolean flag = false;

    private final String FILE_NAME = "Data.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();


        try {
            loadDataFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!flag)
                {
                    etSearchname.setVisibility(View.VISIBLE);
                    flag = true;
                }
                else
                {
                    String delPerson = etSearchname.getText().toString().trim();
                    if(delPerson.isEmpty())
                    {
                        etSearchname.setVisibility(View.GONE);
                        flag = false;
                    }
                    else
                    {
                        int index = findIndexOfPerson(delPerson);
                        if(index == -1)
                        {
                            Toast.makeText(MainActivity.this, "this person is not in the list", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            persons.remove(index);
                            showDataThroughTextview();
                            etSearchname.setText("");
                            etSearchname.setVisibility(View.GONE);
                            flag = false;
                        }
                    }
                }
            }
        });

    }

    public int findIndexOfPerson(String name)
    {
        for(int i=0; i<persons.size(); i++)
        {
            if(persons.get(i).getName().equals(name))
                return i;
        }

        return -1;
    }

    private void init()
    {
        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        tvResult = findViewById(R.id.tvResult);
        etSearchname = findViewById(R.id.etSearchname);
        etSearchname.setVisibility(View.GONE);
        btnSearch = findViewById(R.id.btnSearch);
        persons = new ArrayList<>();
    }

    public void btnAddPersonFunction(View v)
    {
        String name = etName.getText().toString().trim();
        String surname = etSurname.getText().toString().trim();
        if(name.isEmpty() || surname.isEmpty())
        {
            Toast.makeText(this, "Name or surname is empty", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Person p = new Person(name, surname);
            persons.add(p);
            showDataThroughTextview();
            makeEmptyFields();
        }
    }

    public void makeEmptyFields()
    {
        etName.setText("");
        etSurname.setText("");
    }

    public void showDataThroughTextview()
    {
        String text = "";
        for(Person person: persons)
        {
            text = text + person.toString();
        }

        tvResult.setText(text);
        text = "";
    }

    public void btnSaveDataFunction(View v)
    {
        try {
            FileOutputStream file = openFileOutput(FILE_NAME, MODE_PRIVATE);
            OutputStreamWriter outputFile = new OutputStreamWriter(file);

            for(Person person: persons)
            {
                outputFile.write(person.getName() + "," + person.getSurname() + "\n");
            }
            outputFile.flush();
            outputFile.close();

        }
        catch (IOException e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void loadDataFromFile() throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput(FILE_NAME)));

        String line = "";
        while((line = reader.readLine())!=null)
        {
            StringTokenizer tokens = new StringTokenizer(line, ",");
            Person person = new Person(tokens.nextToken(), tokens.nextToken());
            persons.add(person);
        }

        showDataThroughTextview();

    }
}
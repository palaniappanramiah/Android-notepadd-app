/******************************************************************
 * Name           : Palaniappan Ramiah, Sridhar Gerendla
 * ZID            : Z1726972, Z1728314
 * Class          : Android
 * Assignment No. : 3
 * Program Name   : MainActivity.java
 * Description    : Used to take notes
 * Due Date       : 03/25/2015 11:59:59 pm
 *****************************************************************/

package edu.niu.cs.sridhar.journal;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    // GUI controls
    EditText txtData;
    Button btnWriteSDFile;
    Button btnClearScreen;
    Button btnClose;
    private Spinner mySpin;

    SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd_hhmmss");
    //Default text file that is selected
    String fileName = "CSCI522.txt";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // object for the Spinner
        mySpin = (Spinner)findViewById(R.id.mySpinner);
        //Create an array adapter with a string array and use a default layout
        ArrayAdapter<CharSequence> myAdapter = ArrayAdapter.createFromResource(this,R.array.myList,android.R.layout.simple_spinner_item);
        //specify the layout to use the list of choice appears
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //connect the adapter to spinner
        mySpin.setAdapter(myAdapter);
        //Connect the Listener
        mySpin.setOnItemSelectedListener(this);

        // bind GUI elements with local controls
        txtData = (EditText) findViewById(R.id.txtData);
        txtData.setHint("Enter some lines here to save the file...");

        btnWriteSDFile = (Button) findViewById(R.id.btnWriteSDFile);
        btnWriteSDFile.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // write on SD card file data in the text box
                try {
                    Date now = Calendar.getInstance().getTime();
                    String currentDateTimeString = formatter.format(now);
                    File myFile = new File("/sdcard/"+fileName);
                    myFile.createNewFile();

                    FileOutputStream fOut = new FileOutputStream(myFile);
                    OutputStreamWriter myOutWriter =
                            new OutputStreamWriter(fOut);

                    myOutWriter.append("\n"+currentDateTimeString+"\n");
                    myOutWriter.append(txtData.getText());
                    myOutWriter.close();
                    fOut.close();

                    Toast.makeText(getBaseContext(), "Done writing to the file " + fileName,
                            Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }// onClick
        }); // btnWriteSDFile

        btnClearScreen = (Button) findViewById(R.id.btnClearScreen);
        btnClearScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // clear text box
                txtData.setText("");
            }
        }); // btnClearScreen

        btnClose = (Button) findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        }); // btnClose

    }// onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //you can retrieve the item that was selected by using parent.getItemAtPosition(Position)
        fileName = (parent.getItemAtPosition(position)).toString();
        try {
            File myFile = new File("/sdcard/"+fileName);
            if (myFile.exists()) {
                Toast.makeText(this,"The text file selected to read is " + parent.getItemAtPosition(position),Toast.LENGTH_LONG).show();
                FileInputStream fIn = new FileInputStream(myFile);
                BufferedReader myReader = new BufferedReader(
                        new InputStreamReader(fIn));

                String aDataRow = "";
                String aBuffer = "";

                while ((aDataRow = myReader.readLine()) != null) {
                    aBuffer += aDataRow + "\n";
                }
                txtData.setText(aBuffer);
                myReader.close();
            }
            else{
                txtData.setText("");
                txtData.setHint("Enter some lines here to save the file...");
                Toast.makeText(this,"Creating a new file named: " + parent.getItemAtPosition(position),Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
                Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

}
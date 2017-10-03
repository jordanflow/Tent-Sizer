package cj.patience.tentsizer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PlaceOrder extends AppCompatActivity {

    public EditText name;
    public EditText phone;
    public EditText email;
    public EditText comments;
    public double[] firstGPS;
    public double[] secondGPS;
    public double[] thirdGPS;
    public String tentSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        Button x = (Button) findViewById(R.id.place_order);
        Bundle y = getIntent().getExtras();
        tentSize = y.getString("tent_size");
        firstGPS = y.getDoubleArray("first_corner");
        secondGPS = y.getDoubleArray("second_corner");
        thirdGPS = y.getDoubleArray("third_corner");
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameString = name.getText().toString();
                    if(nameString == null){
                        Toast.makeText(PlaceOrder.this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();
                        return;
                    }
                String phoneString = phone.getText().toString();
                    if(phoneString == null || phoneString != "/[0-9]{3}/" + "-" + "/[0-9]{3}/" + "-" + "/[0-9]{4}/"){
                        Toast.makeText(PlaceOrder.this, "Please Enter Your Phone # XXX-XXX-XXXX", Toast.LENGTH_SHORT).show();
                        return;
                    }
                String emailString = email.getText().toString();
                    if(emailString == null || emailString != "/.+/" + "@" + "/.+/"){
                        Toast.makeText(PlaceOrder.this, "Please Enter Your Email You@NeedATent.com", Toast.LENGTH_SHORT).show();
                        return;
                    }
                String commentsString = comments.getText().toString();
                String[] emailBody = {nameString + "\n",phoneString + "\n",emailString + "\n",commentsString + "\n", firstGPS + "\n",secondGPS + "\n",thirdGPS.toString()};
                Intent z = new Intent(Intent.ACTION_SEND);
                z.setType("message/rf822");
                z.putExtra(Intent.EXTRA_EMAIL, new String[]{"YOUR@Company.com"});
                z.putExtra(Intent.EXTRA_EMAIL, "New Order");
                z.putExtra(Intent.EXTRA_TEXT, emailBody);
                startActivity(z);
            }
        });
    }

    public void sendEmail(){

    }
}

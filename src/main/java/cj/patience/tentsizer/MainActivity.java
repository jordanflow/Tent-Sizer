package cj.patience.tentsizer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button x = (Button) findViewById(R.id.button);
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOrder();
            }
        });
    }

    public void startOrder(){
        Intent x = new Intent(this, Order.class);
        startActivity(x);
    }
}

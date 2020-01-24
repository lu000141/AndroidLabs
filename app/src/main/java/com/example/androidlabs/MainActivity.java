package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;




public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         //setContentView(R.layout.activity_main);


        // setContentView loads objects onto the screen.
        // Before this function, the screen is empty.

     //  setContentView(R.layout.activity_main_linear);
       setContentView(R.layout.activity_main_grid);
      //  setContentView(R.layout.activity_main_relative);

        //Now that the screen was loaded, use findViewByid() to
        // get load the objects in Java:
        TextView textView = findViewById(R.id.textView);

        EditText editText = findViewById(R.id.editText);

        Button btn = findViewById(R.id.button);

        btn.setOnClickListener( new View.OnClickListener(){
            @Override
               public void onClick(View v){
                Toast.makeText(MainActivity.this,getResources().getString(R.string.toast_message),Toast.LENGTH_LONG).show();
            }
        });


        final Switch s = findViewById(R.id.switch1);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
               public void onCheckedChanged (CompoundButton cb, final boolean isChecked){
                if (isChecked == true){
                    Snackbar sb = Snackbar.make(cb,getResources().getString(R.string.switch_on), Snackbar.LENGTH_LONG);
                    sb.setAction("undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View click) {
                            s.setChecked(!isChecked);

                        }
                    });
                    sb.show();
                }
                else{

                    Snackbar sb = Snackbar.make(cb, getResources().getString(R.string.switch_off), Snackbar.LENGTH_LONG);
                    sb.setAction("undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View click) {
                            s.setChecked(!isChecked);
                        }
                    });
                    sb.show();
                }
            }
        });


        CheckBox cb = findViewById(R.id.checkBox);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged (CompoundButton cb, final boolean isChecked){
                if (isChecked== true){
                    Snackbar sb = Snackbar.make(cb, getResources().getString(R.string.check_on), Snackbar.LENGTH_LONG);
                    sb.setAction("undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View click) {
                            s.setChecked(!isChecked);
                        }
                    });
                    sb.show();
                }
                else{

                    Snackbar sb = Snackbar.make(cb,getResources().getString(R.string.check_off), Snackbar.LENGTH_LONG);
                    sb.setAction("undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View click) {
                            s.setChecked(!isChecked);
                        }
                    });
                    sb.show();
                }
            }
        });
    }

}

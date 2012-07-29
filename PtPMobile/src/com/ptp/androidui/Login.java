package com.ptp.androidui;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ptp.androidui.enums.Method;

public class login extends Activity  {
    public static final UUID uuid = UUID.randomUUID();
	/** Called when the activity is first created. */
    @Override
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //HttpClient h1= new DefaultHttpClient();
        //setContentView(R.layout.main);
        /*Result userslist=new Result();
        userslist.setResults(new HashMap<String, Result>());
        Result result =new Result();
        result.setCommandId(UUID.randomUUID());
       *//* HTTP Client */
        ScrollView sv=new ScrollView(this);
		LinearLayout ll = new LinearLayout(this);
		Resources res = getResources();
		Drawable d = res.getDrawable(R.drawable.login_image);
		ll.setBackgroundDrawable(d);
        ll.setOrientation(LinearLayout.VERTICAL);  
		sv.addView(ll);
		TextView tv1=new TextView(this);
		tv1.setText("ENTER YOUR NAME!!");
		//tv1.setTextColor(Color.BLUE);
		tv1.setTextAppearance(getApplicationContext(),R.style.CodeFont);
		ll.addView(tv1);
        final EditText  name=new EditText(this);
        ll.addView(name);
        Button b = new Button(this);
       // b.setWidth(150);
        b.setText("REGISTER"); 
       // b.setLayoutParams(ViewGroup. );
       // b.setTextAppearance(getApplicationContext(), R.style.CodeButton);
        ll.addView(b);  
        this.setContentView(sv);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(),PairtoPeer.class);
                try {
					//Command cmd = new Command();
					//cmd.setCommand("connect");
					userName = name.getText().toString();
					//cmd.setArguments(Arrays.asList(userName));
					utility.sendCommand("connect",Arrays.asList(userName),Method.PUT);
					//utility.sendCommand(cmd, Method.POST);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                startActivityForResult(myIntent, 0);
                
            }
            });
       // EditText texteditor =new EditText(this);
       
       
    }
    public static String getUserName()
    {
    	return userName;
    }
    private static String userName;  
	
}
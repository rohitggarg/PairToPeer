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

public class Login extends Activity {
	public static final UUID uuid = UUID.randomUUID();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ScrollView scrollView = new ScrollView(this);
		LinearLayout linearLayout = new LinearLayout(this);
		Resources res = getResources();
		Drawable loginImage = res.getDrawable(R.drawable.login_image);
		linearLayout.setBackgroundDrawable(loginImage);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		scrollView.addView(linearLayout);
		TextView nameTextBox = new TextView(this);
		nameTextBox.setText("ENTER YOUR NAME!!");
		nameTextBox.setTextAppearance(getApplicationContext(), R.style.CodeFont);
		linearLayout.addView(nameTextBox);
		final EditText name = new EditText(this);
		name.setLines(1);
		linearLayout.addView(name);
		Button registerButton = new Button(this);
		registerButton.setText("REGISTER");
		linearLayout.addView(registerButton);
		this.setContentView(scrollView);
		registerButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent myIntent = new Intent(view.getContext(),
						PairtoPeer.class);
				try {
					userName = name.getText().toString();
					Utility.sendCommand("connect", Arrays.asList(userName),
							Method.PUT);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				startActivityForResult(myIntent, 0);

			}
		});
	}

	public static String getUserName() {
		return userName;
	}

	private static String userName;

}
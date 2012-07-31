package com.ptp.androidui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ptp.androidui.enums.Method;
import com.ptp.dataobject.Editor;
import com.ptp.dataobject.Result;

public class PairtoPeer extends Activity {
	private EditText textEditor;
	private String pairedTo;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ScrollView scrollView = new ScrollView(this);
		LinearLayout linearLayout = new LinearLayout(this);
		Resources res = getResources();
		Drawable map = res.getDrawable(R.drawable.map);
		linearLayout.setBackgroundDrawable(map);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		scrollView.addView(linearLayout);
		TextView pairingMessage = new TextView(this);
		pairingMessage.setText("PLEASE SELECT YOUR PAIRING CODER!!");
		pairingMessage.setTextAppearance(getApplicationContext(),
				R.style.CodeFont);
		pairingMessage.setTextColor(Color.MAGENTA);
		linearLayout.addView(pairingMessage);
		try {
			Result userslist = Utility
					.sendCommand("connect", null, Method.POST);

			RadioGroup usersgroup = new RadioGroup(this);
			for (Entry<String, Result> user : userslist.getResults().entrySet()) {
				RadioButton radiousers = new RadioButton(this);
				radiousers.setText(user.getKey());
				radiousers.setTextColor(Color.GREEN);
				radiousers.setLayoutParams(new LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

				usersgroup.addView(radiousers);
			}
			linearLayout.addView(usersgroup);
			usersgroup
					.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

						public void onCheckedChanged(RadioGroup rg,
								int checkedId) {
							for (int i = 0; i < rg.getChildCount(); i++) {
								RadioButton btn = (RadioButton) rg
										.getChildAt(i);
								if (btn.getId() == checkedId) {
									pairedTo = btn.getText().toString();
									try {
										Editor targetEditor;
										if (pairedTo.equalsIgnoreCase(Login
												.getUserName())) {
											targetEditor = Utility.sendCommand(
													"pair",
													Arrays.asList(pairedTo),
													Method.PUT)
													.getResultTargetEditor();
											;// new
										} else {
											targetEditor = Utility.sendCommand(
													"pair",
													Arrays.asList(pairedTo),
													Method.POST)
													.getResultTargetEditor();
											;// new
										}
										textEditor.setText(targetEditor
												.getText());
									} catch (IOException e) {
										e.printStackTrace();
									} catch (ClassNotFoundException e) {
										e.printStackTrace();
									}
									return;
								}
							}
						}
					});
			TextView editorLabel = new TextView(this);
			editorLabel.setText("EDITOR WINDOW: ");
			editorLabel.setTextColor(Color.MAGENTA);
			linearLayout.addView(editorLabel);
			textEditor = new EditText(this);
			textEditor.setHeight(100);
			textEditor.setWidth(100);
			linearLayout.addView(textEditor);
			textEditor.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					try {
						Log.d("mytag", "values :" + s + " " + start + " " + before + " " + count);
						Utility.sendCommand("type", Arrays.asList(pairedTo, s
								+ "", start + "", count + ""), Method.PUT);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// nothing here
				}

				@Override
				public void afterTextChanged(Editable s) {
					// nothing here
				}
			});

			Thread syncerThread = new Thread() {
				public void run() {
					while (true) {
						if (pairedTo != null) {
							try {
								Result result = Utility.sendCommand("type",
										Arrays.asList(pairedTo), Method.POST);
								if (result != null
										&& result.getResults() != null
										&& result.getResults().size() > 0) {
									for (int i = 0; i < result.getResults()
											.size(); i++) {
										Editor type = result.getResults()
												.get(i + "")
												.getResultTargetEditor();
										int position = 0;
										String[] split = textEditor.getText()
												.toString().split("\n");
										for (int j = 1; j < type.getPosition()
												.getLineNumber(); j++) {
											position += split[j].length() + 1;
										}
										position += type.getPosition()
												.getColumnNumber();
										textEditor.getText().insert(position,
												type.getText());
									}
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};
			};
			syncerThread.start();

			TextView cmdtext = new TextView(this);
			cmdtext.setText("COMMAND WINDOW: ");
			cmdtext.setTextColor(Color.GREEN);
			linearLayout.addView(cmdtext);

			final EditText cmdeditor = new EditText(this);
			cmdeditor.setHeight(75);
			cmdeditor.setWidth(75);
			cmdeditor.setLines(1);
			linearLayout.addView(cmdeditor);
			Button commandWindow = new Button(this);
			commandWindow.setText("COMMAND");
			final AlertDialog alertDialog = new AlertDialog.Builder(this)
					.create();
			commandWindow.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					String usertext = cmdeditor.getText().toString();
					String cmdarray[] = usertext.split(" ");
					ArrayList<String> arguments = new ArrayList<String>();
					for (int i = 2; i < cmdarray.length; i++) {
						arguments.add(cmdarray[i]);
					}

					try {
						Result resultObject = Utility.sendCommand(cmdarray[0],
								arguments, Method.valueOf(cmdarray[1]));
						if (resultObject != null
								& resultObject.getResultTargetEditor() != null) {
							textEditor.setText(resultObject
									.getResultTargetEditor().getText()
									.toString());

						}

						alertDialog.setTitle("Result Window");
						if (resultObject != null) {
							alertDialog
									.setMessage("Command Executed Successfully"
											+ "\n" + "Resultant String:"
											+ "\n"
											+ resultObject.getResultString());
						}

						alertDialog.setButton("OK",
								new DialogInterface.OnClickListener() {

									// click listener on the alert box
									public void onClick(DialogInterface arg0,
											int arg1) {
										// the button was clicked
										Toast.makeText(getApplicationContext(),
												"OK button clicked",
												Toast.LENGTH_LONG).show();
									}
								});

						alertDialog.show();

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});

			linearLayout.addView(commandWindow);
			this.setContentView(scrollView);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
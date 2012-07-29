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
import android.view.KeyEvent;
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
	private EditText texteditor1;
	private String pairedTo;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// HttpClient h1= new DefaultHttpClient();
		// setContentView(R.layout.main);
		/*
		 * Result userslist=new Result(); userslist.setResults(new
		 * HashMap<String, Result>()); Result result =new Result();
		 * result.setCommandId(UUID.randomUUID());
		 *//* HTTP Client */
		//LinearLayout mainview=new LinearLayout(this);
		ScrollView sv = new ScrollView(this);
		LinearLayout ll = new LinearLayout(this);
		Resources res = getResources();
		Drawable d = res.getDrawable(R.drawable.map);
		ll.setBackgroundDrawable(d);
		ll.setOrientation(LinearLayout.VERTICAL);
		sv.addView(ll);
		//mainview.addView(sv);
		TextView tv1 = new TextView(this);
		tv1.setText("PLEASE SELECT YOUR PAIRING CODER!!");
		tv1.setTextAppearance(getApplicationContext(),R.style.CodeFont);
		tv1.setTextColor(Color.MAGENTA);
		ll.addView(tv1);
		try {

			//Command cmd = new Command();
			//cmd.setCommand("connect");
			//Result userslist = Utility.sendCommand(cmd, Method.POST);
			Result userslist=Utility.sendCommand("connect",null,Method.POST);//new
			 // userslist.getResults().put("Saurav", result);
			 // userslist.getResults().put("Ravi", result );
			// userslist.getResults().put("Rajan", result);
			 
			// int activatedusers=userslist.getResults().size();
			RadioGroup usersgroup = new RadioGroup(this);
			for (Entry<String, Result> user : userslist.getResults().entrySet()) {
					// createCheckbox(user.getKey(),
					// user.getValue().getCommandId());
					RadioButton radiousers = new RadioButton(this);
					radiousers.setText(user.getKey());
					radiousers.setTextColor(Color.GREEN);
					radiousers
							.setLayoutParams(new LayoutParams(
									LayoutParams.FILL_PARENT,
									LayoutParams.WRAP_CONTENT));

					usersgroup.addView(radiousers);
					/*
					 * final CheckBox checkBox = (CheckBox)
					 * findViewById(R.id.checkbox_id); if (checkBox.isChecked())
					 * { checkBox.setChecked(false); }
					 */
			}
			ll.addView(usersgroup);
			usersgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

						public void onCheckedChanged(RadioGroup rg,
								int checkedId) {
							for (int i = 0; i < rg.getChildCount(); i++) {
								RadioButton btn = (RadioButton) rg
										.getChildAt(i);
								if (btn.getId() == checkedId) {
									pairedTo = btn.getText().toString();
									try {
										//Command cmd = new Command();
										//cmd.setCommand("pair");
										//cmd.setArguments(Arrays.asList(text));
										//Editor sendCommand = Utility.sendCommand(cmd,	Method.PUT).getResultTargetEditor();
										
										Editor sendCommand;
										if(pairedTo.equalsIgnoreCase(Login.getUserName())) {
											sendCommand=Utility.sendCommand("pair",Arrays.asList(pairedTo),Method.PUT).getResultTargetEditor();;//new
										} else {
											sendCommand=Utility.sendCommand("pair",Arrays.asList(pairedTo),Method.POST).getResultTargetEditor();;//new
										}
										
										texteditor1.setText(sendCommand.getText());
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (ClassNotFoundException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									return;
								}
							}
						}
					});
			TextView  edittext= new TextView(this);
			edittext.setText("EDITOR WINDOW: ");
			edittext.setTextColor(Color.MAGENTA);
			ll.addView(edittext);
			texteditor1 = new EditText(this);
			texteditor1.setHeight(100);
			texteditor1.setWidth(100);
			ll.addView(texteditor1);
			final StringBuffer textentered=new StringBuffer();
			final String[] coordinates = new String[]{"0","0"};
			final int[] counter = {0};
			Thread timerThread = new Thread() {
				public void run() {
					while(true) {
						try {
							if(counter[0] <= 2 && textentered.length() > 0) {
								counter[0] = 0;
								//Command c = new Command();
								//c.setCommand("type");
								//c.setArguments(Arrays.asList(login.getUserName(),textentered.toString(),coordinates[0], coordinates[1]));
								//Utility.sendCommand(c, Method.PUT);
								Utility.sendCommand("type",Arrays.asList(pairedTo,textentered.toString(),coordinates[0], coordinates[1]),Method.PUT);//new
								textentered.delete(0, textentered.length());
							} else {
								counter[0] ++ ;
							}
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};
			};
			timerThread.start();
			
			Thread syncerThread = new Thread() {
				public void run() {
					while(true) {
						if(pairedTo != null) {
							try {
								Result result = Utility.sendCommand("type", Arrays.asList(pairedTo), Method.POST);
								if(result!=null && result.getResults()!=null && result.getResults().size() > 0) {
									for (int i = 0; i < result.getResults().size(); i++) {
										Editor type = result.getResults().get(i + "").getResultTargetEditor();
										int position = 0;
										String[] split = texteditor1.getText().toString().split("\n");
										for (int j = 1; j<type.getPosition().getLineNumber();j++) {
											position += split[j].length() + 1;
										}
										position+=type.getPosition().getColumnNumber();
										texteditor1.getText().insert(position, type.getText());
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
			final int[] lastSelectionIndex = new int[] {0};
			texteditor1.setOnKeyListener(new View.OnKeyListener(){
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					counter[0] = 0;
					// TODO Auto-generated method stub
					if (event.getAction()==KeyEvent.ACTION_DOWN)
					{
						int selectionStart = texteditor1.getSelectionStart();
						if(textentered.length() == 0) {
							String string = texteditor1.getText().toString();
							String[] subStrings = string.substring(0, selectionStart).split("\n");
							coordinates[0] = "" + subStrings.length;
							coordinates[1] = "" + subStrings[subStrings.length - 1].length();
						} else {
							if(!(lastSelectionIndex[0]+1 == selectionStart)) {
								//Command c = new Command();
								//c.setCommand("type");
								//c.setArguments(Arrays.asList(login.getUserName(),textentered.toString(),coordinates[0], coordinates[1]));
								try {
									//Utility.sendCommand(c, Method.PUT);
									Utility.sendCommand("type",Arrays.asList(pairedTo,textentered.toString(),coordinates[0], coordinates[1]),Method.PUT);//new
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (ClassNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								textentered.delete(0, textentered.length());
							}
						}
						textentered.append((char)event.getUnicodeChar());
						lastSelectionIndex[0] = selectionStart;
					}
					return true;
				}
				
			});
			
			TextView  cmdtext= new TextView(this);
			cmdtext.setText("COMMAND WINDOW: ");
			cmdtext.setTextColor(Color.GREEN);
			ll.addView(cmdtext);

			final EditText cmdeditor = new EditText(this);
			cmdeditor.setHeight(75);
			cmdeditor.setWidth(75);
			ll.addView(cmdeditor);
			Button command1 = new Button(this);
			command1.setText("COMMAND");
			final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			command1.setOnClickListener(new View.OnClickListener() {      
				public void onClick(View v) {              
					String usertext= cmdeditor.getText().toString(); 
					String cmdarray[]=usertext.split(" ");
					/*Command usercmd = new Command();
					usercmd.setCommand(cmdarray[0]);
					usercmd.setArguments(new ArrayList<String>());
					for(int i = 2;i<cmdarray.length;i++)
					{
						usercmd.getArguments().add(cmdarray[i]);
					}*/
					 ArrayList<String> arguments = new ArrayList<String>();
					 for(int i = 2;i<cmdarray.length;i++)
					 {
					 arguments.add(cmdarray[i]);
					 }

					try {
						//Result sendCommand = Utility.sendCommand(usercmd, Method.valueOf(cmdarray[1]));
						Result sendCommand =Utility.sendCommand(cmdarray[0],arguments,Method.valueOf(cmdarray[1]));
						if(sendCommand!=null & sendCommand.getResultTargetEditor()!=null)
						{
							String projectname=cmdarray[2];
							String filename=cmdarray[3];
							texteditor1.setText(sendCommand.getResultTargetEditor().getText().toString());
							
						}
							
						alertDialog.setTitle("Result Window");
						if(sendCommand!=null)
						{
							alertDialog.setMessage("Command Executed Successfully" + "\n" + "Resultant Error String:" + "\n" +sendCommand.getResultString());							
						}
						
						alertDialog.setButton("OK", new DialogInterface.OnClickListener() { 
							  
			                // click listener on the alert box 
			                public void onClick(DialogInterface arg0, int arg1) { 
			                    // the button was clicked 
			                    Toast.makeText(getApplicationContext(), "OK button clicked", Toast.LENGTH_LONG).show(); 
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
				
			
			//command1.setWidth(10);
			ll.addView(command1);
			this.setContentView(sv);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
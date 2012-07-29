package com.ptp.server.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import com.ptp.dataobject.Editor;
import com.ptp.dataobject.Pointer;

public class EditorTextSyncer {
	
	private HashMap<UUID,ArrayList<Editor>> changes = new HashMap<UUID, ArrayList<Editor>>();
	private Editor editor;
	
	public EditorTextSyncer(Editor editor) {
		this.editor = editor;
		addSession(this.editor);
	}
	
	public void type(Editor e) throws IOException {
		String[] textLines = editor.getText().split("\n");
		StringBuffer textBuffer = new StringBuffer();
		for (int i = 0 ; i < textLines.length ; i++ ) {
			String textToEdit = textLines[i];
			if(e.getPosition().getLineNumber() == i + 1) {
				textBuffer.append(textToEdit.substring(0,e.getPosition().getColumnNumber()));
				textBuffer.append(e.getText());
				textBuffer.append(textToEdit.substring(e.getPosition().getColumnNumber()));
			} else {
				textBuffer.append(textToEdit);
			}
			textBuffer.append("\n");
		}
		editor.setText(textBuffer.toString());
		System.out.println(editor);
		for (Entry<UUID, ArrayList<Editor>> pairedUser : changes.entrySet()) {
			if(pairedUser.getKey().equals(e.getUserId())) {
				continue;
			}
			pairedUser.getValue().add(e);
		}
	}
	
	public void addSession(Editor editor) {
		changes.put(editor.getUserId(), new ArrayList<Editor>());
	}
	public void removeSession(UUID userId) {
		changes.remove(userId);
	}
	
	public ArrayList<Editor> poll(UUID userId) {
		ArrayList<Editor> arrayList = changes.remove(userId);
		changes.put(userId, new ArrayList<Editor>());
		return arrayList;
	}
	
	public Editor getEditor() {
		return editor;
	}
	
	public static void main(String[] args) throws IOException {
		Editor editor2 = new Editor();
		UUID userid2 = UUID.randomUUID();
		editor2.setUserId(userid2);
		editor2.setText("start typing\n test test");
		
		EditorTextSyncer editorTextSyncer = new EditorTextSyncer(editor2);
		editorTextSyncer.addSession(editor2);
		Editor e = new Editor();
		UUID userId = UUID.randomUUID();
		e.setUserId(userId);
		e.setText("asd");
		e.setPosition(new Pointer(2,5));
		editorTextSyncer.addSession(e);
		editorTextSyncer.type(e);
		e = new Editor();
		e.setUserId(userid2);
		e.setText("asdff");
		e.setPosition(new Pointer(1,2));
		editorTextSyncer.type(e);
		System.out.println(editorTextSyncer.poll(userId));
		System.out.println(editorTextSyncer.poll(userid2));
	}
}

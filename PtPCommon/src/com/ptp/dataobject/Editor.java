package com.ptp.dataobject;

import java.io.Serializable;
import java.util.UUID;

public class Editor  implements Serializable, Cloneable {
	private static final long serialVersionUID = 5121619978698545874L;
	private Pointer position;
	private UUID userId;
	private String text;
	public Pointer getPosition() {
		return position;
	}
	public void setPosition(Pointer position) {
		this.position = position;
	}
	public UUID getUserId() {
		return userId;
	}
	public void setUserId(UUID userId) {
		this.userId = userId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	@Override
	public String toString() {
		return text;
	}
}

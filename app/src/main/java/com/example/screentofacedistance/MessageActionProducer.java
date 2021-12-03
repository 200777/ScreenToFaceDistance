package com.example.screentofacedistance;

import java.util.ArrayList;
import java.util.List;

public class MessageActionProducer {

	public static final int MEDIA_BUTTON_CLICKED = 0;

	public static final int MEASUREMENT_STEP = 1;

	public static final int DONE_CALIBRATION = 2;

	public static final int DONE_MEASUREMENT = 3;

	private static MessageActionProducer i = new MessageActionProducer();
	private final List<IMessageActionListener> list = new ArrayList<IMessageActionListener>();
	public static MessageActionProducer get() {
		return i;
	}



	public boolean registration(IMessageActionListener l) {
		if (!list.contains(l)) {
			return list.add(l);
		}

		return false;
	}
	public void messageSend(int id, Object msg) {
		for (IMessageActionListener listMessage : list) {
			listMessage.onMessage(id, msg);
		}
	}
	public boolean unregistration(final IMessageActionListener l) {
		return list.remove(l);
	}



}

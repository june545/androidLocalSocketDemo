package com.exmaple.socket.local;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	Button		sendBtn;

	SocketTest	socketTest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		socketTest = new SocketTest();

		sendBtn = (Button) findViewById(R.id.send_btn);
		sendBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				socketTest.send("" + System.currentTimeMillis());
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		socketTest.close();
	}

}

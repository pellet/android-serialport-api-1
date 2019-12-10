/*
 * Copyright 2009 Cedric Priscal
 * Edit 2017 Benjamin Erdnüß
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package com.erdnuess.serialexample.sample;

import java.io.IOException;

import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class ConsoleActivity extends SerialPortActivity {

	EditText mReception;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.console);

//		setTitle("Loopback test");
		mReception = (EditText) findViewById(R.id.EditTextReception);

        /*
            Edit: Benjamin Erdnüß
            Originally, the EditText "Emmission" got an EditorActionListener but it did not seem to work like it should. I worked around with a TextChangedListener.
            To prevent the textbox to fire the event multiple times, I added - android:
            ="textNoSuggestions" - to the textbox in the layout file!
            Reference: https://stackoverflow.com/a/19298614
        */
        EditText Emission = (EditText) findViewById(R.id.EditTextEmission);
        Emission.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                char c = editable.charAt(editable.length() - 1);
                try {
                    if (c == '\n')
                    {
                        //Write carriage return before newline as expected by modems.
                        mOutputStream.write('\r');
                    }
                    mOutputStream.write(c);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
	}

	@Override
	protected void onDataReceived(final byte[] buffer, final int size) {
		runOnUiThread(new Runnable() {
			public void run() {
				if (mReception != null) {
					mReception.append(new String(buffer, 0, size));
				}
			}
		});
	}
}

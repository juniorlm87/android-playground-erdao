/*
 * Copyright (C) 2009 Huan Erdao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.erdao.android.SnapFace;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;

/* Activity Class
 */
public class SnapFaceActivity extends Activity {
	private PreviewView camPreview_;
	private int fdetLevel_;
	private int appMode_;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* set Full Screen */
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		/* set window with no title bar */
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		/* Restore preferences */
		SharedPreferences settings = getSharedPreferences(getString(R.string.SnapFacePreference), 0);
		appMode_ = settings.getInt(getString(R.string.menu_AppMode), 0);
		fdetLevel_ = settings.getInt(getString(R.string.menu_Preferences), 1);
		
		/* create camera view */
		camPreview_ = new PreviewView(this);
		camPreview_.setAppMode(appMode_);
		camPreview_.setfdetLevel(fdetLevel_,true);
		setContentView(camPreview_);
		/* append Overlay */
		addContentView(camPreview_.getOverlay(), new LayoutParams 
				(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}
	
	/* create Menu */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuItem menu_Preference = menu.add(0,R.id.menu_Preferences,0,R.string.menu_Preferences);
		menu_Preference.setIcon(android.R.drawable.ic_menu_preferences);
		MenuItem menu_AppMode = menu.add(0,R.id.menu_AppMode,0,R.string.menu_AppMode);
		menu_AppMode.setIcon(android.R.drawable.ic_menu_manage);
		return true;
	}

	/* Menu handling */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_Preferences:{
				showDialog(R.id.PreferencesDlg);
				break;
			}
			case R.id.menu_AppMode:{
				showDialog(R.id.AppModeDlg);
				break;
			}
		}
		return true;
	}

	/* onCreateDialog */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			// create progress dialog for search
			// create map selection dialog and change modes
			case R.id.PreferencesDlg: {
				return new AlertDialog.Builder(this)
				.setTitle(R.string.PreferencesDlgTitle)
				.setSingleChoiceItems(R.array.select_fdetlevel, fdetLevel_, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						fdetLevel_ = whichButton;
						camPreview_.setfdetLevel(fdetLevel_,false);
						SharedPreferences settings = getSharedPreferences(getString(R.string.SnapFacePreference), 0);
						SharedPreferences.Editor editor = settings.edit();
						editor.putInt(getString(R.string.menu_Preferences), whichButton);
						editor.commit();
						dismissDialog(R.id.PreferencesDlg);
					}
				})
				.create();
			}
			case R.id.AppModeDlg: {
				return new AlertDialog.Builder(this)
				.setTitle(R.string.PreferencesDlgTitle)
				.setSingleChoiceItems(R.array.select_appmode, appMode_, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						camPreview_.setAppMode(whichButton);
						SharedPreferences settings = getSharedPreferences(getString(R.string.SnapFacePreference), 0);
						SharedPreferences.Editor editor = settings.edit();
						editor.putInt(getString(R.string.menu_AppMode), whichButton);
						editor.commit();
						dismissDialog(R.id.AppModeDlg);
					}
				})
				.create();
			}
		}
		return null;
	}
}

package com.example.svankayalapati.samplecontainerlwc.ui;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.svankayalapati.samplecontainerlwc.R;
import com.example.svankayalapati.samplecontainerlwc.constants.SAConstants;
import com.example.svankayalapati.samplecontainerlwc.controller.Controller;
import com.example.svankayalapati.samplecontainerlwc.receivers.DeviceAdministrator;
import com.example.svankayalapati.samplecontainerlwc.receivers.LicenseReceiver;
import com.example.svankayalapati.samplecontainerlwc.utils.SAUIHelper;


//This is the launcher Activity of this application. It asks user to enable Admin/BYOD 
//and activate KLM and ELM

public class AdminLicenseActivationActivity extends Activity implements OnClickListener {

	Button btnActivateKLM, btnActivateELM;
	EditText edtKLM, edtELM;
	Controller mController;
	SharedPreferences.Editor adminLicensePrefsEditor;
	SharedPreferences adminLicensePrefs;
	DevicePolicyManager mDPM;
	ComponentName mCN;
	LicenseReceiver mLicenseReceiver;
	DeviceAdministrator mDeviceAdmin;
	RadioGroup radioGrpAdminOrByod;
	RadioButton radioAdminButton;
	RadioButton radioByodButton;
	boolean isActivityVisible;

	// The follow keys are hard-coded for demonstration purposes only. You must enter your key
	// values here, and in a production app, you should get your keys by a secure method, and not
	// code them directly in your app.
	static public final String demoKLMKey = "KLM03-64XNS-ZT6V9-LFMHZ-4MJ9E-9R6JA";
	static public final String demoELMKey = "4A8C019C25B9D64343EA4CEF8A141CD7D419169224B65D676A1E4DD664DDB76BDECF2B374D63B090298449459A2B8DEEC46CC7D553A68ED4AB78AB8F6E353646";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(AdminLicenseActivationActivity.this.getLocalClassName(), "in onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_license_activation);

		// pass current Activity context to LicenseReceiver
		if (mLicenseReceiver == null) {
			mLicenseReceiver = new LicenseReceiver(AdminLicenseActivationActivity.this);
		}
		// pass current Activity context to DeviceAdministrator
		if (mDeviceAdmin == null) {
			mDeviceAdmin = new DeviceAdministrator(AdminLicenseActivationActivity.this);
		}

		radioAdminButton = (RadioButton) findViewById(R.id.radioAdmin);
		radioByodButton = (RadioButton) findViewById(R.id.radioByod);
		btnActivateKLM = (Button) findViewById(R.id.admin_license_activation_btn_activate_klm);
		btnActivateELM = (Button) findViewById(R.id.admin_license_activation_btn_activate_elm);

		edtKLM = (EditText) findViewById(R.id.admin_license_activation_edt_klm_key);
		edtELM = (EditText) findViewById(R.id.admin_license_activation_edt_elm_key);

		// Set the ELM and KLM fields to invalid key values. User must input their own valid keys
		// to successfully create a container.
		Log.i(AdminLicenseActivationActivity.this.getLocalClassName(), "set fake key values.");
		edtELM.setText(demoELMKey);
		edtKLM.setText(demoKLMKey);

		adminLicensePrefsEditor = getSharedPreferences(SAConstants.MY_PREFS_NAME, MODE_PRIVATE).edit();
		adminLicensePrefs = getSharedPreferences(SAConstants.MY_PREFS_NAME, MODE_PRIVATE);

	}

	// This method is used to launch the next activity once Admin/BYOD is
	// enabled and KLM,ELM are activated
	void startHomeActivity() {
		System.out.println("AdminLicenseActivationActivity.startHomeActivity(ContainerCreationRemovalActivity)");
		Intent intent = new Intent(AdminLicenseActivationActivity.this, ContainerCreationRemovalActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mController = new Controller(AdminLicenseActivationActivity.this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		isActivityVisible = true;
		// Setting the initial state of UI screen
		setUIStates(SAConstants.INITIAL_STATE);
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		isActivityVisible = false;
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mController = null;
	}

	// Called when admin is enabled
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(AdminLicenseActivationActivity.this.getLocalClassName(), "in onActivityResult()");
		try {
			if (requestCode == SAConstants.RESULT_ENABLE_ADMIN) {
				if (resultCode == Activity.RESULT_CANCELED) {
					Log.i(AdminLicenseActivationActivity.this.getLocalClassName(), "showToast(RESULT_CANCELED)");
					SAUIHelper.showToast(AdminLicenseActivationActivity.this,
							SAConstants.CANCELLED_ENABLING_DEVICE_ADMIN);
				} else if (resultCode == Activity.RESULT_OK) {
					setUIStates(requestCode);
				}
			}
		} catch (Exception e) {
			Log.e(AdminLicenseActivationActivity.this.getLocalClassName(), e.getMessage());
		}
	}

	// This method is used to set the UI state
	public void setUIStates(int condition) {

		System.out.println("AdminLicenseActivationActivity.setUIStates()");

		switch (condition) {

		// initial UI state
		case SAConstants.INITIAL_STATE:

			System.out.println("AdminLicenseActivationActivity.setUIStates(INITIAL_STATE)");

			// if Admin and BYOD both are not enabled
			if (!adminLicensePrefs.getBoolean(SAConstants.ADMIN, false)
					&& !adminLicensePrefs.getBoolean(SAConstants.BYOD, false)) {
				//edtKLM.setText("");
				//edtELM.setText("");
				edtKLM.setEnabled(false);
				edtELM.setEnabled(false);
				disableButton(btnActivateKLM);
				disableButton(btnActivateELM);
				/*
				 * btnActivateKLM.setEnabled(false);
				 * btnActivateELM.setEnabled(false);
				 * btnActivateKLM.setBackground
				 * (getResources().getDrawable(R.drawable.button_disabled));
				 * btnActivateELM
				 * .setBackground(getResources().getDrawable(R.drawable
				 * .button_disabled));
				 */
			}
			// if either admin or BYOD is enabled
			else {

				if (adminLicensePrefs.getBoolean(SAConstants.BYOD, false)) {
					radioByodButton.setChecked(true);
				}
				edtKLM.setEnabled(true);
				edtELM.setEnabled(true);
				enableButton(btnActivateKLM);
				enableButton(btnActivateELM);
				/*
				 * btnActivateKLM.setEnabled(true);
				 * btnActivateELM.setEnabled(true);
				 * btnActivateKLM.setBackground(
				 * getResources().getDrawable(R.drawable.button_enabled));
				 * btnActivateELM
				 * .setBackground(getResources().getDrawable(R.drawable
				 * .button_enabled));
				 */
			}

			// if admin is enabled
			if (adminLicensePrefs.getBoolean(SAConstants.ADMIN, false)) {

				radioAdminButton.setText(SAConstants.DISABLE_ADMIN);
				edtKLM.setEnabled(true);
				edtELM.setEnabled(true);
				enableButton(btnActivateKLM);
				enableButton(btnActivateELM);
				/*
				 * btnActivateKLM.setEnabled(true);
				 * btnActivateELM.setEnabled(true);
				 * btnActivateKLM.setBackground(
				 * getResources().getDrawable(R.drawable.button_enabled));
				 * btnActivateELM
				 * .setBackground(getResources().getDrawable(R.drawable
				 * .button_enabled));
				 */

			}
			/*
			 * // if admin is not enabled or disabled else {
			 * radioAdminButton.setText(SAConstants.ENABLE_ADMIN);
			 * edtKLM.setText(""); edtELM.setText(""); edtKLM.setEnabled(false);
			 * edtELM.setEnabled(false); btnActivateKLM.setEnabled(false);
			 * btnActivateELM.setEnabled(false); }
			 */

			// if KLM and ELM are activated
			if (adminLicensePrefs.getBoolean(SAConstants.KLM, false)
					&& adminLicensePrefs.getBoolean(SAConstants.ELM, false)) {

				// if either admin or BYOD is enabled
				if (isActivityVisible && (adminLicensePrefs.getBoolean(SAConstants.ADMIN, false)
						|| adminLicensePrefs.getBoolean(SAConstants.BYOD, false))) {
					startHomeActivity();
				}
			}

			// if KLM is activated
			if (adminLicensePrefs.getBoolean(SAConstants.KLM, false)) {

				disableButton(btnActivateKLM);
				// btnActivateKLM.setEnabled(false);
				edtKLM.setEnabled(false);
				// btnActivateKLM.setBackground(getResources().getDrawable(R.drawable.button_disabled));
			}

			// if ELM is activated
			if (adminLicensePrefs.getBoolean(SAConstants.ELM, false)) {

				disableButton(btnActivateELM);
				// btnActivateELM.setEnabled(false);
				edtELM.setEnabled(false);
				// btnActivateELM.setBackground(getResources().getDrawable(R.drawable.button_disabled));
			}

			break;

		// state when admin is enabled
		case SAConstants.RESULT_ENABLE_ADMIN:

			System.out.println("AdminLicenseActivationActivity.setUIStates(RESULT_ENABLE_ADMIN)");

			radioAdminButton.setChecked(false);
			// change the text to "Disable admin"
			radioAdminButton.setText(SAConstants.DISABLE_ADMIN);
			adminLicensePrefsEditor.putBoolean(SAConstants.ADMIN, true);
			adminLicensePrefsEditor.putBoolean(SAConstants.BYOD, false);
			adminLicensePrefsEditor.commit();
			// enable KLM,ELM edittexts and buttons
			edtKLM.setEnabled(true);
			edtELM.setEnabled(true);
			enableButton(btnActivateKLM);
			enableButton(btnActivateELM);
			/*
			 * btnActivateKLM.setEnabled(true); btnActivateELM.setEnabled(true);
			 * btnActivateKLM
			 * .setBackground(getResources().getDrawable(R.drawable
			 * .button_enabled));
			 * btnActivateELM.setBackground(getResources().getDrawable
			 * (R.drawable.button_enabled));
			 */

			break;

		// state when admin is disabled
		case SAConstants.RESULT_DISABLE_ADMIN:

			System.out.println("AdminLicenseActivationActivity.setUIStates(RESULT_DISABLE_ADMIN)");

			radioAdminButton.setChecked(false);
			// change the text to "Enable admin"
			radioAdminButton.setText(SAConstants.ENABLE_ADMIN);
			adminLicensePrefsEditor.putBoolean(SAConstants.ADMIN, false);
			adminLicensePrefsEditor.putBoolean(SAConstants.KLM, false);
			adminLicensePrefsEditor.putBoolean(SAConstants.ELM, false);
			adminLicensePrefsEditor.commit();
			// disable KLM,ELM edittexts and buttons
			//edtKLM.setText("");
			//edtELM.setText("");
			edtKLM.setEnabled(false);
			edtELM.setEnabled(false);
			disableButton(btnActivateKLM);
			disableButton(btnActivateELM);
			/*
			 * btnActivateKLM.setEnabled(false);
			 * btnActivateELM.setEnabled(false);
			 * btnActivateKLM.setBackground(getResources
			 * ().getDrawable(R.drawable.button_disabled));
			 * btnActivateELM.setBackground
			 * (getResources().getDrawable(R.drawable.button_disabled));
			 */

			break;

		// state when KLM is activated
		case SAConstants.RESULT_KLM_ACTIVATED:

			System.out.println("AdminLicenseActivationActivity.setUIStates(RESULT_KLM_ACTIVATED)");

			disableButton(btnActivateKLM);
			// btnActivateKLM.setEnabled(false);
			edtKLM.setEnabled(false);
			// btnActivateKLM.setBackground(getResources().getDrawable(R.drawable.button_disabled));
			adminLicensePrefsEditor.putBoolean(SAConstants.KLM, true);
			adminLicensePrefsEditor.commit();
			// if ELM is activated
			if (adminLicensePrefs.getBoolean(SAConstants.ELM, false)) {
				// if either admin or BYOD is enabled, launch next activity
				if (isActivityVisible && (adminLicensePrefs.getBoolean(SAConstants.ADMIN, false)
						|| adminLicensePrefs.getBoolean(SAConstants.BYOD, false))) {
					startHomeActivity();
				}
			}

			break;

		// state when ELM is activated
		case SAConstants.RESULT_ELM_ACTIVATED:

			System.out.println("AdminLicenseActivationActivity.setUIStates(RESULT_ELM_ACTIVATED)");

			disableButton(btnActivateELM);
			// btnActivateELM.setEnabled(false);
			edtELM.setEnabled(false);
			// btnActivateELM.setBackground(getResources().getDrawable(R.drawable.button_disabled));
			adminLicensePrefsEditor.putBoolean(SAConstants.ELM, true);
			adminLicensePrefsEditor.commit();

			// if KLM is activated
			if (adminLicensePrefs.getBoolean(SAConstants.KLM, false)) {
				// if either admin or BYOD is enabled, launch next activity
				if (isActivityVisible && (adminLicensePrefs.getBoolean(SAConstants.ADMIN, false)
						|| adminLicensePrefs.getBoolean(SAConstants.BYOD, false))) {
					startHomeActivity();
				}
			}

			break;

		// state when BYOD is enabled
		case SAConstants.RESULT_BYOD_ENABLED:

			System.out.println("AdminLicenseActivationActivity.setUIStates(RESULT_BYOD_ENABLED)");

			adminLicensePrefsEditor.putBoolean(SAConstants.BYOD, true);
			adminLicensePrefsEditor.commit();
			// enable KLM,ELM edittexts and buttons
			edtKLM.setEnabled(true);
			edtELM.setEnabled(true);
			enableButton(btnActivateKLM);
			enableButton(btnActivateELM);
			/*
			 * btnActivateKLM.setEnabled(true); btnActivateELM.setEnabled(true);
			 * btnActivateKLM
			 * .setBackground(getResources().getDrawable(R.drawable
			 * .button_enabled));
			 * btnActivateELM.setBackground(getResources().getDrawable
			 * (R.drawable.button_enabled));
			 */

			break;

		// state when BYOD is disabled
		case SAConstants.RESULT_BYOD_DISABLED:

			System.out.println("AdminLicenseActivationActivity.setUIStates(RESULT_BYOD_DISABLED)");

			adminLicensePrefsEditor.putBoolean(SAConstants.BYOD, false);
			adminLicensePrefsEditor.commit();
			// disable KLM,ELM edittexts and buttons
			//edtKLM.setText("");
			//edtELM.setText("");
			edtKLM.setEnabled(false);
			edtELM.setEnabled(false);
			disableButton(btnActivateKLM);
			disableButton(btnActivateELM);
			/*
			 * btnActivateKLM.setEnabled(false);
			 * btnActivateELM.setEnabled(false);
			 * btnActivateKLM.setBackground(getResources
			 * ().getDrawable(R.drawable.button_disabled));
			 * btnActivateELM.setBackground
			 * (getResources().getDrawable(R.drawable.button_disabled));
			 */

			break;

		default:
			break;

		}

	}

	public void enableButton(Button buttonObj) {
		buttonObj.setEnabled(true);

	}

	public void disableButton(Button buttonObj) {
		buttonObj.setEnabled(false);
	}

	// This method handles onClick event of any clicked button
	@Override
	public void onClick(View v) {

		Log.i(AdminLicenseActivationActivity.this.getLocalClassName(), "in onclick");

		switch (v.getId()) {
		// Admin radio button clicked
		case R.id.radioAdmin:
			// if admin needs to be enabled
			if (!adminLicensePrefs.getBoolean(SAConstants.ADMIN, false)) {
				// enable admin
				if (!mController.activateAdmin()) {
					SAUIHelper.showToast(AdminLicenseActivationActivity.this, SAConstants.ADMIN_ALREADY_ENABLED);
				}
			}
			// if admin needs to be disabled
			else {
				// disable admin
				if (mController.deactivateAdmin()) {
					setUIStates(SAConstants.RESULT_DISABLE_ADMIN);
				} else {
					SAUIHelper.showToast(AdminLicenseActivationActivity.this, SAConstants.ADMIN_ALREADY_DISABLED);
				}
			}

			break;

		// BYOD radio button clicked
		case R.id.radioByod:

			// if BYOD needs to be enabled
			if (!adminLicensePrefs.getBoolean(SAConstants.BYOD, false)) {
				// if admin is enabled
				if (adminLicensePrefs.getBoolean(SAConstants.ADMIN, false)) {
					// cannot enable BYOD,disable admin first
					radioByodButton.setChecked(false);
					SAUIHelper.showToast(AdminLicenseActivationActivity.this, SAConstants.PLEASE_DISABLE_ADMIN_FIRST);
				}
				// if admin is not enabled
				else {
					// enable BYOD
					setUIStates(SAConstants.RESULT_BYOD_ENABLED);
				}
			}

			break;

		// Activate KLM button clicked
		case R.id.admin_license_activation_btn_activate_klm:

			// if KLM key is not empty
			if (!edtKLM.getText().toString().equalsIgnoreCase("")) {
				// if admin is enabled
				if (adminLicensePrefs.getBoolean(SAConstants.ADMIN, false)) {
					// activate KLM with admin enabled API
					mController.activateKLMWithAdminEnabled(edtKLM.getText().toString());
				}
				// if BYOD is enabled
				else if (adminLicensePrefs.getBoolean(SAConstants.BYOD, false)) {
					// activate KLM
					mController.activateKLM(edtKLM.getText().toString());
				}

			}
			// if KLM key is empty
			else {
				SAUIHelper.showAlert(AdminLicenseActivationActivity.this, SAConstants.LICENSE_KEY,
						SAConstants.ENTER_KLM_KEY, SAConstants.OK);
			}
			break;

		// Activate ELM button clicked
		case R.id.admin_license_activation_btn_activate_elm:

			// if ELM key is not empty
			if (!edtELM.getText().toString().equalsIgnoreCase("")) {
				// if admin is enabled
				if (adminLicensePrefs.getBoolean(SAConstants.ADMIN, false)) {
					// activate ELM with admin enabled API
					mController.activateELMWithAdminEnabled(edtELM.getText().toString());
				}
				// if BYOD is enabled
				else if (adminLicensePrefs.getBoolean(SAConstants.BYOD, false)) {
					// activate ELM
					mController.activateELM(edtELM.getText().toString());
				}
			}
			// if ELM key is empty
			else {
				SAUIHelper.showAlert(AdminLicenseActivationActivity.this, SAConstants.LICENSE_KEY,
						SAConstants.ENTER_ELM_KEY, SAConstants.OK);
			}
			break;

		default:
			break;

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.i(AdminLicenseActivationActivity.this.getLocalClassName(), "in onCreateOptionsMenu()");
		getMenuInflater().inflate(R.menu.about_app, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i(AdminLicenseActivationActivity.this.getLocalClassName(), "in onOptionsItemSelected()");
		switch (item.getItemId()) {

		case R.id.action_about_app:
			Intent aboutAppIntent = new Intent(this, AboutActivity.class);
			startActivity(aboutAppIntent);

			break;

		default:
			return super.onOptionsItemSelected(item);
		}

		return true;
	}

}

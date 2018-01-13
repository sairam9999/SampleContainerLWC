package com.example.svankayalapati.samplecontainerlwc.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.svankayalapati.samplecontainerlwc.R;
import com.example.svankayalapati.samplecontainerlwc.beans.ListItem;
import com.example.svankayalapati.samplecontainerlwc.constants.SAConstants;
import com.example.svankayalapati.samplecontainerlwc.controller.Controller;
import com.example.svankayalapati.samplecontainerlwc.enums.Enums.APIType;
import com.example.svankayalapati.samplecontainerlwc.enums.Enums.KnoxSDKVersion;
import com.example.svankayalapati.samplecontainerlwc.enums.Enums.SAFESDKVersion;
import com.example.svankayalapati.samplecontainerlwc.interfaces.APIListPopulator;
import com.example.svankayalapati.samplecontainerlwc.ui.listadapter.PolicyListAdapter;

import java.util.ArrayList;
import java.util.Map;


//This Activity displays all APIs of KnoxContainerManager

public class ContainerCreationRemovalActivity extends Activity implements
		APIListPopulator {

	ListView mApiListView;
	ArrayList<ListItem> mApiList = null;
	PolicyListAdapter adapter;
	Controller controller;
	String TAG = "ContainerCreationRemovalActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("ContainerCreationRemovalActivity::onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.api_list);
		// create items of listview
		mApiList = populateAPIList();

		adapter = new PolicyListAdapter(ContainerCreationRemovalActivity.this,
				mApiList, 4, SAConstants.CONTAINER_CREATION_REMOVAL);
		mApiListView = (ListView) findViewById(R.id.apiList);
		mApiListView.setTextFilterEnabled(true);
		mApiListView.setAdapter(adapter);
		// pass current activity context to Controller class
		controller = new Controller(ContainerCreationRemovalActivity.this,
				SAConstants.CONTAINER_CREATION_REMOVAL);

		/*
		 * Setting the container type to be knox-b2b as default In case, the
		 * user does not select any container type and taps on 'Create
		 * Container' option, knox-b2b type container will be created. For
		 * changing the type, click the option "Select Container Type" and click
		 * on the desired type and then create container
		 */
		Controller.setSharedPrefData(SAConstants.TYPE, SAConstants.KNOX_B2B, SAConstants.STRING_DATATYPE);
	}

	@Override
	protected void onStart() {
		System.out.println("ContainerCreationRemovalActivity::onStart()");
		super.onStart();
		if (controller == null) {
			// pass current activity context to Controller class
			controller = new Controller(ContainerCreationRemovalActivity.this,
					SAConstants.CONTAINER_CREATION_REMOVAL);
		}
		// this method is called to transfer handling of all onItemClick events
		// in listview, to Controller class
		controller.handleItemClick(mApiListView);
	}

	@Override
	protected void onStop() {
		System.out.println("ContainerCreationRemovalActivity::onStop()");
		// TODO Auto-generated method stub
		super.onStop();
		// deletes the instance of the Model class which calls KNOX APIs
		// relevant to items in this listview
		Controller.freeInstance(SAConstants.CONTAINER_CREATION_REMOVAL);
		controller = null;
	}

	// This method creates items of the listview

	@Override
	public ArrayList<ListItem> populateAPIList() {

		System.out.println("ContainerCreationRemovalActivity::populateAPIList()");

		ArrayList<ListItem> arrAPIs = new ArrayList<ListItem>();
		int count = 0;
		SharedPreferences sharedpreferences = getSharedPreferences(
				SAConstants.MY_PREFS_NAME, Context.MODE_PRIVATE);

		/// TODO Remove this temporary code VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV
		Map<String,?> keys = sharedpreferences.getAll();

		for(Map.Entry<String,?> entry : keys.entrySet()){
			Log.i("Shared Preference Values", entry.getKey() + ": " +	entry.getValue().toString());
		}
		/// TODO Remove this and code above ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

		// if admin is enabled
		if (sharedpreferences.getBoolean(SAConstants.ADMIN, false)) {

			arrAPIs.add(new ListItem(count++,
					getString(R.string.select_container_type),
					APIType.DISPLAY_LIST_POPUP, KnoxSDKVersion.VER_2_0, "", "",
					"", null, null, null));
			
			arrAPIs.add(new ListItem(count++,
					getString(R.string.api_clone_lwc_type),
					APIType.DISPLAY_ACTIVITY, KnoxSDKVersion.VER_2_1,
					ContainerCreationRemovalActivity.this,
					LightWeightConfigTypeActivity.class));

			arrAPIs.add(new ListItem(count++,
					getString(R.string.api_create_container),
					APIType.OPERATION, KnoxSDKVersion.VER_2_0));

			arrAPIs.add(new ListItem(count++,
					getString(R.string.api_create_container_sdp),
					APIType.OPERATION, KnoxSDKVersion.VER_2_2));

			arrAPIs.add(new ListItem(count++,
					getString(R.string.api_get_containers), APIType.GETTER,
					SAFESDKVersion.VER_2_0));

			arrAPIs.add(new ListItem(count++,
					getString(R.string.api_remove_container),
					APIType.DISPLAY_NUMERIC_EDITTEXT_POPUP,
					KnoxSDKVersion.VER_2_0,
					getString(R.string.api_remove_container),
					getString(R.string.enter_container_id), "", null, null,
					null));

		}

		// if BYOD is enabled
		else if (sharedpreferences.getBoolean(SAConstants.BYOD, false)) {

			arrAPIs.add(new ListItem(count++,
					getString(R.string.select_container_type),
					APIType.DISPLAY_LIST_POPUP, KnoxSDKVersion.VER_2_0, "", "",
					"", null, null, null));
			
			arrAPIs.add(new ListItem(count++,
					getString(R.string.api_clone_lwc_type),
					APIType.DISPLAY_ACTIVITY, KnoxSDKVersion.VER_2_1,
					ContainerCreationRemovalActivity.this,
					LightWeightConfigTypeActivity.class));

			arrAPIs.add(new ListItem(count++,
					getString(R.string.api_create_container_admin_inside),
					APIType.DISPLAY_QUERY_POP_UP, KnoxSDKVersion.VER_2_0,
					getString(R.string.uninstall_admin_from_device_heading),
					getString(R.string.uninstall_admin_from_device), "", null,
					null, null));
			
			arrAPIs.add(new ListItem(count++,
					getString(R.string.api_create_container_sdp_admin_inside),
					APIType.DISPLAY_QUERY_POP_UP, KnoxSDKVersion.VER_2_2,
					getString(R.string.uninstall_admin_from_device_heading),
					getString(R.string.uninstall_admin_from_device), "", null,
					null, null));

			arrAPIs.add(new ListItem(count++,
					getString(R.string.api_get_containers), APIType.GETTER,
					SAFESDKVersion.VER_2_0));

			arrAPIs.add(new ListItem(count++,
					getString(R.string.api_remove_container),
					APIType.DISPLAY_NUMERIC_EDITTEXT_POPUP,
					KnoxSDKVersion.VER_2_0,
					getString(R.string.api_remove_container),
					getString(R.string.enter_container_id), "", null, null,
					null));

		}

		return arrAPIs;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		System.out.println("ContainerCreationRemovalActivity::onCreateOptionsMenu()");
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		System.out.println("ContainerCreationRemovalActivity::onOptionsItemSelected(" + item.getItemId() + ")");
		switch (item.getItemId()) {
		case R.id.action_disable_admin_byod:
			System.out.println("Selected item is = " + item.getTitle());
			SharedPreferences sharedpreferences = getSharedPreferences(
					SAConstants.MY_PREFS_NAME, Context.MODE_PRIVATE);
			Editor editor = sharedpreferences.edit();

			if (sharedpreferences.getBoolean(SAConstants.ADMIN, false)
					&& controller.deactivateAdmin()) {
				editor.putBoolean(SAConstants.ADMIN, false);
			} else if (sharedpreferences.getBoolean(SAConstants.BYOD, false)) {
				editor.putBoolean(SAConstants.BYOD, false);
			}
			editor.putBoolean(SAConstants.KLM, false);
			editor.putBoolean(SAConstants.ELM, false);
			editor.commit();
			Intent intent = new Intent(ContainerCreationRemovalActivity.this,
					AdminLicenseActivationActivity.class);
			startActivity(intent);
			finish();

			break;

		case R.id.action_about_app:
			System.out.println("Selected item is = " + item.getTitle());
			Intent aboutAppIntent = new Intent(this, AboutActivity.class);
			startActivity(aboutAppIntent);

			break;

		default:
			System.out.println("Selected item is = " + item.getTitle());
			return super.onOptionsItemSelected(item);
		}

		return true;
	}

}

package com.example.svankayalapati.samplecontainerlwc.model;

import android.content.Context;
import android.util.Log;

import com.example.svankayalapati.samplecontainerlwc.R;
import com.example.svankayalapati.samplecontainerlwc.beans.ListItem;
import com.example.svankayalapati.samplecontainerlwc.constants.SAConstants;
import com.example.svankayalapati.samplecontainerlwc.controller.Controller;
import com.example.svankayalapati.samplecontainerlwc.interfaces.ActivityLauncher;
import com.example.svankayalapati.samplecontainerlwc.utils.SACodeUtils;
import com.sec.enterprise.knox.container.CreationParams;
import com.sec.enterprise.knox.container.KnoxConfigurationType;
import com.sec.enterprise.knox.container.KnoxContainerManager;

import java.util.List;

// This class deals with KnoxContainerManager APIs

public class ContainerCreationRemovalModel extends BaseModel {

	String TAG = "ContainerCreationRemovalModel";
	static Context mContext;

	static ContainerCreationRemovalModel containerCreationRemovalModelObj;

	private ContainerCreationRemovalModel() {
		// Get the current activity mContext
		mContext = Controller.getCurrentContext();

	}

	// This method creates an instance of this class
	public static synchronized ContainerCreationRemovalModel getInstance() {
		System.out.println("ContainerCreationRemovalModel::getInstance()");
		if (containerCreationRemovalModelObj == null) {
			containerCreationRemovalModelObj = new ContainerCreationRemovalModel();
		}
		return containerCreationRemovalModelObj;
	}

	// This method deletes the existing instance of this class
	public static synchronized void freeInstance() {

		System.out.println("ContainerCreationRemovalModel.freeInstance()");

		if (containerCreationRemovalModelObj != null) {
			containerCreationRemovalModelObj = null;
		}
	}

	/*
	 * This method is an overridden method from class BaseModel. It handles
	 * calling of all such KNOX APIs, which enables/disables a policy by
	 * checking/un-checking a check box or which performs some operation, like,
	 * creating a container. Basically, all those APIs are handled by this
	 * method, which do not require a user input.
	 * 
	 * Input parameters:
	 * 
	 * listItemObj: It contains the item object (of the listview) which was
	 * clicked by the user.
	 * 
	 * enable: It specifies whether to set the policy to true or false
	 */

	@Override
	public void invokeKNOXAPI(ListItem listItemObj, boolean enable) {
		System.out.println("ContainerCreationRemovalModel::invokeKNOXAPI()");
		System.out.println("args = " + listItemObj.itemId + ", " + enable);

		// TODO Auto-generated method stub

		int listItemObjId = listItemObj.itemId;
		Log.i(TAG,
				"in ContainerCreationRemovalModel invokeKNOXAPI, listItemObjId "
						+ listItemObjId);
		// itemId is the row index of the item of the listview, which was
		// clicked
		switch (listItemObjId) {

		// Get configuration Types
		case 0:
			System.out.println("ContainerCreationRemovalModel::invokeKNOXAPI(case 0)");
			String[] knoxConfigTypeNameList = null;
			// Get the configuration types
			List<KnoxConfigurationType> knoxConfigTypeList = KnoxContainerManager
					.getConfigurationTypes();
			knoxConfigTypeNameList = new String[knoxConfigTypeList.size()];
			int i = 0;
			for (KnoxConfigurationType knoxConfigTypeObj : knoxConfigTypeList) {
				knoxConfigTypeNameList[i++] = knoxConfigTypeObj.getName();
				// TODO: Remove this and below .out.
				System.out.println("knoxConfigTypeNameList[" + i + "] = " + knoxConfigTypeObj.getName());
			}
			if (knoxConfigTypeNameList != null
					&& knoxConfigTypeNameList.length > 0) {
				// Show all configuration types in a list
				// TODO: Remove this and below .out.
				System.out.println("calling Controller.showAlertWithList()");
				Controller.showAlertWithList(knoxConfigTypeNameList,
						SAConstants.SELECT_CONTAINER_TYPE, SAConstants.TYPE);
			} else
				Controller.showToast(SAConstants.NO_ITEMS_TO_SHOW);

			break;

		// Create container
		case 2:
			System.out.println("ContainerCreationRemovalModel::invokeKNOXAPI(case 2)");
			try {
				// Get the container type selected by user
				String type = (String) Controller.getSharedPrefData(
						SAConstants.TYPE, SAConstants.STRING_DATATYPE);
				// Create the container of the user selected type. If the user
				// did not select any container type, knox-b2b is selected
				// as type by default
				int requestid = KnoxContainerManager.createContainer(type);

				// If an error is thrown
				if (requestid < 0) {
					// Get the message (to be displayed to user), by passing in
					// the error code
					String messageToUser = SACodeUtils.getMessage(requestid,
							mContext);
					// show toast message to user displaying the issue
					Controller.showToast(messageToUser);
				}
				// if container is created successfully
				else {
					Log.d(TAG, SAConstants.CONTAINER_CREATION_PROGRESS
							+ requestid);
					Controller
							.showToast(SAConstants.CONTAINER_CREATION_PROGRESS
									+ requestid);
				}
			} catch (SecurityException e) {
				Log.e(TAG, e.getMessage());
			}
			break;

		// Create SDP enabled container
		case 3:
			System.out.println("ContainerCreationRemovalModel::invokeKNOXAPI(case 3)");
			try {
				// Get the container type selected by user
				String type = (String) Controller.getSharedPrefData(
						SAConstants.TYPE, SAConstants.STRING_DATATYPE);
				// create a CreationParams object
				CreationParams params = new CreationParams();

				// Build creation params as per needs.
				params.setConfigurationName(type);
				// The key used by administrator in following API is mandatory
				// to enable SDP otherwise appropriate error code will be
				// returned.

				// Password reset token shall be at least 8 characters long, and
				// composed of a combination with at least one letter and one
				// non-letter.
				params.setPasswordResetToken(SAConstants.PWD_RESET_TOKEN);

				// create container by passing in the CreationParams object
				int requestid = KnoxContainerManager.createContainer(params);
				// If an error is thrown
				if (requestid < 0) {
					// Get the message (to be displayed to user), by passing in
					// the error code
					String messageToUser = SACodeUtils.getMessage(requestid,
							mContext);
					// show toast message to user displaying the issue
					Controller.showToast(messageToUser);
				}
				// if container is created successfully
				else {
					Log.d(TAG, SAConstants.CONTAINER_CREATION_PROGRESS
							+ requestid);
					Controller
							.showToast(SAConstants.CONTAINER_CREATION_PROGRESS
									+ requestid);
				}
			} catch (SecurityException e) {
				Log.e(TAG, e.getMessage());
			}
			break;

		// Get containers
		case 4:
			System.out.println("ContainerCreationRemovalModel::invokeKNOXAPI(case 4)");
			List<Integer> arrListContainers = KnoxContainerManager
					.getContainers();
			if (arrListContainers != null && arrListContainers.size() > 0) {
				// Format container ids by putting commas between two ids
				String csvContainers = arrListContainers.toString()
						.replace("[", "").replace("]", "").replace(", ", ",");

				Controller.showToast(listItemObj.name + " : " + csvContainers);
			} else {
				Controller.showToast(mContext
						.getString(R.string.no_container_exists));
			}
			break;

		}
	}

	/*
	 * This method is an overridden method from class BaseModel. It handles
	 * calling of all such KNOX APIs, which require a user input in an EditText
	 * field.
	 * 
	 * Input parameters:
	 * 
	 * userEnteredData: It captures the user input.
	 * 
	 * listItemObj: It contains the item object (of the listview) which was
	 * clicked by the user.
	 * 
	 * activityLauncher: It is a reference variable of the interface
	 * 'ActivityLauncher'. It will be used to launch an activity after calling a
	 * KNOX API.
	 */
	@Override
	public void invokeKNOXAPI(ListItem listItemObj, String userEnteredData,
			ActivityLauncher activityLauncher) {

		System.out.println("ContainerCreationRemovalModel::invokeKNOXAPI_2()");
		if (listItemObj != null) { System.out.println("listItemObj = " + listItemObj.toString()); } else { System.out.println("listItemObj is null"); }
		if (userEnteredData != null) {	System.out.println("userEnteredData = " + userEnteredData); } else { System.out.println("userEnteredData is null"); }
		if (activityLauncher != null) { System.out.println("activityLauncher = " + activityLauncher.toString()); } else { System.out.print("activityLauncher is null"); }
		List<Integer> arrListContainers = null;
		boolean containerIdFound = false;
		int listItemObjId = listItemObj.itemId;
		String type = null;
		switch (listItemObjId) {

		// Create BYOD container with admin inside container
		case 2:

			System.out.println("ContainerCreationRemovalModel::invokeKNOXAPI_2(case 2)");
			try {

				int requestid = -1;

				// Get the container type selected by user
				type = (String) Controller.getSharedPrefData(SAConstants.TYPE,
						SAConstants.STRING_DATATYPE);

				// check if user wants to uninstall self from device and
				// save
				// the user
				// response in a shared preference file
				if (userEnteredData.equalsIgnoreCase(mContext
						.getString(R.string.yes))) {
					Controller.setSharedPrefData(SAConstants.DO_SELF_UNINSTALL,
							SAConstants.YES, SAConstants.STRING_DATATYPE);
				} else {
					Controller.setSharedPrefData(SAConstants.DO_SELF_UNINSTALL,
							SAConstants.NO, SAConstants.STRING_DATATYPE);
				}

				System.out.println("createContainer(type, packageName");

				requestid = KnoxContainerManager.createContainer(type,
						mContext.getPackageName());

				// If an error is thrown
				if (requestid < 0) {
					// Get the message (to be displayed to user), by passing
					// in
					// the error code
					String messageToUser = SACodeUtils.getMessage(requestid,
							mContext);
					// show toast message to user displaying the issue
					Controller.showToast(messageToUser);
				}
				// if container is created successfully
				else {
					Log.d(TAG, SAConstants.CONTAINER_CREATION_PROGRESS
							+ requestid);
					Controller
							.showToast(SAConstants.CONTAINER_CREATION_PROGRESS
									+ requestid);
				}
			} catch (SecurityException e) {
				Log.e(TAG, e.getMessage());
			}
			break;

		case 3:
			System.out.println("ContainerCreationRemovalModel::invokeKNOXAPI_2(case 3)");
			try {

				// Get the container type selected by user
				type = (String) Controller.getSharedPrefData(SAConstants.TYPE,
						SAConstants.STRING_DATATYPE);

				// check if user wants to uninstall self from device and
				// save
				// the user
				// response in a shared preference file
				if (userEnteredData.equalsIgnoreCase(mContext
						.getString(R.string.yes))) {
					Controller.setSharedPrefData(SAConstants.DO_SELF_UNINSTALL,
							SAConstants.YES, SAConstants.STRING_DATATYPE);
				} else {
					Controller.setSharedPrefData(SAConstants.DO_SELF_UNINSTALL,
							SAConstants.NO, SAConstants.STRING_DATATYPE);
				}

				// create a CreationParams object
				CreationParams params = new CreationParams();

				// Build creation params as per needs.
				params.setConfigurationName(type);
				// The key used by administrator in following API is mandatory
				// to enable SDP otherwise appropriate error code will be
				// returned.

				// Password reset token shall be at least 8 characters long, and
				// composed of a combination with at least one letter and one
				// non-letter.
				params.setPasswordResetToken(SAConstants.PWD_RESET_TOKEN);
				params.setAdminPackageName(mContext.getPackageName());

				// create container by passing in the CreationParams object
				int requestid = KnoxContainerManager.createContainer(params);
				// If an error is thrown
				if (requestid < 0) {
					// Get the message (to be displayed to user), by passing in
					// the error code
					String messageToUser = SACodeUtils.getMessage(requestid,
							mContext);
					// show toast message to user displaying the issue
					Controller.showToast(messageToUser);
				}
				// if container is created successfully
				else {
					Log.d(TAG, SAConstants.CONTAINER_CREATION_PROGRESS
							+ requestid);
					Controller
							.showToast(SAConstants.CONTAINER_CREATION_PROGRESS
									+ requestid);
				}
			} catch (SecurityException e) {
				Log.e(TAG, e.getMessage());
			}
			break;

		// Remove container

		case 5:
			System.out.println("ContainerCreationRemovalModel::invokeKNOXAPI_2(case 5)");
			// Get all the containers owned by this admin
			arrListContainers = KnoxContainerManager.getContainers();
			// Get the container id entered by user
			int userEnteredContainerId = Integer.parseInt(userEnteredData);

			for (int id : arrListContainers) {
				if (userEnteredContainerId == id) {
					containerIdFound = true;
					break;
				}
			}
			// if the container with container id entered by user is owned by
			// this admin
			if (containerIdFound) {
				System.out.println("Container ID was found");
				try {
					// Remove container
					System.out.println("Trying to remove container with call to KnoxContainerManager.removeContainer()");
					int errorCode = KnoxContainerManager
							.removeContainer(userEnteredContainerId);
					// if successful
					if (errorCode == KnoxContainerManager.REMOVE_CONTAINER_SUCCESS) {
						System.out.println("Successfully removed container.");
						// success
						String currContIdStr = (String) Controller
								.getSharedPrefData(
										SAConstants.CURRENT_CONTAINER_ID,
										SAConstants.STRING_DATATYPE);
						int currContIdInt = -1;
						// if current container id is the one, which is removed
						if (currContIdStr != null) {
							currContIdInt = Integer.parseInt(currContIdStr);
						}
						if (currContIdInt != -1
								&& userEnteredContainerId == currContIdInt) {
							// set current container id to null in shared
							// preference
							Controller.setSharedPrefData(
									SAConstants.CURRENT_CONTAINER_ID, null,
									SAConstants.STRING_DATATYPE);
						}
						Controller
								.showToast(SAConstants.CONTAINER_UNINSTALLATION_SUCCESSFUL);

					}
					// if not successful
					else {
						System.out.println("Failed to remove container.");
						// Get the message (to be displayed to user) by passing
						// in error code
						String messageToUser = SACodeUtils.getMessage(
								errorCode, mContext);
						Controller.showToast(messageToUser);
					}

				} catch (SecurityException e) {
					Log.e(TAG, e.getMessage());
				}

			}
			// if the container id entered by user does not exist or not owned
			// by this admin
			else {
				Controller.showToast(SAConstants.CONTAINER_DOES_NOT_EXIST);
			}
			containerIdFound = false;
			break;
		}

	}

}

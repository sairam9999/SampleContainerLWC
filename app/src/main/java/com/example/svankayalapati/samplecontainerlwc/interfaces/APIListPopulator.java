package com.example.svankayalapati.samplecontainerlwc.interfaces;

import com.example.svankayalapati.samplecontainerlwc.beans.ListItem;

import java.util.ArrayList;


//This interface is used by implementing classes to create all listview item objects and 
//display them in the listview

public interface APIListPopulator {
	public ArrayList<ListItem> populateAPIList();

}

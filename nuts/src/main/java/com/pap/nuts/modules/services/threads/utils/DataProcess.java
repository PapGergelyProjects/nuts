package com.pap.nuts.modules.services.threads.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for the process tracking.
 * 
 * @author Pap Gergely
 *
 */
public class DataProcess {
	
	private static List<String> taskFinishedList = new ArrayList<>();
	
	public static void addToList(String doneTask){
		taskFinishedList.add(doneTask);
	}
	
	public static List<String> getFinishedTaskList(){
		return taskFinishedList;
	}
	
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Decoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Enache
 */
public class Task {

    private static ArrayList<Task> openTasks;

    protected int task_code = -1;
    protected int task_id = -1;
    protected Map<String, String> parameters = null;
    protected Map<String, String> task_result = null;
    protected boolean finalized = false;

    private Task(int code, HashMap<String, String> param) {
        this.task_code = code;
        this.parameters = param;

    }

    public int getStatus() {
        return task_code;
    }

    public int getID() {
        return task_id;
    }

    public Map<String, String> getParams() {
        return this.parameters;
    }
    
    public Map<String,String> getResults(){
        return this.task_result;
    }

    public void setResults(Map<String, String> results) {
        task_result = results;
    }
    
    public void Finalize(){
        finalized=true;
    }

    public boolean isResolved() {
        return task_result != null;
    }

    public boolean isFinalized() {
        return finalized;
    }

    protected Task refresh(int code, int id, HashMap<String, String> param) {
        task_code = code;
        parameters = param;
        task_result = null;
        task_id = id;
        return this;
    }

    public static Task createTask(int code, int id, HashMap<String, String> param) {

        Task reusable_task = null;

        for (int i = 0; i < openTasks.size(); i++) {
            if (openTasks.get(i).finalized && reusable_task == null) {
                reusable_task = openTasks.get(i).refresh(code, id, param);
            } else if (openTasks.get(i).finalized && reusable_task != null && i!= openTasks.size()-1) {
                openTasks.remove(i);
            }
        }

        if (reusable_task == null) {
            reusable_task = new Task(code, param);
            openTasks.add(reusable_task);

        }

        return reusable_task;
    }
}

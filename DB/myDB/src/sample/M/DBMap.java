package sample.M;

import java.util.ArrayList;
import java.util.LinkedList;


public class DBMap {
    private LinkedList<String> key = new LinkedList<>();
    private LinkedList<ArrayList<Object>> value = new LinkedList<>();

    //put
    public void put(String keyToPut, ArrayList<Object> valueToPut) {
        key.add(keyToPut);
        value.add(valueToPut);
    }

    //get
    public ArrayList<Object> get(String keyToGet) {
        return value.get(key.indexOf(keyToGet));
    }

    //remove
    public void remove(int index) {
        key.remove(index);
        value.remove(index);
    }
    //size
    public int size(){
        return key.size();
    }

    //ketSet
    public String[] keySet(){
        String[] tmp = new String[key.size()];
        int i = 0 ;
        for (String str : key){
            tmp[i]=str;
            i++;
        }
        return  tmp;
    }

    //containsKey
    public boolean containsKey(String findKey){
        return key.contains(findKey);
    }

    //clear
    public void clear(){
        key.clear();
        value.clear();
    }


}

package M;


import java.util.LinkedList;

public  class JsonObject extends Json {

    private LinkedList<Node> jsonObj ;
    private int counter = 0;
    JsonObject(){
        jsonObj = new LinkedList<>();
    }
    public void put(String str, Object obj) {
        Node node = new Node();
        node.first="\""+str+"\":";
        node.second="\""+obj+"\"";
        jsonObj.add(counter,node);
        counter++;
    }

    public LinkedList<Node> getList(){

        return jsonObj;
    }

}

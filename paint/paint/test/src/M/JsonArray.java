package M;

import java.util.LinkedList;

public  class JsonArray extends Json{

    private LinkedList<Node> strArr;
    private int counter = 1;
    JsonArray(){
        strArr = new LinkedList<>();
        Node first = new Node();
        Node last = new Node();
        first.first="[".toString();
        last.first="]".toString();
        strArr.add(first);
        strArr.add(last);
    }

    @Override
    public void add(Object obj ,String str) {
        Node node = new Node();
        node.first=obj.toString();
        node.second=str;
        strArr.add(counter,node);
        counter++;
    }
}

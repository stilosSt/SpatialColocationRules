package colocmining_ens;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;


public class Node{

    private Integer id;
    private double x,y;    
    private HashMap<Node,Double> conTo;
    private double distance;
    private boolean visited;
    private HashSet<Edge> adjEdges;
    
    public Node(int id,double x,double y){
        
        this.id=id;
        this.x=x;
        this.y=y;
        this.conTo=new HashMap<>();
        adjEdges=new HashSet<>();
    }

    
    public void setConnections(Node node,double weight){
        
        conTo.put(node,weight);
        
    }    
    
    public void setEdges(Edge e){
        
        adjEdges.add(e);
        
    }
    
    
    public void init(){
        
        distance=999999;
        visited=false;
              
    }
    
    
    public void setDistance(double dis){
        
        distance=dis;
               
    }
    
    
    public double getDistance(){
        
        return distance;
        
    }
        
    
    public void setVisited(){
        
        visited=true;
        
    }
    
    public boolean getVisited(){
        
        return visited;
        
    }
    
    public int getID(){
        
        return id;
        
    }
    
    public double getX(){
        
        return x;
        
    }
    
    public double getY(){
        
        return y;
        
    }
        
    
    public HashMap<Node,Double> getConnections(){
        
        return conTo;
    }
    
    public HashSet<Edge> getEdges(){
        
        return adjEdges;
    }
    

//    @Override
//    public int hashCode(){
//        
//        return id.hashCode();
//        
//        
//    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.id);
        return hash;
    }
    
    
    

    @Override
    public boolean equals(Object other) {
        
        Node otherNode=(Node)other;
        return otherNode.getID()==this.getID();
    }

    @Override
    public String toString(){
        
        return "id:"+this.id+" X:"+this.x+" Y:"+this.y;
    }
        
    
}






package colocmining_ens;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

public class DBManager {
    
    //connection parameters
    private final String url="jdbc:postgresql://localhost:5432/MonacoDB";
    private final String user="postgres";
    private final String password="%st11078";
      
    private Connection c = null;
    private Statement stmt = null;
    private ResultSet rs = null;
        
    //getNodes() structures and parameters
    private Node node;
    private HashSet<Node> nodesSet;
                
    //getEdges structures and parameters
    private Edge edge;
    private HashSet<Edge> edgesSet;
    private Node startNode;
    private Node endNode;
   
    
    
    //getPOIs() structures and parameters
    
    private NetworkObject object;
    private ArrayList<NetworkObject> objectsList;
    private TreeMap<String,ArrayList<NetworkObject>> objectsMap;
    private TreeMap<String,Integer> featuresSum;
    
    //method with which we get the connection-c from the database 
    public DBManager(){
          
        try {
            
            Class.forName("org.postgresql.Driver");
           
            c = DriverManager.getConnection(url,user,password);
            c.setAutoCommit(false);
            
            System.out.println("Connected to the PostgreSQL server successfully.");
 
        } catch (ClassNotFoundException | SQLException e) {
                
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
         
    }
    
    //method with which we get all the nodes from the database   
    public void conToNodes(){
        
        try{
                
            stmt = c.createStatement();
            String query="SELECT id,ST_X(node),ST_Y(node) FROM myschema.mynodes ORDER BY id";//ORDER BY gid ASC LIMIT 20;
            rs = stmt.executeQuery(query);
            
            int id;
            double st_x,st_y;
            int count=0;
            nodesSet=new HashSet<>();
            
            while(rs.next()){
                
                id=rs.getInt("id");
                st_x=rs.getDouble("st_x");
                st_y=rs.getDouble("st_y");
                
                //we create them as new object
                node=new Node(id,st_x,st_y);
                //and we add them to a HashSet 
                nodesSet.add(node);
                count++;
            }            
            
            System.out.print(count);            
            rs.close();
            stmt.close();
        }catch ( SQLException e ) {
                
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        
            System.out.println(" Nodes GENERATED...");
           
        
    }
    
    
    //method with which we get all the edges from the database
    public void conToEdges(){
       
        try{
                
            stmt = c.createStatement();
            String query="SELECT id,ST_X(st_p),ST_Y(st_p),ST_X(en_p) AS en_x,ST_Y(en_p) AS en_y,weight FROM myschema.edgepoints";//ORDER BY gid ASC LIMIT 20;
            rs = stmt.executeQuery(query);
                
            int edgeID;
            double st_x,st_y,en_x,en_y,weight;
            edgesSet=new HashSet<>();
            
            while (rs.next()){
                    
                edgeID=rs.getInt("id");
                st_x=rs.getDouble("st_x");
                st_y=rs.getDouble("st_y");
                en_x=rs.getDouble("en_x");
                en_y=rs.getDouble("en_y");
                weight=rs.getDouble("weight");    
                              
                int count=0;
                
                    
                for(Node n:nodesSet){
                    
                    if(st_x==n.getX() && st_y==n.getY()){
                        
                        startNode=n;
                        count++;
                        
                    }  
                    if(en_x==n.getX() && en_y==n.getY()){
                        
                        endNode=n;
                        count++;
                        
                    }
                    if(count==2){
                        
                        startNode.setConnections(endNode,weight);
                        endNode.setConnections(startNode,weight);
                        
                        break;
                        
                    }
                                                        
                }
                               
                //create new edge
                
                edge=new Edge(edgeID,startNode,endNode,weight);  
                if(edge==null){
                    
                    continue;
                }
                //add to the node HashSet(no duplicates) each edge at which it belogs
                startNode.setEdges(edge);
                endNode.setEdges(edge);
                
                //add edge at the edges HashSet
                edgesSet.add(edge);
                                                                            
            }   
                
            
            rs.close();
            stmt.close();
            
                
        }catch ( SQLException e ) {
                
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        
            System.out.println("Edges GENERATED...");
           

    }
    
    //method with which we get all the pois from the database
    public void conToPOIs(){
       
        try{
                
            stmt = c.createStatement();
            String query="SELECT id, type,toedge,pos FROM myschema.objects";//ORDER BY object ASC LIMIT 20
            rs = stmt.executeQuery(query);
                
            int objectID;
            String type;
            double pos;
            int toedge;
            String temp=null;
            int oid=0;
          
            objectsList=new ArrayList<>();
            objectsMap=new TreeMap<>();
            
            while (rs.next()){
                
                
                type=rs.getString("type");
                objectID=rs.getInt("id");
                toedge=rs.getInt("toedge");
                pos=rs.getDouble("pos");
                        
                oid++;
                //set the edge where each object is snapped to
                for(Edge e:edgesSet){
                    
                    if(toedge==e.getID()){
                        
                        object=new NetworkObject(type,oid,objectID,e,pos);
                        //System.out.println(object);
                        e.addSnappedObject(object);                        
                        break;
                        
                    }
                    
                }

                //we want a map with keys -types of edges,so we change key if we have read all the objects of each type
                //we use temp for this purpose
                if(!type.equals(temp)){
                    if(temp!=null){
                        
                        objectsMap.put(temp,objectsList);
                        //System.out.println(objectsMap);
                    }
                    
                    objectsList=new ArrayList<>();  
                    objectsList.add(object);
                
                }
                else{
                    
                    objectsList.add(object);
                    
                }
                
                
                
                temp=type;
                    
            }
            
            //insert the last element of the map
            objectsMap.put(temp,objectsList);
            
            featuresSum=new TreeMap<>();
            
            for(String feature:objectsMap.keySet()){                
                
                featuresSum.put(feature,objectsMap.get(feature).size());
                
            }

            rs.close();
            stmt.close();
            c.close();
                
        }catch ( SQLException e ) {
                
                System.err.println( e.getClass().getName()+": "+ e.getMessage() );
                System.exit(0);
        }
           
        
        System.out.println("POIs  GENERATED...");
           

    }
    
    //Class getters
    
    public HashSet<Node> getNodes(){
        
         return nodesSet;
         
    }
    
    public HashSet<Edge> getEdges(){
        
        return edgesSet;
        
    }
    
    public TreeMap<String,Integer> getFeaturesSum(){
        
        return featuresSum;
        
    }    
    
    public TreeMap<String,ArrayList<NetworkObject>> getPOIsMap(){
        
        return objectsMap;
        
    }
    
    
}



import java.util.HashMap;
import java.util.HashSet;

public class Main {
    public static Integer id = 34838386;

    public static void main(String[] args) {
        HashSet<Integer> friendsIds = vkAPI.getAllFriendsIds(id);
        HashMap<Integer, User> friends = new HashMap();
        friendsIds.add(id);
        for (Integer i :friendsIds) {
            User newFriend = vkAPI.getUserInfo(i);
            friends.put(i, newFriend);
            newFriend.print();
        }
        Graph graph = new Graph(friends);
        System.out.println("Total users: "+graph.connections.size());

        try ( neo4jAPI grapi = new neo4jAPI("bolt://localhost:7687", "neo4j", "Neo4j" ,graph) )
        {
            grapi.cleanDB();
            grapi.add(graph);
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }
}

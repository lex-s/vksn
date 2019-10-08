import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Graph {
    HashMap<User, ArrayList<User>> connections = new HashMap<>();
    HashMap<Integer, User> friends;

    public Graph(HashMap<Integer, User> friends) {
        this.friends = friends;
        getConnections();
    }

    private void getConnections(){
        Set<Integer> friendsIds = friends.keySet();
        for (Integer i: friendsIds) {
            HashSet<Integer> ids = vkAPI.getAllFriendsIds(i);
            ArrayList<User> friendlist = new ArrayList<>();
            for (Integer j: ids) {
                if (friendsIds.contains(j)&&i<j) {
                    friendlist.add(friends.get(j));
                }
            }
            connections.put(friends.get(i),friendlist);
        }
    }

    public int getWeight (User usr1, User usr2) {
        int res = 0;
        if (usr1.getSex().equals(usr2.getSex()) && !usr1.getSex().equals("Not specified"))
            res++;
        if (usr1.getJobs().equals(usr2.getJobs()) && !usr1.getJobs().equals("Not specified"))
            res++;
        HashSet<String> set1 = usr1.getSchools();
        HashSet<String> set2 = usr2.getSchools();
        for (String s1:set1) {
            if (set2.contains(s1)) {
                res++;
                break;
            }
        }
        set1 = usr1.getUniversities();
        set2 = usr2.getUniversities();
        for (String s1:set1) {
            if (set2.contains(s1)) {
                res++;
                break;
            }
        }
        return res;
    }
}

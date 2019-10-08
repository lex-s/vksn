import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;

import java.util.Collection;
import java.util.HashSet;

import static org.neo4j.driver.v1.Values.parameters;

public class neo4jAPI implements AutoCloseable{
    private final Driver driver;
    Graph graph;

    @Override
    public void close() throws Exception {
        driver.close();
    }

    public neo4jAPI( String uri, String user, String password, Graph graph ) {
        this.graph = graph;
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }

    private void addUser(User user) {
        try (Session session = driver.session())
        {
            try (Transaction tx = session.beginTransaction())
            {
                    tx.run("MERGE (a:User {fullname: {x}, id: {z}})", parameters("x", user.getFirstName()+" "+user.getSecondName(), "z", user.getId()));
                tx.success();
            }
        }
    }

    private void addRelation(User user1, User user2){
        try (Session session = driver.session())
        {
            try (Transaction tx = session.beginTransaction())
            {
                String str = "_"+graph.getWeight(user1,user2)+"_";
                tx.run("MATCH (u:User {fullname: {x}, id: {z}}), (r:User {fullname: {x1}, id: {z1}})\n" +
                        "CREATE (u)-[:"+str+"]->(r)\n", parameters("x", user1.getFirstName()+" "+user1.getSecondName(), "z", user1.getId(), "x1", user2.getFirstName()+" "+user2.getSecondName(),"z1", user2.getId()));
                tx.success();
            }
        }
    }

    public void cleanDB (){
        try (Session session = driver.session())
        {
            try (Transaction tx = session.beginTransaction())
            {
                tx.run("MATCH (n)\n" +
                        "OPTIONAL MATCH (n)-[r]-()\n" +
                        "DELETE n,r");
                tx.success();
            }
        }
    }
    
    public void add(Graph graph) {
        for (User user: graph.friends.values()) {
            addUser(user);
        }
        Collection<User> set = graph.friends.values();
        for (User user:set) {
            for (User user2: graph.connections.get(user)) {
                addRelation(user,user2);
            }
        }
    }
}

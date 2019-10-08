import java.util.ArrayList;
import java.util.HashSet;

public class User {
    private String firstName;
    private String secondName;
    private Integer id;
    private String sex;
    private String job;
    private HashSet<String> schools;
    private HashSet<String> universities;

    public User (String firstName, String secondName, Integer id, String sex, String job, HashSet<String> universities, HashSet<String> schools) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.id = id;
        this.sex = sex;
        this.job = job;
        this.schools = schools;
        this.universities = universities;
    }

    public String getFirstName ()
    {
        return this.firstName;
    }

    public String getSecondName ()
    {
        return this.secondName;
    }

    public Integer getId ()
    {
        return this.id;
    }

    public String getSex ()
    {
        return this.sex;
    }

    public HashSet<String> getUniversities ()
    {
        return this.universities;
    }

    public String getJobs ()
    {
        return this.job;
    }

    public HashSet<String> getSchools ()
    {
        return this.schools;
    }

    public void print()
    {
        System.out.println(this.id+": "+this.firstName+" "+this.secondName+" "+this.sex);
    }

}

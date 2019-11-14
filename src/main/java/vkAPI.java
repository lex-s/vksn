import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

public class vkAPI {
    static String access_token = "";

    public static HashSet<Integer> getAllFriendsIds(Integer id) {
        try {
            TimeUnit.MILLISECONDS.sleep(250);
        }
        catch (Exception ex) {
            System.out.println(ex.toString());
        }
        String request = "https://api.vk.com/method/friends.get?user_id=" + id + "&access_token=" + access_token + "&order=mobile&fields=online&namecase=nom&v=5.68";
        HashSet<Integer> ans = new HashSet<>();
        String res, userId;
        int counter = 0;

        try {
            res = GetHtmlByURL(request);
            while (true) {
                Integer k = res.indexOf("id\":");
                if (k == -1)
                    break;
                res = res.substring(k + 4);
                userId = res.substring(0, res.indexOf(","));
                ans.add(Integer.parseInt(userId));
            }
        }

        catch (Exception ex)
        {
            System.out.println(ex.toString());
        }
        return ans;
    }

    public static User getUserInfo(Integer id) {
        try {
            TimeUnit.MILLISECONDS.sleep(300);
        }
        catch (Exception ex) {
            System.out.println(ex.toString());
        }

        String firstName = "Not specified";
        String secondName = "Not specified";
        String sex = "Not specified";
        String job = "Not specified";
        HashSet<String> schools = new HashSet<>();
        HashSet<String> universities = new HashSet<>();

        String request = "https://api.vk.com/method/users.get?user_ids=" + id + "&access_token=" + access_token + "&fields=sex,education,universities,schools,occupation&v=5.68";
        String input = new String();
        try {
            input = GetHtmlByURL(request);
        }
        catch (Exception ex) {
            System.out.println(ex.toString());
        }

        JsonParser parser = new JsonParser();
        JsonObject mainObject = parser.parse(input).getAsJsonObject();
        JsonArray pItem = mainObject.getAsJsonArray("response");

        try {
            for (JsonElement user : pItem) {

                JsonObject userObject = user.getAsJsonObject();
                if (userObject.get("last_name") != null)
                {
                    secondName = userObject.get("last_name").toString();
                    secondName = parseField(secondName, "last_name");
                }
                if (userObject.get("first_name") != null)
                {
                    firstName = userObject.get("first_name").toString();
                    firstName = parseField(firstName, "first_name");
                }
                if (userObject.get("sex") != null)
                {
                    sex = userObject.get("sex").toString();
                    sex = parseField(sex, "sex");
                }
                if (userObject.get("universities") != null)
                {
                    JsonArray uItem = userObject.getAsJsonArray("universities");
                    for (JsonElement uni : uItem) {
                        JsonObject uniObject = uni.getAsJsonObject();
                        if (uniObject.get("name") != null)
                        {
                            String uniname = uniObject.get("name").toString();
                            uniname = uniname.replace("\"","");
                            universities.add(uniname);
                        }
                    }
                }
                else if (userObject.get("occupation") != null) {
                    JsonObject uniObject = userObject.getAsJsonObject("occupation");
                    if (uniObject.get("type").toString().replace("\"","").equals("university")) {
                        String uniname = uniObject.get("name").toString();
                        uniname = uniname.replace("\"","");
                        universities.add(uniname);
                    }
                }
                if (userObject.get("schools") != null)
                {
                    JsonArray sItem = userObject.getAsJsonArray("schools");
                    for (JsonElement sch : sItem) {
                        JsonObject schObject = sch.getAsJsonObject();
                        if (schObject.get("name") != null)
                        {
                            String schname2 = schObject.get("city").toString();
                            String schname = schObject.get("name").toString();
                            schname = schname.replace("\"","");
                            schools.add(schname2+schname);
                        }
                    }
                }
                if (userObject.get("occupation") != null)
                {
                    JsonObject jobObject = userObject.getAsJsonObject("occupation");
                    if (jobObject.get("type").toString().replace("\"","").equals("work")) {
                        job = jobObject.get("name").toString().replace("\"","");
                    }
                }
            }
        }
        catch (Exception ex) {
            System.out.println(ex.toString());
        }

        return new User(firstName, secondName, id, sex, job, universities, schools);
    }

    public static String GetHtmlByURL(String address) throws Exception {
        String out = new String();
        URL url = new URL(address);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            out += inputLine;
        in.close();
        return out;
    }

    public static String parseField(String s, String type) {
        switch (type){
            case "bdate": {
                String buf = s.substring(s.lastIndexOf(".") + 1);
                return buf.substring(0, buf.length() - 1);
            }
            case "first_name":
            case "last_name":
                return s.substring(1, s.length() - 1);
            case "city": {
                String buf = s.substring(s.indexOf("title"));
                buf = buf.substring(buf.indexOf(":") + 2);
                buf = buf.substring(0, buf.indexOf("\""));
                return buf;
            }
            case "sex":{
                int i =Integer.parseInt(s);
                switch (i){
                    case 1:
                        return "Female";
                    case 2:
                        return  "Male";
                    default:
                        return "Not specified";
                }
            }
        }
        return "";
    }
}

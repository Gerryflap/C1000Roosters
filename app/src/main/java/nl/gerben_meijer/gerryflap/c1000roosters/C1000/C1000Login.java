package nl.gerben_meijer.gerryflap.c1000roosters.C1000;

import android.content.SharedPreferences;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Gerryflap on 2015-05-30.
 */
public class C1000Login {
    Map<String, String> cookies;
    private String accountId;
    private String session;
    private boolean loggedIn = false;


    public C1000Login(String session){
        cookies = new HashMap<>();
        cookies.put("pmt_real_session", session);
    }

    public C1000Login(){

    }

    public void login(String username, String password) {
        Connection.Response response = getSite(true);
        if(response != null) {
            String out = postData(getToken(response), username, password);
            System.out.println(cookies);
            session = cookies.get("pmt_real_session");
        }
    }

    public Connection.Response getSite(boolean setCookies){
        try {
            Connection.Response res = Jsoup.connect("https://www.c1000net.nl/steenwijk")
                    .timeout(0)
                    .method(Connection.Method.GET)
                    .execute();
            if(setCookies){
                cookies = res.cookies();
            }
            return res;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public String getToken(Connection.Response response){
        try {
            Document doc = response.parse();
            String token = doc.getElementById("login__csrf_token").val();
            return token;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String postData(String token, String email, String password){
        try {
            Connection.Response response = Jsoup.connect("https://www.c1000net.nl/steenwijk/login").data("login[_csrf_token]", token,
                    "login[username]", email,
                    "login[password]", password)
                    .cookies(cookies)
                    .method(Connection.Method.POST)
                    .execute();
            Document doc = response.parse();
            cookies = response.cookies();
            loggedIn = doc.getElementsByTag("form").size() == 0;

            return doc.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Werkdag> getWeek(){
        ArrayList<Werkdag> werkdagen = new ArrayList<Werkdag>();

        try {
            Document document = Jsoup.connect("https://www.c1000net.nl/steenwijk/employeeweekschedule").cookies(cookies).get();
            if(accountId == null){
                Pattern pattern = Pattern.compile("\"account_id\": ([0-9]*)");
                Matcher m = pattern.matcher(document.toString());
                if(m.find()) {
                    accountId = m.group(1);
                    System.out.println(accountId);
                }
            }
            Elements table = document.getElementsByTag("table");
            Elements column;
            for (Element tbody: table.select("tbody")){
                column = tbody.select("td");
                try {
                    if (!column.get(0).html().equals("-") && !tbody.select("th").get(0).html().equals("Definitief")) {
                        werkdagen.add(new Werkdag(tbody.select("th").get(0).html(), tbody.select("th").get(1).html(), column.get(0).html(), column.get(1).html(), column.get(2).html()));
                    }
                } catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }

            }
            return werkdagen;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Werkdag> getWeek(int weeknum, int year){
        ArrayList<Werkdag> werkdagen = new ArrayList<Werkdag>();

        try {
            Document document = Jsoup.connect("https://www.c1000net.nl/steenwijk/employeeweekschedule/" + accountId + "/" + weeknum + "/" +year).cookies(cookies).get();
            Elements table = document.getElementsByTag("table");
            Elements column;
            for (Element tbody: table.select("tbody")){
                column = tbody.select("td");
                try {
                    if (!column.get(0).html().equals("-") && !tbody.select("th").get(0).html().equals("Definitief")) {
                        werkdagen.add(new Werkdag(tbody.select("th").get(0).html(), tbody.select("th").get(1).html(), column.get(0).html(), column.get(1).html(), column.get(2).html()));
                    }
                } catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }

            }

            //TODO: Remove test elements
            werkdagen.add(new Werkdag("Gerbendag", "(31-feb)", "13:00", "13:01", "00:45"));
            return werkdagen;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public String getSession() {
        return session;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
}

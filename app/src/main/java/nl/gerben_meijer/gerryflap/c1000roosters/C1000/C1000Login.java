package nl.gerben_meijer.gerryflap.c1000roosters.C1000;

import android.content.SharedPreferences;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
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
    public static final int STATUS_LOGGED_OUT = 0;
    public static final int STATUS_LOGGIN_IN = 1;
    public static final int STATUS_LOGGED_IN_NO_ID = 2;
    public static final int STATUS_LOGGED_IN_WITH_ID = 3;
    public static final int STATUS_LOADING_SCHEDULE = 4;
    public static final int STATUS_SHEDULE_LOADED = 5;
    public static final int STATUS_LOADING_FAILED = 6;

    public static final Map<Integer, String> statusStrings;
    static {
        Map<Integer, String> aMap = new HashMap<>();
        aMap.put(STATUS_LOGGED_OUT, "Not logged in");
        aMap.put(STATUS_LOGGIN_IN, "Logging in");
        aMap.put(STATUS_LOGGED_IN_NO_ID, "Logged in, waiting for account id");
        aMap.put(STATUS_LOGGED_IN_WITH_ID, "Logged in");
        aMap.put(STATUS_LOADING_SCHEDULE, "Loading schedule");
        aMap.put(STATUS_SHEDULE_LOADED, "Logged in, Schedule loaded");
        aMap.put(STATUS_LOADING_FAILED, "Loading schedule failed!");
        statusStrings = Collections.unmodifiableMap(aMap);
    }


    Map<String, String> cookies;
    private String accountId;
    private String session;
    private List<Werkdag> werkdagList = new ArrayList<>();
    private int status = STATUS_LOGGED_OUT;
    private boolean loggedIn = false;


    public C1000Login(String session){
        cookies = new HashMap<>();
        cookies.put("pmt_real_session", session);
        this.session = session;
        loggedIn = true;
        status = STATUS_LOGGED_IN_NO_ID;
    }

    public C1000Login(){

    }

    public void login(String username, String password) {
        status = STATUS_LOGGIN_IN;
        Connection.Response response = getSite(true);
        if(response != null) {
            String out = postData(getToken(response), username, password);
            System.out.println(cookies);
            session = cookies.get("pmt_real_session");
            status = STATUS_LOGGED_IN_NO_ID;
            return;
        }
        status = STATUS_LOGGED_OUT;
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
        status = STATUS_LOADING_SCHEDULE;
        ArrayList<Werkdag> werkdagen = new ArrayList<Werkdag>();
        if(loggedIn) {

            try {
                Document document = Jsoup.connect("https://www.c1000net.nl/steenwijk/employeeweekschedule").cookies(cookies).get();
                if (accountId == null) {
                    Pattern pattern = Pattern.compile("\"account_id\": ([0-9]*)");
                    Matcher m = pattern.matcher(document.toString());
                    if (m.find()) {
                        accountId = m.group(1);
                        System.out.println(accountId);
                    }
                }
                Elements table = document.getElementsByTag("table");
                Elements column;
                for (Element tbody : table.select("tbody")) {
                    column = tbody.select("td");
                    try {
                        if (!column.get(0).html().equals("-") && !tbody.select("th").get(0).html().equals("Definitief")) {
                            werkdagen.add(new Werkdag(tbody.select("th").get(0).html(), tbody.select("th").get(1).html(), column.get(0).html(), column.get(1).html(), column.get(2).html()));
                        }
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }

                }
                status = STATUS_SHEDULE_LOADED;
                return werkdagen;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        status = STATUS_LOADING_FAILED;
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
            status = STATUS_SHEDULE_LOADED;
            return werkdagen;
        } catch (IOException e) {
            status = STATUS_LOADING_FAILED;
            e.printStackTrace();
        }
        return null;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
        if(status == STATUS_LOGGED_IN_NO_ID){
            status = STATUS_LOGGED_IN_WITH_ID;
        }
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

    public int getStatus(){
        return status;
    }

    public void setWerkdagList(List<Werkdag> werkdagList){
        //Due to some referencing it might be smart to keep the same object.
        this.werkdagList.clear();
        this.werkdagList.addAll(werkdagList);
        System.out.println("WDList: " + werkdagList);
    }

    public String getStatusString(){
        return statusStrings.get(status);
    }

    public List<Werkdag> getWerkdagList() {
        return werkdagList;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

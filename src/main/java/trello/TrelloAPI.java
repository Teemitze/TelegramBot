package trello;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;

public class TrelloAPI {

    private static final Logger logger = LoggerFactory.getLogger(TrelloAPI.class);

    private static final String TRELLO_API = "https://api.trello.com/1/";
    private static final String KEY_API = "07b44335e6da7a0f889bfd6fd3eba8a4";
    private static final String TOKEN = "c306f42077243f09bc71eb3bbaf4f96ee722548a370af8c9de9d17b253d8802d";
    private static final String ID_LIST = "5d3b0bdbdb63191350a43fdb";

    public String AddCard(String name, String desc) {
        HttpClient client = new HttpClient();

        URIBuilder builder = null;
        String result = null;
        try {
            builder = new URIBuilder(TRELLO_API + "cards");
            builder.addParameter("name", name);
            builder.addParameter("desc", desc);
            builder.addParameter("pos", "top");
            builder.addParameter("idList", ID_LIST);
            builder.addParameter("key", KEY_API);
            builder.addParameter("token", TOKEN);
        } catch (URISyntaxException e) {
            logger.error("Could not get the result using YouTube API");
            e.printStackTrace();
        }

        PostMethod method = new PostMethod(builder.toString());

        try {
            client.executeMethod(method);
            byte[] responseBody = method.getResponseBody();
            JSONObject jsonObject = new JSONObject(new String(responseBody));
            result = (String) jsonObject.get("id");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public String AddCheckList(String idCard, String checkListName) {
        HttpClient client = new HttpClient();

        URIBuilder builder = null;
        String idCheckList = null;
        try {
            builder = new URIBuilder(TRELLO_API + "checklists");
            builder.addParameter("idCard", idCard);
            builder.addParameter("name", checkListName);
            builder.addParameter("key", KEY_API);
            builder.addParameter("token", TOKEN);
        } catch (URISyntaxException e) {
            logger.error("Could not get the result using YouTube API");
            e.printStackTrace();
        }

        PostMethod method = new PostMethod(builder.toString());
        System.out.println(builder.toString());

        try {
            client.executeMethod(method);
            byte[] responseBody = method.getResponseBody();
            JSONObject jsonObject = new JSONObject(new String(responseBody));
            System.out.println(jsonObject);
            idCheckList = (String) jsonObject.get("id");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return idCheckList;
    }
    public void AddCheckBox(String idCheckList, String checkBoxName) {
        HttpClient client = new HttpClient();

        URIBuilder builder = null;
        try {
            builder = new URIBuilder(TRELLO_API + "checklists/" + idCheckList + "/checkItems");
            builder.addParameter("name", checkBoxName);
            builder.addParameter("pos", "bottom");
            builder.addParameter("checked", "false");
            builder.addParameter("key", KEY_API);
            builder.addParameter("token", TOKEN);
        } catch (URISyntaxException e) {
            logger.error("Could not get the result using YouTube API");
            e.printStackTrace();
        }

        PostMethod method = new PostMethod(builder.toString());
        System.out.println(builder.toString());

        try {
            client.executeMethod(method);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package API.trello;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parsers.Parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import static API.HttpBuilder.trelloBuilder;

public class TrelloAPI {

    public TrelloAPI() {
        try {
            Properties property = new Properties();
            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
            property.load(fis);
            KEY_API = property.getProperty("trello.key");
            TOKEN = property.getProperty("trello.token");
            ID_LIST = property.getProperty("trello.idList");
        } catch (Exception e) {
            logger.error("ОШИБКА: Файл свойств отсуствует!");
        }
    }


    private final Logger logger = LoggerFactory.getLogger(TrelloAPI.class);

    public static final String TRELLO_API = "https://api.trello.com/1/";
    static final String TRELLO_CHECKLISTS = "checklists";
    static String KEY_API = "07b44335e6da7a0f889bfd6fd3eba8a4";
    static String TOKEN = "c306f42077243f09bc71eb3bbaf4f96ee722548a370af8c9de9d17b253d8802d";
    static String ID_LIST = "5d3b0bdbdb63191350a43fdb";

    private HttpClient client = new HttpClient();
    private TrelloFillParameters trelloFillParameters = new TrelloFillParameters();

    private PostMethod postMethodAddCard;
    private PostMethod postMethodCheckList;

    private void addCard(String name, String desc) {
        try {
            HashMap<String, String> parameters = trelloFillParameters.fillParametersForAddCard(name, desc);
            String apiURL = trelloBuilder("cards", parameters);
            postMethodAddCard = new PostMethod(apiURL);
            client.executeMethod(postMethodAddCard);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("addCard IOException!");
        }
    }

    private void addCheckList(String idCard) {
        try {
            String apiURL = trelloBuilder(TRELLO_CHECKLISTS, trelloFillParameters.fillParametersForAddCheckList(idCard));
            postMethodCheckList = new PostMethod(apiURL);
            client.executeMethod(postMethodCheckList);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("addCheckList IOException!");
        }
    }

    private void addCheckBox(String idCheckList, String checkBoxName) {
        try {
            HashMap<String, String> parameters = trelloFillParameters.fillParametersForAddCheckBox(checkBoxName);
            String apiURL = trelloBuilder(TRELLO_CHECKLISTS + "/" + idCheckList + "/checkItems", parameters);
            PostMethod postMethodCheckBox = new PostMethod(apiURL);
            client.executeMethod(postMethodCheckBox);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("addCheckBox IOException!");
        }
    }

    public void trelloFillColumn(Parser parser) {
        String title = parser.parsingTitle();
        ArrayList<String> content = parser.parsingContent();

        addCard(title, parser.getSite());
        String idCard = getIdCard();
        String idCheckList = null;

        for (String s : content) {
            if (content.indexOf(s) % 200 == 0) {
                addCheckList(idCard);
                idCheckList = getIdCheckList();
            }
            addCheckBox(idCheckList, s);
        }
    }

    private JSONObject convertByteToJsonObject(PostMethod postMethod) {
        JSONObject jsonObject = null;
        try {
            byte[] responseBody = postMethod.getResponseBody();
            jsonObject = new JSONObject(new String(responseBody));
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("convertByteToJsonObject IOException!");
        }
        return jsonObject;
    }

    private String getIdCard() {
        return (String) convertByteToJsonObject(postMethodAddCard).get("id");
    }

    private String getIdCheckList() {
        return (String) convertByteToJsonObject(postMethodCheckList).get("id");
    }
}

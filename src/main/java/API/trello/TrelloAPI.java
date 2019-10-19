package API.trello;

import API.ServiceAPI;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parsers.Parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static API.HttpBuilder.trelloBuilder;

public class TrelloAPI implements ServiceAPI {

    private final Logger logger = LoggerFactory.getLogger(TrelloAPI.class);

    public static final String TRELLO_API = "https://api.trello.com/1/";
    static final String TRELLO_CHECKLISTS = "checklists";

    private HttpClient client = new HttpClient();
    private TrelloFillParameters trelloFillParameters = new TrelloFillParameters();

    private HttpMethod httpMethod;

    private void addCard(String name, String desc) {
        try {
            HashMap<String, String> parameters = trelloFillParameters.fillParametersForAddCard(name, desc);
            String apiURL = trelloBuilder("cards", parameters);
            httpMethod = new PostMethod(apiURL);
            client.executeMethod(httpMethod);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("addCard IOException!");
        }
    }

    private void addCheckList(String idCard) {
        try {
            String apiURL = trelloBuilder(TRELLO_CHECKLISTS, trelloFillParameters.fillParametersForAddCheckList(idCard));
            httpMethod = new PostMethod(apiURL);
            client.executeMethod(httpMethod);
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
        String idCard = getIdCheckListOrIdCard(httpMethod);
        String idCheckList = null;

        for (String s : content) {
            if (content.indexOf(s) % 200 == 0) {
                addCheckList(idCard);
                idCheckList = getIdCheckListOrIdCard(httpMethod);
            }
            addCheckBox(idCheckList, s);
        }
    }

    private JSONObject convertByteToJsonObject(HttpMethod postMethod) {
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

    private String getIdCheckListOrIdCard(HttpMethod postMethod) {
        return (String) convertByteToJsonObject(postMethod).get("id");
    }
}

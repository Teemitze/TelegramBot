package API.trello;

import API.trello.dto.TrelloCard;
import API.trello.dto.TrelloCheckList;
import org.apache.commons.httpclient.HttpMethod;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parsers.Parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrelloHelper {

    private final Logger logger = LoggerFactory.getLogger(TrelloHelper.class);

    public void trelloFillCard(Parser parser) {
        TrelloCard trelloCard = new TrelloCard();
        trelloCard.setName(parser.parsingTitle());
        trelloCard.setDescription(parser.getSite());
        trelloCard.setCheckLists(new ArrayList<>());

        List<String> checkboxes = parser.parsingContent();

        TrelloServiceAPI trelloServiceAPI = new TrelloServiceAPI(trelloCard);
        trelloServiceAPI.addCard();

        TrelloCheckList trelloCheckList = null;

        for (String checkbox : checkboxes) {
            if (checkboxes.indexOf(checkbox) % 200 == 0) { // Проверяем что в чек-листе не больше 200 элементов
                trelloCheckList = new TrelloCheckList(); // Создаём новый чек-лист
                trelloCheckList.setCheckBox(new ArrayList<>()); // В чек-листе создаём пустой лист чек-боксов
                trelloCard.getCheckLists().add(trelloCheckList); // Добавляем в карточку чек-лист
                trelloServiceAPI.addCheckList(trelloCheckList); // Отправляем в Trello
            }
            if (trelloCheckList != null) {
                String numberCheckList = 1 + checkboxes.indexOf(checkbox) + ") ";
                String resultCheckBox = numberCheckList + checkbox;
                trelloServiceAPI.addCheckBox(trelloCheckList.getIdCheckList(), resultCheckBox); // Добавляем чек-боксы в чек-лист
            }
        }
    }

    private static JSONObject convertByteToJsonObject(HttpMethod postMethod) {
        JSONObject jsonObject = null;
        try {
            byte[] responseBody = postMethod.getResponseBody();
            jsonObject = new JSONObject(new String(responseBody));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static String getIdCheckListOrIdCard(HttpMethod postMethod) {
        return convertByteToJsonObject(postMethod).get("id").toString();
    }
}

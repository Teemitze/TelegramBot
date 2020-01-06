package API.trello;

import java.util.HashMap;
import java.util.Map;

import static API.trello.TrelloServiceAPI.*;


class TrelloFillParameters {

    Map<String, String> fillParametersForAddCard(String name, String desc) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("desc", desc);
        parameters.put("pos", "top");
        parameters.put("idList", TRELLO_ID_LIST);
        parameters.put("key", TRELLO_KEY_API);
        parameters.put("token", TRELLO_TOKEN);
        return parameters;
    }

    Map<String, String> fillParametersForAddCheckList(String idCard) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("idCard", idCard);
        parameters.put("name", "Чек-лист");
        parameters.put("key", TRELLO_KEY_API);
        parameters.put("token", TRELLO_TOKEN);
        return parameters;
    }

    Map<String, String> fillParametersForAddCheckBox(String checkBoxName) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("name", checkBoxName);
        parameters.put("pos", "bottom");
        parameters.put("checked", "false");
        parameters.put("key", TRELLO_KEY_API);
        parameters.put("token", TRELLO_TOKEN);
        return parameters;
    }
}

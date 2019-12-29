package API.trello;

import java.util.HashMap;

import static API.trello.TrelloAPI.*;


class TrelloFillParameters {

    HashMap<String, String> fillParametersForAddCard(String name, String desc) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("desc", desc);
        parameters.put("pos", "top");
        parameters.put("idList", ID_LIST);
        parameters.put("key", KEY_API);
        parameters.put("token", TOKEN);
        return parameters;
    }

    HashMap<String, String> fillParametersForAddCheckList(String idCard) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("idCard", idCard);
        parameters.put("name", "Чек-лист");
        parameters.put("key", KEY_API);
        parameters.put("token", TOKEN);
        return parameters;
    }

    HashMap<String, String> fillParametersForAddCheckBox(String checkBoxName) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("name", checkBoxName);
        parameters.put("pos", "bottom");
        parameters.put("checked", "false");
        parameters.put("key", KEY_API);
        parameters.put("token", TOKEN);
        return parameters;
    }
}

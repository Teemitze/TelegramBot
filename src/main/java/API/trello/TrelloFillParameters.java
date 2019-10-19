package API.trello;

import API.ServiceAPI;

import java.util.HashMap;

class TrelloFillParameters {

    ServiceAPI trelloAPI = new TrelloAPI();

    private String KEY_API = trelloAPI.loadProperty().getProperty("trello.key");
    private String TOKEN = trelloAPI.loadProperty().getProperty("trello.token");
    private String ID_LIST = trelloAPI.loadProperty().getProperty("trello.idList");

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

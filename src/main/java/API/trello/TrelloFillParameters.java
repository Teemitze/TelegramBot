package API.trello;

import configuration.Config;

import java.util.HashMap;
import java.util.Map;


class TrelloFillParameters {

    Config config;

    String trelloApiKey;
    String trelloToken;
    String trelloIdList;

    public TrelloFillParameters(Config config) {
        this.config = config;
        trelloToken = config.trelloToken;
        trelloApiKey = config.trelloApiKey;
        trelloIdList = config.trelloIdList;
    }

    Map<String, String> fillParametersForAddCard(String name, String desc) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("desc", desc);
        parameters.put("pos", "top");
        parameters.put("idList", trelloIdList);
        parameters.put("key", trelloApiKey);
        parameters.put("token", trelloToken);
        return parameters;
    }

    Map<String, String> fillParametersForAddCheckList(String idCard) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("idCard", idCard);
        parameters.put("name", "Чек-лист");
        parameters.put("key", trelloApiKey);
        parameters.put("token", trelloToken);
        return parameters;
    }

    Map<String, String> fillParametersForAddCheckBox(String checkBoxName) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("name", checkBoxName);
        parameters.put("pos", "bottom");
        parameters.put("checked", "false");
        parameters.put("key", trelloApiKey);
        parameters.put("token", trelloToken);
        return parameters;
    }
}

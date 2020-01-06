package API.trello;

import API.ConfigureAPI;
import API.trello.dto.TrelloCheckList;

public interface TrelloAPI extends ConfigureAPI {

    void addCard();

    void addCheckList(TrelloCheckList trelloCheckList);

    void addCheckBox(String idCheckList, String checkBox);
}

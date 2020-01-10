package API.trello;

import API.trello.dto.TrelloCheckList;

public interface TrelloAPI {

    void addCard();

    void addCheckList(TrelloCheckList trelloCheckList);

    void addCheckBox(String idCheckList, String checkBox);
}

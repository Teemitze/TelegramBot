package API.trello.dto;

import java.util.ArrayList;
import java.util.List;

public class TrelloCard {
    public String idCard;
    public String name;
    public String description;
    public List<TrelloCheckList> checkLists;

    public List<TrelloCheckList> getCheckLists() {
        return checkLists == null ? new ArrayList<>() : checkLists;
    }

    public void setCheckLists(List<TrelloCheckList> checkLists) {
        this.checkLists = checkLists;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
}

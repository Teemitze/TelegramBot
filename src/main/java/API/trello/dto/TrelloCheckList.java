package API.trello.dto;

import java.util.List;

public class TrelloCheckList {
    public String idCheckList;
    public List<String> checkBox;

    public String getIdCheckList() {
        return idCheckList;
    }

    public void setIdCheckList(String idCheckList) {
        this.idCheckList = idCheckList;
    }

    public List<String> getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(List<String> checkBox) {
        this.checkBox = checkBox;
    }
}

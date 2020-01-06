package API.trello;

import API.trello.dto.TrelloCard;
import API.trello.dto.TrelloCheckList;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static API.HttpBuilder.getHttpBuilder;

public class TrelloServiceAPI implements TrelloAPI {

    private final Logger logger = LoggerFactory.getLogger(TrelloServiceAPI.class);

    public static final String TRELLO_API = "https://api.trello.com/1/";
    public static final String TRELLO_API_CARDS = TRELLO_API + "cards";
    public static final String TRELLO_CHECKLISTS = TRELLO_API + "checklists";
    public static final String TRELLO_CHECKBOX = TRELLO_CHECKLISTS + "/%s/checkItems";

    private HttpClient client = new HttpClient();
    private TrelloFillParameters trelloFillParameters = new TrelloFillParameters();

    TrelloCard trelloCard;

    public TrelloServiceAPI(TrelloCard trelloCard) {
        this.trelloCard = trelloCard;
    }

    @Override
    public void addCard() {
        try {
            String nameCard = trelloCard.getName();
            String descCard = trelloCard.getDescription();
            Map<String, String> parameters = trelloFillParameters.fillParametersForAddCard(nameCard, descCard);
            String apiURL = getHttpBuilder(TRELLO_API_CARDS, parameters);
            HttpMethod httpMethod = new PostMethod(apiURL);
            client.executeMethod(httpMethod);
            trelloCard.setIdCard(TrelloHelper.getIdCheckListOrIdCard(httpMethod));
        } catch (Exception e) {
            logger.error("Could not add card to Trello!", e);
        }
    }

    @Override
    public void addCheckList(TrelloCheckList trelloCheckList) {
        try {
            String apiURL = getHttpBuilder(TRELLO_CHECKLISTS, trelloFillParameters.fillParametersForAddCheckList(trelloCard.getIdCard()));
            HttpMethod httpMethod = new PostMethod(apiURL);
            client.executeMethod(httpMethod);
            trelloCheckList.setIdCheckList(TrelloHelper.getIdCheckListOrIdCard(httpMethod));
        } catch (Exception e) {
            logger.error("Could not add check list to Trello!", e);
        }
    }

    @Override
    public void addCheckBox(String idCheckList, String checkBox) {
        try {
            Map<String, String> parameters = trelloFillParameters.fillParametersForAddCheckBox(checkBox);
            String apiURL = getHttpBuilder(String.format(TRELLO_CHECKBOX, idCheckList), parameters);
            HttpMethod postMethodCheckBox = new PostMethod(apiURL);
            client.executeMethod(postMethodCheckBox);
        } catch (Exception e) {
            logger.error("Could not add check box to Trello!", e);
        }
    }
}

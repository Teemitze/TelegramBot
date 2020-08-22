package API.trello;

import API.trello.dto.TrelloCard;
import API.trello.dto.TrelloCheckList;
import configuration.Config;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;


public class TrelloServiceAPI {

    private final Logger logger = LoggerFactory.getLogger(TrelloServiceAPI.class);

    private static final String TRELLO_API = "https://api.trello.com/1/";
    private static final String TRELLO_API_CARDS = TRELLO_API + "cards";
    private static final String TRELLO_CHECKLISTS = TRELLO_API + "checklists";
    private static final String TRELLO_CHECKBOX = TRELLO_CHECKLISTS + "/%s/checkItems";

    private final HttpClient client = new HttpClient();
    private final Config config = Config.loadConfig();
    private final TrelloFillParameters trelloFillParameters = new TrelloFillParameters(config);

    private final TrelloCard trelloCard;

    public TrelloServiceAPI(TrelloCard trelloCard) {
        this.trelloCard = trelloCard;
    }

    public void addCard() {
        Map<String, String> parameters =
                trelloFillParameters.fillParametersForAddCard(trelloCard.getName(), trelloCard.getDescription());



        createHttpBuilder(TRELLO_API_CARDS, parameters)
                .flatMap(this::sendPostRequest)
                .map(TrelloHelper::getIdCheckListOrIdCard)
                .ifPresent(trelloCard::setIdCard);
    }

    private Optional<HttpMethod> sendPostRequest(String apiURL) {
        HttpMethod postMethod = new PostMethod(apiURL);
        try {
            client.executeMethod(postMethod);
            if (isStatusOK(postMethod)) {
                return Optional.of(postMethod);
            }
        } catch (Exception e) {
            logger.error("Failed to send POST request: {}", apiURL);
        }
        return Optional.empty();
    }

    private boolean isStatusOK(HttpMethod httpMethod) {
        return httpMethod.getStatusCode() == 200;
    }

    public void addCheckList(TrelloCheckList trelloCheckList) {
        String apiURL = createHttpBuilder(TRELLO_CHECKLISTS, trelloFillParameters.fillParametersForAddCheckList(trelloCard.getIdCard())).get();
        HttpMethod postMethod = sendPostRequest(apiURL).get();
        trelloCheckList.setIdCheckList(TrelloHelper.getIdCheckListOrIdCard(postMethod));
    }

    public void addCheckBox(String idCheckList, String checkBox) {
        Map<String, String> parameters = trelloFillParameters.fillParametersForAddCheckBox(checkBox);
        String apiURL = createHttpBuilder(String.format(TRELLO_CHECKBOX, idCheckList), parameters).get();
        sendPostRequest(apiURL);
    }

    private Optional<String> createHttpBuilder(String linkApi, Map<String, String> parameters) {
        try {
            URIBuilder builder = new URIBuilder(linkApi);

            parameters.forEach(builder::addParameter);
            return Optional.of(builder.toString());
        } catch (Exception e) {
            logger.error("Failed to create HTTP request: {}", linkApi);
            return Optional.empty();
        }
    }
}

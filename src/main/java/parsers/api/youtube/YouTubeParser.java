package parsers.api.youtube;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parsers.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static parsers.api.youtube.YouTubeParserUtil.MAX_RESULTS;
import static parsers.api.youtube.YouTubeParserUtil.getVideoPlaylistJSON;

public class YouTubeParser implements Parser {

    public final static Logger LOG = LoggerFactory.getLogger(YouTubeParser.class);

    private final String URL;

    public YouTubeParser(String URL) {
        this.URL = URL;
    }

    @Override
    public String getURL() {
        return URL;
    }

    @Override
    public Optional<String> getTitle() {
        try {
            return Optional.of(getPlaylistId(URL))
                    .map(YouTubeParserUtil::getPlaylistTitleJSON)
                    .flatMap(jsonObject -> jsonObject
                            .map(this::getTitleFromJson));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<String>> getContent() {
        try {
            return getVideoPlaylistJSON(getPlaylistId(URL), "")
                    .map(this::getContentFromJSON).filter(item -> !item.isEmpty());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public String getTitleFromJson(JSONObject jsonObject) {
        Optional<String> titleJSON;
        try {
            titleJSON = Optional.of(jsonObject.getJSONArray("items"))
                    .map(jArray -> jArray.getJSONObject(0))
                    .map(json -> json.getJSONObject("snippet").getJSONObject("localized").get("title").toString());

            if (titleJSON.isPresent()) {
                return titleJSON.get();
            } else {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private List<String> getContentFromJSON(JSONObject pageJson) {
        List<String> videoList = new ArrayList<>();

        try {
            Optional<Integer> pageCount = Optional.ofNullable(pageJson)
                    .map(json -> json.getJSONObject("pageInfo").get("totalResults"))
                    .map(Object::toString)
                    .map(this::countPage);

            if (pageCount.isPresent()) {
                for (int i = pageCount.get(); i > 0; i--) {
                    pageJson.getJSONArray("items")
                            .forEach(item ->
                            {
                                JSONObject jsonObject = (JSONObject) item;
                                String element = jsonObject.getJSONObject("snippet").getString("title");
                                videoList.add(element);
                            });

                    if (i >= 2) {
                        Optional<JSONObject> optionalPageJson = Optional.of(pageJson.getString("nextPageToken"))
                                .map(token -> {
                                    try {
                                        return getVideoPlaylistJSON(getPlaylistId(URL), token);
                                    } catch (TrimPlaylistIdException e) {
                                        return Optional.<JSONObject>empty();
                                    }
                                })
                                .filter(Optional::isPresent)
                                .map(Optional::get);

                        if (optionalPageJson.isPresent()) {
                            pageJson = optionalPageJson.get();
                        }

                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Couldn't parse content from JSON", e);
        }
        return videoList;
    }

    private int countPage(String videoCount) {
        return (int) Math.ceil(Double.parseDouble(videoCount) / Double.parseDouble(MAX_RESULTS));
    }

    public String getPlaylistId(String url) throws TrimPlaylistIdException {
        if (URL.length() > 38 && URL.length() <= 72) {
            return url.trim().substring(38);
        } else {
            throw new TrimPlaylistIdException();
        }
    }
}

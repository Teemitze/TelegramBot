package API.youtube;

import org.json.JSONObject;
import org.testng.annotations.Test;
import parsers.api.youtube.TrimPlaylistIdException;
import parsers.api.youtube.YouTubeParser;
import parsers.api.youtube.YouTubeParserUtil;

import static org.testng.Assert.assertEquals;

public class YouTubeHelperTest {

    final String siteURL = "https://www.youtube.com/playlist?list=PL6yLoZ_3Y0HKGL3F7vv2SNSrA3TkbXtBX";
    final YouTubeParser youTubeHelper = new YouTubeParser(siteURL);
    final YouTubeParserUtil youTubeParserUtil = new YouTubeParserUtil();

    @Test
    public void testGetTitleFromJson() {
        final JSONObject jsonObject = new JSONObject("{\"items\":[{\"snippet\":{\"localized\":{\"title\":\"Spring-потрошитель\"}}}]}");
        final String actual = youTubeHelper.getTitleFromJson(jsonObject);
        final String expected = "Spring-потрошитель";
        assertEquals(actual, expected);
    }

    @Test
    public void testGetSite() {
        assertEquals(siteURL, youTubeHelper.getURL());
    }

    @Test
    public void testGetPlaylistId() {
        final String actual;
        try {
            actual = youTubeHelper.getPlaylistId(siteURL);
        } catch (TrimPlaylistIdException e) {
            throw new RuntimeException(e);
        }
        final String expected = "PL6yLoZ_3Y0HKGL3F7vv2SNSrA3TkbXtBX";
        assertEquals(actual, expected);
    }
}
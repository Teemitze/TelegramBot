package API.youtube;

import org.json.JSONObject;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class YouTubeHelperTest {

    final String siteURL = "https://www.youtube.com/playlist?list=PL6yLoZ_3Y0HKGL3F7vv2SNSrA3TkbXtBX";
    final YouTubeHelper youTubeHelper = new YouTubeHelper(siteURL);

    @Test
    public void testGetTitleFromJson() {
        final JSONObject jsonObject = new JSONObject("{\"items\":[{\"snippet\":{\"localized\":{\"title\":\"Spring-потрошитель\"}}}]}");
        final String actual = youTubeHelper.getTitleFromJson(jsonObject);
        final String expected = "Spring-потрошитель";
        assertEquals(actual, expected);
    }

    @Test
    public void testGetSite() {
        final String actual = siteURL;
        final String expected = youTubeHelper.getSite();
        assertEquals(actual, expected);
    }

    @Test
    public void testGetPlaylistId() {
        final String actual = youTubeHelper.getPlaylistId(siteURL);
        final String expected = "PL6yLoZ_3Y0HKGL3F7vv2SNSrA3TkbXtBX";
        assertEquals(actual, expected);
    }
}
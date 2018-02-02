package com.github.noraui.videoreorder;

import org.junit.Test;

public class VideoReordUT {

    @Test
    public void runVideoReord() throws Exception {
        VideoReord mrc = new VideoReord();
        System.out.println("DEBUG SGR: " + System.getProperty("webdriver.chrome.driver"));
        mrc.main(null);
    }

}

/**
 * NoraUi is licensed under the license GNU AFFERO GENERAL PUBLIC LICENSE
 * 
 * @author Nicolas HALLOUIN
 * @author Stéphane GRILLON
 */
package com.github.noraui.videoreorder;

import org.junit.Test;

public class VideoReordUT {

    @Test
    public void runVideoReord() throws Exception {
        VideoReord mrc = new VideoReord();
        mrc.main(null);
    }

}

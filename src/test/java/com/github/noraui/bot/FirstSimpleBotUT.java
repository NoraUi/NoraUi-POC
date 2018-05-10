/**
 * NoraUi is licensed under the license GNU AFFERO GENERAL PUBLIC LICENSE
 * 
 * @author Nicolas HALLOUIN
 * @author Stéphane GRILLON
 */
package com.github.noraui.bot;

import org.junit.Test;

public class FirstSimpleBotUT {

    @Test
    public void runFirstSimpleBot() throws Exception {
        FirstSimpleBot firstSimpleBot = new FirstSimpleBot();
        firstSimpleBot.main(null);
    }

}

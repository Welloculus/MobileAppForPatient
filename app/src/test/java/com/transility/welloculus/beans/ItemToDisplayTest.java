package com.transility.welloculus.beans;

import com.transility.welloculus.BuildConfig;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

/**
 * Created by sneha.bansal on 3/23/2017.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,manifest = Config.NONE)
public class ItemToDisplayTest {


    @Test
    public void testsetLabelText(){
        String labelText = "";
        String dataText = "";
        String messageText="";
        int labelColor=1;
        int dataColor=1;
        int messageColor=1;
        int dataBackground=1;
        String dataDrawable="";
        ItemToDisplayBean toDisplay = new ItemToDisplayBean();
        toDisplay.setLabelText(labelText);
        toDisplay.setDataText(dataText);
        toDisplay.setMessageText(messageText);
        toDisplay.setLabelColor(labelColor);
        toDisplay.setDataColor(dataColor);
        toDisplay.setMessageColor(messageColor);
        toDisplay.setDataBackground(dataBackground);
        toDisplay.setDataDrawable(dataDrawable);
        Assert.assertNotNull("labelText is null.",toDisplay.getLabelText());

    }
}



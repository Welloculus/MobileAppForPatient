package com.transility.welloculus.app;

/**
 * Created by ImpetusDev on 4/17/2017.
 */
import android.test.ApplicationTestCase;

import org.junit.Before;

public class ApplicationTest extends ApplicationTestCase<HealthCareApp> {

    private HealthCareApp application;

    public ApplicationTest() {
        super(HealthCareApp.class);
    }

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
        application = getApplication();

    }

    public void testCorrectVersion() throws Exception {
//        PackageInfo info = application.getPackageManager().getPackageInfo(application.getPackageName(), 0);
//        assertNotNull(info);
//        MoreAsserts.assertMatchesRegex("\\d\\.\\d", info.versionName);
    }

}

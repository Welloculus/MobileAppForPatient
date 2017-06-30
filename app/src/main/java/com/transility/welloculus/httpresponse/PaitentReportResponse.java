package com.transility.welloculus.httpresponse;

import com.transility.welloculus.beans.ReportResponseBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * The type Paitent report response.
 */
public class PaitentReportResponse extends BaseResponse {

    /**
     * Gets paitent report.
     *
     * @return the paitent report
     */
    public ReportResponseBean getPaitentReport() {
        return paitentReport;
    }

    /**
     * Sets paitent report.
     *
     * @param paitentReport the paitent report
     */
    public void setPaitentReport(ReportResponseBean paitentReport) {
        this.paitentReport = paitentReport;
    }

    /**
     * The Paitent report.
     */
    ReportResponseBean paitentReport;

    @Override
    public void processResponseSuccess(Object... arg) {
        //parsing logic goes here
        Type type = new TypeToken<ReportResponseBean>() {
        }.getType();
        paitentReport = new Gson().fromJson(getmData(), type);
    }

}

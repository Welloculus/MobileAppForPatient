package com.transility.welloculus.beans;

/**
 * Created by sneha.bansal on 4/11/2017.
 */
public class ReportResponseBean extends ResponseBean {

    private PatientReportBean responseContent;

    /**
     * Gets response content.
     *
     * @return the response content
     */
    public PatientReportBean getResponseContent ()
    {
        return responseContent;
    }

    /**
     * Sets response content.
     *
     * @param responseContent the response content
     */
    public void setResponseContent (PatientReportBean responseContent)
    {
        this.responseContent = responseContent;
    }

    @Override
    public String toString() {
        return "ClassPojo [errorMessage = " + errorMessage + ", responseCode = " + responseCode + ", status = " + status + ", responseContent = " + responseContent + ", errorCode = " + errorCode + ", success = " + success + "]";
    }

}

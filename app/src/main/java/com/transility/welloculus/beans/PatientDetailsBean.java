package com.transility.welloculus.beans;

/**
 * Created by sneha.bansal on 3/31/2017.
 */
public class PatientDetailsBean {

    private String patient_gender;

    private String patient_lastname;

    private String patient_dob;

    private String patient_firstname;

    private String patient_username;

    /**
     * Gets patient gender.
     *
     * @return the patient gender
     */
    public String getPatient_gender ()
    {
        return patient_gender;
    }

    /**
     * Sets patient gender.
     *
     * @param patient_gender the patient gender
     */
    public void setPatient_gender (String patient_gender)
    {
        this.patient_gender = patient_gender;
    }

    /**
     * Gets patient lastname.
     *
     * @return the patient lastname
     */
    public String getPatient_lastname ()
    {
        return patient_lastname;
    }

    /**
     * Sets patient lastname.
     *
     * @param patient_lastname the patient lastname
     */
    public void setPatient_lastname (String patient_lastname)
    {
        this.patient_lastname = patient_lastname;
    }

    /**
     * Gets patient dob.
     *
     * @return the patient dob
     */
    public String getPatient_dob ()
    {
        return patient_dob;
    }

    /**
     * Sets patient dob.
     *
     * @param patient_dob the patient dob
     */
    public void setPatient_dob (String patient_dob)
    {
        this.patient_dob = patient_dob;
    }

    /**
     * Gets patient firstname.
     *
     * @return the patient firstname
     */
    public String getPatient_firstname ()
    {
        return patient_firstname;
    }

    /**
     * Sets patient firstname.
     *
     * @param patient_firstname the patient firstname
     */
    public void setPatient_firstname (String patient_firstname)
    {
        this.patient_firstname = patient_firstname;
    }

    /**
     * Gets patient username.
     *
     * @return the patient username
     */
    public String getPatient_username ()
    {
        return patient_username;
    }

    /**
     * Sets patient username.
     *
     * @param patient_username the patient username
     */
    public void setPatient_username (String patient_username)
    {
        this.patient_username = patient_username;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [patient_gender = "+patient_gender+", patient_lastname = "+patient_lastname+", patient_dob = "+patient_dob+", patient_firstname = "+patient_firstname+", patient_username = "+patient_username+"]";
    }
}

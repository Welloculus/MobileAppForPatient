package com.transility.welloculus.beans;

/**
 * The type Health data bean.
 */
public class HealthDataBean {

    private String entry_created;

    private String value;

    private String key;

    /**
     * Gets entry created.
     *
     * @return the entry created
     */
    public String getEntry_created ()
    {
        return entry_created;
    }

    /**
     * Sets entry created.
     *
     * @param entry_created the entry created
     */
    public void setEntry_created (String entry_created)
    {
        this.entry_created = entry_created;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue ()
    {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue (String value)
    {
        this.value = value;
    }

    /**
     * Gets key.
     *
     * @return the key
     */
    public String getKey ()
    {
        return key;
    }

    /**
     * Sets key.
     *
     * @param key the key
     */
    public void setKey (String key)
    {
        this.key = key;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [entry_created = "+entry_created+", value = "+value+", key = "+key+"]";
    }
}

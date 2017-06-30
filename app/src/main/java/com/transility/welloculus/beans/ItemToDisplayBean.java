package com.transility.welloculus.beans;

/**
 * The type Item to display bean.
 */
public class ItemToDisplayBean {
    // Text for display
    private String labelText;
    private String dataText;
    private String messageText;

    // Display text colors
    private int labelColor;
    private int dataColor;
    private int messageColor;

    // Data box background
    private int dataBackground;

    // Data box drawable
    private String dataDrawable;
    /**
     * Gets label text.
     *
     * @return the label text
     */
    public String getLabelText() {
        return labelText;
    }

    /**
     * Sets label text.
     *
     * @param labelText the label text
     */
    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }

    /**
     * Gets data text.
     *
     * @return the data text
     */
    public String getDataText() {
        return dataText;
    }

    /**
     * Sets data text.
     *
     * @param dataText the data text
     */
    public void setDataText(String dataText) {
        this.dataText = dataText;
    }

    /**
     * Gets message text.
     *
     * @return the message text
     */
    public String getMessageText() {
        return messageText;
    }

    /**
     * Sets message text.
     *
     * @param messageText the message text
     */
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    /**
     * Gets label color.
     *
     * @return the label color
     */
    public int getLabelColor() {
        return labelColor;
    }

    /**
     * Sets label color.
     *
     * @param labelColor the label color
     */
    public void setLabelColor(int labelColor) {
        this.labelColor = labelColor;
    }

    /**
     * Gets data color.
     *
     * @return the data color
     */
    public int getDataColor() {
        return dataColor;
    }

    /**
     * Sets data color.
     *
     * @param dataColor the data color
     */
    public void setDataColor(int dataColor) {
        this.dataColor = dataColor;
    }

    /**
     * Gets message color.
     *
     * @return the message color
     */
    public int getMessageColor() {
        return messageColor;
    }

    /**
     * Sets message color.
     *
     * @param messageColor the message color
     */
    public void setMessageColor(int messageColor) {
        this.messageColor = messageColor;
    }

    /**
     * Gets data background.
     *
     * @return the data background
     */
    public int getDataBackground() {
        return dataBackground;
    }

    /**
     * Sets data background.
     *
     * @param dataBackground the data background
     */
    public void setDataBackground(int dataBackground) {
        this.dataBackground = dataBackground;
    }

    /**
     * Gets data drawable.
     *
     * @return the data drawable
     */
    public String getDataDrawable() {
        return dataDrawable;
    }

    /**
     * Sets data drawable.
     *
     * @param dataDrawable the data drawable
     */
    public void setDataDrawable(String dataDrawable) {
        this.dataDrawable = dataDrawable;
    }

    @Override
    public String toString() {
        return "ItemToDisplay{" +
                "labelText='" + labelText + '\'' +
                ", dataText='" + dataText + '\'' +
                ", messageText='" + messageText + '\'' +
                ", labelColor=" + labelColor +
                ", dataColor=" + dataColor +
                ", messageColor=" + messageColor +
                ", dataBackground=" + dataBackground +
                ", dataDrawable='" + dataDrawable + '\'' +
                '}';
    }
}

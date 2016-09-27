package com.onenow.hedgefund.nosql.beans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Model
{
    @XmlElement
    private String lookup;
    @XmlElement
    private String itemJson;
    @XmlElement
    private String tableName;

    public Model()
    {
    }

    public String getLookup()
    {
        return lookup;
    }

    public void setLookup(String lookup)
    {
        this.lookup = lookup;
    }

    public String getItemJson()
    {
        return itemJson;
    }

    public void setItemJson(String itemJson)
    {
        this.itemJson = itemJson;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }
}

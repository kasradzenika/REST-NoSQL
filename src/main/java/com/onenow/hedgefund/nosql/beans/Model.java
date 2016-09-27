package com.onenow.hedgefund.nosql.beans;

public class Model
{
    private String lookup;
    private String itemJson;
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

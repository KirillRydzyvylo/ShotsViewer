package com.dribbble.shotsviewer.db;



public class DBTable {
    public static final String TABLE_NAME = "shots";
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String IMAGE_URL = "image_url";
    public static final String DAY = "day";
    public static final String UNIX_DATE = "unix_date";



    public static final String CREATE_TABLE = "create table '"+TABLE_NAME+"' ("+ID+" int primary key,"+
            TITLE + " text,"+
            DESCRIPTION + " text,"+
            IMAGE_URL + " text ,"+
            DAY + " real ,"+
            UNIX_DATE + " real );";


    public static final String DROP_TABLE = "DROP TABLE IF EXISTS shots;";



}

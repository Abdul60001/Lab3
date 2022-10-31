package com.example.lab3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHandler extends SQLiteOpenHelper {
    private static final String TABLE_NAME="products";
    private static final String COLUMN_ID="id";
    private static final String COLUMN_PRODUCT_NAME="name";
    private static final String COLUMN_PRODUCT_PRICE="price";
    private static final String DATABASE_NAME = "products.db";
    private static final int DATABASE_VERSION=1;

    public MyDBHandler(Context context){ // Take context to the database called in
        super(context, DATABASE_NAME ,null, DATABASE_VERSION );
    }
    @Override
    public void onCreate(SQLiteDatabase db) { //Create table with SQL COMMANDES
        String create_table_cmd="CREATE TABLE "+ TABLE_NAME+ "("+COLUMN_ID+" INTEGER PRIMARY KEY, " +COLUMN_PRODUCT_NAME+" TEXT, "+COLUMN_PRODUCT_PRICE+" DOUBLE "+");";
        db.execSQL(create_table_cmd);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { //If already have database
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME); //SQL COMMANDE
        onCreate(db);

    }

    public Cursor getData() {
        SQLiteDatabase db =this.getReadableDatabase();
        String query="SELECT * FROM "+TABLE_NAME;
        return db.rawQuery(query,null);//return all products from the table
    }

    public void addProduct(Product product) {
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(COLUMN_PRODUCT_NAME,product.getProductName());
        values.put(COLUMN_PRODUCT_PRICE,product.getProductPrice());
        db.insert(TABLE_NAME,null,values);
        db.close();
    }

    public void deleteProduct(Product product){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_PRODUCT_NAME + "=?", new String[] {product.getProductName()});
        db.close();
    }

    public Cursor findProductByName(Product product){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("Select * from "+TABLE_NAME+" WHERE "+COLUMN_PRODUCT_NAME+"=?", new String[]{product.getProductName()});
        return cursor;
    }

    public Cursor findProductByPrice(Product product){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("Select * from "+TABLE_NAME+" WHERE "+COLUMN_PRODUCT_PRICE+"=?", new String[]{String.valueOf(product.getProductPrice())});
        return cursor;
    }

    public Cursor findProductByNameAndPrice(Product product){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("Select * from "+TABLE_NAME+" WHERE "+COLUMN_PRODUCT_NAME+"=? AND "+COLUMN_PRODUCT_PRICE+"=?", new String[]{product.getProductName(), String.valueOf(product.getProductPrice())});
        return cursor;
    }
}

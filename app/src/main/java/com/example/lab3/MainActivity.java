package com.example.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView productId;
    EditText productName, productPrice;
    Button addBtn, findBtn, deleteBtn;
    ListView productListView;
    ArrayList<String> productList;
    ArrayAdapter adapter;
    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        productList=new ArrayList<>();
        //Layout information
        productId = findViewById(R.id.textView2);
        productName = findViewById(R.id.editTextTextPersonName3);
        productPrice = findViewById(R.id.editTextTextPersonName4);

        //Buttons information
        addBtn = findViewById(R.id.addBtn);
        findBtn = findViewById(R.id.findBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        //ListView
        productListView=findViewById(R.id.productListView);

        dbHandler = new MyDBHandler(this);

        viewProducts(dbHandler.getData());

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=productName.getText().toString();
                double price=Double.parseDouble(productPrice.getText().toString());
                Product product =new Product(name,price);
                dbHandler.addProduct(product);
                productName.setText("");
                productPrice.setText("");

                viewProducts(dbHandler.getData());
            }
        });

        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=productName.getText().toString();
                String priceString=productPrice.getText().toString();
                Cursor cursor;
                double price;

                if (!name.equals("") && !priceString.equals("")) {
                    price=Double.parseDouble(priceString);
                    Product product = new Product(name, price);
                    cursor = dbHandler.findProductByNameAndPrice(product);
                }
                else if (!name.equals("")) {
                    price=0;
                    Product product = new Product(name, price);
                    cursor = dbHandler.findProductByName(product);
                }
                else if(!priceString.equals("")) {
                    price=Double.parseDouble(priceString);
                    Product product =new Product(name,price);
                    cursor = dbHandler.findProductByPrice(product);
                }
                else {
                    cursor=dbHandler.getData();
                }

                productName.setText("");
                productPrice.setText("");

                viewProducts(cursor);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=productName.getText().toString();
                double price=Double.parseDouble(productPrice.getText().toString());
                Product product =new Product(name,price);

                dbHandler.deleteProduct(product);
                productName.setText("");
                productPrice.setText("");
                viewProducts(dbHandler.getData());
            }
        });

    }

    private void viewProducts(Cursor cursor) {
        productList.clear();
//        Cursor cursor=dbHandler.getData();    //Cursor to go
        if(cursor.getCount()==0) {
            Toast.makeText(MainActivity.this, "Nothing to show", Toast.LENGTH_SHORT).show();
        }
        else {
            while (cursor.moveToNext()) {
                productList.add(cursor.getString(1)+" ( "+cursor.getString(2)+") "); // 1 is product name
            }
        }
        //Need an adaptor to change from Arraylist to something that Android can understand it
        adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,productList); // Means adapt array to look like simple list item 1
        productListView.setAdapter(adapter);
    }
}
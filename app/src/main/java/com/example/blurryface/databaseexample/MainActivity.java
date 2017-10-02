package com.example.blurryface.databaseexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView id;
    EditText myName,myQuantity;
    MyDBHandler myDBHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        id = (TextView)findViewById(R.id.productId);
        myName = (EditText)findViewById(R.id.productName);
        myQuantity = (EditText)findViewById(R.id.productQuantity);
        myDBHandler = new MyDBHandler(this,null,null,3);

    }
    public void addProduct(View view)
    {
        String name = myName.getText().toString();
        int quantity = Integer.parseInt(myQuantity.getText().toString());
        Product product = new Product(name,quantity);
        myDBHandler.addData(product);
        myName.setText("");
        myQuantity.setText("");
    }
    public void findProduct(View view)
    {
        String name = myName.getText().toString();
        Product product = myDBHandler.searchData(name);
        if (product!=null)
        {
            id.setText(String.valueOf(product.get_id()));
            myQuantity.setText(String.valueOf(product.get_quantity()));
        }
        else
            Toast.makeText(getApplicationContext(),"No Match Found!!",Toast.LENGTH_LONG).show();
    }
    public void removeProduct(View view)
    {
        String name = myName.getText().toString();
        boolean deleted= myDBHandler.removeData(name);
        if(deleted)
            Toast.makeText(getApplicationContext(),"Delete Successful",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplicationContext(),"Delete Unsuccessful",Toast.LENGTH_LONG).show();
    }
}

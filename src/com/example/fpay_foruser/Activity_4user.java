package com.example.fpay_foruser;

import com.martin.cloudmarket.R;
import com.martin.cloudmarket.demo.PayMsg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;


public class Activity_4user extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_4user);
        
        Intent intent=getIntent();
        String msg=intent.getStringExtra("msg");
        
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
        
        PayMsg paymsg = new PayMsg(msg);
        
        Intent flg=new Intent();
        flg.putExtra("ordernum",paymsg.getOrdernum());
        setResult(1,flg);
        
//        finish();
    }

   
    

    
    
}

package lcukerd.com.logintest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Application;
import android.widget.Button;
import android.widget.EditText;

import com.kumulos.android.Kumulos;
import com.kumulos.android.ResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    private int ID=0;
    private EditText UName,UPass,UAge;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Kumulos.initWithAPIKeyAndSecretKey("da92472a-5a6a-4720-b06e-e3b5d956182e", "6nnEVswUkkPsFr4fICtA5wZ4WTJ69LpDE+RH", this);

        UName =(EditText) findViewById(R.id.name);
        UPass = (EditText) findViewById(R.id.password);
        UAge =  (EditText) findViewById(R.id.age);
        Button login = (Button) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                String username = UName.getText().toString();
                String password = UPass.getText().toString();

                LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
                params.put("accountName", username);
                params.put("password",password);
                        Kumulos.call("login", params, new ResponseHandler() {
                            @Override
                            public void didCompleteWithResult(Object result) {
                                ArrayList<LinkedHashMap<String, Object>> objects = (ArrayList<LinkedHashMap<String, Object>>) result;
                                if (objects.size()==0)
                                    UName.setText("Login Failed");
                                else {
                                    LinkedHashMap<String, Object> item = objects.get(0);
                                    ID = Integer.parseInt(item.get("credentialID").toString());

                                    HashMap<String, String> paramage = new HashMap<String, String>();
                                    paramage.put("credential", Integer.toString(ID));
                                    Kumulos.call("getage", paramage, new ResponseHandler() {
                                        @Override
                                        public void didCompleteWithResult(Object result) {
                                            ArrayList<LinkedHashMap<String, Object>> objects = (ArrayList<LinkedHashMap<String, Object>>) result;
                                            LinkedHashMap<String, Object> item = objects.get(0);
                                            int age = Integer.parseInt(item.get("age").toString());
                                            UAge.setText(Integer.toString(age));
                                        }

                                    });
                                    createNew();

                                }
                            }
                        });

                params.clear();



            }
        });
        Button signup = (Button) findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                String username = UName.getText().toString();
                String password = UPass.getText().toString();
                LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
                params.put("accountName", username);
                params.put("password",password);

                Kumulos.call("signup", params, new ResponseHandler() {
                        @Override
                        public void didCompleteWithResult(Object result)
                        {
                            ID = (int) result;
                            UName.setText("");
                            UPass.setText("");
                            params.put("age",UAge.getText().toString());
                            params.put("credential", Integer.toString(ID));
                            Kumulos.call("setAge", params, new ResponseHandler());
                            UAge.setText("");
                            createNew();
                        }
                    });



            }
        });
    }
    public void createNew() {
        Intent intent = new Intent(this, chatActivity.class);
        intent.putExtra("ID", ID);
        startActivity(intent);
    }

}

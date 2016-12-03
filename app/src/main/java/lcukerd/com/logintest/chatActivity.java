package lcukerd.com.logintest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kumulos.android.Kumulos;
import com.kumulos.android.ResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class chatActivity extends AppCompatActivity {

    private static EditText msg;
    Thread recieve;
    private int ID=0,status=0;
    private EditText chat ;
    private int id =0;
    private LinkedHashMap<String, String> param;
    private String message,sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        ID = getIntent().getIntExtra("ID",0);
        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_chat);
        Kumulos.initWithAPIKeyAndSecretKey("da92472a-5a6a-4720-b06e-e3b5d956182e", "6nnEVswUkkPsFr4fICtA5wZ4WTJ69LpDE+RH", this);


        msg  = (EditText) findViewById(R.id.message);
        chat = (EditText) findViewById(R.id.chat);
        chatActivity cl = chatActivity.this;
        recieve = new Thread(new waitforclick());
        recieve.start();
        param = new LinkedHashMap<String, String>();


        //while (true) {
            param.clear();
            Kumulos.call("getid", new ResponseHandler() {
                @Override
                public void didCompleteWithResult(Object result) {
                    ArrayList<LinkedHashMap<String, Object>> objects = (ArrayList<LinkedHashMap<String, Object>>) result;
                    LinkedHashMap<String, Object> item = objects.get(0);
                    id = Integer.parseInt(item.get("messageID").toString());

                    param.clear();
                    //while(true) {
                    param.clear();
                    param.put("messageID", Integer.toString(id));
                    Kumulos.call("getmsg", param, new ResponseHandler() {
                        @Override
                        public void didCompleteWithResult(Object result) {
                            ArrayList<LinkedHashMap<String, Object>> messages = (ArrayList<LinkedHashMap<String, Object>>) result;
                            LinkedHashMap<String, Object> item = messages.get(0);
                            message = item.get("message").toString();
                            LinkedHashMap<String, Object> sentby = (LinkedHashMap<String, Object>) item.get("user");
                            sender = sentby.get("accountName").toString();
                            id++;
                            chat.setText(sender + ": " + message + System.getProperty("line.separator"));
                        }

                        public void didFailWithError(Object result) {

                        }
                    });
                    //}
                }
            });

        //}



    }


    class waitforclick implements Runnable {

        private Button Send = (Button) findViewById(R.id.send);

        public void run()
        {
            Send.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick (View view){
                    String message = msg.getText().toString();
                    LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
                    params.put("message", message);
                    params.put("user", String.valueOf(ID));
                    Kumulos.call("sendmsg", params, new ResponseHandler());
                    msg.setText("");
                }
            }

            );
        }

    }


    /*public static int showmsg(String mess,String sender)
    {
        msg.setText(sender + ":" + mess + System.getProperty("line.separator"));
        return 1;

    }*/
}


/*class Reciever implements Runnable {
    EditText chatBox;
    int id =0;
    private chatmsg storemsg=new chatmsg();
    LinkedHashMap<String, String> param;
    int status=0;
    String message,sender;

    public void run()
    {
        param = new LinkedHashMap<String,String>();
        Kumulos.call("getid", new ResponseHandler() {
            @Override
            public void didCompleteWithResult(Object result)
            {
                ArrayList<LinkedHashMap<String, Object>> objects = (ArrayList<LinkedHashMap<String, Object>>) result;
                LinkedHashMap<String, Object> item = objects.get(0);
                id = Integer.parseInt(item.get("messageID").toString());

                param.put("messageID",Integer.toString(id));
                Kumulos.call("getmsg",param, new ResponseHandler(){
                @Override
                public void didCompleteWithResult(Object result)
                {
                    ArrayList<LinkedHashMap<String, Object>> messages = (ArrayList<LinkedHashMap<String, Object>>) result;
                    LinkedHashMap<String, Object> item = messages.get(0);
                    message = item.get("message").toString();
                    LinkedHashMap<String, Object> sentby = (LinkedHashMap<String,Object>) item.get("user");
                    sender = sentby.get("accountName").toString();
                    status=1;
                    storemsg.set(message,sender);
                    id++;
                }
                public void didFailWithError(Object result )
                {

                }
            });
            }
        });
        while(status==0)
        {
            int a=0;
        }
        chatBox.setText(sender + ":" + message + System.getProperty("line.separator"));

    }




}*/

/*class chatmsg
{
    private String msg;
    private String sender;
    private int state=0;

    public void set(String mess,String sentby)
    {
        msg=mess;
        sender=sentby;
        state=0;
        display();

    }
    private void display()
    {
        state=chatActivity.showmsg(msg,sender);
    }
}*/


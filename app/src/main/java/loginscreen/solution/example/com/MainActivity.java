package loginscreen.solution.example.com.loginscreen;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Vitor//

public class MainActivity extends AppCompatActivity {

    public DetailsDb db;
    private EditText et_email;
    private EditText et_name;
    private EditText et_password;
    private EditText et_phone;
    private Button bt_login;
    private Button bt_signup;
    private Button bt_create;
    private Button bt_sign_in;

    @Override
    public void onCreate(Bundle instanceState) {
        super.onCreate(instanceState);
        setContentView(R.layout.content_main);

        db = new DetailsDb(this);

        bt_login = findViewById(R.id.bt_login);
        bt_signup = findViewById(R.id.bt_signup);
        bt_create = findViewById(R.id.bt_create);
        bt_sign_in = findViewById(R.id.bt_sign_in);

        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_phone = findViewById(R.id.et_phone);

        final LinearLayout it_name = findViewById(R.id.lt_name);
        final ViewFlipper vf = findViewById(R.id.view_flipper);

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (vf.getDisplayedChild() == 1) {
                    vf.showNext();
                    it_name.setVisibility(View.INVISIBLE);
                    et_email.requestFocus();
                }
            }
        });

        bt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (vf.getDisplayedChild() == 0) {
                    vf.showPrevious();
                    it_name.setVisibility(View.VISIBLE);
                    it_name.requestFocus();
                }

            }
        });

        bt_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateData()) {

                    String name = et_name.getText().toString();
                    String email = et_email.getText().toString();
                    String password = et_password.getText().toString();
                    int phone = Integer.parseInt(et_phone.getText().toString());

                    ContentValues values = new ContentValues();

                    values.put("username", name);
                    values.put("email", email);
                    values.put("password", password);
                    values.put("phone", phone);

                    if(db.insert(values)){

                    vf.showNext();
                    it_name.setVisibility(View.INVISIBLE);
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, "Account created successfully", duration);
                    toast.show();
                    }else{
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, "Account already exists", duration);
                        toast.show();
                    }

                } else {
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, "One or more fields are invalid", duration);
                    toast.show();
                }
            }
        });

        bt_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                String name,phone;

                if(email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    et_email.setError("Empty or invalid email");

                }else if(password.isEmpty() || !isValidPassword(password)){
                    et_password.setError("Password is empty or isn't valid");
                }else{


                    CheckCredentials cc = new CheckCredentials(MainActivity.this);
                    if (cc.isauthenticate(email, password)) {

                        Cursor c = db.validate(email, password);
                        if (c.moveToFirst()) {
                            name = c.getString(0);
                            phone = Integer.toString(c.getInt(3));

                            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                            intent.putExtra("email", email);
                            intent.putExtra("name", name);
                            intent.putExtra("phone", phone);

                            startActivity(intent);

                        } else {
                            Context context = getApplicationContext();
                            int duration = Toast.LENGTH_LONG;
                            Toast toast = Toast.makeText(context, "User not found!Please create user", duration);
                            toast.show();
                        }

                    } else {
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, "User not found!Please create user", duration);
                        toast.show();
                    }
                }
            }
        });
    }

    public boolean validateData() {
        boolean valid = true;

        String name = et_name.getText().toString();
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();
        String phone = et_phone.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_email.setError("Enter a valid email address");
            valid = false;
        } else {
            et_email.setError(null);
        }
        if (isValidPassword(password) && password.length()>5) {
            et_password.setError(null);
        } else {
            et_password.setError("Enter a valid password");
            valid = false;
        }
        if (name.isEmpty()) {
            et_name.setError("Name cannot be empty");
            valid = false;
        }else{
            et_name.setError(null);
        }
        if (phone.length() > 10 || phone.length() < 10) {
            et_phone.setError("Invalid phone number");
            valid = false;
        } else {
            et_phone.setError(null);
        }
        return valid;
    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

}


package loginscreen.solution.example.com.loginscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {



  @Override
  public void onCreate(Bundle instanceState) {
    super.onCreate(instanceState);
    setContentView(R.layout.content_welcome);

      TextView tv_name = findViewById(R.id.tv_name);
      TextView tv_email = findViewById(R.id.tv_email);
      TextView tv_phone = findViewById(R.id.tv_phone);

      Intent intent = getIntent();
      String name = intent.getStringExtra("name");
      String email = intent.getStringExtra("email");
      String phone = intent.getStringExtra("phone");

      tv_email.setText(email);
      tv_name.setText(name);
      tv_phone.setText(phone);

  }

}

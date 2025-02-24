package com.example.logingoogle;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Executors;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etEmail;
    private Button btnSendOTP;

    // Lưu OTP tạm thời
    public static String generatedOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmail = findViewById(R.id.etEmail);
        btnSendOTP = findViewById(R.id.btnSendOTP);

        btnSendOTP.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(ForgotPasswordActivity.this, "Vui lòng nhập email!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Generate OTP (6 digits)
            generatedOTP = generateOTP();

            // Send OTP via Email
            sendOTPEmail(email, generatedOTP);

            // Move to ResetPasswordActivity
            Intent intent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
        });
    }

    // Generate a random 6-digit OTP
    private String generateOTP() {
        int otp = 100000 + new Random().nextInt(900000);
        return String.valueOf(otp);
    }

    // Method to send OTP via email using Gmail SMTP
    // Method to send OTP via email using Gmail SMTP (Matching Firebase Configuration)
    private void sendOTPEmail(String recipientEmail, String otp) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                String senderEmail = "yumitlse173238@fpt.edu.vn"; // Your Gmail
                String senderPassword = "dztzdqxcpxstwcfq"; // 🔥 Use App Password (no spaces)

                // SMTP Configuration (Updated to match Firebase)
                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587"); // STARTTLS Port
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true"); // Must use STARTTLS
                props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

                // Session Authentication
                Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new javax.mail.PasswordAuthentication(senderEmail, senderPassword);
                    }
                });

                // Create Email
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(senderEmail));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
                message.setSubject("Your OTP Code");
                message.setText("Your OTP code is: " + otp);

                // Send Email
                Transport.send(message);

                // Show Toast on UI Thread
                runOnUiThread(() -> Toast.makeText(ForgotPasswordActivity.this, "OTP sent successfully!", Toast.LENGTH_LONG).show());

            } catch (MessagingException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(ForgotPasswordActivity.this, "Failed to send OTP. Check credentials.", Toast.LENGTH_LONG).show());
            }
        });
    }
}

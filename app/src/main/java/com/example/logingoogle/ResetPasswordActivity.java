package com.example.logingoogle;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText etOTP, etNewPassword, etConfirmPassword;
    private Button btnResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        etOTP = findViewById(R.id.etOTP);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);

        btnResetPassword.setOnClickListener(v -> {
            String enteredOTP = etOTP.getText().toString().trim();
            String newPassword = etNewPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (enteredOTP.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(ResetPasswordActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(ResetPasswordActivity.this, "Mật khẩu xác nhận không khớp!", Toast.LENGTH_SHORT).show();
                return;
            }

            // So sánh OTP đã nhập với OTP được gửi (lưu tạm trong ForgotPasswordActivity)
            if (!enteredOTP.equals(ForgotPasswordActivity.generatedOTP)) {
                Toast.makeText(ResetPasswordActivity.this, "OTP không đúng!", Toast.LENGTH_SHORT).show();
                return;
            }

            // OTP đúng, thực hiện cập nhật mật khẩu
            updatePassword(newPassword);
        });
    }

    private void updatePassword(String newPassword) {
        // Ở đây bạn cần gọi API server hoặc cập nhật thông tin trong cơ sở dữ liệu người dùng
        Toast.makeText(this, "Mật khẩu đã được thay đổi thành công!", Toast.LENGTH_LONG).show();

        // Sau khi đổi mật khẩu, có thể quay lại màn hình đăng nhập
        Intent intent = new Intent(ResetPasswordActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

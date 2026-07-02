package com.example.simpledictionary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditActivity extends AppCompatActivity {

    private EditText etWord;
    private EditText etMeaning;
    private Button btnSave;
    private TextView tvScreenTitle;

    private int position = -1; // لحفظ موقع العنصر إذا كنا نقوم بالتعديل

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        // ربط العناصر المرئية بالكود
        tvScreenTitle = findViewById(R.id.tv_screen_title);
        etWord = findViewById(R.id.et_word);
        etMeaning = findViewById(R.id.et_meaning);
        btnSave = findViewById(R.id.btn_save);

        // فحص ما إذا كانت هناك بيانات مرسلة للتعديل
        Intent intent = getIntent();
        if (intent.hasExtra("word_model")) {
            // حالة التعديل (Edit Mode)
            tvScreenTitle.setText("تعديل الكلمة");
            WordModel model = (WordModel) intent.getSerializableExtra("word_model");
            position = intent.getIntExtra("position", -1);

            if (model != null) {
                etWord.setText(model.getWord());
                etMeaning.setText(model.getMeaning());
            }
        } else {
            // حالة إضافة جديدة (Add Mode)
            tvScreenTitle.setText("إضافة كلمة جديدة");
        }

        // حدث الضغط على زر الحفظ SAVE
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private void saveData() {
        String word = etWord.getText().toString().trim();
        String meaning = etMeaning.getText().toString().trim();

        // التحقق من أن الحقول ليست فارغة
        if (word.isEmpty() || meaning.isEmpty()) {
            Toast.makeText(this, "الرجاء ملء جميع الحقول", Toast.LENGTH_SHORT).show();
            return;
        }

        // تجهيز الـ Intent لإرجاع البيانات إلى MainActivity
        Intent replyIntent = new Intent();
        WordModel wordModel = new WordModel(word, meaning);
        replyIntent.putExtra("word_model", wordModel);
        replyIntent.putExtra("position", position); // إرجاع الموقع (-1 للإضافة، أو رقم العنصر للتعديل)

        setResult(RESULT_OK, replyIntent);
        finish(); // إغلاق الشاشة والعودة للرئيسية
    }
}

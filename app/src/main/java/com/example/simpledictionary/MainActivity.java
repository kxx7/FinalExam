package com.example.simpledictionary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvDictionary;
    private DictionaryAdapter adapter;
    private ArrayList<WordModel> wordList;
    private FloatingActionButton fabAdd;

    // مُطلق الأنشطة لاستقبال البيانات الراجعة من شاشة الإضافة والتعديل
    private ActivityResultLauncher<Intent> addEditLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. ربط العناصر بالواجهة
        rvDictionary = findViewById(R.id.rv_dictionary);
        fabAdd = findViewById(R.id.fab_add);

        // 2. إدارة البيانات وحالة تدوير الشاشة
        if (savedInstanceState != null) {
            // استرجاع القائمة السابقة إذا تم تدوير الشاشة
            wordList = (ArrayList<WordModel>) savedInstanceState.getSerializable("word_list");
        } else {
            // إنشاء قائمة جديدة وإضافة عينات وهمية كبداية للتطبيق
            wordList = new ArrayList<>();
            wordList.add(new WordModel("Apple", "تفاحة"));
            wordList.add(new WordModel("Car", "سيارة (BMW)"));
            wordList.add(new WordModel("Computer", "حاسوب"));
        }

        // 3. إعداد الـ RecyclerView والـ Adapter
        adapter = new DictionaryAdapter(wordList);
        rvDictionary.setLayoutManager(new LinearLayoutManager(this));
        rvDictionary.setAdapter(adapter);

        // 4. تجهيز الـ Launcher لاستقبال البيانات عند الحفظ
        setupActivityResultLauncher();

        // 5. التعامل مع أحداث الضغط داخل القائمة (الـ Adapter)
        adapter.setOnItemClickListener(new DictionaryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // عند الضغط العادي: فتح شاشة التعديل وتمرير البيانات الحالية والموقع
                Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                intent.putExtra("word_model", wordList.get(position));
                intent.putExtra("position", position);
                addEditLauncher.launch(intent);
            }

            @Override
            public void onItemLongClick(int position) {
                // عند الضغط المطول: حذف الكلمة وإظهار Toast للتنبيه
                String deletedWord = wordList.get(position).getWord();
                wordList.remove(position);
                adapter.notifyItemRemoved(position);
                Toast.makeText(MainActivity.this, "تم حذف الكلمة: " + deletedWord, Toast.LENGTH_SHORT).show();
            }
        });

        // 6. عند الضغط على الزر العائم FAB لطلب إضافة كلمة جديدة
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                addEditLauncher.launch(intent);
            }
        });
    }

    // دالة استقبال ومعالجة البيانات القادمة من شاشة الحفظ والتعديل
    private void setupActivityResultLauncher() {
        addEditLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Intent data = result.getData();
                            WordModel model = (WordModel) data.getSerializableExtra("word_model");
                            int position = data.getIntExtra("position", -1);

                            if (model != null) {
                                if (position == -1) {
                                    // حالة إضافة عنصر جديد تماماً
                                    wordList.add(model);
                                    adapter.notifyItemInserted(wordList.size() - 1);
                                    Toast.makeText(MainActivity.this, "تم إضافة الكلمة بنجاح", Toast.LENGTH_SHORT).show();
                                } else {
                                    // حالة تعديل عنصر موجود مسبقاً
                                    wordList.set(position, model);
                                    adapter.notifyItemChanged(position);
                                    Toast.makeText(MainActivity.this, "تم تحديث الكلمة بنجاح", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
        );
    }

    // حفظ البيانات في الحزمة المخصصة لحالة النشاط عند حدوث تدوير للشاشة
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("word_list", wordList);
    }
}
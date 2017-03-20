package top.resonlei.popmenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import resonlei.top.popmenuview.PopMenu;

public class MainActivity extends AppCompatActivity {
    private PopMenu mPopupMenu;
    private ListView mListView;
    private List<String> mStringList;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStringList = new ArrayList<>();
        for (int i = 0; i < 300; i++) {
            mStringList.add(i+"");
        }

        mPopupMenu = (PopMenu) findViewById(R.id.popmenu);
        mListView = (ListView) findViewById(R.id.listview);

        mListView.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,mStringList));
//        ImageView imageView = new ImageView(this);
//        imageView.setImageResource(R.mipmap.item_screen);
//        imageView.setTag("screen");
//
//        mPopupMenu.addView(imageView);
        //动态设置布局
        mPopupMenu.setMenu(R.mipmap.item_del)
                .setItem(R.mipmap.ic_launcher,"android1")
                .setItem(R.mipmap.ic_launcher,"android2")
                .setItem(R.mipmap.ic_launcher,"android3")
                .setItem(R.mipmap.ic_launcher,"android4")
                .setItem(R.mipmap.ic_launcher,"android5");


        mPopupMenu.setOnMenuItemClickListener(new PopMenu.OnMenuItemClickListener() {
            @Override
            public void onClick(View view, int pos) {
                Toast.makeText(MainActivity.this,view.getTag().toString(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void a(View v){
        Log.e("button","onClick");
    }
}

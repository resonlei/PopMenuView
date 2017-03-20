# PopMenuView
一个简单的卫星菜单控件

## 效果图
![效果图](http://resonlei.top/photo/gif/popmenu.gif "效果图")

## 使用方法
### 配置:
1. 在你的项目的gradle里面天机如下信息：
```gradle
allprojects {
	repositories {
		... //some code
		maven { url 'https://jitpack.io' } //添加这条信息
	}
}
```

2. 在你的module的gradle里面添加依赖：
```gradle
dependencies {
            compile 'com.github.resonlei:PopMenuView:1.0.0'
}
```

### 使用
example.xml
```xml
 <resonlei.top.popmenuview.PopMenu
        android:id="@+id/popmenu"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        resonlei:radius="200dp" 
        resonlei:position="bottom_right">

    <!--
    <ImageView
            android:id="@+id/id_button"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@mipmap/menu" />

    
    <ImageView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:scaleType="centerCrop"
        android:src="@mipmap/item_about"
        android:tag="Camera" />
-->
</resonlei.top.popmenuview.PopMenu>



```
使用说明
```xml
resonlei:radius="200dp" //卫星菜单的半径
resonlei:position="bottom_right" //卫星菜单的位置 ，有以下选择
        <enum name="top_left" value="0"/>
        <enum name="top_right" value="1"/>
        <enum name="bottom_left" value="2"/>
        <enum name="bottom_right" value="3"/>

父布局最好使用RelativeLayout,通过xml配置mentItem请记得添加tag用来进行点击判断。
```


main.java
```java
//动态设置布局
mPopupMenu.setMenu(R.mipmap.item_del)
        .setItem(R.mipmap.ic_launcher,"android1")
        .setItem(R.mipmap.ic_launcher,"android2")
        .setItem(R.mipmap.ic_launcher,"android3")
        .setItem(R.mipmap.ic_launcher,"android4")
        .setItem(R.mipmap.ic_launcher,"android5");

//设置menuItem的点击事件
mPopupMenu.setOnMenuItemClickListener(new PopMenu.OnMenuItemClickListener() {
            @Override
            public void onClick(View view, int pos) {
                Toast.makeText(MainActivity.this,view.getTag().toString()+"clicked",Toast.LENGTH_SHORT).show();
            }
        });
```


### 其他
提供 `mPopupMenu.toggleMenu(int duration)` 方法来控制菜单开关。

推荐使用jitpack进行open source.
使用非常简单，下篇介绍一下，只需要三步就可以发布自己的开源库，相对于jcenter少了很多麻烦还容易出错的步骤。


#### 欢迎改进和完善
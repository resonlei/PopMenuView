package resonlei.top.popmenuview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;


/**
 * Created by Administrator on 2017/3/17.
 */
public class PopMenu extends ViewGroup implements View.OnClickListener {
    public interface OnMenuItemClickListener
    {
        void onClick(View view, int pos);
    }

    /**
     * 回调接口
     */
    private OnMenuItemClickListener onMenuItemClickListener;

    private static final String TAG = "PopMenu";
    /**
     * 菜单的显示位置
     */
    private Position mPosition = Position.BOTTOM_LEFT;

    /**
     * 菜单显示的半径，默认100dp
     */
    private int mRadius = 100;
    /**
     * 用户点击的按钮
     */
    private View mButton;
    /**
     * 当前PopMenu的状态
     */
    private Status mCurrentStatus = Status.CLOSE;

    /**
     * 状态的枚举类
     */
    public enum Status{
        OPEN,CLOSE
    }

    /**
     * 设置菜单现实的位置，四选1，默认右下
     *
     * @author zhy
     */
    public enum Position
    {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTON_RIGHT;
    }


    public PopMenu(Context context) {
        this(context,null);
    }

    public PopMenu(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PopMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取用户设置的半径，
        mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                mRadius, getResources().getDisplayMetrics());
        TypedArray a = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.PopMenu, defStyleAttr, 0);

        //处理自定义属性
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.PopMenu_position) {
                int val = a.getInt(attr, 0);
                switch (val) {
                    case 0:
                        mPosition = Position.TOP_LEFT;
                        break;
                    case 1:
                        mPosition = Position.TOP_RIGHT;
                        break;
                    case 2:
                        mPosition = Position.BOTTOM_LEFT;
                        break;
                    case 3:
                        mPosition = Position.BOTTON_RIGHT;
                        break;
                }

            } else if (attr == R.styleable.PopMenu_radius) {// dp convert to px
                mRadius = a.getDimensionPixelSize(attr, (int) TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100f,
                                getResources().getDisplayMetrics()));

            }
        }
        a.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).measure(MeasureSpec.UNSPECIFIED,
                    MeasureSpec.UNSPECIFIED);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed)
        {

            layoutButton();
            int count = getChildCount();
            /**
             * 设置所有孩子的位置 例如(第一个为按钮)： 左上时，从左到右 ] 第2个：mRadius(sin0 , cos0)
             * 第3个：mRadius(sina ,cosa) 注：[a = Math.PI / 2 * (cCount - 1)]
             * 第4个：mRadius(sin2a ,cos2a) 第5个：mRadius(sin3a , cos3a) ...
             */
            for (int i = 0; i < count - 1; i++)
            {
                View child = getChildAt(i + 1);
                child.setVisibility(View.GONE);

                int cl = (int) (mRadius * Math.sin(Math.PI / 2 / (count - 2)
                        * i));
                int ct = (int) (mRadius * Math.cos(Math.PI / 2 / (count - 2)
                        * i));

                //TODO 处理异常，在只有一个item的时候，防止计算错误
                if (count == 2){
                    cl = (int) (mRadius * Math.sin(Math.PI/4));
                    ct = (int) (mRadius * Math.sin(Math.PI/4));
                }
                Log.e(TAG,cl+":"+ct);



                // childview width
                int cWidth = child.getMeasuredWidth();
                // childview height
                int cHeight = child.getMeasuredHeight();

                // 右上，右下
                if (mPosition == Position.BOTTOM_LEFT
                        || mPosition == Position.BOTTON_RIGHT)
                {
                    ct = getMeasuredHeight() - cHeight - ct;
                }
                // 右上，右下
                if (mPosition == Position.TOP_RIGHT
                        || mPosition == Position.BOTTON_RIGHT)
                {
                    cl = getMeasuredWidth() - cWidth - cl;
                }

                Log.e(TAG, cl + " , " + ct);
                child.layout(cl, ct, cl + cWidth, ct + cHeight);

            }
        }
    }

    private void layoutButton() {
        View cButton = getChildAt(0);

        cButton.setOnClickListener(this);

        int l = 0;
        int t = 0;
        int width = cButton.getMeasuredWidth();
        int height = cButton.getMeasuredHeight();
        switch (mPosition)
        {
            case TOP_LEFT:
                l = 0;
                t = 0;
                break;
            case BOTTOM_LEFT:
                l = 0;
                t = getMeasuredHeight() - height;
                break;
            case TOP_RIGHT:
                l = getMeasuredWidth() - width;
                t = 0;
                break;
            case BOTTON_RIGHT:
                l = getMeasuredWidth() - width;
                t = getMeasuredHeight() - height;
                break;

        }
        Log.e(TAG, l + " , " + t + " , " + (l + width) + " , " + (t + height));
        cButton.layout(l, t, l + width, t + height);
    }

    /**
     * 主菜单按钮的点击事件
     */
    @Override
    public void onClick(View v)
    {
//        mButton = findViewById(R.id.id_button);
//        if (mButton == null)
//        {
//            mButton = getChildAt(0);
//        }
//
//        rotateView(mButton, 0f, 360f, 300);
//        toggleMenu(300);

        mButton = getChildAt(0);
        if (mButton == null){
            return;
        }
        rotateView(mButton, 0f, 360f, 300);
        toggleMenu(300);
    }

    /**
     * 按钮的旋转动画
     *
     * @param view
     * @param fromDegrees
     * @param toDegrees
     * @param durationMillis
     */
    public static void rotateView(View view, float fromDegrees,
                                  float toDegrees, int durationMillis)
    {
        RotateAnimation rotate = new RotateAnimation(fromDegrees, toDegrees,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotate.setDuration(durationMillis);
        rotate.setFillAfter(true);
        view.startAnimation(rotate);
    }

    /**
     * menu菜单的开关
     * @param durationMillis
     */
    public void toggleMenu(int durationMillis)
    {
        int count = getChildCount();
        for (int i = 0; i < count - 1; i++)
        {
            final View childView = getChildAt(i + 1);
            childView.setVisibility(View.VISIBLE);

            int xflag = 1;
            int yflag = 1;

            if (mPosition == Position.TOP_LEFT
                    || mPosition == Position.BOTTOM_LEFT)
                xflag = -1;
            if (mPosition == Position.TOP_LEFT
                    || mPosition == Position.TOP_RIGHT)
                yflag = -1;

            // child left
            int cl = (int) (mRadius * Math.sin(Math.PI / 2 / (count - 2) * i));
            // child top
            int ct = (int) (mRadius * Math.cos(Math.PI / 2 / (count - 2) * i));

            //TODO 处理异常，在只有一个item的时候，防止计算错误
            if (count == 2){
                cl = (int) (mRadius * Math.sin(Math.PI/4));
                ct = (int) (mRadius * Math.sin(Math.PI/4));
            }
            Log.e(TAG,cl+":"+ct);

            AnimationSet animset = new AnimationSet(true);
            Animation animation = null;
            if (mCurrentStatus == Status.CLOSE)
            {// to open
                animset.setInterpolator(new OvershootInterpolator(2F));
                animation = new TranslateAnimation(xflag * cl, 0, yflag * ct, 0);
                childView.setClickable(true);
                childView.setFocusable(true);
            } else
            {// to close
                animation = new TranslateAnimation(0f, xflag * cl, 0f, yflag
                        * ct);
                childView.setClickable(false);
                childView.setFocusable(false);
            }
            animation.setAnimationListener(new Animation.AnimationListener()
            {
                public void onAnimationStart(Animation animation)
                {
                }

                public void onAnimationRepeat(Animation animation)
                {
                }

                public void onAnimationEnd(Animation animation)
                {
                    if (mCurrentStatus == Status.CLOSE)
                        childView.setVisibility(View.GONE);

                }
            });

            animation.setFillAfter(true);
            animation.setDuration(durationMillis);
            // 为动画设置一个开始延迟时间，纯属好看，可以不设
            animation.setStartOffset((i * 100) / (count - 1));
            RotateAnimation rotate = new RotateAnimation(0, 720,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(durationMillis);
            rotate.setFillAfter(true);
            animset.addAnimation(rotate);
            animset.addAnimation(animation);
            childView.startAnimation(animset);
            final int index = i + 1;
            childView.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (onMenuItemClickListener != null)
                        onMenuItemClickListener.onClick(childView, index - 1);
                    menuItemAnin(index - 1);
                    changeStatus();

                }
            });

        }
        changeStatus();
        Log.e(TAG, mCurrentStatus.name() +"");
    }

    /**
     * 更改菜单关闭还是打开状态
     */
    private void changeStatus() {
        mCurrentStatus = (mCurrentStatus == Status.CLOSE? Status.OPEN: Status.CLOSE);
    }

    /**
     * 开始菜单动画，点击的MenuItem放大消失，其他的缩小消失
     * @param item
     */
    private void menuItemAnin(int item){
        for (int i = 0; i < getChildCount() - 1; i++)
        {
            View childView = getChildAt(i + 1);
            if (i == item)
            {
                childView.startAnimation(scaleBigAnim(300));
            } else
            {
                childView.startAnimation(scaleSmallAnim(300));
            }
            childView.setClickable(false);
            childView.setFocusable(false);

        }

    }

    /**
     * 缩小消失动画
     * @param durationMillis
     * @return
     */
    private Animation scaleSmallAnim(int durationMillis){
        Animation anim = new ScaleAnimation(1.0f, 0f, 1.0f, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        anim.setDuration(durationMillis);
        anim.setFillAfter(true);
        return anim;
    }
    /**
     * 放大，透明度降低
     * @param durationMillis
     * @return
     */
    private Animation scaleBigAnim(int durationMillis){
        AnimationSet animationset = new AnimationSet(true);

        Animation anim = new ScaleAnimation(1.0f, 4.0f, 1.0f, 4.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        Animation alphaAnimation = new AlphaAnimation(1, 0);
        animationset.addAnimation(anim);
        animationset.addAnimation(alphaAnimation);
        animationset.setDuration(durationMillis);
        animationset.setFillAfter(true);
        animationset.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                toggleItemVisible(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return animationset;
    }

    /**
     * 设置item的GONE跟VIsiBLE状态
     * @param status
     */
    private void toggleItemVisible(int status) {
        if (status != View.GONE && status != View.VISIBLE){
            throw new IllegalArgumentException("status should be View.VISIBLE or View.GONE");
        }
        int count = getChildCount();
        for (int i = 1; i < count; i++) {
            getChildAt(i).setVisibility(status);
        }
    }

    /**
     * item的点击事件
     * @param onMenuItemClickListener
     */
    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener){
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    /**
     * 动态设置主菜单
     * @return
     */
    public PopMenu setMenu(int iconRes){
        if (getChildCount() > 0){
            //throw new IllegalArgumentException("xml文件已经设置，请不要重复设置");
            Log.e(TAG,"xml文件已经设置，请不要重复设置");
        }else{
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(iconRes);
            imageView.setTag("menu");
            this.addView(imageView,0);
        }
        return this;
    }

    /**
     * 动态设置主菜单项目
     * @param iconRes
     * @param tag
     * @return
     */
    public PopMenu setItem(int iconRes,String tag) {
        if (getChildCount() == 0){
            throw  new IllegalSetException("menu not exist，please use setMenu to set!");
        }else{
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(iconRes);
            imageView.setTag(tag);
            this.addView(imageView);
        }

        return this;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mCurrentStatus == Status.OPEN)
            toggleMenu(300);
        Log.e(TAG,"onTOuchEvent");
        return false;
    }
}

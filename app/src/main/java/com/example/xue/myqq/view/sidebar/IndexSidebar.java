package com.example.xue.myqq.view.sidebar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 侧边索引栏 仅仅是侧边带文字的索引栏而已 中间的提示文本 需要使用 HintSidebar
 *
 * @author Sidney Ding
 * @version 1.0
 * @date 2019/4/21 22:17
 **/
public class IndexSidebar extends View {

    private Paint paint = new Paint();
    private int choose = -1;
    private boolean showBG;
    private static final String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z", "#"};
    private OnChooseLetterChangedListener onChooseLetterChangedListener;

    public IndexSidebar(Context context) {
        super(context);
    }

    public IndexSidebar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IndexSidebar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showBG) {
            canvas.drawColor(Color.parseColor("#D9D9D9"));
        }
        int height = getHeight();
        int width = getWidth();
        // 平均每个字母占的高度
        int singleHeight = height / letters.length;
        for (int i = 0; i < letters.length; i++) {
            paint.setColor(Color.BLACK);
            paint.setAntiAlias(true);
            // 设置字母大小
            paint.setTextSize(25);
            if (choose == i) {
                paint.setColor(Color.parseColor("#FF2828"));
                paint.setFakeBoldText(true);
            }
            float x = width / 2f - paint.measureText(letters[i]) / 2;
            float y = singleHeight * i + singleHeight;
            canvas.drawText(letters[i], x, y, paint);
            paint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float y = event.getY();
        int oldChoose = choose;
        int c = (int) (y / getHeight() * letters.length);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                showBG = true;
                // 停在了不是之前的字母 且有选择字母监听
                if (oldChoose != c && onChooseLetterChangedListener != null) {
                    // 防止越界
                    if (c > -1 && c < letters.length) {
                        onChooseLetterChangedListener.onChooseLetter(letters[c]);
                        choose = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != c && onChooseLetterChangedListener != null) {
                    if (c > -1 && c < letters.length) {
                        onChooseLetterChangedListener.onChooseLetter(letters[c]);
                        choose = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                showBG = false;
                choose = -1;
                if (onChooseLetterChangedListener != null) {
                    onChooseLetterChangedListener.onNoChooseLetter();
                }
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 不是很关注点击事件 所以不用重写 performClick()
        return super.onTouchEvent(event);
    }

    public void setOnTouchingLetterChangedListener(OnChooseLetterChangedListener listener) {
        this.onChooseLetterChangedListener = listener;
    }

    public interface OnChooseLetterChangedListener {
        void onChooseLetter(String s);
        void onNoChooseLetter();
    }
}

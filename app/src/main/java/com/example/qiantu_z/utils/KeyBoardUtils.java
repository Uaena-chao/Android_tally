package com.example.qiantu_z.utils;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.example.qiantu_z.R;

public class KeyBoardUtils {
    private final Keyboard k1;               //自定义键盘
    OnEnsureListener onEnsureListener;
    private KeyboardView keyboardView;
    private EditText editText;
    KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = editText.getText();
            int start = editText.getSelectionStart();
            switch (primaryCode) {
                case Keyboard.KEYCODE_DELETE:   //删除
                    if (editable != null && editable.length() > 0)
                        if (start > 0)
                            editable.delete(start - 1, start);
                    break;
                case Keyboard.KEYCODE_CANCEL:   //清零
                    editable.clear();
                    break;
                case Keyboard.KEYCODE_DONE:     //确定
                    onEnsureListener.onEnsure();   //通过接口回调的方法，当点击确定，可以调用这个方法
                    break;

                default:
                    editable.insert(start, Character.toString((char) primaryCode));
                    break;


            }

        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };


    public KeyBoardUtils(KeyboardView keyboardView, EditText editText) {
        this.keyboardView = keyboardView;
        this.editText = editText;
        this.editText.setInputType(InputType.TYPE_NULL); //取消弹出系统键盘
        k1 = new Keyboard(this.editText.getContext(), R.xml.key);

        this.keyboardView.setKeyboard(k1);                        // 设计键盘的样式i
        this.keyboardView.setEnabled(true);
        this.keyboardView.setPreviewEnabled(false);
        this.keyboardView.setOnKeyboardActionListener(listener);   //设置键盘被点击的监听

    }

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    //    显示键盘的方法
    public void showKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.INVISIBLE || visibility == View.GONE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }

    //    隐藏键盘的方法
    public void hideKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.INVISIBLE || visibility == View.VISIBLE) {
            keyboardView.setVisibility(View.GONE);
        }
    }

    public interface OnEnsureListener {
        public void onEnsure();
    }


}

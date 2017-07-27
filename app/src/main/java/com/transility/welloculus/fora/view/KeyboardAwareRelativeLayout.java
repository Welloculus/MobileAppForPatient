package com.transility.welloculus.fora.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.transility.welloculus.fora.constant.PCLinkLibraryDemoConstant;
import com.transility.welloculus.fora.util.GuiUtils;

/**
 * Input {@link RelativeLayout}
 * <p>
 * 為了能夠偵測鍵盤在顯示時，被按下隱藏鍵盤圖示(EX: sony的手機, htc sensation, etc....)後，做一些處理的自訂RelativeLayout
 * 
 * @author Jay Lee
 * 
 */
public class KeyboardAwareRelativeLayout extends RelativeLayout {

	public static final byte KEYBOARD_STATE_SHOW = -3;
	public static final byte KEYBOARD_STATE_HIDE = -2;
	public static final byte KEYBOARD_STATE_INIT = -1;
	
	private boolean mHasInit = false;
	private boolean mHasKeyboard = false;
	private int mHeight;
	
    private OnKeyboardHiddenListener mCallBack;
    private OnKeyboardStateChangedListener onKeyboardStateChangedListener;
    private OnLayoutListener onLayoutListener;
    
    /**
     * 鍵盤被隱藏的Listener
     * 
     */
    public interface OnKeyboardHiddenListener {

        /**
         * 當鍵盤被點選穩藏時要執行的動作
         */
        public void onKeyboardHidden();
    }

    public interface OnLayoutListener {
		public void onLayoutChanged(boolean changed, int l, int t, int r, int b);
	}
    
    public interface OnKeyboardStateChangedListener {
		public void onKeyboardStateChanged(int state);
	}
    
    private boolean mKeyboardVisible = false;

    /**
     * Construct a KeyboardAwareRelativeLayout object.
     * 
     * @param context
     *            Context
     */
    public KeyboardAwareRelativeLayout(Context context) {
        super(context);
    }

    /**
     * Construct a KeyboardAwareRelativeLayout object.
     * 
     * @param context
     *            Context
     * @param attrs
     *            AttributeSet
     */
    public KeyboardAwareRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Construct a KeyboardAwareRelativeLayout object.
     * 
     * @param context
     *            Context
     * @param attrs
     *            AttributeSet
     * @param defStyle
     *            Default style
     */
    public KeyboardAwareRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setKeyboardVisible(boolean keyboardVisible) {
        this.mKeyboardVisible = keyboardVisible;
    }

    @Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		
		if(!mHasInit) {
			mHasInit = true;
			mHeight = b;
			if(onKeyboardStateChangedListener != null) {
				onKeyboardStateChangedListener.onKeyboardStateChanged(KEYBOARD_STATE_INIT);
			}
		} else {
			mHeight = mHeight < b ? b : mHeight;
		}
		
		if(mHasInit && mHeight > b) {
			mHasKeyboard = true;
			if(onKeyboardStateChangedListener != null) {
				onKeyboardStateChangedListener.onKeyboardStateChanged(KEYBOARD_STATE_SHOW);
			}
		}
		if(mHasInit && mHasKeyboard && mHeight == b) {
			mHasKeyboard = false;
			if(onKeyboardStateChangedListener != null) {
				onKeyboardStateChangedListener.onKeyboardStateChanged(KEYBOARD_STATE_HIDE);
			}
		}
		
		if(onLayoutListener != null) {
			onLayoutListener.onLayoutChanged(changed, l, t, r, b);
		}
	}

	/**
     * 定義當隱藏鍵盤的Listener
     * 
     * @param listener
     *            OnKeyboardHiddenListener
     */
    public void setOnKeyboardHiddenListener(OnKeyboardHiddenListener listener) {
        mCallBack = listener;
    }

    public void setOnKeyboardStateChangedListener(OnKeyboardStateChangedListener listener) {
		onKeyboardStateChangedListener = listener;
	}
    
    public void setOnLayoutChangedListener(OnLayoutListener listener) {
		onLayoutListener = listener;
	}
    
    /**
     * 判斷鍵盤是否開啟或關閉
     */
    private void updateKeyboardVisible() {
        // FIXME 此為目前可以暫時判斷軟體鍵盤隱藏的方式，但不見得百分之百能成功。
        int viewHeight = getHeight();
        int displayHeight = GuiUtils.getScreenResolution(getContext()).get(
        		PCLinkLibraryDemoConstant.SCREEN_HEIGHT);
        // 計算時，需要包含Status Bar，而Status Bar的高度為25dp
        // 需要利用程式動態計算出來
        int maxStatusBarHeight = GuiUtils.getStatusBarHeight(getContext());
        boolean oldValue = mKeyboardVisible;
        mKeyboardVisible = (viewHeight + maxStatusBarHeight < displayHeight);
        if (oldValue != mKeyboardVisible && !mKeyboardVisible && mCallBack != null) {
            mCallBack.onKeyboardHidden();
        } /* end of if */
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (!this.isInEditMode()) {
            updateKeyboardVisible();
        } /* end of if */
    }

}

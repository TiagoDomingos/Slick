package test;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.mrezanasirloo.slick.OnDestroyListener;
import com.mrezanasirloo.slick.Presenter;

public class ExampleCustomView extends LinearLayout implements ExampleView, OnDestroyListener {

    @Presenter
    ExamplePresenter presenter;

    public ExampleCustomView(Context context) {
        super(context);
    }

    public ExampleCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ExampleCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ExamplePresenter_Slick.bind(this, 1, 2f);
        ExamplePresenter_Slick.onAttach(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ExamplePresenter_Slick.onDetach(this);
    }

    @Override
    public void onDestroy() {
        ExamplePresenter_Slick.onDestroy(this);
    }
}
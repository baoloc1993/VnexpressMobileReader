package ngo.vnexpress.reader.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class LoadingRecyclerView extends RelativeLayout implements LoadingComponent {
    public ProgressBar progressBar;
    public RecyclerView recyclerView;

    public LoadingRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public LoadingRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LoadingRecyclerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public LoadingRecyclerView(Context context) {
        super(context);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initialize(this.getContext());
    }

    public void initialize(Context context){


        for(int i = 0 ; i < this.getChildCount();i++){
            View child = this.getChildAt(i);
            if(child instanceof RecyclerView){
                this.recyclerView = (RecyclerView) child;

            }
            if(child instanceof ProgressBar){
                this.progressBar = (ProgressBar) child;
            }
        }
        progressBar.setVisibility(GONE);
    }
    @Override
    public void showLoading() {
        recyclerView.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
    }

    @Override
    public void hideLoading() {
        recyclerView.setVisibility(VISIBLE);
        progressBar.setVisibility(GONE);
    }
}

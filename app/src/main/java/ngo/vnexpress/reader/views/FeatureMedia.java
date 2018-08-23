package ngo.vnexpress.reader.views;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.*;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;
import ngo.vnexpress.reader.MainActivity;
import ngo.vnexpress.reader.R;

import static android.graphics.Color.GREEN;

public class FeatureMedia extends RelativeLayout {
    private float ratio;
    private boolean isVideo;
    private ImageView videoThumbnail;
    private ImageView imageView;
    private String mediaUrl;
    
    public FeatureMedia(Context context) {
        super(context);
        ini();
    }
    
    public FeatureMedia(Context context, AttributeSet attrs) {
        super(context, attrs);
        ini(attrs);
    }
    public FeatureMedia(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ini(attrs);
    }
    
    private void ini(){
        imageView = new ImageView(getContext());
        imageView.setId(generateViewId());
        RelativeLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(layoutParams);
        videoThumbnail = new ImageView(getContext());
        videoThumbnail.setId(generateViewId());
        RelativeLayout.LayoutParams layoutParams1 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        videoThumbnail.setOnClickListener(v -> {
            this.playVideo();
        });
        
        videoThumbnail.setLayoutParams(layoutParams1);
        
        setRatio(-1);
        setVideo(false);
        
        this.addView(videoThumbnail);
        this.addView(imageView);
    }
    
    private void playVideo() {
        
        VideoPlayerDialog videoPlayerDialog = new VideoPlayerDialog(mediaUrl);
        MainActivity context = (MainActivity) getContext();
        videoPlayerDialog.show(context.getSupportFragmentManager(),"VideoPlayerManager");
        
    }
    
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        
    }
    
    
    
    private void ini(AttributeSet attributeSet){
        ini();
        TypedArray a = getContext().obtainStyledAttributes(attributeSet,R.styleable.FeatureMedia );
        setRatio(a.getFloat(R.styleable.FeatureMedia_ratio, 1f));
        setVideo(a.getBoolean(R.styleable.FeatureMedia_isVideo,false));
    }
    
    public boolean isVideo() {
        return isVideo;
    }
    
    
    
    private void setVideo(boolean video) {
        isVideo = video;
        if(isVideo){
            videoThumbnail.setVisibility(VISIBLE);
            imageView.setVisibility(GONE);
        }else{
            videoThumbnail.setVisibility(GONE);
            imageView.setVisibility(VISIBLE);
        }
    }
    
    private void addPlayPauseButton() {
    }
    
    public float getRatio() {
        return ratio;
    }
    
    public void setRatio(float ratio) {
        this.ratio = ratio;
    }
    
    public ImageView getVideoView() {
        return videoThumbnail;
    }
    
    public ImageView getImageView() {
        return imageView;
    }
    
    public String getMediaUrl() {
        return mediaUrl;
    }
    
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.i(this.getClass().getName(),"Drawing media: "+mediaUrl);
        int width = this.getMeasuredWidth();
        int height = (int) (this.getMeasuredWidth()/ratio);
        ImageView target;
        RequestCreator requestCreator;
        if(isVideo){
            target = videoThumbnail;
            requestCreator = Picasso.get().load(R.drawable.google_plus_icon);
        }else{
            target = imageView;
            requestCreator = Picasso.get().load(mediaUrl);
        }
        requestCreator.into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.i(this.getClass().getName(),"drawn: "+mediaUrl);
                int bitmapHeight = height;
                int bitmapWidth = width;
                if(ratio > 0){
                
                }else{
                    bitmapHeight = (int) (bitmap.getHeight()*width*1f/bitmap.getWidth());
                }
                bitmap = Bitmap.createScaledBitmap(bitmap,bitmapWidth,bitmapHeight,false);
            
                target.setImageBitmap(bitmap);

//                        imageView.setLayoutParams(new LayoutParams(width,height));
            
            }
        
            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Log.e(this.getClass().getName(),"drawn failed: "+mediaUrl);
                imageView.setVisibility(GONE);
            }
        
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            
            }
        });
    }
    
    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
        if(this.mediaUrl.matches("(http(s?):)([/|.|\\w|\\s|-])*\\.(?:jpg|gif|png)")){
            this.setVideo(false);
        }else{
            if(this.mediaUrl.contains("youtube.com")){
                String videoId = this.mediaUrl.split("v=")[1];
                this.mediaUrl = "https://img.youtube.com/vi/"+videoId+"/0.jpg";
                this.setVideo(false);
            }else{
                this.setVideo(true);
            }
            
        }
        
    }
}

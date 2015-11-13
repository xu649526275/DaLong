package com.cjj.refresh;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.TextView;




/**
 * Created by cjj on 2015/8/4.
 */
public class BeautifulRefreshLayout extends RefreshLayout {
    private float waveHeight = 120;
    private float headHeight = 60;

    public BeautifulRefreshLayout(Context context) {
        this(context, null, 0);
    }

    public BeautifulRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BeautifulRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    /**
     * 初始化
     */
    private void init(AttributeSet attrs) {
        /**
         * attrs  需要在xml设置什么属性  自己自定义吧  啊哈哈
         */

        /**
         * 初始化headView
         */
        final View headView = LayoutInflater.from(getContext()).inflate(R.layout.view_head, null);
        final WaveView waveView = (WaveView) headView.findViewById(R.id.draweeView);
        final ImageView meishi = (ImageView) headView.findViewById(R.id.meishi);
        final TextView tv_tip = (TextView) headView.findViewById(R.id.tv_tip);
        final ImageView draweeView = (ImageView) headView.findViewById(R.id.text_loading);

        /**
         * 设置波浪的高度
         */
        setWaveHeight(DensityUtil.dip2px(getContext(), waveHeight));
        /**
         * 设置headView的高度
         */
        setHeaderHeight(DensityUtil.dip2px(getContext(), headHeight));
        /**
         * 设置headView
         */
        setHeaderView(headView);
        /**
         * 监听波浪变化监听
         */
        setPullWaveListener(new PullWaveListener() {
            @Override
            public void onPulling(RefreshLayout refreshLayout, float fraction) {
                draweeView.setVisibility(View.GONE);
                Log.v("dalongsss",fraction+"");
                float headW = DensityUtil.dip2px(getContext(), waveHeight);
                waveView.setHeadHeight((int) (DensityUtil.dip2px(getContext(), headHeight) * limitValue(1, fraction)));
                waveView.setWaveHeight((int) (headW * Math.max(0, fraction - 1)));
                waveView.invalidate();


                /**
                 *下拉刷新不松手的事件
                 * */
                Log.v("dalongs", headW + "     " + fraction);
                meishi.setVisibility(View.VISIBLE);
                meishi.setScaleX(fraction / 2);
                meishi.setScaleY(fraction/2);

                float pullDownY=headW * Math.max(0, fraction - 1);
                if(pullDownY<=DensityUtil.dip2px(getContext(), 104)||pullDownY>=DensityUtil.dip2px(getContext(), 107)){
                meishi.setImageResource(R.drawable.pull_image);
                }
                if(pullDownY>=DensityUtil.dip2px(getContext(), 107)&&pullDownY<=DensityUtil.dip2px(getContext(), 110)){
                    meishi.setImageResource(R.drawable.pull_end_image_frame_01);
                }
                if(pullDownY>=DensityUtil.dip2px(getContext(), 110)&&pullDownY<=DensityUtil.dip2px(getContext(), 114)){
                    meishi.setImageResource(R.drawable.pull_end_image_frame_02);
                }
                if(pullDownY>=DensityUtil.dip2px(getContext(), 114)&&pullDownY<=DensityUtil.dip2px(getContext(),117)){
                    meishi.setImageResource(R.drawable.pull_end_image_frame_03);
                }
                if(pullDownY>=DensityUtil.dip2px(getContext(), 117)&&pullDownY<=DensityUtil.dip2px(getContext(), 120)){
                    meishi.setImageResource(R.drawable.pull_end_image_frame_04);
                }
                if(pullDownY>=DensityUtil.dip2px(getContext(), 120)) {
                    meishi.setImageResource(R.drawable.pull_end_image_frame_05);
                }

            //    tv_tip.setVisibility(View.VISIBLE);
         /*   if(headW==headW * Math.max(0, fraction - 1))
                {
                    meishi.setVisibility(View.VISIBLE);
                    tv_tip.setVisibility(View.VISIBLE);
                }else
                {
                    meishi.setVisibility(View.GONE);
                    tv_tip.setVisibility(View.GONE);
                }*/
            }

            @Override
            public void onPullReleasing(RefreshLayout refreshLayout, float fraction) {
                if (!refreshLayout.isRefreshing) {
                    /**
                     * 成功了
                     * */
                    draweeView.setVisibility(View.GONE);
                }
            }
        });

        /**
         * 松开后的监听
         */
        setPullToRefreshListener(new PullToRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {

                waveView.setHeadHeight((int) (DensityUtil.dip2px(getContext(), headHeight)));
                ValueAnimator animator = ValueAnimator.ofInt(waveView.getWaveHeight(), 0);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        waveView.setWaveHeight((int) animation.getAnimatedValue());
                        waveView.invalidate();
                    }
                });
                animator.setInterpolator(new BounceInterpolator());
                animator.setDuration(300);
                animator.start();
                refreshLayout.postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {

                                /**
                                 * 松开手监听的事件
                                 * */
                                meishi.setVisibility(View.GONE);
                                tv_tip.setVisibility(View.GONE);
                                draweeView.setVisibility(View.VISIBLE);
                            }
                        }, 300
                );

                if(listener!=null)
                {
                    listener.onRefresh(BeautifulRefreshLayout.this);
                }
            }
        });

    }


    /**
     * 限定值
     * @param a
     * @param b
     * @return
     */
    public float limitValue(float a, float b) {
        float valve = 0;
        final float min = Math.min(a, b);
        final float max = Math.max(a, b);
        valve = valve > min ? valve : min;
        valve = valve < max ? valve : max;
        return valve;
    }

    public interface BuautifulRefreshListener {
        void onRefresh(BeautifulRefreshLayout refreshLayout);
    }

    private BuautifulRefreshListener listener;
    public void setBuautifulRefreshListener(BuautifulRefreshListener listener)
    {
        this.listener = listener;
    }
}

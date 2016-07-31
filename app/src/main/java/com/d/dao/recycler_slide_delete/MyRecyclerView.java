package com.d.dao.recycler_slide_delete;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * Created by dao on 7/30/16.
 */
public class MyRecyclerView extends RecyclerView {



    private int mTouchSlop;//多少算是发生了滑动

    private int maxLength;//滑动的最大距离,超出不再增加

    private Scroller mScroller;//
    private LinearLayout.LayoutParams mLayoutParams;

    public MyRecyclerView(Context context) {
        this(context, null);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //滑动到最小距离
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();//48
        //滑动的最大距离
        maxLength = ((int) (100 * context.getResources().getDisplayMetrics().density + 0.5f));
        mScroller = new Scroller(context, new LinearInterpolator(context, null));
    }


    private int downX;//按下的X
    private int downY;//按下的Y

    private Rect mTouchFrame = new Rect();

    private int pos;//数据的position

    private LinearLayout root;//根布局
    private TextView tv;//第几页
    private TextView delete;//删除

    private int moveX, moveY;// action_move时的坐标

    private int lastMoveX;//上次点击的X
    private int lastMoveY;//上次点击的Y

    private int totalDeltaY = 0;//记录总的偏移Y
    private int totalDeltaX = 0;//记录总的偏移X

    private boolean isMoving = false;//是否在移动

    private boolean isFirst = true;//用来标记  按下后是否区分出水平位移还是垂直位移

    private boolean sendToSuper = true;//是否交给super处理

    @Override
    public boolean onTouchEvent(MotionEvent e) {


        //按下的位置
        int x = (int) e.getX();
        int y = (int) e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (isMoving) {
                    return true;
                }
                //跟QQ效果一样,如果有delete显示出来,点击其他地方消失
                if (root != null) {
                    if (delete != null) {
                        Rect rect = new Rect();
                        delete.getHitRect(rect);
                        if (rect.contains(downX, downY)) {
                            return super.onTouchEvent(e);
                        } else {
                            int scrollX = root.getScrollX();
                            if (root.getScrollX() == maxLength) {
//                                root.scrollBy(-maxLength, 0);
                                mScroller.startScroll(scrollX, 0, -scrollX, 0, Math.abs(scrollX) / maxLength * 100);
                                invalidate();
                                return super.onTouchEvent(e);
                            }
                        }
                    }
                }

                //初始化
                sendToSuper = false;
                isMoving = false;
                isFirst = true;

                totalDeltaY = 0;
                totalDeltaX = 0;

                //记录坐标
                downX = x;
                downY = y;
                lastMoveX = x;
                lastMoveY = y;

                //通过点击的坐标计算当前的position

                int mFirstPosition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();//找到第一个可见的item
                Rect frame = mTouchFrame;
                int childCount = getChildCount();//当前可见的child数目


                //这里关于pos的处理可以去掉,没有用到,只用到了i
                for (int i = 0; i < childCount; i++) {
                    View child = getChildAt(i);
                    if (child.getVisibility() == VISIBLE) {
                        child.getHitRect(frame);//获取整个矩形区域
                        if (frame.contains(downX, downY)) {
                            pos = mFirstPosition + i;//获取到position,可以根据需求进行处理
                        }
                    }
                }

                //通过position得到item的viewHolder和控件
                View view = getChildAt(pos - mFirstPosition);// pos-mFirstPosition就是i
                FirstAdapter.TextHolder viewHolder = (FirstAdapter.TextHolder) getChildViewHolder(view);

                root = viewHolder.root;
                tv = viewHolder.tv;

                delete = viewHolder.delete;

                break;

            case MotionEvent.ACTION_MOVE:

                //记录坐标
                moveX = x;
                moveY = y;

                int deltaX = x - lastMoveX;//每一次的位移
                int deltaY = y - lastMoveY;

                totalDeltaX += Math.abs(deltaX);//总位移等于每一次的位移相加
                totalDeltaY += Math.abs(deltaY);

                Log.e("deltaX", "" + deltaX);
                Log.e("deltaY", "" + deltaY);


                if (isFirst) {// 移动的第一下,可能都是x,y方向的位移都是0,这个时候无法位移方向区分到底是竖直还是水平,不处理,等下一个X,Y再进行处理
                    if (totalDeltaX == 0 && totalDeltaY == 0) {//如果都等于0,没分出来是水平还是竖直移动,不处理
                        Log.e("move", "是第一下,但是分不出来,稍后");
                    } else {
                        isFirst = false;
                        Log.e("deltaY", "" + Math.abs(deltaY));
                        Log.e("deltaX", "" + Math.abs(deltaX));
                        if (Math.abs(deltaY) < Math.abs(deltaX)) {//X轴的偏移量大于y轴,认为水平滑动
                            Log.e("move", "是第一下,认为X");
                            isMoving = true;
                            sendToSuper = false;
                        } else {//认为竖直滑动,交给super
                            Log.e("move", "是第一下,认为Y");
                            isMoving = false;
                            sendToSuper = true;
                        }
                    }
                } else {//不是第一下,也就是已经判断出方向
                    if (isMoving) {//水平滑动开始
                        sendToSuper = false;
                        if (root.getScrollX() > maxLength || root.getScrollX() < 0) {
                        } else if (root.getScrollX() == maxLength && deltaX < 0) {
                        } else if (root.getScrollX() == 0 && deltaX > 0) {
                        } else if (Math.abs(deltaX) > mTouchSlop) {
                            if (root.getScrollX() - deltaX > maxLength) {
                                root.scrollBy(maxLength - root.getScrollX(), 0);
                            } else if (root.getScrollX() - deltaX < 0) {
                                root.scrollBy(-root.getScrollX(), 0);
                            } else {
                                root.scrollBy(-deltaX, 0);
                            }
                            lastMoveX = moveX;
                            lastMoveY = moveY;
                        } else {
                        }
                    } else {//竖直滑动
//                        sendToSuper = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:

                int scrollX = root.getScrollX();
                Log.e("scrollX", "" + scrollX);//117
                if (Math.abs(scrollX) > maxLength / 2) {//向左
                    mScroller.startScroll(scrollX, 0, maxLength - scrollX, 0, (maxLength - scrollX) / maxLength * 1500 < 100 ? 100 : (maxLength - scrollX) / maxLength * 1500);
                    invalidate();
                    isMoving = false;
                } else {//向右
                    mScroller.startScroll(scrollX, 0, -scrollX, 0, Math.abs(scrollX) / maxLength * 1500 <100 ? 100:Math.abs(scrollX) / maxLength * 1500);
                    invalidate();
                    isMoving = false;
                }
                break;
        }
        if (!sendToSuper) {//如果正在滑动,直接屏蔽事件
            return true;
        } else {
            return super.onTouchEvent(e);
        }
    }

    //必须重写
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            root.scrollTo(mScroller.getCurrX(), 0);
            invalidate();
        }
    }

    //删除前关闭
    public void close() {
        root.scrollBy(-maxLength, 0);
    }


}

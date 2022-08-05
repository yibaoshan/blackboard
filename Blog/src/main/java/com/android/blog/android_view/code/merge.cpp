//
// Created by Bob on 2022/7/30.
//

#include "merge.h"

SurfaceFlinger {
    frameworks/native/services/surfaceflinger/MessageQueue.cpp
        ->init()
        ->waitMessage()
        ->invalidate()
}

#setView流程

//三个阶段：创建APP进程、加载视图文件、将视图文件传递给为wms、sf进程的连接

/*

在第一个阶段，ActivityThread.main()
ActivityThread，入口函数中初始化handler机制
ApplicationThread，ams传话筒
Activity，开发者的主战场
PhoneWindow，空壳子
WindowManager，wms的代理对象

在第二个阶段，Activity.setContentView
调用PhoneWindow设置视图
根据不同主题，为DecorView设置不同的子View（无论使用哪种主题视图，其中必然包含名为content的FrameLayout）
将开发者的布局文件添加到子View名为content的FrameLayout当中
为PhoneWindow设置DecorView

**/

/frameworks/base/core/java/android/app/ActivityThread.java
class ActivityThread {

    //zygote进程fork成功后调用入口函数
    void main(){
        Looper.prepareMainLooper();
        attach();//attach方法和ams建立连接，提供给ams控制四大组件的句柄
        Looper.loop();
    }

    //分两步解释更容易理解
    //1. 不管是从桌面点击图标进入还是adb命令启动，最终都交由ams发送启动请求给zygote进程，接着zygote孵化出该APP进程调用main方法
    //2. APP进程启动将创建ApplicationThread对象，并发起IPC把此对象传递给ams，此后四大组件相关回到都将有ApplicationThread对象负责，最终转发给H类执行
    void attach() {
        //获取ams代理并将ApplicationThread将给ams，这个对象以后将是ams的传声筒
        IActivityManager mgr = ActivityManagerNative.getDefault();
        mgr.attachApplication(new ApplicationThread());
    }

    //ApplicationThreadNative封装一系列的关于四大组件回调方法的跨进程通信命令
    //ApplicationThread对象所有操作几乎都由AMS发起调用
    class ApplicationThread extends ApplicationThreadNative {

        void scheduleLaunchActivity(){
            handleMessage(LAUNCH_ACTIVITY);
        }

    }

    class H extends Handler {

        //转发来自ApplicationThread的消息
        void handleMessage(Message msg) {
            case LAUNCH_ACTIVITY::handleLaunchActivity();
            case RESUME_ACTIVITY::handleResumeActivity();
        }

        //转发来自handleMessage的消息
        void handleLaunchActivity(){
            performLaunchActivity();
            handleResumeActivity();
        }

        //转发来自handleMessage的消息
        void handleResumeActivity(){
            performResumeActivity()
            activity.makeVisible();//调用此方法说明第二阶段视图加载已经完成，准备提交到wms服务
        }

        //执行创建Activity对象并回调生命周期
        Activity performLaunchActivity(){
            Activity activity = new Activity();
            activity.attach();//回调attach
            activity.onCreate();//回调Activity
            return activity;
        }

        //执行回调生命周期
        void performResumeActivity(){
            activity.onResume();
        }

    }

}

/frameworks/base/core/java/android/app/Activity.java
class Activity {

    View mDecor;//用户设置的跟视图，通常会在ActivityThread中被赋值
    Window mWindow;//Activity首次被创建调用attach()方法时同步创建，创建动作在Activity
    WindowManager  mWindowManager;

    //1. 创建PhoneWindow保存到变量mWindow，此时的Window还没有View视图
    //2. 获取wms代理对象，塞到刚刚创建的window对象当中，同时保存到本地mWindowManager变量
    void attach(Window window){
        mWindow = new PhoneWindow(this, window);
        mWindow.setWindowManager(getSystemService(Context.WINDOW_SERVICE));
        mWindowManager = mWindow.getWindowManager();//获取WindowManager动作在Activity中，获取完成接着设置给自己的局部变量，这我是真的没想到，找的好辛苦
    }

    //至此，APP进程启动成功，第一阶段结束，准备进入第二阶段
    void onCreate(){
        setContentView();
    }

    //第二阶段开始：加载视图文件并绑定到DecorView
    void setContentView(View view) {
        mWindow.setContentView(view);
    }

    //第二阶段已经完成，准备进入第三阶段
    void onContentChanged(){
    }

    //第三阶段开始：将视图传递给wms
    //makeVisible()在ActivityThread.H.handleResumeActivity()方法中被调用
    //此阶段完成后会请求vsync信号，并在下一次vsync到来时绘制View树，在下下次sf进程合成，在下下下次展示给用户，整个流程如下：
    //vsync->view.draw()
    //     vysnc->sf.compose()
    //          vsync->drm.flip() 用户可以看到
    void makeVisible() {
        mWindowManager.addView(mDecor);
    }

}

/frameworks/base/core/java/com/android/internal/policy/PhoneWindow.java
class PhoneWindow extends Window {

    DecorView mDecor;
    ViewGroup mContentParent;

    //1. 创建DecorView对象
    //2. 将开发者设置的视图文件作为子View添加到mContentParent
    //3. 通知Activity中onContentChanged方法
    void setContentView(View view) {
        mDecor = generateDecor();
        mContentParent = generateLayout();//看generateLayout方法的注释
        mContentParent.addView(view);//将开发者设置的视图添加为子View
        getCallback().onContentChanged();//回调Activity中onContentChanged()方法
    }

    //创建一个空的DecorView，也就是FrameLayout，里面啥也没有
    void generateDecor() {
        return new DecorView(this);
    }

    //1. 根据不同主题设置不同布局文件，加载该布局文件并设置成DecorView的子View
    //2. 返回子View中id为content的ViewGroup，通常还是个FrameLayout
    //以上两步执行完成以后，DecorView的布局变成：
    //<FrameLayout>//DecorView的根布局
    //  <LinearLayout>//开发者设置带有ActionBar的主题，注意，这里的视图可变的，根据主题来选择不同的视图
    //      <ActionBar/>
    //      <FrameLayout
    //      android:id="@android:id/content"/>//这里的FrameLayout才是最终包含开发者在setContentView中设置的布局
    //  </LinearLayout>
    //</FrameLayout>
    void generateLayout() {
        //加载不同的theme主题的布局文件，比如我们在xml中指定android:theme=@style/NoActionBar
        View root = inflater.inflate(layoutResource);
        //将上一步解析的视图作为根布局添加到DecorView，常见的比如垂直方向的LinearLayout，这样布局DecorView
        mDecor.addView(root);
        //找到用来装用户视图的ViewGroup，通常还是个FrameLayout
        ViewGroup contentParent = mDecor.findViewById(R.id.content);
        return contentParent;
    }

}

//自身无逻辑，可以跳过
/frameworks/base/core/java/com/android/internal/policy/DecorView.java
class DecorView extends FrameLayout {

    //DecorView和PhoneWindow互相持有，这代码写的，啧啧啧
    PhoneWindow mWindow;

    DecorView(PhoneWindow window){
        mWindow = window;
    }

}

//定义View操作接口，顶级接口
/frameworks/base/core/java/android/view/ViewManager.java
public interface ViewManager
{
    public void addView();
    public void updateViewLayout();
    public void removeView();
}

//啥也不是
/frameworks/base/core/java/android/view/WindowManager.java
public interface WindowManager extends ViewManager {

}

//WindowManager的最终实现
/frameworks/base/core/java/android/view/WindowManagerImpl.java
public class WindowManagerImpl implements WindowManager {

    WindowManagerGlobal mGlobal = WindowManagerGlobal.getInstance();

    void addView(View decorView) {
        mGlobal.addView(decorView);
    }

}

//全局单例，和WMS建立连接通信，也是APP进程中，所有窗口实际的管理者
//内部mViews和mRoots变量保存着所有创建的Activity对应的View和ViewRootImpl
class WindowManagerGlobal {

	List<View> mViews;
	List<ViewRootImpl> mRoots;

	void addView(View decorView){
		ViewRootImpl root = new ViewRootImpl(decorView);
		mViews.add(decorView);
		mRoots.add(root);
	}

}

//对应一个Activity
class ViewRootImpl {

    View mView;//保存DecorView

    final Surface mSurface = new Surface();

    void setView(View decorView){
        mView = decorView;//将DecorView保存到ViewRootImpl的成员变量mView中
        requestLayout();//请求vsync信号
        //通过binder向wms添加窗口
      	res = mWindowSession.addToDisplay();
    }

    void requestLayout() {
        checkThread();
        scheduleTraversals();
    }

    //请求vsync，等待刷新
    void scheduleTraversals() {
        mTraversalBarrier = mHandler.getLooper().getQueue().postSyncBarrier();//创建一个同步屏障（详见Android消息机制）
        mChoreographer.postCallback(Choreographer.CALLBACK_TRAVERSAL, mTraversalRunnable, null);//发送一条异步消息，mTraversalRunnable是处理这条消息的回调
        notifyRendererOfFramePending();
        pokeDrawLockIfNeeded();
    }

    //开始刷新
    void doTraversal() {
        if (mTraversalScheduled) {
            mTraversalScheduled = false;
            mHandler.getLooper().getQueue().removeSyncBarrier(mTraversalBarrier);//移除同步屏障
            performTraversals();//View的绘制起点

        }
    }

    //源码超级长
    void performTraversals(){
        performMeasure();
        performLayout();
        performDraw();
    }

    void performMeasure(){
        mView.measure();
    }

    void performLayout(){
        mView.layout();
    }

    void performDraw(){
        mView.draw();
    }

}

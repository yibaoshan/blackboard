package com.android.notebook.backup.android.docs

class AndroidOSLaunchProcess {

    /**
     * 1. power：主板上电
     * 2. BootLoader：刻录在芯片内部电路去找下一步执行的代码片
     * 3. uboot
     *   3.1 初始化外设
     *   3.2 设置启动参数
     *   3.3 引导内核启动
     * 4. kernel
     *   4.1 设置硬件加载驱动
     *   4.2 根据uboot参数启动系统
     * 5. Android
     *   5.1 启动系统服务：mediaserver、servicemanager、adbd、bootanimation、surfaceflinger、zygote
     *
     * 1. 主板上电，BootLoader拉起Linux内核代码
     * 2. Kernel内核代码初始化内存、磁盘等之后退化成0号进程idle，然后启动1号进程init进程和2号进程kthreadd进程
     *  @see IdleProcess
     *  @see InitProcess
     *  @see KthreaddProcess
     * 3. init进程读取/解析init.rc文件，启动ServiceManager(启动binder)进程、Zygote进程(启动vm/初始化jni)
     * 4. Zygote进程启动SystemServer进程
     *
     * system_server进程运行的服务：
     * 1. InputManagerService
     *
     * */

    private class IdleProcess {

        /**
         * 守护进程，当系统关键进程意外退出后重启它
         * */

    }

    private class InitProcess {

        /**
         * Android10之后分为启动过程分为两个阶段
         *
         * 阶段一：FirstStageMain
         * 1. 设置环境变量
         * 2. 挂载系统文件
         *
         * 阶段二：SecondStageMain
         * 1. 加载init.rc
         * 2. 加载所有etc/init的rc配置文件，比如：SurfaceFlinger、MediaService、logd等
         * 3. 挂载binderfs驱动，Android10会分配三个节点
         *  3.1 /dev/binder : 框架/应用进程之间的 IPC，使用 AIDL 接口
         *  3.2 /dev/hwbinder : 框架/供应商进程之间的 IPC，使用 HIDL 接口
         *  3.3 /dev/vndbinder : 供应商/供应商进程之间的 IPC，使用 AIDL 接口
         * 注册jni方法
         * 读取init.rc文件
         * 启动ServiceManager进程
         * 启动zygote进程
         * */

    }

    private class KthreaddProcess {

    }

}
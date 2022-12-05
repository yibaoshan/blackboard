
/**

驱动层实现了哪些功能，提供哪些接口？

1. 实现了跨进程通信的功能

在驱动层中，binder_open、binder_mmap、binder_ioctl 这三个函数最重要

先通过 binder_open() 打开 Binder 设备，然后通过 binder_mmap() 完成内存映射

最后使用 binder_ioctl() 来进行实际的读写（通信）操作

Client 对于 Server 端的请求，以及 Server 对于 Client 请求结果的返回，都是通过 binder_ioctl() 完成的。

驱动提供的接口

1. 通过init()，创建/dev/binder设备节点
2. 通过open()，获取Binder Driver的文件描述符
3. 通过mmap()，在内核分配一块内存，用于存放数据
4. 通过ioctl()，将IPC数据作为参数传递给Binder Driver

Client进程通过RPC(Remote Procedure Call Protocol)与Server通信，可以简单的分为三层，驱动层、IPC层、业务层。

demo()是client和server共同协商好的统一方法，RPC数据、code、handle、协议这四项组成了IPC的层的数据，通过IPC层进行数据传输，而真正在Client和Server两端建立通信的基础设施是Binder Driver。

例如：当AMS的client向ServiceManger注册服务的过程中，IPC层的数据组成为：handle=0，RPC数据为AMS,code为ADD_SERVICE_TRANSACTION，binder协议为BC_TRANSACTION。

先问是不是，再问为什么？what→how→why 法则

简单聊聊 Android binder 通信，目的是了解 binder 提供的功能，具体实现等有时间了单独出一个系列

binder 首先是个驱动程序，通过 read() 我们可以获取键盘、鼠标等事件，同样的，通过 ioctl() 我们也可以控制/使用 binder 设备

binder 是一种虚拟字符设备，没有直接操作硬件，只是对设备内存的处理。但是，为了方便理解，我认为可以把 binder 看做是一种硬件设备

主要是驱动设备的初始化（binder_init）,打开（binder_open）,映射（binder_mmap）,数据操作（binder_ioctl）

1. binder_open 、binder_mmap 、binder_ioctl 这几个方法参数的含义，什么时候建立的通信？
2. binder_mmap ，是分配内存对吧，为谁分配内存呢？毕竟有 client 和 server 两个进程呢

*/

// https://lxr.missinglinkelectronics.com/linux+v4.13/drivers/android/binder.c
//linux/drivers/android/binder.c
class Binder {

    /*
        Android 的 AOSP 开源项目中，并未包含 Linux 代码

        binder 是 Linux 内核的驱动，代码实现在 kernel 部分
    */

    // 保存 IPC 过程中的数据，与用户空间共用结构体
    struct binder_write_read;
    struct binder_transaction_data;

    // 内部实现用到的结构体，等同于 Java 中的类
    struct binder_node; // 描述Binder实体节点，即：对应了一个Server
    struct binder_ref; // 描述对于Binder实体的引用
    struct binder_buffer; // 描述Binder通信过程中存储数据的Buffer
    struct binder_proc; // 描述使用Binder的进程
    struct binder_thread; // 描述使用Binder的线程

    // 描述了Binder驱动发送给进程的命令
    static const char * const binder_return_strings[] = {
            "BR_ERROR",
            "BR_OK",
            "BR_TRANSACTION", // 通知进程收到一次Binder请求（Server端）
            "BR_REPLY", // 通知进程收到Binder请求的回复（Client）
            "BR_SPAWN_LOOPER", // 通知Binder进程创建一个新的线程
    };

    // 描述了进程发送给Binder驱动的命令
    static const char * const binder_command_strings[] = {
            "BC_TRANSACTION", // Binder事务，即：Client对于Server的请求
            "BC_REPLY", // 事务的应答，即：Server对于Client的回复
            "BC_FREE_BUFFER", // 通知驱动释放Buffer
            "BC_INCREFS_DONE", // 通知驱动主线程ready
            "BC_ENTER_LOOPER", // 通知驱动主线程ready
            "BC_EXIT_LOOPER", // 通知驱动线程已经退出
    };

    static int __init binder_init(void){
        // 注册一个 misc 类型的设备
        ret = init_binder_device(device_name);
    }

    static int __init init_binder_device(const char *name){
        ret = misc_register(&binder_device->miscdev); // 向内核中注册了Binder设备
    }

    // 常用方法 一 ，返回值等于0表示成功，小于0表示失败
    // 任何进程在使用Binder之前，都需要先通过open("/dev/binder")打开Binder设备
    static int binder_open(struct inode *nodp, struct file *filp){
        // 创建进程对应的binder_proc对象，每个进程
        struct binder_proc *proc = kzalloc(sizeof(*proc), GFP_KERNEL);
        // 添加到全局列表binder_procs中
        // 在Binder驱动中， binder_procs 记录了所有使用 Binder 的进程，每个初次打开Binder设备的进程都会被添加到这个列表中的。
        hlist_add_head(&proc->proc_node, &binder_procs);
    }

    // 常用方法 二
    static int binder_mmap(struct file *filp, struct vm_area_struct *vma){
        // 在内核空间获取一块内存地址
        struct vm_struct *area = get_vm_area(vma->vm_end - vma->vm_start, VM_IOREMAP);
        binder_update_page_range(proc, 1, proc->buffer, proc->buffer + PAGE_SIZE, vma);
    }

    // 常用方法 三
    // 控制 binder 驱动的方法，cmd 参数描述了进程发送给Binder驱动的命令
    static long binder_ioctl(struct file *filp, unsigned int cmd, unsigned long arg){
        // cmd 表示，binder 通信的协议
        switch(cmd){
            case BINDER_WRITE_READ; // 读写操作，最常用的命令。IPC过程就是通过这个命令进行数据传递
            case BINDER_SET_MAX_THREADS; // 设置进程支持的最大线程数量，一个 server 端可能同时处理来自多个 client 的请求，所以，多线程是非常必要的
            case BINDER_SET_CONTEXT_MGR; // 设置自身为 ServiceManager，也就是 binder_context_mgr_node 变量
            // ...
        }
    }

    static int binder_update_page_range(struct binder_proc *proc, int allocate,void *start, void *end,struct vm_area_struct *vma){
        // 执行真正进行内存的分配
        *page = alloc_page(GFP_KERNEL | __GFP_HIGHMEM | __GFP_ZERO);
        // 完成在内核空间进行内存映射
        ret = map_vm_area(&tmp_area, PAGE_KERNEL, &page_array_ptr);
        // 完成在用户空间进行内存映射
        ret = vm_insert_page(vma, user_page_addr, page[0]);
    }
}
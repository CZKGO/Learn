#### send,post,Message
```java
public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
       MessageQueue queue = mQueue;
       if (queue == null) {
           RuntimeException e = new RuntimeException(
                   this + " sendMessageAtTime() called with no mQueue");
           Log.w("Looper", e.getMessage(), e);
           return false;
       }
       return enqueueMessage(queue, msg, uptimeMillis);
}

private boolean enqueueMessage(MessageQueue queue, Message msg, long uptimeMillis) {
    msg.target = this;
    if (mAsynchronous) {
        msg.setAsynchronous(true);
    }
    return queue.enqueueMessage(msg, uptimeMillis);
}
```
#### MessageQueue,
名为队列，为何使用链表？ -消息会根据携带的时间插入，所以会很频繁的插入，链表比较方便
```java
boolean enqueueMessage(Message msg, long when) {
    if (msg.target == null) {
        throw new IllegalArgumentException("Message must have a target.");
    }
    if (msg.isInUse()) {
        throw new IllegalStateException(msg + " This message is already in use.");
    }

    synchronized (this) {
        if (mQuitting) {
            IllegalStateException e = new IllegalStateException(
                    msg.target + " sending message to a Handler on a dead thread");
            Log.w(TAG, e.getMessage(), e);
            msg.recycle();
            return false;
        }

        msg.markInUse();
        msg.when = when;
        Message p = mMessages;
        boolean needWake;
        if (p == null || when == 0 || when < p.when) {
            // New head, wake up the event queue if blocked.
            msg.next = p;
            mMessages = msg;
            needWake = mBlocked;
        } else {
            // Inserted within the middle of the queue.  Usually we don't have to wake
            // up the event queue unless there is a barrier at the head of the queue
            // and the message is the earliest asynchronous message in the queue.
            needWake = mBlocked && p.target == null && msg.isAsynchronous();
            Message prev;
            for (;;) {
                prev = p;
                p = p.next;
                if (p == null || when < p.when) {
                    break;
                }
                if (needWake && p.isAsynchronous()) {
                    needWake = false;
                }
            }
            msg.next = p; // invariant: p == prev.next
            prev.next = msg;
        }

        // We can assume mPtr != 0 because mQuitting is false.
        if (needWake) {
            nativeWake(mPtr);
        }
    }
    return true;
}
```
#### handleCallback, handleMessage<-dispatchMessage

#### Looper - ActivityThread -> main -> Looper.prepareMainLooper(); -> Looper.loop() ->ThreadLocal;

#### 弱引用，强引用， 软引用，虚引用
[Java基础篇 - 强引用、弱引用、软引用和虚引用](https://blog.csdn.net/baidu_22254181/article/details/82555485)

#### Thread， ThreadLocalMap

#### Looper.prepare() ，sThreadLocal

####sThreadLocal值为静态，也就是说，所有线程通过相同的对象去获取自己的Looper，而不是每个线程创建一个对象
 参考[ThreadLocal类型变量为何声明为静态](https://blog.csdn.net/chicm/article/details/40894299?utm_source=blogkpcl9)
 [【朝花夕拾】Android多线程之（二）ThreadLocal篇](https://www.cnblogs.com/andy-songwei/p/12040372.html)
Looper中sThreadLocal 所有线程共享，都可以通过它取到对应的looper
如何获取？
每个线程中都维护有ThreadLocalMap的对象threadLocals，通过Looper中的sThreadLocal为Key，获取到的value是Looper

为什么线程中的对象threadLocals是map？
ThreadLocal是java API
Looper是安卓API
这点可以轻松说明ThreadLocal不是为Looper设计的，Looper使用ThreadLocal的机制
也就是说，在threadLocal，可以以一个新的值ThreadLocal，去存储新的不同于Looper的变量，如Id

考虑没有ThreadLocal机制
那么多个线程具有相同变量
Thread{
  VarA a;
  VarA b;
  ...
  VarA z;
}
这也太繁琐了，也没法扩展，在没学习ThreadLocal之前，
Thread{
  HashMap map<int， object>
}
使用int值不好维护，容易乱，使用每一静态的ThreadLocal，好处就显而易见了

#### handler内存泄漏
>内存泄漏的原因
>匿名内部类，单例，静态变量引用内部类
handler持有Activity，message持有handler
内部类默认外部类对象
```java
private boolean enqueueMessage(MessageQueue queue, Message msg, long uptimeMillis) {
    msg.target = this;
    if (mAsynchronous) {
        msg.setAsynchronous(true);
    }
    return queue.enqueueMessage(msg, uptimeMillis);
}
```
MessageQueue持有handler
解决：
方法一：Activity销毁时清除所有message
方法二：静态内部类，避免Activity强引用，采用弱引用

#### wait和sleep

这两个方法来自不同的类分别是Thread和Object  
最主要是sleep方法没有释放锁，而wait方法释放了锁，使得其他线程可以使用同步控制块或者方法(锁代码块和方法锁)。
wait，notify和notifyAll只能在同步控制方法或者同步控制块里面使用，而sleep可以在任何地方使用(使用范围)  
sleep必须捕获异常，而wait，notify和notifyAll不需要捕获异常  

####  [对象的锁池和等待池]{https://blog.csdn.net/qq_15875773/article/details/88567577}

[MessageQueue阻塞线程](https://blog.csdn.net/zip_tts/article/details/86097136)





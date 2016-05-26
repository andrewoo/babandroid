package com.util.thread;

/**
 * 
 * ThreadWithDialogTask 的 Interface
 * 
 * ThreadWithDialogListener TaskMain 子线程操作 ProgressDialog显示 OnTaskDismissed
 * 取消操作 ProgressDialog在dismiss时调用 OnTaskDone 主线程方法
 * 
 * @author born
 * 
 */
public interface ThreadWithDialogListener {

	boolean TaskMain();

	boolean OnTaskDismissed();

	boolean OnTaskDone();

}

# Thread Note
## Define Thread Class
`class A extends Thread{ run(){}} `

或 

`class A implements Runnable{ run(){}} `

都可以建立 thread 的類別

## Create and Start a Thread
啟動 thread 的方法分別是

`new A().start();`

和

`new Thread(new A()).start();`

## Suspend a Thread
`sleep()`

## Terminate a Thread
`stop()` is a method used to stop thread but which is not recommended to used. 
Official solution recommends to implement a function for run() that can make it terminate.

For example:
```
	boolean flag = false;
	void run(){ while(flag == false){..}}
	void terminate(){ flag = true; }
	void main(){
		Thread t = new Thread();
		t.start();
		t.terminate();
	}
```	
the method t.terminate change the value of flag into false which will make while in run() break.


# Synchronized Threads

`public synchronized void foo(){}`

The modifer synchronized make foo() mutual exclusive.

```
void foo(){
	synchronized([expression]){
		...
	}
	...
}
```

using `synchronized` enclosing snippet in function only makes part of foo mutual exclusive

### wait set 

waiting list/blocked queue of Java Thread

```
Thread obj = new Thread();
obj.wait() => wait()
obj.notify()/obj.notifyAll()  => awake()
```
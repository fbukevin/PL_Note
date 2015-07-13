class thread_run_start{

  public static void main(String[] args) {

    Thread t1 = new Thread(new Runnable() {
     @Override
     public void run() {
       for (int i = 0; i < 1000; i++) {
          System.out.println("t1");
         }
     }

    });

    Thread t2 = new Thread(new Runnable() {
     @Override
     public void run() {
       for (int i = 0; i < 1000; i++) {
          System.out.println("t2");
         }
      }

    });

    System.out.println("Caller run()\n");
    t1.run();
    t2.run();
  
    System.out.println("====================\nCaller start()\n");
    t1.start();
    t2.start();
  }
}

/*
  Java Thread 的 run() 和 start() 驅動方法：
  這兩個方法都是啟動 thread 的方法，而且都會呼叫 Thread(new Runnable(){ @Override public void run(){ }})
  但使用 run ，他是循序性的，也就是說他會跑完 run 裡面的程式，才會跳脫，而 start 不是
  已此例，t1.run() t2.run() 會先把 t1 執行完才執行 t2，若改用 t1.start() t2.start() 則有可能 t1 執行到一般就切到 t2 了
  事實上，t1.run() 只是呼叫了 t1 的 run 方法，不是啟動執行續，因此沒有平行化
  t1.start() 才會啟動執行續，且 t1.start() 會去呼叫 t1.run() 
 */

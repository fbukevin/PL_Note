'use strict'

var Promise = require('promise');

var promiseCount = 0;
function testPromise() {
  var thisPromiseCount = ++promiseCount;

  console.log( '(' + thisPromiseCount +') 开始(同步代码开始)');

  // 我们创建一个新的promise: 然后用'result'字符串解决这个promise (3秒后)
  var p1 = new Promise(
    // 解决函数带着解决（resolve）或拒绝（reject）promise的能力被执行
    function(resolve, reject) {
      console.log('(' + thisPromiseCount + ') Promise开始(异步代码开始)');

      // 这只是个创建异步解决的示例
      setTimeout(
        function() {
          // 我们满足（fullfil）了这个promise!
          resolve(2);          
        }, Math.random() * 2000 + 1000);
    });

  // 定义当promise被满足时应做什么
  p1.then(
    // 输出一段信息和一个值
    function(val) {
      console.log('(' + val + ') Promise被满足了(异步代码结束)' );
    });

  console.log('(' + thisPromiseCount + ') 建立了Promise(同步代码结束)');
}

testPromise();

# Node.js常见问题

## 基础问题

为什么 `undefined == null`

> 4\. If x is null and y is undefined, return true.

### 引用

**call-by-sharing**

> Instead, the situation is that the item passed in is passed by value. But the item that is passed by value is _itself_ a reference.

```javascript
function changeStuff(a, b, c)
{
  a = a * 10;
  b.item = "changed";
  c = {item: "changed"};
}

var num = 10;
var obj1 = {item: "unchanged"};
var obj2 = {item: "unchanged"};

changeStuff(num, obj1, obj2);

console.log(num);
console.log(obj1.item);    
console.log(obj2.item);
// 10 changed unchanged
```

### 内存释放

**when?**

1. 引用类型
1. 值（闭包）
1. 值（非闭包）

## 事件/异步

### Promise

1. `.then` 的第一参数与 `catch` 区别在于就近与全局
1. `new Promise()` 立即执行

```javascript
setTimeout(function() {
  console.log(1)
}, 0);
new Promise(function executor(resolve) {
  console.log(2);
  for( var i=0 ; i<10000 ; i++ ) {
    i == 9999 && resolve();
  }
  console.log(3);
}).then(function() {
  console.log(4);
});
console.log(5);
// 2 3 5 4 1
```

### Event

`fs`, `net`, `http` 模块依赖 `Stream` 基于 `Event`

> 通过继承 EventEmitter 来使得一个类具有 node 提供的基本的 event 方法，这样的对象可以称作 emitter ，而触发(emit)事件的 cb 则称作 listener  
emitter 的触发不存在冒泡, 逐层捕获等事件行为

EventListener 按注册顺序同步调用 listener `emitter.on('key', foo); emitter.on('key', bar);`

### 阻塞/异步

> Node.js 中执行 js 代码的过程是单线程的. 只有当前代码都执行完, 才会切入事件循环, 然后从事件队列中 pop 出下一个回调函数开始执行代码

由此可以如下实现一个 sleep 函数

```javascript
function sleep(ms) {
  let start = Date.now(), expire = start + ms;
  while(Date.now() < expire) ;
  return;
}
```

### 任务队列

异步执行的运行机制如下：

> 1. 所有同步任务都在主线程上执行，形成一个执行栈（execution context stack）
> 1. 主线程之外，还存在一个"任务队列"（task queue）。只要异步任务有了运行结果，就在"任务队列"之中放置一个事件
> 1. 一旦"执行栈"中的所有同步任务执行完毕，系统就会读取"任务队列"，看看里面有哪些事件。那些对应的异步任务，于是结束等待状态，进入执行栈，开始执行
> 1. 主线程不断重复上面的第三步

![Event Loop](pic/event_loop.png)

栈中的代码调用外部API，在"任务队列"中加入事件。当栈中代码执行完毕，主线程去“任务队列”依次执行回调函数。因此：

> 指定回调函数的部分（onload和onerror），在send()方法的前面或后面无关紧要

同理，`setTimeout` 只是将事件插入了“任务队列”，必须等待执行栈执行完。

Node.js 中 `process.nextTick` 在当前执行栈尾部添加事件，`setImmediate` 在任务队列后添加事件。

## 模块

### 模块机制

#### 上下文

每个 node 进程只有一个 VM 的上下文，`.js` 独立一个环境，是因为 node 在外层包了一圈自执行

```javascript
// b.js
(function (exports, require, module, __filename, __dirname) {
  t = 111;
})();

// a.js
(function (exports, require, module, __filename, __dirname) {
  // ...
  console.log(t); // 111
})();
```

#### 循环加载

`a.js; b.js` 相互依赖，`module.exports` 和 `exports` 的区别，决定了相互引用时未执行完的一方被引用时拿到的是 {}

##### CommonJS模块的加载原理

`require` 命令第一次加载该脚本，就会执行整个脚本，然后在内存生成一个对象。需要用到这个模块的时候，就会到 `exports` 属性上面取值。CommonJS 模块无论加载多少次，都只会在第一次加载时运行一次。CommonJS 输入的是被输出值的拷贝，不是引用。

简言之，`a.js` 执行过程中遇到 `require(b)` 则去执行 `b.js` 此时 `require(a)` 只拿到已执行完的部分。

##### ES6模块的循环加载

ES6 模块是动态引用，`import` 加载从模块加载变量，变量不会被缓存，而是成为一个指向被加载模块的引用。

简言之，`a.js` 执行过程中遇到从 `b.js` `import` 的方法 `foo` 会到 `b.js` 中执行 `foo`。

### 热更新

代码中 `require` 会拿到之前的编译好缓存在 v8 内存 (code space) 中的的旧代码，清除 `catch` 再次 `require` 可在**局部**拿到新代码。

## 进程

### process 对象

```javascript
console.log(process); // process 对象

function test() { // 递归调用 process.nextTick 导致 任务队列阻塞？
  process.nextTick(() => test());
}
```

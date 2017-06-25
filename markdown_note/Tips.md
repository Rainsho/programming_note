## Oracle数据库
* 虚拟表dual
* 连字符'||'
* 伪列rownum和rowid
* 函数
  * lower/upper/initcap/concat/substr/length/instr/trim/replace
  * round/trunc/mod/ceil/floor
  * sysdate/months_between/add_months/next_day/last_day
  * to_char(sysdate, 'mm/dd/yyyy hh24:mi:ss')
  * to_date('1995-1-1','yyyy"年"mm"月"dd"日"')
  * nvl/nvl2/nullif/coalesce
  * case/decode

## JavaScript
* 数组方法
  * slice() //arguments: 0复制 1起 2起止
  * push和pop //末尾
  * unshift和shift //头
  * sort/reverse/splice/concat/join
* for ... in <-> for ... of
* var/let/const
* apply/call //重新指定this的指向
  * Math.max.apply(null, [3, 5, 4]);
  * Math.max.call(null, 3, 5, 4);
  * apply装饰器
* map/reduce/filter //filter (element, index, self) => ...
* 箭头函数内部的this是词法作用域，由上下文确定
* function* foo(){yield n;}
* JSON.parse/stringify();
* DOM: 删除多个节点时，要注意children属性时刻都在变化
* AJAX: XMLHttpRequest/ActiveXObject
  * onreadystatechange
  * readyState/status
  * open/send()
  * 跨域请求
    * flash/同源proxy
    * JSONP跨域引用JS
    * CORS(HTML5)
* Promise对象
  * `(function(a, b){}).then(a).catch(b)`
  * `job1.then(job2).then(job3).catch(error)`
  * `Promise.all/race([p1, p2]).then();`
* jQuery
  * `dom=$('#id').get(0)`<-->`jq=$(dom)`
  * `$('.red.green')`'a,b'/'a>b'/'first/last/nth-child(0)'/'a:eq/gt/lt'
  * find/parent/next/prev('a')
  * filter('a'/foo())
  * .attr/prop('checked')/**is(':checked')** --> 'checked'/true/true | 'selected'
  * 扩展`$.fn.foo = function(){}`
* 异步错误处理，注意try...catch的时机

### JS闭包示例
```javascript
function make_pow(n) {
    return function (x) {
        return Math.pow(x, n);
    }
}
// 创建两个新函数:
var pow2 = make_pow(2);
var pow3 = make_pow(3);
pow2(5); // 25
pow3(7); // 343
```

### JS面向对象
* 所谓继承关系不过是把一个对象的原型指向另一个对象而已。`obj.__proto__ = Obj`  
* 原型链,JS引擎自下往上查找属性
```
var arr = [1, 2, 3];
arr ----> Array.prototype ----> Object.prototype ----> null
```
* 构造函数`function Obj(){...}` <--> new Obj()
> 不同实例里的方法单独存在，`a.foo === b.foo //false`

#### JS面向对象示例
```javascript
function Student(props) {
    this.name = props.name || '匿名'; // 默认值为'匿名'
    this.grade = props.grade || 1; // 默认值为1
}

Student.prototype.hello = function () {
    alert('Hello, ' + this.name + '!');
};

function createStudent(props) {
    return new Student(props || {})
}

var xiaoming = createStudent({
    name: '小明'
});
```

#### 原型继承与class继承
借助空函数修正原型链。
```javascript
/*
new SubClass() ----> SubClass.prototype ----> BaseClass.prototype ----> Object.prototype ----> null
*/

function SubClass(props) {
    // 调用Student构造函数，绑定this变量:
    Base.call(this, props);
    this.grade = props.grade || 1;
}

function F() {}

F.prototype = Base.prototype;
Sub.prototype = new F();
Sub.prototype.constructor = Sub;

//封装成方法
function inherits(Child, Parent) {
    var F = function () {};
    F.prototype = Parent.prototype;
    Child.prototype = new F();
    Child.prototype.constructor = Child;
}
```
修正后的原型链：  
![js-proto-extend](pic/js-proto-extend.png)

ES6新增`class`关键字。
```javascript
class Base {
  constructor() {
    // do something
  }
  foo() {
    // do something
  }
}

class Sub extends Base {
  constructor() {
    super();
    // do something
  }
}
```

### Promise异步计算示例
```javascript
// 0.5秒后返回input*input的计算结果:
function multiply(input) {
    return new Promise(function (resolve, reject) {
        console.log('calculating ' + input + ' x ' + input + '...');
        setTimeout(resolve, 500, input * input);
    });
}

// 0.5秒后返回input+input的计算结果:
function add(input) {
    return new Promise(function (resolve, reject) {
        console.log('calculating ' + input + ' + ' + input + '...');
        setTimeout(resolve, 500, input + input);
    });
}

var p = new Promise(function (resolve, reject) {
    console.log('start new Promise...');
    resolve(123);
});

p.then(multiply)
 .then(add)
 .then(multiply)
 .then(add)
 .then(function (result) {
    console.log('Got value: ' + result);
});
```

### browser API

```javascript
window.onbeforeunload = () => {
    console.log(1); // ok
    alert(2); // blocked
    return 3; // default msg
}

navigator.getBattery(); // => Promise
```

## HTML5

### File API
通过File/FileReader对象实现图片预览
```javascript
var
    fileInput = document.getElementById('test-image-file'),
    info = document.getElementById('test-file-info'),
    preview = document.getElementById('test-image-preview');
// 监听change事件:
fileInput.addEventListener('change', function () {
    // 清除背景图片:
    preview.style.backgroundImage = '';
    // 检查文件是否选择:
    if (!fileInput.value) {
        info.innerHTML = '没有选择文件';
        return;
    }
    // 获取File引用:
    var file = fileInput.files[0];
    // 获取File信息:
    info.innerHTML = '文件: ' + file.name + '<br>' +
                     '大小: ' + file.size + '<br>' +
                     '修改: ' + file.lastModifiedDate;
    if (file.type !== 'image/jpeg' && file.type !== 'image/png' && file.type !== 'image/gif') {
        alert('不是有效的图片文件!');
        return;
    }
    // 读取文件:
    var reader = new FileReader();
    reader.onload = function(e) {
        var
            data = e.target.result; // 'data:image/jpeg;base64,/9j/4AAQSk...(base64编码)...'            
        preview.style.backgroundImage = 'url(' + data + ')';
    };
    // 以DataURL的形式读取文件:
    reader.readAsDataURL(file);
});
```

### Canvas

基础操作

```javascript
$('body').before('<canvas id="canvas"></canvas>');

let context = $('#canvas')[0].getContext('2d');
context.fillStyle = 'red';
context.fillRect(20, 20, 100, 200);
```

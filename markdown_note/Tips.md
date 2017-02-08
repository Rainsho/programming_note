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
* apply/call
 * Math.max.apply(null, [3, 5, 4]);
 * Math.max.call(null, 3, 5, 4);
 * apply装饰器
* map/reduce/filter //filter (element, index, self) => ...
* 箭头函数内部的this是词法作用域，由上下文确定
* function* foo(){yield n;}
* JSON.parse/stringify();

### JS闭包实例
```JavaScript
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
```JavaScript
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
```JavaScript
/*
new SubClass() ----> SubClass.prototype ----> BaseClass.prototype ----> Object.prototype ----> null
*/

function SubClass(props) {
    // 调用Student构造函数，绑定this变量:
    Base.call(this, props);
    this.grade = props.grade || 1;
}

function F(){}

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
```JavaScript
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

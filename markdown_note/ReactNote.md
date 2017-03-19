# React Note

React 包含
* 组件
* JSX
* Virtual DOM
* Data Flow

**JSX** 使JS支持嵌入HTML，使前端实现组件化  
**Virtual DOM**  
1. 组件 `state` 改变，调用 `render` 重新渲染 UI
2. 实现 diff 算法仅更新改变 DOM
3. 纯粹的 JS 数据结构

> Babel 编译， Webpack 打包，构建开发环境

## JSX

JS 的写法
```js
React.createElement('a', {href: 'http://facebook.github.io/react/'}, 'Hello!');

var child = React.createElement('li', null, 'Text Content');
var root = React.createElement('ul', { className: 'my-list' }, child);
React.render(root, document.body);
```

### 使用 JSX

```js
// 子组件也可以作为表达式使用
var content = <Container>{window.isLoggedIn ? <Nav /> : <Login />}</Container>;

// 注释
{/* child comment, put {} around */}
```

**HTML 转义**，防止 XSS  
* 直接使用 UTF-8 字符
* 使用对应字符的 Unicode 编码
* 使用数组组装 `<div>{['cc ', <span>&copy;</span>, ' 2015']}</div>`
* 直接插入原始的 HTML `<div dangerouslySetInnerHTML={{__html: 'cc &copy; 2015'}} />`

自定义 HTML 属性  
> 如果在 JSX 中使用的属性不存在于 HTML 的规范中，这个属性会被忽略。如果要使用自定义属性，可以用 data- 前缀  
可访问性属性的前缀 aria- 也是支持的

### 属性扩散

_spread attributes_
```js
var props = { };
props.foo = x;
props.bar = y;
var component = <Component {...props} foo={'override'} />;
```

## 组件

**props**  
> props 就是组件的属性，由外部通过 JSX 属性传入设置，一旦初始设置完成，就可以认为 this.props 是不可更改的，所以不要轻易更改设置 this.props 里面的值

**state**
> state 是组件的当前状态，可以把组件简单看成一个“状态机”，根据状态 state 呈现不同的 UI 展示。
一旦状态（数据）更改，组件就会自动调用 render 重新渲染 UI，这个更改的动作会通过 this.setState 方法来触发

**划分状态数据**  
> 下面这些可以认为不是**状态**：  
* 可计算的数据：比如一个数组的长度
* 和 props 重复的数据：除非这个数据是要做变更的

**无状态组件**  
开销很低，如果可能的话尽量使用无状态组件
```js
const HelloMessage = (props) => <div> Hello {props.name}</div>;
render(<HelloMessage name="John" />, mountNode);
```

### 组件生命周期

组件类由 `extends Component` 创建

#### `getInitialState`
初始化 this.state 的值，只在组件装载之前调用一次。  
如果是使用 ES6 的语法，你也可以在构造函数中初始化状态，比如：
```js
class Counter extends Component {
  constructor(props) {
    super(props);
    this.state = { count: props.initialCount };
  }

  render() {
    // ...
  }
}
```

#### `getDefaultProps`
只在组件创建时调用一次并缓存返回的对象（即在 `React.createClass` 之后就会调用）。  
因为这个方法在实例初始化之前调用，所以在这个方法里面不能依赖 `this` 获取到这个组件的实例。  
保证没有传值时，对应属性也总是有值的。  
ES6 语法，可以直接定义 `defaultProps` 这个类属性来替代：
```js
Counter.defaultProps = { initialCount: 0 };
```

#### `render`
**必须**，可返回 `null` 或者 `false`  
这时候 `ReactDOM.findDOMNode(this)` 会返回 `null`

#### 生命周期函数
* `componentWillMount`
* `componentDidMount`
  * 从这里开始，可以通过 `ReactDOM.findDOMNode(this)` 获取到组件的 DOM 节点
* `componentWillReceiveProps`
* `shouldComponentUpdate`
* `componentWillUpdate`
* `componentDidUpdate`
* `componentWillUnmount`

### 事件处理

React 里面绑定事件的方式和在 HTML 中绑定事件类似，注意要显式调用 `bind(this)` 将事件函数上下文绑定要组件实例上。  
**传递参数** `bind(this, arg1, arg2, ...)`

#### “合成事件”和“原生事件”
> React 实现了一个“合成事件”层（synthetic event system），这个事件模型保证了和 W3C 标准保持一致  
**事件委托** “合成事件”会以事件委托（event delegation）的方式绑定到组件最上层，并且在组件卸载（unmount）的时候自动销毁绑定的事件  
**原生事件** 比如在 `componentDidMount` 方法里面通过 `addEventListener` 绑定的事件就是浏览器原生事件  
“合成事件”的 `event` 对象只在当前 event loop 有效，比如你想在事件里面调用一个 promise，在 resolve 之后去拿 `event` 对象会拿不到

### DOM 操作
<!-- // TODO -->


## Data Flow

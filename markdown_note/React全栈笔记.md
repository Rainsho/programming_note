# React 全栈

[TOC]

## 现代前端开发

### ES6

#### 特性

1. `const` / `let`
1. 箭头函数 (语法糖+ `this` )
1. 默认参数
1. Rest 参数 与 展开操作符
1. 模板字符串
1. 解构赋值
1. 类 (`constructor` `static`)
1. 模块

#### Babel

1. `Babel CLI` 命令行编译

```bash
npm install -g babel-cli
babel es6.js -o compiled.js
```

2. 配置

`Babel` 通过插件 (plugin) 或者预设 (preset) 编译代码

```json
// .babelrc
{
  "presets": [],
  "plugins": []
}
```

安装 preset / plugin

```bash
# es2015 ES6编译成ES5
npm install --save-dev babel-preset-es2015
# transform-object-rest-spreead 对象展开操作
npm install --save-dev babel-plugin-transform-object-rest-spreead
```

### 组件化

module 侧重语言层面，component 侧重业务层面包括所需要的所有资源(逻辑JS、样式CSS、模板HTML/template、图片、字体等)

#### JS 模块化方案

1. 全局变量+命名空间 (namespace)

```javascript
const foo = window.foo;
const bar = 'hello, world';
// export
foo.bar = bar;
// 模块内部通过自执行函数实现局部作用域
(function () {
  // define @ export ...
})()
```

2. AMD & CommonJS

AMD 模块在全局环境下定义 `require` 和 `define`，通过文件路径或模块自己申明进行定位，模块实现中声明的依赖，由加载器操作，提供
打包工具自动分析依赖并合并。常见 AMD 模块如下:

```javascript
define(function (require) {
  // 通过相对路径获得依赖模块
  const bar = require('./bar');
  // 输出模块
  return function () {
    // ......
  };
});
```

CommonJS (Node.js 使用此规范) 不适合浏览器环境，经转换后可在浏览器执行，较 AMD 更简洁，典型模块如下:

```javascript
// 通过相对路径获得依赖模块
const bar = require('./bar');
// 输出模块
module.exports = function () {
  // ......
}
```

3. ES6 模块

`import` `export`

#### 前端的模块化和组件化

1. 基于命名空间的多入口文件组件  
  * 例如 `jQuery` 引入 `<script>` 和 `<link>`，插件在全局的 `$` 中添加内容
2. 基于模块的多入口文件组件  
  * `require` 模块，`import` 样式等
3. 单 JS 入口组件  
  * 依赖现代打包工具 `browserify`、`webpack`
  * 组件的所有依赖在自己的实现中申明
4. Web Component  
  * Custom Element
  * HTML Template
  * Shadow DOM
  * HTML Import

React 推荐通过 `webpack` 或 `browserify` 构建应用，搭配 `loader` 或 `plugin` 实现单 JS 入口组件

### 辅助工具

#### 包管理工具 Package Manager

1. 安装包

```bash
# 本地安装
npm install lodash
# 全局安装
npm install -g jshint

# 使用 package.json
# dependencies 生产环境依赖
npm install --save package
# devDependencies 开发测试环境依赖
npm install --save-dev package
```

2. 包和模块

包是用 package.json 描述的文件或文件夹，模块指任何可以被 Node.js 中 `require` 载入的文件。所有的模块都是包，但一些 CLI 
包只包括可执行命令行工具。

#### 任务流工具 Task Runner

前端常见工作: `jshint` 检验 JS 文件格式，`uglifyjs` 压缩。Task Runner 避开 shell script，使用 JS 语法实现。

1. Grunt

```bash
npm install -g grunt-cli
```

Grunt 通过插件与其他工具结合，例如 `grunt-contrib-jshint`，通过 Gruntfile.js 配置，通过 `grunt --help` 查看，通过 `grunt` 
执行。

```javascript
module.exports = function(grunt) {
  // 定义任务配置
  grunt.initConfig({
    jshint: {
      src: 'scr/test.js'
    },
    uglify: {
      build: {
        src: 'src/test.js',
        dest: 'build/test.min.js'
      }
    }
  });
  // 导入插件
  grunt.loadNpmTasks('grunt-contrib-jshint');
  grunt.loadNpmTasks('grunt-contrib-uglify');
  // 注册任务
  grunt.registerTask('default', ['jshint','uglify']);
}
```

2. Gulp

```bash
npm install -g gulp-cli
```

Gulp 通过插件与其他工具适配，通过 gulpfile.js 配置，通过流简化任务间的配置和输出，`gulp` 默认执行 `default` 任务。

```javascript
// Gulp 主体和插件
var gulp = require('gulp');
var jshint = require('gulp-jshint');
var uglify = require('gulp-uglify');
// 定义 lint 任务，使用 pipe 向下传递
gulp.task('lint', function() {
  return gulp.src('src/test.js')
    .pipe(jshint())
    .pipe(jshint.reporter('default'));
});
// 定义 compress 任务
gulp.task('compress', function(){
  return gulp.src('src/test.js')
    .pipe(uglify())
    .pipe(gulp.dest('build'));
});
// 组合任务
gulp.task('default', ['lint', 'compress']);
```

#### 模块打包工具 Bundler

浏览器环境下 Node.js 中用 `require` 同步加载的方式无法使用，通过 Bundler 编译、转换、合并以生成浏览器良好运行的代码。

1. browserify
  * 支持 CommonJS 模块
  * `browserify test.js > bundle.js`
2. webpack
  * 支持 AMD 和 CommonJS，通过 `loader` 机制也可使用 ES6 模块

## webpack

支持多种模块方案，视一切资源为可管理模块。code spliting 方案支持大规模 Web 应用，loader/plugin 开发扩展配套工具。

### webpack 特点

#### webpack 与 RequireJS / browserify

RequireJS 是一个基于 AMD 规范的 JS 模块加载器，同时提供构建工具 r.js 将匿名模块具名化并进行合并。
RequireJS 从入口文件开始递归的进行静态分析，找出直接或间接依赖然后转换合并，大致如下:

```javascript
// bundle.js
define('hello', [], function (require) {
  module.exports = 'hello!';
});
define('say', ['require', 'hello'], function (require) {
  var hello = require('./hello');
  alert(hello);
});
```

browserify 以在浏览器中使用 Node.js 模块为出发点。对 CommonJS 规范的模块进行转换盒包装，对很多标准 package 进行了浏览器端适配。

webpack 为前端模块打包构建而生。解决了代码的拆分与异步加载、对非 JS 资源的支持，`loader` 设计使其更像一个平台。

#### 模块规范

AMD 将模块的实现包在匿名函数(AMD factory)中实现作用域隔离，使用文件路径作为 ID 实现命名空间控制，将模块的工厂方法作为参数传入
全局的 define，使工厂方法的执行时机可控，变相模拟出了同步的局部 `require`。

CommonJS 约等于去掉 `define` 及工厂方法外壳的 AMD，因此无法直接执行(浏览器环境无法实现同步的 `require` 方法)，但书写更简洁。

#### 非 javascript 模块支持

组件化开发对局部逻辑进行封装，通过尽可能少的必要接口与其他组件组装及交互。减少组件入口文件数，尽可能对所有依赖内部申明(高内聚)。

RequireJS 较不完善，browserify 通过 transform 插件实现引入与打包，webpack 对应的是 loader。

#### 构建产物

r.js 构建 `define(function(){...})` 的集合，需要引入 AMD 模块加载器(loader.js / bundle.js)，而 browserify 与 webpack 构建
结果都是可以直接执行的 JS 代码(bundle.js)任务。

#### 使用

1. r.js: `r.js -o app.build.js`
2. browserify
  * CLI: `browserify [entry files] {OPTIONS}`
  * Node.js API: `var browserify = require('browserify')`
3. webpack (CLI && Node.js API)

webpack 支持部分命令行配置，但主要配置通过配置文件 (webpack.config.js) 进行配置。配置文件在 Node.js 环境运行，export JS 对象
作为配置信息，亦可 `require` 其他模块，实现复杂任务配置的组织。

#### webpack 特色

1. code spliting

将应用代码拆分为多个块(chunk)，按需异步加载。实际使用中，多用来拆分第三方库或某些功能的内部逻辑。以此提升单页面应用初始加载速度。

2. 智能静态分析

webpack 支持含变量的简单表达式应用，对于 `require('./template/'+name+'.jade')` webpack 会提取出以下信息:

* 目录位于 ./template 下
* 相对路径符合正则表达式: `/^.*\.jade$/`

然后将以上模块全部打包，在执行期，依据实际值决定使用。

3. Hot Module Replacement

修改完某一模块后无须刷新页面即可动态将受影响的模块替换为新模块，在后续执行中使用新模块逻辑。这一功能需要修改 module 本身，可配合 
style-loader 或者 react-hot-loader 等第三方工具实现。通过 `webpack-dev-server --hot` 启动功能。

### 基于 webpack 进行开发

#### Hello world

`webpack index.js bundle.js`

webpack 主要做了两部分工作:

* 分析得到所有必需模块并合并
* 提供让模块有序、正常执行的环境

bundle.js 简要解析:

* 立即执行函数 IIFE `(modules) => __webpack_require__(0)`
* webpackBootstrap 提供
  * module 缓存对象 `installedModules = {}`
  * require 函数 `__webpack_require__(moduleId)`
    * module 在 cache 中直接 `return`
    * 否则新建一个放入 cache `{exports: {}, id: moduleId, loader: false}`
    * 执行 module 函数 `modules[moduleId].call()`
    * 标记已加载 `module.loader = true`
    * `return module.exports`
  * 暴露 modules 对象
  * 暴露 modules 缓存
  * 设置 webpack 公共路径
  * 读取入口模块并返回 `reutrn __webpack_require__(0)`
* modules 作为 webpackBootstrap 的入参匿名函数(factory)集合
  * `(module, exports, __webpack_require__) => {}`
  * `(module, exports) => {}`

构建中指定 index 模块所对应的 JS 文件，webpack 通过静态分析语法树，递归检测所有依赖并合入。`__webpack_require__` 只需要模块在 
modules 中的索引作为标识即可。

#### 使用 loader

> functions (running in node.js) that take the source of a resource file as the parameter and return the new source

webpack 中 `loader` 通常为 `xxx-loader` 的 npm 包，例如: `jsx-loader`(JSX -> JS)、`style-loader`(将 CSS 以 `<style>` 
插入页面)、`css-loader`(检查 `import` 并合并)，`require('style!css!./index.css')` 通过 `style!css!` 指定 `loader`。
打包后 CSS 通过 JS 以 `<style>` 的形式插入页面。存在段子的无样式瞬间，可借助 `extract-text-webpack-plugin` 将样式内容抽离并
输出到额外的 CSS 文件中，然后在 `<head>` 引用。

> 常见前端页面性能优化建议: `<link>` 插入 `<head>`，`<script>` 放在 `<body>` 最后

#### 配置文件

webpack 默认使用当前目录下的 webpack.config.js，配置文件只需 export 配置信息即可 `module.exports`。

配置信息对象基本内容:

* entry 入口文件 (e.g. `path.join(__dirname, 'index')`)
* output 输出结果
  * path 输出目录 (e.g. `__dirname + '/dist'`)
  * filename 输出文件名 (e.g. `bundle_[hash].js`)
  * publicPath 目录所对应的外部路径(浏览器入口) (e.g. `http://cnd.abc.com/static/`)
* module
  * loaders (e.g. `[{test: /\.css$/, loaders: ['style-loader', 'css-loader']}]`)

#### 使用 plugin

`loader` 专注资源内容转换，`plungin` 实现扩展，诸如 `HtmlWebpackPlugin` 自动生成 HTML 页面，`EnvironmentPlugin` 向构建过程
中注入环境变量，`BannerPlugin` 向 chunk 结果中添加注释，其中后两者为 webpack 内置，更多第三方的可通过 npm 包引入。plugin 相关
配置对应 webpack 配置信息中的 plugins 字段，为数组，每一项为一个 plugin 实例。

```javascript
// 使用内置 plugin
var webpack = require('webpack');
webpack.BannerPlugin;

// 配置 plugin
var HtmlWebpackPlugin = require('html-webpack-plugin');
module.exports = {
  entry: '',
  output: {},
  module: {},
  plugins: [
    new HtmlWebpackPlugin({
      title: 'use plugin' // 该 plugin 的配置信息
    })
  ]
}
```

#### 实时构建

```bash
webpack --watch/-w
# webpack-dev-server 辅助开发
# 基于 Express 框架的 Node.js 服务器
# 可在配置文件添加 devServer 进行配置
webpack-dev-server
```

## React

三大特点: ~~抽烟、喝酒、烫头~~ 组件、JSX、Virtual DOM

> 传统前端开发需同时维护数据及视图，**模板引擎**帮助我们解决了初始状态下二者的对应关系。然而，在组件状态改变时，依旧需要被各自维护。
对此，一般的 MVVM 框架通过对模板的分析获取数据与视图(DOM 节点)的对应关系，然后对数据进行监控，在数据变化时更新视图。然而，传统的流
行模板都是通用模板，渲染仅仅是文本替换，语法上不足以支撑 MVVM 的需求，因此 MVVM 框架都会在 HTML 的基础上扩展一套独立的
**模板语法**，使用**自定义命令 (Directive)** 进行逻辑描述，从而使这部分逻辑可被解析并复用。

性能问题: 避开操作 DOM，只重新生成虚拟 DOM，在虚拟 DOM 生成真实 DOM 前进行比较 (Diff)，仅将变化部分作用到真实 DOM。

> 在 React 的哲学里，直接操作 DOM 是典型的反模式。React 的出现允许我们以简单粗暴的方式构建界面: 仅仅声明**数据到视图的转换逻辑**，
然后维护数据的变动，自动更新视图。

### React + webpack 开发环境

#### 安装配置 Babel

```bash
# 安装
# babel-core 和 babel-loader 配合 webpack
npm install --save-dev babel-core babel-loader
# 两个 presets ES6 和 React 支持
npm install --save-dev babel-preset-es2015 babel-preset-react
```

```json
// 配置 .babelrc
{
  "presets": ["es2015", "react"]
}
```

#### 安装配置 ESLint

ESLint 和 JSLint、JSHint、JSCS 等工具提供代码检查，ESLint 提供一个完全可配置的检查规则，而且有众多第三方 plugin 支持。
启用 ESLint 为 webpack 添加 `preLoaders` 在 `loader` 处理资源之前进行处理。

```bash
# 安装
npm install --save-dev eslint eslint-loader
# 插件
npm install --save-dev eslint-plugin-import eslint-plugin-react eslint-plugin-jsx-a11y
npm install --save-dev eslint-config-airbnb
```

```json
// 配置 .eslintrc
{
  "extends": "airbnb",
  "rules": {
    "comma-dangle": ["error", "never"]
  }
}
```

以上配置直接继承了 `eslint-config-airbnb` 的配置规则，同时覆盖了其 `comma-dangle` 的规则。

#### 配置 webpack

```javascript
// webpack.config.js 示例
/**** 引入模块定义常量 ****/
module.exports = {
  entry: { app: APP_PATh },
  output: { path, filename },
  devtool: 'eval-source-map', // 开启 dev source map
  devServer: { historyApiFallback, hot, inline, progress },
  module: {
    preLoaders: [{ test, loaders, include }],
    loaders: [{ test, loaders, include }],
    plugins: [new HtmlWebpackPlugin()]
  },
  resolve: {
    extensions: ['', '.js', '.jsx'] // 支持 import 加载 JSX
  }
}
```

#### 添加热加载 HMR

ps: !!!修改业务可直接应用，但是修改 DOM 可能会触发页面刷新。

```bash
# 安装 Babel 的 preset
# 同时处理 hmr 和 catch-errors
npm install --save-dev babel-preset-react-hmre
```

```json
// 加入 .babelrc
"presets": [],
// 开发时启用
"env": {
  "development": {
    "presets": ["react-hmre"]
  }
}
```

### 组件

一些零碎概念:

* ES6 class 写法在 `constructor` 定义 `this.state` 代替 ~~`getInitialState`~~
  * 不会把自定义的 `callback` 绑定到实例上，需要手动绑定
* 组件 `ReactElement` 是一种 JS 数据结构，通过 `ReactDOM` 挂载到 DOM 节点
* 组件本身是一个状态机，通过 `this.setState` 让组件再次调用 `render` 来渲染 UI

一些关于 React 的补充:

1. props

React 可以让用户自定义组件属性的变量类型，验证组件传入属性:

```javascript
MyComponent.propTypes = {
  name: PropTypes.string.isRequired,
  age: PropTypes.number.isRequired
};
```

2. events

React 并未把事件绑定在特点的 DOM 节点上，只是用事件代理的方式在最外层绑定了事件回调，并在 `unmounted` 时自动删除。

3. lifecycle

* 初始化: `getDefaultProps` -> `getInitialState` -> `componentWillMount` -> `render` -> `componentDidMount`
* props 更新: `componentWillReceiveProps` -> `shouldComponentUpdate` -> `componentWillUpdate` -> 
`render` -> `componentDidUpdate`
* 卸载: `componentWillUnmount`

使用 ES6 class 写法时通过 `static defaultProps = {}` 获得默认 `props`。`render` 是唯一一个必要方法并且应该是一个纯函数，即在给
定相同条件时返回相同的 `ReactElement` 对象。通常在 `componentDidMount` 中执行 AJAX 操作。

4. stateless functional component

```javascript
// 无状态组件推荐写成纯函数
function Hobby(props) {
  return <li>{props.hobby}</li>;
}
```

5. state 设计原则

尽量多的无状态组件关心渲染数据，在组件外层设计一个包含 `state` 的父级组件负责处理事件、交流逻辑、修改 `state`。
`state` 应该是轻量的 JSON 数据，仅包含组件事件回调可能引发 UI 更新的，不包含可计算出来的。

### Virtual DOM

> `ReactElement` 是一种轻量级的、无状态的、不可改变的、DOM 元素的虚拟表述。核心在于用 JS 对象来表述 DOM 结构，使用 Diff 算法来
取得两个对象之间的差异，并用最少的 DOM 操作完成更新。

### 实践

一些开发的 Tips: 

* 将原型图拆分成组件
* 所有组件归档至 components 文件夹，使用 index 输出
* 循环时为子组件添加唯一 `key` 值
* `state` 设计原则 DRY (Don't Repeart Yourself)
* thinking-in-react
  * 画模型图
  * 将模型图拆分成组件
  * 实现静态版本的程序和组件
  * 组合静态版本
  * 设计 `state` 的组成和实现
  * 添加交互方法
  * 组合以上

### 测试

这里使用 Mocha 作为前端测试框架，Chai 作为断言库(提供 `assert`、`should`、`expect` 三种 API 完成断言测试)。

```javascript
// test.js
var expect = require('chai').expect;

describe('test suit', function () {
  it('test suit', function () {
    expect(4 + 5).to.not.equal(8);
    expect(true).to.be.true;
  });
})
```

```bash
# 安装(是否全局自己考量)
npm install mocha chai
# 测试
mocha test.js
```

#### React 测试

Airbnb 的 Enzyme 对官方的 `react-addons-test-utils` 进行了较好的封装，以下使用该库。
`render` 前是虚拟 DOM 对象，`render` 后是真实 DOM。对前者可按测试对象的方式检测，对后者可测试一些真实的交互对 DOM 结构的影响。

1. 测试环境

```json
// package.json
{
  "scripts": {
    "test": "set NODE_ENV=production && mocha --compiler js:babel-core/register --require ignore-style"
  }
}
```

以上命令: `mocha` 使用 `babel` 编译 JS 代码，使用 `ignore-style` 插件忽略 ES6 语法 `import './style.less'` 这样的样式文件。
通过 `--compilers` 指定预编译器，通过 `--require` 指定需要引入的辅助插件。

2. Shallow Render

该 API 生成虚拟 DOM 实例，然后测试属性，适合无状态组件。针对无状态组件，测试的方法一般是根据传入属性，判断是否生成了对应的数据。

```javascript
// 渲染 shallow 组件对象
const wrapper = shallow(<MyComponent />);
// find() 选择器 text() 取值
expect(wrapper.find('h1').text()).to.equal('hello, world');
// find() @param 'h1'/'#id'/'.class'/Component
expect(list.find(ListItem).length).to.equal(testDate.length);
```

3. DOM Rendering

该 API 会将虚拟 DOM 转化成真实 DOM，用来测试交互操作，如单击或在输入框输入值。

```javascript
// 挂载 DOM 结构
const deskmark = mount(<Deskmark />);
// 单击操作
deskmark.find('.create-bar-component').simulate('click');
// expect ...
// 输入操作
const input = deskmark.find('input');
input.node.value = 'new title';
input.simulate('change', input);
// expect ...
```

## Flux

> Flux 是 Facebook 官方提出的一套前端应用架构模式，他的核心概念是单向数据流。它更像是一种软件开发模式，而不是具体的一个框架。

MVC 架构双向数据流，controller 是 model 和 view 之间的交互媒介(处理 view 交互，通知 model 更新，操作成功后通知 view 更新)。

Flux 流程: Action -> Dispatcher -> Store -> View

* Action
  * 描述一个行为及相关信息
  * 如创建文章: `{actionName: 'create-post', data: {content: 'balabala...'}}`
* Dispatcher
  * 信息分发中心，负责连接 Action 和 Store
  * 使用 `dispatch` 方法执行 Action
  * 使用 `register` 方法注册回调
* Store
  * 处理完毕后，使用 `emit` 发送 `change` 广播，告知 Store 已改变
* View
  * 监听 `change` 事件，被触发后调用 `setState` 更新 UI

### Dispatcher 和 action

交互动作作为 action 交给 Dispatcher 调度中心就行处理。action 只是一个普通的 JS 对象，用 `actionType` 字段表用用途，用另一字段
传递信息。当程序复杂度较高时，可能导致在不同的 view 中 `dispatch` 相同的对象，鉴于此 Flux 提出了 `action creator` 的概念，将
数据抽象到一些函数中。

```javascript
// ./dispatcher
// 实例化一个 Dispatcher 并返回
import { Dispatcher } from 'flux';
export default new Dispatcher();

// ./components
import TodoAction from '../actions';
class Todo extends React.Component {
  /* ... */
  deleteTodo(id) {
    TodoAction.delete(id);
  }
  /* ... */
}

// ./actions
import AppDispatcher from '../dispatcher';
const TodoAction = {
  /* ... */
  delete(id) {                        // ---- action creator ----
    AppDispatcher.dispatch({          // ----     action     ----
      actionType: 'DELETE_TODO',      //
      id                              //
    });                               // ----     action     ----
  },                                  // ---- action creator ----
  /* ... */
}
```

### store 和 Dispatcher

store 是单例(Singleton)模式，故每种 store 都仅有一个实例。不同类型的数据应该创建多个 store。Dispatcher 的 `register` 方法用来
注册不同事件的处理回调，并在回调中对 store 进行处理。action 对应 `dispatch` 传来的 action，store 是更新数据的唯一场所，action 
和 Dispatcher 不应该做数据操作。

```javascript
// ./stores
const TodoStore = {
  todos: [],
  getAll() {
    return this.todos;
  },
  deleteTodo(id) {
    this.todos = this.todos.filter(x => x.id !== id);
  }
};
// dispatch 传来的 action
// register 接受一个函数作为回调，所有动作都会发送到这个回调
AppDispatcher.register((action) => {
  switch (action.actionType) {
    case 'CREATE_TODO':
      TodoStore.addTodo(action.todo);
      break;
    /* ... */    
  }
});
```

### store 和 view

store 的变化需要通知 view 让其展示新的数据。通过类似订阅-发布(Pub-Sub)模式，借助 Node.js 标准库的 EventEmitter 在 store 中添加
这一特性。store 的变化适用 `emit` 方法广播出去，view 层在初始化完毕时监听 store 的 `change` 事件。

```javascript
// ./stores
// npm install --save events
import EventEmitter from 'events';
const TodoStore = Object.assign({}, EventEmitter.prototype, {
  /* ... */
  emitChange() {
    this.emit('change');
  },
  addChangeListener(callback) {
    this.on('change', callback);
  },
  removeChangeListener(callback) {
    this.removeListener('change', callback);
  }
});
// 添加广播
AppDispatcher.register((action) => {
  switch (action.actionType) {
    case 'CREATE_TODO':
      TodoStore.addTodo(action.todo);
      TodoStore.emitChange();
      break;
    /* ... */    
  }
});

// ./components
class Todo extends React.Component {
  /* ... */
  componentDidMount() {
    TodoStore.addChangeListener(this.onChange);
  }
  componentWillUnmount() {
    TodoStore.removeChangeListener(this.onChange);
  }
  onChange() {
    this.setState({
      todos: TodoStore.getAll()
    });
  }
  /* ... */
}
```

### 小结

Flux 流程: 用户在 view 上有一个交互时，Dispatcher 广播(`dispatch` 方法)一个 action (`Object` 对象)，在整个程序的总调度台(
Dispatcher)里注册有各种类型的 action，在对应的类型中，store(`Object` 对象，实现了 Pub-Sub 模式)对这个 action 进行相应，对数据
做相应处理，然后触发一个自定义事件，同时，在 view 上注册这个 store 的事件回调，相应事件并重新渲染界面。Flux 并不是简化代码，而是
由它带来清晰的数据流，并把数据和 state 分离。

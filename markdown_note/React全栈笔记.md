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

## Redux

Redux 是 JS 的状态容器，提供可预测的状态管理。Redux 和 React 之间没有特别的关系，任何框架都可以将 Redux 作为状态管理器应用。

Redux 试图让 state 的变化可以预测，这里的 state 指应用运行时需要的各种动态数据，当项目的 state 不可预测时可能产生 model 改变导致
其他 model 或者不相干 view 变化的情况。

### 三大定律

* 单一数据源
  * 应用的 state 存储在一个 JS 对象中(store)
* state 是只读的
  * 改变 state 的唯一方法是触发 action
  * action 是一个信息载体，一个普通的 JS 对象
* 使用纯函数执行修改
  * 使用 reducer 描述 action 怎样改变 state 规定修改规则
  * reducer 是纯函数，接受之前的 state 和 action 返回新的 state
  * reducer 根据应用大小拆分成多个，分别操纵 state 的不同部分

### 组成

1. action

action 作为信息的载体，包含 action 的名称和要传递的信息，可利用 store 的 `dispatch` 方法传递至 store，是 store 的唯一信息来源。

```javascript
const USER_LOGIN = 'USER_LOGIN';
const userLoginAction = {
  type: USER_LOGIN,
  data: {name: 'myname', email: 'mail@mail.cc'}
};
```

action 需要有一个 type 属性值表示该 action 的功能，通常被定义为常量。当项目比较复杂时，可以把 action 的 type 统一到一个模块下。
利用 `action creator` 创建不同的 action (方便异步调用)。此处 `action creator` 是一个返回 action 的方法，而 Flux 中 Action 
Creator 通常会调用 `dispatch`。

```javascript
// Redux
function userLogin(data) {
  return {
    type: UUSER_LOGIN,
    data: data
  }
}
// Flux
function userLogin(data) {
  const action = {
    type: USER_LOGIN,
    data: data
  };
  Appdispatcher.dispatch(action);
}
```

2. reducer

action 定义要执行的操作，reducer 定义 state 如何相应。特别注意: **不能改变 state 值**，因此需要使用 `Object.assign` 或解构返回
一个 state 的备份，且每次都是新的对象。 

```javascript
function user(state = initalUserState, action) {
  switch(action.type) {
    case USER_LOGIN:
      return {
        ...state,
        isLogin: true,
        userDate: action.data
      };
    default:
      return state;
  }
}
```

当应用比较复杂时，考虑拆分多个 reducer，通过 `combineReducers` 合并输出。

```javascript
import { combineReducers } from 'redux';
const rootReducer = combineReducers({posts, user});
// 等价于
function rootReducer(state = initalState, action) {
  return {
    posts: posts(state.posts, action),
    user: user(state.user, action)
  };
}
```

3. store

store 就是 action 和 reducer 的黏合剂，负责:

* 保存整个程序的 state
* 通过 `getState()` 访问 state
* 通过 `dispatch()` 执行 action
* 通过 `subscribe(listener)` 注册回调，监听 state 变化

```javascript
const store = createStore(rootReducer);
console.log(store.getState());
store.subscribe(() => {
  console.log(store.getState());
});
store.dispatch(userLogin({name: 'myname', email: 'mail@mail.cc'}));
```

### 数据流

* 调用 `store.dispatch(action)` 执行一个 action
* store 调用传入的 reducer 函数，将当前的 state 和 action 传入到 reducer 函数中
  * store 由 reducer 生成 `const store = createStore(rootReducer)`
* reducer 处理 action 并返回新的 state
* store 保存 reducer 返回的完整 state
  * 通过 `store.getState()` 获取 state，或 `store.subscribe(listener)` 监听 state 变化

### 使用 middleware

Redux 中的 middleware 在 action 被 dispatch 时触发，提供了调用 reducer 之前的扩展能力。middleware 可以在原有 action 的基础上
创建一个新的 action 和 dispatch (异步 action 处理等)，也可以触发一些额外的行为(日志记录等)。最后通过 `next` 触发后续的 
middleware 与 reducer 本身。**以下 1-5 提供了一些编程思想上的方法，Redux 的实现方式参见 6**。

1. 手动添加 log 信息

```javascript
console.log('dispatching', action);
store.dispatch(action);
console.log('next state', store.getState());
```

2. 覆盖 `store.dispatch` 方法

```javascript
const next = store.dispatch;
store.dispatch = (action) => {
  console.log();
  const result = next(action);
  console.log();
  return result;
}
store.dispatch(action);
```

3. 添加多个 middleware

```javascript
// middleware one
function patchStoreToAddLogging(store) {
  const next = store.dispatch;
  /* 2. 覆盖 store.dispatch 方法中代码 */
}
// middleware two
function patchStoreToAddCrashReporting(store) {
  const next = store.dispatch;
  store.dispatch = function dispatchAndReportErrors(action) {
    try {
      return next(action);
    } catch (e) {
      console.error(e);
      throw e;
    }
  }
}
// 依次调用两个方法覆盖原 store.dispatch 得到增强的 store.dispatch 方法
patchStoreToAddLogging(store);
patchStoreToAddCrashReporting(store);
// 最后调用 store.dispatch 此时已具有两个 middleware 的功能
store.dispatch(action);
```

4. curry 化 middleware

直接覆盖 API 是一种 hack 做法，但通过修改 API 可链式的增强原方法。也可考虑使用 currying 写法，将 `store.dispatch` 的引用作为参数
传递到函数中。curry 化的本质是在调用函数时传入更少的参数，而这个函数会返回另外一个函数并且可以继续接受其他参数。这也更接近官方的写法，
将store、next(store.dispatch 的副本)和 action 依次传入，且在中间件中可以直接使用原 API 方法。

```javascript
function logger(store) {
  return function wrapDispatchToAddLogging(next) {
    return function dispatchAndLog(action) {
      console.log();
      const result = next(action);
      console.log();
      return result;
    }
  }
}
// ES6 写法
const logger = store => next => action => {
  console.log();
  const result = next(action);
  console.log();
  return result;
}
```

5. 简单版本的 applyMiddleware 方法

把 middleware 和 store.dispatch 方法结合，通过 applyMiddleware 返回一个增强型的 store 和 store.dispatch。

```javascript
// Redux 的实现原理
function applyMiddleware(store, middlewares) {
  // 读取 middlewares 数组
  middlewares = middlewares.slice();
  middlewares.reverse();
  // 保存副本
  const dispatch = store.dispatch;
  // 循环 middleware 覆盖 dispatch
  middlewares.forEach(middleware => 
    dispatch = middleware(store)(dispatch)
  );
  // 此时的 dispatch 已经附加了多个 middleware 的功能
  // 返回 store 对象修改过的副本
  return Object.assign({}, store, { dispatch });
}
store = applyMiddleware(store, [logger, crashReporter]);
store.dispatch(action);
```

6. Redux 最终实现

```javascript
applyMiddleware(...mids) : createStore => enhancedCreateStore
```

简单实用 middleware 的方式就是实用 enhancer 来处理原先的 createStore 方法，得到新的 createStore 方法，并用新方法创建 store。

```javascript
const createStoreWithMiddleware = applyMiddleware(logger, crashReporter)(createStore);
const store = createStoreWithMiddleware(rootReducer);
```

从 Redux v3.1.0 开始，createStore 方法已支持直接传入第三方或自己实现的 middleware 作为 enhancer 参数。

```javascript
const store = createStore(
  rootReducer,
  applyMiddleware(logger, crashReporter)
);
```

### 使用 Redux

Redux 着眼于对状态整体维护，React 是一个由状态整体输出界面整体 view 层的实现(~~简直搞基之合~~)。使用 Redux 更多的是如何获取并使
用 store 的内容，以及创建触发 action。

#### 在 React 项目中使用 Redux

1. 从 store 获取数据

通过属性传递将唯一的 store 从 React 根节点传入 `<App state={store.getState()} />`，这一问题在组件层级较多时简直噩梦。

或者，让组件自行获取状态数据。通过 export 将 `createStore` 的方法暴露，让所有组件 import 该 store 然后对其 subscribe。为了将
状态反映到界面，需要将这部分数据放到组件的 state 中，并对 store 进行监听。通过这样，组件私自与 store 建立联系，导致数据流难以追
溯，拥有内部 state 的组件不便于测试，subscribe && setState 逻辑繁琐。

2. 创建与触发 action

使用 `actionCreator` 创建 action，通过 `store.dispatch` 触发。同样存在与 1. 类似的问题。

#### react-redux

react-redux 是以上两个问题的解决方案，API 包括一个 React Component (Provider) 和一个高阶 connect。

1. Provider

Provider 负责提供 store，将其包裹在原根节点之上，使整个组件树上的节点都可以通过 connect 获取 store。

```javascript
ReactDOM.render(
  <Provider store={store}>
    <RootComponent />
  </Provider>,
  rootEl
);
```

2. connect

connect 用来连接 store 与组件，常见用法如下:

```javascript
// ms2p
function mapStateToProps(state) {
  return {num: state.num};
}
// md2p
function mapDispatchToProps(dispatch) {
  return {
    onBtnClick() {
      dispatch(add()); // add: () => action
    }
  };
}
// component
function Counter(props) {
  return (
    <p>
      {props.num}
      <button onClick={props.onBtnClick}>+1</button>
    </p>
  );
}
// HOC
export default connect(mapStateToProps, mapDispatchToProps)(Counter);
```

示例中，通过 connect 让组件 Counter 与 store 连接，从 store 中取得 num 信息并在单击按钮时触发 store 上的 add 方法。

* enhancer
  * connect(ms2p, md2p) 得到一个高阶组件(关于 HOC 的定义详见官方文档)作为 enhancer
  * 通过 enhancer 使 component 可以接触到 store (无须知道其存在) 监听读取状态并触发 action
  * store 通过 Provider 引入，通过 context 实现 store 内容的隐私传递 (实现 `getChildContext` 方法)
  * enhancer 通过组件 context 属性获取 store 对象，从而调用其提供的 API
  * connect 接收以下三个参数并决定 enhancer 的行为
* mapStateToProps
  * `(state) => {}` 此处的 state 即 `store.getState` 的结果
  * mapStateToProps 从全局状态中挑选、计算展示组件所需的数据
  * 该方法在 state 改变时调用并计算结果，结果被作为展示控件属性影响其行为
* mapDispatchToProps
  * `(dispatch) => {}` 此处的 dispatch 正是 store 的 dispatch 方法
  * 上面方法生成数据属性，该方法生成行为属性
* mergeProps
  * ms2p 和 md2p 可接受第二参数 ownProps
  * mergeProps 用于将 stateProps、dispatchProps、ownProps 合并 (通常不需要)

3. 设计考量

回看 react-redux 设计思路，以 Provider 与 connect 各为一端，在 store 和 component 间建立一条纽带。纽带的实现细节被隐藏，只需要
申明式的实现全局数据到具体组件的映射关系；同时，所有对于 store 的读取和作用都被限为有限的形式，避免数据的滥用。这共同构成了 
redux-react 应用低调试难度的基础。(以下两个问题自己考量！)

* Provider 与 connect 是否可合并成一个接口
  * store 需要 `import` 进组件
  * 这里多一个接口，但抽取了使组件树中获取 store 的公共逻辑
* 为什么 connect 方法实现为这种比较复杂，甚至有点难用的形式
  * connect 将组件连接到 store，就是变相调用 store 的 `getState` 和 `dispatch` 能力 并通过 `subscribe` 订阅
  * `store.subscribe` 本身可被抽离，抽离后仅剩 store 更新后从新 state 计算 props 的逻辑
  * 同时依赖 state 的 props 往往是数据属性，而依赖 dispatch 的往往是回调性质的属性
  * React 倾向通过 HOC 而不是继承或 Mixin 实现复用，connet 就是根据配置信息 ms2p 及 md2p 生成高阶组件

#### 组件组织

> 总有一些组件，他们应该从父级通过属性获得部分或全部信息，另外一些组件，他们通过 connect 方法直接获取全局唯一的状态数据。参考 
Redux 作者 Dan Abramov 的文章 《Presentational and Container Components》。

1. 展示组件与容器组件

* 展示组件 Presentational Component
  * 关心应用外观
  * 通常包含属于组件自身的 DOM 节点与样式信息
  * 通常允许通过 `this.props.children` 实现嵌套
  * 对应用的其余部分 (Flux action / store) 没有依赖
  * 不会指定数据如何加载或改变
  * 只通过 props 获取数据与行为 (回调)
  * 极少会包含自身 state，如果有，一定是界面状态而非数据
  * 一般写成 functional component
  * 典型的例子: Page / Sidebar / Story / UserInfo / List
* 容器组件 Container Component
  * 关心应用如何工作
  * 通常不包含 DOM 节点，一定不包含样式信息
  * 为展示组件或其他容器组件提供数据与行为
  * 抵用 Flux action 并将其作为回调函数给展示组件
  * 往往是有状态的，扮演数据源的角色
  * 通常由 HOC 生成
  * 典型例子: UserPage / FollowersSiderbar / StoryContainer / FollowedUserList

通过以上将组件职责明确，展示组件将有更好的复用性，同时通过对展示组件进行组装配合 mock 数据即可得到静态页面。容器组件是通过 
connect 的结果函数处理得到的组件，而展示组件是被作为参数传入或组成其他展示组件的组件。

2. 组织不同类型的组件

> 对于一个中间件，如果某些数据仅仅用来向下传递给它的子节点，则自己并不消费。每次他的子节点所需的数据发生变化，都要相应地修改
他的 props 以适应变化，**那么这些数据往往并不应该由它来提供给它的子节点**。通过对子节点进行 connect 产生一个新的容器组件，
**由它直接从 store 中获取数据并提供给子节点**。

#### 开发工具

Chrome 插件 Redux DevTools，安装后作为 store enhancer 引入:

```javascript
const store = createStore(
  reducer,
  initialState,
  // 做一步存在检查
  window.devToolsExtension && window.devToolsExtension()
);
// compose 其他 middleware
// compose(f, g) 等价 (...args) => f(g(...args))
compose(
  applyMiddleware(mid1, mid2, mid3),
  window.devToolsExtension ? window.devToolsExtension() : f => f
)
```

### 使用 Redux 重构项目

整体步骤:

* 整理 action，实现 action creator
* 设计 store state，实现 reducer
* 划分界面内容，实现展示组件
* 通过容器组件连接 store 与展示组件

#### 创建与触发 action

1. 定义类型 action type  
  -- 一般定义为常量
2. 定义 action 内容的格式  
  -- Redux 仅要求 type 为字符串或 Symbol 并不要求其他信息，常用 Flux Standard Action 约定 {type, payload, error}
3. 定义 action creator  
  -- 用来创建 action，收集简单参数，组装成 action 并对象返回

#### 使用 middleware

这里介绍 redux-thunk 和 redux-promise-middleware 帮助处理异步 action 的创建与触发。

1. redux-thunk

redux-thunk 允许 dispatch 一个函数，如果收到的 action 是一个函数，则将 dispatch 与 getState 作为参数传入，以此有条件的进行 
dispatch。

```javascript
function updateEntryList(items) {
  return { type: TYPE, items };
}
function fetchEntryList() {
  return dispatch => {
    storage.getAll()
      .then(items => dispatch(updateEntryList(items)));
  }
}
```

更常见的两种(异步 action)做法:

* 定义多个 action type ( *\_PENDING / *\_REJECT / *\_RESOLVE )
  * *\_PENDING 时做乐观更新 (optimistic updates)，在 *\_REJECT 时做回滚
* Flux Standard Action
  * 将同一行为的不同状态视为同一 type，通过 error 字段做区分

2. redux-promise-middleware

以 promise 作为 action 的 payload，redux-promise-middleware 会根据 promise 状态触发不同的 action。

```javascript
// initialState 增加 isFetching 标识资源状态
const initialState = {
  isFetching: false,
  data: []
};
// 两个异步方法，在 action type 后接上对应后缀
// 得到该异步 action 所对应的被 redux-promise-middleware 处理后的 action type
const { pendingOf, fullfilledOf } = ActionTypes;
// export reducer
export default function (state = initalState, action) {
  const { type, payload } = action;
  swith(type) {
    // 开始请求 entryList 更新 isFetching
    case pendingOf(ActionTypes.FETCH_ENTRY_LIST):
      return {
        ...state,
        isFetching: true,
      };
    // 完成请求后，更新 data 和 isFetching
    case fullfilledOf(ActionTypes.FETCH_ENTRY_LIST):
      return {
        ...state,
        isFetching: false,
        data: payload,
      };
    default:
      return state;
  }
}
```

#### 实现 reducer

store 由 reducer 创建且不包含业务逻辑，讨论设计 store 其实就是实现 reducer。注意合理的拆分与组装 (`combineReducers`) 即可。

#### 创建与连接 store

> 主要的区别在于，原来的数据从 `this.state` 获得，现在从 `this.props.state` 获得；原先的行为通过调用组件自身的方法、自身的方法
再调用 `setState` 进行状态更新，而现在通过 `this.props.actions` 获取行为对应的方法，直接调用。

```javascript
const App = connect(
  state => ({ state }),
  dispatch => ({
    actions: bindActionCreators(actionCreators, dispatch),
  })
)(MyComponent);
```

这里简单的整个 store state 的内容传递给了组件，并通过 Redux 提供的辅助方法 `bindActionCreators` 将 actionCreators 和 store 
的 dispatch 方法进行绑定，且作为 action 属性传递给组件。`bindActionCreators` 的逻辑就是将 actionCreator 中的每一项 
actionCreator 进行绑定。**原 action creator 基于参数创建 action，绑定后的函数就是基于参数创建并触发 action**，通过这样，在
展示组件中也可以方便的使用。

```javascript
actionCreator => (...args) => dispatch(actionCreator(...args))
```

### 小结

示例代码中的几个 Tips:

```javascript
// 1. create store with middlewares
const store = applyMiddleware(
  thunkMiddleware
)(createStore)(rootReducer);

// 2. create root component based on component Deskmark
const App = connect(
  state => ({ state }),
  dispatch => ({
    actions: bindActionCreators(actionCreators, dispatch),
  })
)(Deskmark);

// 3. file storage based on localStorage
const STORAGE = window.localStorage;
STORAGE.getItem(STORAGE_KEY);
STORAGE.setItem(STORAGE_KEY, JSON.stringify(results));

// 4. update object in a list
items.map(item => item.id === id ? ({...item, content}) : item);
```

## React + Redux 进阶

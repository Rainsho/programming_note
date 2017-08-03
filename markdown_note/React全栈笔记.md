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



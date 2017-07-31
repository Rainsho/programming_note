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

AMD 模块在全局环境下定义 `require` 和 `define`，通过文件路径或模块自己申明进行定位，模块实现中声明的依赖，由加载器操作，提供打包工具自动分析依赖并合并。常见 AMD 模块如下：

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

CommonJS (Node.js 使用此规范) 不适合浏览器环境，经转换后可在浏览器执行，较 AMD 更简洁，典型模块如下：

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

包是用 package.json 描述的文件或文件夹，模块指任何可以被 Node.js 中 `require` 载入的文件。所有的模块都是包，但一些 CLI 包只包括可执行命令行工具。

#### 任务流工具 Task Runner

前端常见工作: `jshint` 检验 JS 文件格式，`uglifyjs` 压缩。Task Runner 避开 shell script，使用 JS 语法实现。

1. Grunt

```bash
npm install -g grunt-cli
```

Grunt 通过插件与其他工具结合，例如 `grunt-contrib-jshint`，通过 Gruntfile.js 配置，通过 `grunt --help` 查看，通过 `grunt` 执行。

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

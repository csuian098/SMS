# springboot32e20828n 本地运行说明

## 1. 日常启动（不改前端源码）

在项目根目录执行：

```bat
mvnw.cmd spring-boot:run
```

打开：

`http://localhost:8080/springboot32e20828n/admin/dist/index.html`

## 2. 修改了前端 Vue 源码后

先重新打包前端：

```bat
cd src\main\resources\admin
npm run build
```

然后回到项目根目录启动后端：

```bat
cd ..\..\..\..
mvnw.cmd spring-boot:run
```

## 3. 一键脚本

- `build-admin.bat`：重新打包前端并同步到后端读取目录
- `start-local.bat`：启动 SpringBoot（8080）

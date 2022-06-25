# Spring Security JSON Login
在整合 `Spring Security` 时, `Spring Security` 默认给我们提供了一个 `FormLogin` 的接口，但是现在大多数的项目都是前后端分离的项目，一般而言，登录的接口和其他接口约定一致，都是使用 `JSON` 来传递参数。例如：

``` json 
{
    "username":"username",
    "password":"password"
}
```


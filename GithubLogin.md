##gitbub授权登录的过程
1.调用 GET https://github.com/login/oauth/authorize
携带下列参数
redirect_uri=http://localhost:8080/callback&scope=user&state=1
redirect_uri返回地址    
scope要获取的信息    
state状态   
2.第一次调用完上面地址后会返回一个code,和state
在一次调用GitHub的api POST https://github.com/login/oauth/access_token 来获取token
我们需要使用其他人的jar包来帮助我们发出post的请求我们使用OkHttp的jar，里面需要将对象转换
为json字符串，我们使用阿里的fastJSON来转换
maven依赖为
```xml
     <dependency>
        <groupId>com.squareup.okhttp3</groupId>
        <artifactId>okhttp</artifactId>
        <version>3.14.1</version>
    </dependency>
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>fastjson</artifactId>
        <version>1.2.30</version>
    </dependency>
```
这时我们就可以发出请求来获取Token，但还有携带参数
client_id:这是github注册apps后生成的
client_secret:这是github注册apps后生成的
code:这就是上面获取的code
redirect_uri:回调的接口
state:上面获取的
3.获取到Token并处理完后调用最后的api
GET https://api.github.com/user
把token拼接在后面，返回来的就是我们需要的user信息



##Spring 拦截器
    基本使用方法:
        
# New Scope is designed for Spring application (Token scope)

## Purpose

Access Token or any token can initilize bean according to token key. 

It means that when you send a request to backend if token exists then spring bean is alive after token is removed from request header (Ex: authorization) It will be removed as well.

Also you can keep track of any information related to token like session scope. In session scope there is JSESSIONID. In Token scope you have Token.

My main purpose was for scheduler jobs that does not have any token. When you send a external request in a scheduled job. You need access token or refresh token to refresh your token. Token Bean can simply solve this problem.

EX: create a bean that keeps your token will is in token scope and it will be automatically refreshed.

## Usage 

1- Example of creation of bean is below.

```
@Component
@Scope(value = "tokenScope", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ExampleTokenScopeService {

    public String sayHello() {
        return "Hello";
    }
}
```

2- Configuration is below

```
@Configuration
@EnableTokenScopeConfig
public class AppConfig {
}
```

3- Example of usage is below

```
@RestController
@RequestMapping(value = "/example")
@RequiredArgsConstructor
public class SampleController {

    private final ExampleTokenScopeService tokenScopeService;

    @GetMapping(value = "/")
    public ResponseEntity<String> echo() {
       return ResponseEntity.ok(Echo.builder().msg(tokenScopeService.sayHello()).build());
    }
}
```

4- Also you need to define your refresh token repository to refresh your access token. If it is empty, it wont refresh it.

```
@Repository
public class TokenRepository implements RefreshTokenRepository {
    @Override
    public String getRefreshToken() {
        return "";
    }
}
```
## Property configuration for server uses servlet.

* token.header=Authorization
* token.prefix=Bearer

These two parameters are configurable.


## Customizable classes


1- Unique key can be set anywhere in your code then it can be retrieved in scheduler job or anywhere in current thread scope.

```
public final class TokenScopeUniqueKeyAcquirer {

    private TokenScopeUniqueKeyAcquirer() {
    }

    private static final ThreadLocal<String> CURRENT_UNIQUE_KEY = new ThreadLocal<>();
    
    public static void put(String uniqueKey) {
        CURRENT_UNIQUE_KEY.set(uniqueKey);
    }
    
    public static String get() {
        return CURRENT_UNIQUE_KEY.get();
    }
}
```

2- You can autowire TokenContextHolder to any class and store your access tokens into it.

As you can see it is a application scope bean so be careful about removal of tokens. When you insert it within your code, you have to manage it to avoid memory leak.
Also I will implement scheduler to remove all expired access tokens and refresh tokens in near future.

```
@Component
@ApplicationScope
public class TokenContextHolder implements TokenContext {

    @Override
    public void putIfAbsent(Token token, String uniqueTokenKey) {
        tokenMap.putIfAbsent(uniqueTokenKey, token);
    }

    @Override
    public void remove(String uniqueKey) {
        tokenMap.remove(uniqueKey);
    }

}

```

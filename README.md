# File-uploader-for-Android
This uploading method can be used when the api  does not support MULTI-PARTING to where the file should be uploaded.
It sends the file in one single stream.

I've used HTTP CLIENT for http calls. Http client is deprecated.But still it can be used by adding the library in your build.gradle file , or you can also use HTTP URL CONNECTION
#### using http client::
Library::: ### org.http.apache.legacy

```java
compile 'org.jbundle.util.osgi.wrapped:org.jbundle.util.osgi.wrapped.org.apache.http.client:4.1.2'
```
add the above library in your gradle dependencies on app level

check this site for more details.(https://hc.apache.org/httpcomponents-client-4.3.x/android-port.html)

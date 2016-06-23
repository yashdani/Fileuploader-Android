# File-uploader-for-Android
This uploading method can be used when the api where the file is being uploaded does not support MULTI-PARTING.
It sends the file in one single stream.

I've used HTTP CLIENT for http calls. Http client is deprecated.But still it can be used by adding the library in your build.gradle file , or you can also use ####http url connection 
#### using http client::
Library::: ### org.http.apache.legacy

```java
compile 'org.jbundle.util.osgi.wrapped:org.jbundle.util.osgi.wrapped.org.apache.http.client:4.1.2'
```
add the above library in your gradle dependencies on app level

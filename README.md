
### Setup
```
cd <future location  of  the  cloned  project>
git clone https://github.com/lekhtuz/url-async-load.git
cd url-async-load
```
### Compile
```
(cd src && javac com/protego/urlasyncload/UrlAsyncLoader.java)
```
### Run
```
(cd src && java com.protego.urlasyncload.UrlAsyncLoader <input file> <optional number of threads>)
```
### Notes
1. Error handling is pretty much non-existent and will most likely result in NPE.
2. In order to avoid external dependencies, I only used classes provided by JRE.
3. Debug printout confirms that the correct number of threads is spawned.
### Future Enhancements
1. Greatly improve error handling. Right now it's pathetic.
2. Expand parameter list, convert it to property file if needed.
3. Allow other digest algorithms besides MD5.
4. Add custom network timeouts.
5. Convert to the Maven project.
6. Convert to the Spring Boot application.
7. Implement different methods of input file delivery, including but not limited to:
   - pipes
   - URI
   - body of the POST method
8. Implement asynchronous delivery of the digest hashes.
9. Package as a docker container.
10. Discover and load external configuration using [consul.io](https://www.consul.io) (or similar) service.
11. Follow [this roadmap](https://www.smart-jokes.org/programmer-evolution.html).

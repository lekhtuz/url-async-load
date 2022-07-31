## Setup
```
cd <location  of  the  cloned  project>
git clone https://github.com/lekhtuz/url-async-load.git
cd url-async-load
```
## Compile
```
(cd src; javac com/protego/urlasyncload/UrlAsyncLoader.java)
```
## Run
```
(cd src; java -cp . com.protego.urlasyncload.UrlAsyncLoader <input file>  <optional number of threads>)
```
## Notes
1. Error handling is pretty much non-existent and will most likely result in NPE.
2. In order to avoid external dependencies, I only used classes provided by JRE.
3. Debug printout confirms that the correct number of threads is spawned.

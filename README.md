Part two of the nanodegree program.

<a href="https://docs.google.com/document/d/11JDnp_WTNGcIm_gs1raroUuDyxo9H_WsQxnpeozMov4/pub?embedded=true">Rubric</a>

Popular Movies Twoexpects the following file:<br />
<i>PopularMoviesTwo\app\src\main\res\values\movieapi.xml</i>

<br />
With the following content:
<pre><code>
<pre style="font-family: Andale Mono, Lucida Console, Monaco, fixed, monospace; color: #000000; background-color: #eee;font-size: 12px;border: 1px dashed #999999;line-height: 14px;padding: 5px; overflow: auto; width: 100%"><code>&lt;?xml version=&quot;1.0&quot; encoding=&quot;utf-8&quot;?&gt;
&lt;resources&gt;
    &lt;item name=&quot;imgurl&quot; type=&quot;string&quot;&gt;http://image.tmdb.org/t/p/%1$s%2$s&lt;/item&gt;
    &lt;item name=&quot;apimainurl&quot; type=&quot;string&quot;&gt;http://api.themoviedb.org/3/&lt;/item&gt;
    &lt;item name=&quot;moviepath&quot; type=&quot;string&quot;&gt;discover/movie&lt;/item&gt;
    &lt;item name=&quot;trailerpath&quot; type=&quot;string&quot;&gt;movie/%s/videos&lt;/item&gt;
    &lt;item name=&quot;reviewspath&quot; type=&quot;string&quot;&gt;movie/%s/reviews&lt;/item&gt;
    &lt;item name=&quot;apikey&quot; type=&quot;string&quot;&gt;your-api-key&lt;/item&gt;
&lt;/resources&gt;
</code></pre>
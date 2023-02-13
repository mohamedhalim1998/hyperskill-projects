<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Code</title>
    <link rel="stylesheet"
           href="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css">
    <script src="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js"></script>
    <script>hljs.initHighlightingOnLoad();</script>
</head>
<body>

    <pre id="code_snippet">
    <#if code.hasViewLimit()>
    <span id="views_restriction"> ${code.viewLimit} more views allowed </span>
       </#if>
       <#if code.timeLimit gt 0>
    <span id="time_restriction"> The code will be available for ${code.remainTime} seconds </span>
    </#if>
    <code>
               ${code.code}
               </code>
                </pre>
                <span id="load_date">
                ${code.formatDateTime()}
                </span>
</body>
</html>
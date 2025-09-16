# Youtrack research

We would like to find customers in YouTrack:

- Projects with a lot of Performance problems
  - There are customers
  - There are project filters for the deep research
- YouTrack users hwo describe Performance problems
  - There are customers
  - There are user filters for the deep research
- Calculate duration metrics for Performance problems
  - From `created` to `resolved`
  - Split the performance issues by the duration:
    - fast performance problems
    - slow with a long duration
- Get the performance problems bottlenecks and problems via comparing fast and slow performance problems
- Chat with the potential customers


## Draft

- The first project is YouTrack
- The first customers are: Smirnov Viacheslav, Kolobov Roman

## TODO

- Get the data from YouTrack and store it in the database.
- Identify the Projects with a lot of Performance problems
- Identify the YouTrack users who describe Performance problems
- Calculate duration metrics for Performance problems
- Chat with the potential customers

## API


https://www.jetbrains.com/help/youtrack/devportal/resource-api-issues.html#get_all-Issue-method

```JS
// GET /api/issues?{fields}&{$top}&{$skip}&{query}&{customFields}

var endpoint = "https://youtrack.jetbrains.com/api/issues";
var userFields = `id,login,fullName,email,ringId`;
var attachments = `attachments(id,name,author(${userFields}),created,updated,size,extension,charset,mimeType,metaData,draft,removed,url,visibility,issue(id),comment(id))`;
var comments = `comments(id,${attachments},created,deleted,pinned,text,updated)`;
var fields = `id,${attachments},${comments},commentsCount,created,description,idReadable,isDraft,links(id,direction,linkType(id,name)),numberInProject,project(id,name,shortName),reporter(${userFields}),resolved,summary,tags(id,name),updated,updater(${userFields}),voters,votes,customFields(name,value(name))`;
var topN = 100;
var skipN = 0;
var query = "Type: {Performance problem} created: {2025-09-01T00:00:00} .. {2025-10-01T00:00:00} ";
var customFields = "customFields=type&customFields=assignee&customFields=priority";
var url = endpoint + "?fields=" + encodeURIComponent(fields) + "&$top=" + topN + "&$skip=" + skipN + "&query=" + encodeURIComponent(query) + "&" + customFields;
fetch(url, {
    "headers": {
        "accept": "application/json",
        "accept-language": "en-US,en;q=0.9,ru;q=0.8",
        "authorization": `Bearer ${token}`,
        "content-type": "application/json",
        "priority": "u=1, i",
        "sec-ch-ua": "\"Chromium\";v=\"140\", \"Not=A?Brand\";v=\"24\", \"Google Chrome\";v=\"140\"",
        "sec-ch-ua-mobile": "?0",
        "sec-ch-ua-platform": "\"macOS\"",
        "sec-fetch-dest": "empty",
        "sec-fetch-mode": "cors",
        "sec-fetch-site": "same-origin",
    },
    "referrer": "https://youtrack.jetbrains.com/issues?q=Type:%20%7BPerformance%20problem%7D%20",
    "body": null,
    "method": "GET",
    "mode": "cors",
    "credentials": "include"
});
```

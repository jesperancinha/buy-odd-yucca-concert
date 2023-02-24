# buy-oyc-api-service

Buy Odd Yucca Concert Gateway API service

---

## Swagger UI

-   [buy-oyc-api-service](http://localhost:8088/swagger/views/swagger-ui/)

## [Sequence diagram](https://mermaid-js.github.io/mermaid)

```mermaid
sequenceDiagram
    participant REST Customer Service CLIENT
    participant API REST Endpoint
    participant Redit Pub Sub
    participant Postgres Database
    participant REST Ticket Client
    participant Internal REST Ticket Service
    
    rect rgb(1,40,25)
    par API Service REST
        REST Customer Service CLIENT->>API REST Endpoint: Request Ticket Reservation (POST)
        API REST Endpoint ->> Postgres Database: Generate Reservation Reference Number
        Postgres Database ->> API REST Endpoint: Returns reference number
        API REST Endpoint  ->> Redit Pub Sub: Set Ticket to be processed in the queue
        API REST Endpoint ->> REST Customer Service CLIENT: Returns ticket number to client
    end
    par API Service Redit Pub Sub
        Redit Pub Sub ->> REST Ticket Client: Receives Ticket 
        REST Ticket Client ->> Internal REST Ticket Service: posts it to internal Ticket service (POST)
    end
    end
```

---

## Micronaut 3.2.0 Documentation

-   [User Guide](https://docs.micronaut.io/3.2.0/guide/index.html)
-   [API Reference](https://docs.micronaut.io/3.2.0/api/index.html)
-   [Configuration Reference](https://docs.micronaut.io/3.2.0/guide/configurationreference.html)
-   [Micronaut Guides](https://guides.micronaut.io/index.html)

---

## Feature http-client documentation

-   [Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#httpClient)

---

## References

-   [Micronaut Tutorial: Reactive](https://piotrminkowski.com/2019/11/12/micronaut-tutorial-reactive/)
-   [Micronaut Redis](https://micronaut-projects.github.io/micronaut-redis/latest/guide/#introduction)
-   [Kotlin Symbol Processing: Early Thoughts](https://www.zacsweers.dev/kotlin-symbol-processor-early-thoughts/#:~:text=Google%20announced%20Kotlin%20Symbol%20Processing,first%20party%20tool%20for%20this.)
-   [What is CoroutineContext and how does it work?](https://kt.academy/article/cc-coroutine-context)
-   [Top 6 Queuing Systems for Backend Developers](https://geekflare.com/queuing-systems-for-backend-developers/)
-   [Getting started with Spring Data Redis with Kotlin](https://www.geekyhacker.com/2019/07/09/getting-started-with-spring-data-redis-with-kotlin/)
-   [Micronaut Tutorial: Beans and Scopes](https://piotrminkowski.com/2019/04/15/micronaut-tutorial-beans-and-scopes/)
---

## About me

[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/JEOrgLogo-20.png "João Esperancinha Homepage")](http://joaofilipesabinoesperancinha.nl)
[![GitHub followers](https://img.shields.io/github/followers/jesperancinha.svg?label=Jesperancinha&style=social "GitHub")](https://github.com/jesperancinha)
[![alt text](https://raw.githubusercontent.com/jesperancinha/project-signer/master/project-signer-templates/icons-20/mastodon-20.png "Mastodon")](https://masto.ai/@jesperancinha)
[![Twitter Follow](https://img.shields.io/twitter/follow/joaofse?label=João%20Esperancinha&style=social "Twitter")](https://twitter.com/joaofse)
| [Sessionize](https://sessionize.com/joao-esperancinha/)
| [Spotify](https://open.spotify.com/user/jlnozkcomrxgsaip7yvffpqqm?si=b54b89eae8894960)
| [Medium](https://medium.com/@jofisaes)
| [YouTube](https://www.youtube.com/@joaoesperancinha/featured)
| [Instagram](https://www.instagram.com/joaofisaes/)
| [Buy me a coffee](https://www.buymeacoffee.com/jesperancinha)
| [Credly Badges](https://www.credly.com/users/joao-esperancinha)
| [Google Apps](https://play.google.com/store/apps/developer?id=Joao+Filipe+Sabino+Esperancinha)
| [Sonatype Search Repos](https://search.maven.org/search?q=org.jesperancinha)
| [Docker Images](https://hub.docker.com/u/jesperancinha)
| [Stack Overflow Profile](https://stackoverflow.com/users/3702839/joao-esperancinha)
| [Reddit](https://www.reddit.com/user/jesperancinha/)
| [Dev.TO](https://dev.to/jofisaes)
| [Hackernoon](https://hackernoon.com/@jesperancinha)
| [Code Project](https://www.codeproject.com/Members/jesperancinha)
| [BitBucket](https://bitbucket.org/jesperancinha)
| [GitLab](https://gitlab.com/jesperancinha)
| [Coursera](https://www.coursera.org/user/da3ff90299fa9297e283ee8e65364ffb)
| [FreeCodeCamp](https://www.freecodecamp.org/jofisaes)
| [HackerRank](https://www.hackerrank.com/jofisaes)
| [LeetCode](https://leetcode.com/jofisaes)
| [Codebyte](https://coderbyte.com/profile/jesperancinha)
| [CodeWars](https://www.codewars.com/users/jesperancinha)
| [Code Pen](https://codepen.io/jesperancinha)
| [Hacker Earth](https://www.hackerearth.com/@jofisaes)
| [Khan Academy](https://www.khanacademy.org/profile/jofisaes)
| [Hacker News](https://news.ycombinator.com/user?id=jesperancinha)
| [InfoQ](https://www.infoq.com/profile/Joao-Esperancinha.2/)
| [LinkedIn](https://www.linkedin.com/in/joaoesperancinha/)
| [Xing](https://www.xing.com/profile/Joao_Esperancinha/cv)
| [Tumblr](https://jofisaes.tumblr.com/)
| [Pinterest](https://nl.pinterest.com/jesperancinha/)
| [Quora](https://nl.quora.com/profile/Jo%C3%A3o-Esperancinha)
| [VMware Spring Professional 2021](https://www.credly.com/badges/762fa7a4-9cf4-417d-bd29-7e072d74cdb7)
| [Oracle Certified Professional, Java SE 11 Programmer](https://www.credly.com/badges/87609d8e-27c5-45c9-9e42-60a5e9283280)
| [Oracle Certified Professional, JEE7 Developer](https://www.credly.com/badges/27a14e06-f591-4105-91ca-8c3215ef39a2)
| [IBM Cybersecurity Analyst Professional](https://www.credly.com/badges/ad1f4abe-3dfa-4a8c-b3c7-bae4669ad8ce)
| [Certified Advanced JavaScript Developer](https://cancanit.com/certified/1462/)
| [Certified Neo4j Professional](https://graphacademy.neo4j.com/certificates/c279afd7c3988bd727f8b3acb44b87f7504f940aac952495ff827dbfcac024fb.pdf)
| [Deep Learning](https://www.credly.com/badges/8d27e38c-869d-4815-8df3-13762c642d64)
| [![Generic badge](https://img.shields.io/static/v1.svg?label=GitHub&message=JEsperancinhaOrg&color=yellow "jesperancinha.org dependencies")](https://github.com/JEsperancinhaOrg)
[![Generic badge](https://img.shields.io/static/v1.svg?label=All%20Badges&message=Badges&color=red "All badges")](https://joaofilipesabinoesperancinha.nl/badges)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Status&message=Project%20Status&color=red "Project statuses")](https://github.com/jesperancinha/project-signer/blob/master/project-signer-quality/Build.md)

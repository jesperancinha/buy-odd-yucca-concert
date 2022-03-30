# Buy Odd Yucca Concert

#### Logbook

---

2022/03/30 - Laying out important foundations
- Hazelcast will be used to cache reservations:
  - Ticket
  - Parking(optional
- Rate limiting calculation
  - Ticket reservation endpoint - On the basis of machine capacity
  - Ticket confirmation endpoint
    - Capacity on the basis of how many seats/places are available
    - Rate limiting should be capacity/second.
  - Internal endpoints
    - Capacity based on possible number of elements (meal and drinks on the basis of a common-sense usage i.e. 2 meals x attendant x day and 2 drinks x meal x attendant x day)

Protection for internal endpoints should be guaranteed in case of entry level security compromise.

---

2022/03/27 - Back from Hiatus
- After what it seemed to be an endless roadmap of maintenance across all my projects, I'm finally able to come back
- Reference added to all data models to formalize the request and return ticket to client.
- To consider why we are using rate limiting:
  - Brute force attacks 
  - (D)DoS Detection
  - Web scraping
  - Further protections:
    - [What kinds of bot attacks are stopped by rate limiting?](https://www.cloudflare.com/en-gb/learning/bots/what-is-rate-limiting/)

---

2021/12/08
- All micronaut services up and running
- Kong with minumal mapping without consumer. Only services and routes

---

#### Planning

1. Rate limiting per country considering total population number. Per Host. Security concern
2. Rate limiting per class. This is fist row, second row, back row, front balconies and back balconies. Per path. Security concern
3. Secure per consumer. Per Consumer Id. Users expected to use the app. Rate limited lower.
4. Special route
5. Talk about Annotation Processor
6. How Kong Works
7. Exemplify with Locust
8. Show example on GUI

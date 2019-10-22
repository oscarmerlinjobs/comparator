# comparator

The goal of this assignment is to show your coding skills and what you value in software
engineering. We value new ideas so next to the original requirement feel free to
improve/add/extend.

We evaluate the assignment depending on your role (Developer/Tester) and your level of
seniority.

## Requirements

- Provide 2 http endpoints that accepts JSON base64 encoded binary data on both
endpoints
    - <host>/v1/diff/<ID>/left and <host>/v1/diff/<ID>/right
- The provided data needs to be diff-ed and the results shall be available on a third end
point
    - <host>/v1/diff/<ID>
- The results shall provide the following info in JSON format
    - If equal return that
    - If not of equal size just return that
    - If of same size provide insight in where the diffs are, actual diffs are not needed.
          - So mainly offsets + length in the data
- Make assumptions in the implementation explicit, choices are good but need to be
communicated

## Deploy

### With docker

`docker build -t waes-comparator .`

and then 

`docker run -p 8080 waes-comparator`

### With maven

This project is also configured for running with maven. Maven wrapper is included for easy deployment:

`./mvnw spring-boot:run`

## API Documentation

### Json Registration

For right:

`POST /v1/diff/{id}/right`

For left:

`POST /v1/diff/{id}/left`

**In both cases:**


| Param |  Type  | Mandatory | 
|-------|--------|-----------|
| id    |  path  | yes       | 



**Body:**

An encoded base64 json. 

#### Response codes

| Code |  Meaning  | 
|-------|--------|
| 201    |  Created  |
| 400    |  Bad request  |
| 404    |  Not found  |

### Comparison between registered values

`GET /v1/diff/{id}`

| Param |  Type  | Mandatory | 
|-------|--------|-----------|
| id    |  path  | yes       | 

#### Response codes

| Code |  Meaning  | 
|-------|--------|
| 200    |  OK  |
| 400    |  Bad request  |
| 404    |  Not found  |

#### Response body specification

If both saved json for right and left are equal:

```
{
     "result": "Equal size and content"
}
```
 
 If both json for indicated id have different length:
 
 ```
 {
      "result": "Not equal size"
 }
  ```
  
 If both json strings have same length but different content:
 
```
{
     "result": "Equal size but different content",
     "diffs": [
         {
             "started_at": 3,
             "ended_at": 6,
             "length": 3
         },
         {
             "started_at": 12,
             "ended_at": 16,
             "length": 4
         }
     ]
 }
 ```
 
###Future Improvements
 
- Continuous integration and continuous delivery.
- Comparison between more than 2 encoded Json.
- Support for more than one encoding way.
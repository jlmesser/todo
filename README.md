# todo
basic spring boot todo app

plan:
- structure
    - standard controller -> service -> repository -> database
    - db data
        - uuid id (created by database)
        - string body / task description
        - boolean complete (default false, can only transition to true?)
    - DTOs
        - same as db for returning single task (get, put, patch, post)
        - list of task for get all tasks
- business logic
    - no more than 10 incomplete tasks
        - return error when you try to add a new task when there are 10 incomplete
        - completed tasks are not deleted by default, totally fine to return more than 10 tasks, constraint is on creation of task which would make current count of incomplete tasks 11
    - p much everything else will be about error handling on invalid requests (e.g. deleting/modifying things that don't exist, missing data in requests to create/update etc)
        - need to handle completed task being set to incomplete resulting in 11 incomplete tasks

task breakdown:

- [x] create repo for personal project
- [x] get basic sb app running
- [x] get postman request 200
- [x] create endpoints and DTOs, hard code responses in correct shape
- [x] service layer (save data in memory)
- [x] set up postgres and flyway
- [x] repo layer
- [ ] handle edge cases and errors
- [ ] test everything + create postman collection
- [ ] swagger, javadocs
- [ ] docker compose
- [ ] final cleanup + readme with prereqs
- [ ] investigate front end stuff
db.users.insertOne(
    { username: "telegraf", password: "$2y$12$KTixTO0jHomQg17Ri1vi5.tTCB6Si11BZpyiwTJnnx521RI.o99N6" }
)
db.acl.insertOne(
    { username: "telegraf", cliendid: "telegraf", pubsub: [ "#" ] }
)
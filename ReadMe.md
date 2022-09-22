CRUD	| Verb	| Path	    | Name	       | Purpose
Create	| POST	| /todo	    | “create” route	| Creates a todo list entry
Read	| GET	| /todo/{id} | “show” route	| Responds with a single todo
Update	| PATCH	| /todo/{id} | “update” route| Updates attributes of the todo
Delete	| DELETE| /todo/{id} | “delete” route	| Deletes the todo
List	       | GET	| /todo	    | “list” route       | Responds with a list of todo
In Your Database:
Todo Entity:
Data Type	Attribute
Long	           id
String 	       description
String             priority (low/medium/high)
LocalDate       due date (yyyy-MM-dd)
Bonus: Add a optional filter to the list route to filter based on priority# springDemoTodo

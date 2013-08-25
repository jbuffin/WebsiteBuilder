##Site schema (postgresql)

```javascript
id: bigserial,
hostName: varchar(255)
```

##Page schema(mongodb)

```javascript
_id: ObjectId()
page: {
	siteId: Number,
	uri: String,
	title: String,
	parent: ID
	rows: [
		{
			order: Number,
			columns: [
				{
					order: Number,
					columnHtml: String
				}
			]
		}
	]
}
```
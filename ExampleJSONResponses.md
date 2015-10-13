## rtm.transactions ##
**rtm.transactions.undo**

```
{"rsp":{"stat":"ok"}}
```

## rtm.lists ##
**rtm.lists.getList**
  * NOTE: `filter` is only returned when the list is a smart list

```
{"rsp":{"stat":"ok","lists":{"list":[{"id":"530602","name":"Inbox","deleted":"0","locked":"1",
"archived":"0","position":"-1","smart":"0","sort_order":"0"},{"id":"835870","name":"School",
"deleted":"0","locked":"0","archived":"0","position":"0","smart":"0","sort_order":"0"},
{"id":"838139","name":"Shopping","deleted":"0","locked":"0","archived":"0","position":"0",
"smart":"0","sort_order":"0"},{"id":"530606","name":"Sent","deleted":"0","locked":"1",
"archived":"0","position":"1","smart":"0","sort_order":"0"}]}}} 
```

**rtm.lists.add**
  * NOTE: `filter` is only returned when the list is a smart list

```
{"rsp":{"stat":"ok","transaction":{"id":"835300997","undoable":"0"},"list":{"id":"2346752",
"name":"Completed List","deleted":"0","locked":"0","archived":"0","position":"0",
"smart":"1","sort_order":"0","filter":"status:completed"}}}
```

**rtm.lists.delete**
  * NOTE: `filter` is only returned when the list is a smart list

```
{"rsp":{"stat":"ok","transaction":{"id":"829976404","undoable":"1"},"list":{"id":"2346361",
"name":"incomplete","deleted":"1","locked":"0","archived":"0","position":"0","smart":"1",
"sort_order":"0","filter":"(status:incomplete)"}}}
```

**rtm.lists.archive**
  * NOTE: `filter` is only returned when the list is a smart list

```
{"rsp":{"stat":"ok","transaction":{"id":"831423147","undoable":"1"},"list":{"id":"2346361",
"name":"incomplete","deleted":"1","locked":"0","archived":"1","position":"0","smart":"1",
"sort_order":"0","filter":"(status:incomplete)"}}}
```

## rtm.tasks ##
**rtm.tasks.add**

```
{"rsp":{"stat":"ok","transaction":{"id":"851965798","undoable":"0"},"list":{"id":"835870",
"taskseries":{"id":"9087179","created":"2008-01-09T13:10:42Z","modified":"2008-01-09T13:10:42Z",
"name":"Jason was here","source":"api","url":"","location_id":"","tags":[],
"participants":[],"notes":[],"task":{"id":"13472979","due":"2008-01-10T05:00:00Z",
"has_due_time":"0","added":"2008-01-09T13:10:42Z","completed":"","deleted":"","priority":"N",
"postponed":"0","estimate":""}}}}}
```

## rtm.tasks.notes ##
**rtm.tasks.notes.add**

```
{"rsp":{"stat":"ok","transaction":{"id":"899890895","undoable":"0"},"note":{"id":"1162593",
"created":"2008-01-09T20:42:55Z","modified":"2008-01-09T20:42:55Z","title":"note title here",
"$t":"a really long note"}}}
```

**rtm.tasks.notes.edit**

```
{"rsp":{"stat":"ok","transaction":{"id":"901169813","undoable":"0"},"note":{"id":"1162644",
"created":"2008-01-09T20:54:52Z","modified":"2008-01-09T20:55:12Z","title":"a new title",
"$t":"a new body text"}}}
```

**rtm.tasks.notes.delete**

```
{"rsp":{"stat":"ok","transaction":{"id":"901380484","undoable":"1"}}}
```

## rtm.locations ##
**rtm.locations.getList**

No Location
```
{"rsp":{"stat":"ok","locations":[]}}
```

One Location
```
{"rsp":{"stat":"ok","locations":{"location":{"id":"132824","name":"Home","longitude":"-76.555352210999",
"latitude":"37.129973890842","zoom":"16","address":"","viewable":"1"}}}}
```

Multiple Locations
```
{"rsp":{"stat":"ok","locations":{"location":[{"id":"132829","name":"Apartment","longitude":"-80.447752475739","latitude":"37.212805881294","zoom":"17","address":"","viewable":"1"},{"id":"132824","name":"Home","longitude":"-76.555352210999","latitude":"37.129973890842","zoom":"16","address":"","viewable":"1"}]}}}
```
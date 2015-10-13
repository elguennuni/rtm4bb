### tags ###
  * If there are no tags associated with a task, an empty JSON array is returned: `"tags":[]`

  * If there is just one tag associtated with a task, a JSON object is returned: `"tags":{"tag":"onetag"}`

  * If there are multiple tags associated with a task, a JSON object is returned with a JSON array inside: `"tags":{"tag":["second","testtag"]}`

### participants ###
  * if there are no contacts associated with a task, an empty JSON array is returned: `"participants":[]`

  * if there is one contact associated with a task, a single JSON object is returned: `"participants":{"contact":{"id":"472772","fullname":"Jason Emerick","username":"bbrtm"}}`

  * if there are more than one contacts associated with a task a JSON object is returned withe a JSON array of contacts: `"participants":{"contact":[{"id":"327108","fullname":"Jorge Tasks","username":"dmd1272"},{"id":"472772","fullname":"BBRTM Emerick","username":"bbrtm"}]}`

### notes ###
  * if there are no notes associated with a task, an empty JSON array is returned: `"notes":[]`

  * if there is one note associated with a task, a single JSON object is returned: `"notes":{"note":{"id":"1153680","created":"2008-01-08T04:31:40Z","modified":"2008-01-08T04:31:48Z","title":"","$t":"just one note"}}`

  * if there are more than one note associated with a task a JSON object is returned withe a JSON array of contacts: `"notes":{"note":[{"id":"1149301","created":"2008-01-07T12:36:35Z","modified":"2008-01-07T12:36:35Z","title":"","$t":"this is another note"},{"id":"1149300","created":"2008-01-07T12:36:34Z","modified":"2008-01-07T12:36:34Z","title":"","$t":"this is a note"}]}`

  * Another note with the JSON response of a note.  The actual content of the note is returned with a key of `$t` which I am not sure is intentional or a bug.

### rtm.locations.getList ###

No Location - empty array
```
{"rsp":{"stat":"ok","locations":[]}}
```

One Location - object
```
{"rsp":{"stat":"ok","locations":{"location":{"id":"132824","name":"Home","longitude":"-76.555352210999",
"latitude":"37.129973890842","zoom":"16","address":"","viewable":"1"}}}}
```

Multiple Locations - object with array
```
{"rsp":{"stat":"ok","locations":{"location":[{"id":"132829","name":"Apartment","longitude":"-80.447752475739","latitude":"37.212805881294","zoom":"17","address":"","viewable":"1"},{"id":"132824","name":"Home","longitude":"-76.555352210999","latitude":"37.129973890842","zoom":"16","address":"","viewable":"1"}]}}}
```
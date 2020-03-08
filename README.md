# Highspot PlayList Ingest

## Overview
`Highspot Play List Ingest` is a project which can ingest a `mixtape.json` with a changes file.
 The changes file contains a list of changes to be applied on `mixtape.json`. 
 And write the result to an output file which has the same structure with `mixtape.json`.
 
## Features
The project support following changes that can be applied to `mixtape.json`
* Add a new play list, the play list should contains at least on song.
* Remove a play list
* Add an existing song to an existing play list.

## Prerequisite
* Java >= 1.8
* Maven = 3.6.3
* macOS

## Getting the Source
 ```
 git clone https://github.com/weiwei03/PlayListIngest.git
 ```
## Building
* go to the project folder and run the following command:
```
mvn clean && mvn package
```

## Usage
### Get Help
* go to the project folder, run the following command:
```
mvn exec:java -Dexec.mainClass="com.highspot.playlist.IngestApp" -Dexec.args="-h"
```

#### Console Output
```
Usage: ingest [-hV] [-c=<changeFile>] [-i=<inputFile>] [-o=<outputFile>]
                 [COMMAND]
   ingest is a commandline tool which is used to ingest a mixtage.json with a
   change file
     -c, --change=<changeFile> File path for the change file
     -h, --help                Show this help message and exit.
     -i, --input=<inputFile>   Input file path for mixtape.json
     -o, --output=<outputFile> Output file path
     -V, --version             Print version information and exit.
   Commands:
     help  Displays help information about the specified command
```

### Ingest File Example
* go to the project folder, run the following command:
```
mvn exec:java -Dexec.mainClass="com.highspot.playlist.IngestApp" -Dexec.args="-i ./src/test/resources/mixtape-data.json -c ./src/test/resources/changes.json -o ./src/test/resources/output.json"
```

## Data structure
### Data Structure For MixTape.json
There is an example of MixTape.json, it's location is src/test/resources/mixtape-data.json.

It mainly contains three section:
1. Users
2. Songs
3. PlayList.

* Data example for User 
```
{
  "id" : "1",
  "name" : "Albin Jaye"
}
```
* Data example for Song
```
{
 "id" : "33",
 "artist": "Drake",
 "title": "Nice For What"
}
```
* Data example for PlayList
```
{
 "id" : "2",
 "user_id" : "3",
 "song_ids" : ["6", "8", "11"]
}
```
 
### Data Structure For Change File.
The changes file is an array of changes. the data structure is very simple.
Here is a brief example of change file
```
[
    {
        "action" : "add_playlist",
        "playlist" : {
            "id" : "4",
            "user_id" : "3",
            "song_ids" : ["6", "7", "13" ]
        }
    },
    {
        "action" : "remove_playlist",
        "playlist_id" : "1"
    },
    {
        "action" : "add_song",
        "song_id" : "8",
        "playlist_id": "3"
    }
]
```
* Add a new play list example
```
{
    "action" : "add_playlist",
    "playlist" : {
        "id" : "4",
        "user_id" : "3",
        "song_ids" : ["6", "7", "13"]
    }
}
```
* Remove a play list example
```
{
    "action" : "remove_playlist",
    "playlist_id" : "1"
}
```
* Add an existing song to an existing play list example
```
{
    "action" : "add_song",
    "song_id" : "8",
    "playlist_id": "3"
}
```


## Large file processing
For the large files files, it has two cases:
* Change file is too large
* Mixtape.json file is too large

I will discuss them separately.

### Changes file is too large
From the requirement, 
we can see for each change, it has no relationship with the other changes.  
So if the changes file is too large, we can has two way to handle it:
1. Do a stream parsing instead of paring the whole file. When parsed on change apply it on 
Mixtape.json right away.
2. Split change file into small piece of changes file. Doing a hash on playlist id and put changes 
into small files based on hash. And one more we can even do a pre calculation to merge the changes 
applied on the same play list into one.

### Mixtape.json file is too large
The data structure for Mixtape.json is very clear, we can think them as a simple key/value 
data structure. And the project is mainly to modify play lists, user and song are just for references.
So when Mixtape.json file grows too large, we can have three ways to solve it:
1. Doing stream parse for Mixtape.json and put the record into a database, the database contains three tables, user,
 song and playlist.Apply the sequenced changes to the database records.
2. Doing stream parse, put user and song record into separate temporary files. And generate indexes for user and song,
we only use userId and songId to validate the data in changes file. So we can create a simple index which just contains
userId and songId. And for play list, if the amount of playlist are too many, we can doing a hash
to split the records into small chunks and save them into small files. For each changes we can do operation on these
small files. And merge all the files into a big output file once all the changes are applied.
3. If count of changes is not very big, we can store all the changes in memory.
 And we can do steaming parsing for Mixtape.json, for each play list
we can find changes related to the play list, apply the changes on the play list and save the play list into output right away. 



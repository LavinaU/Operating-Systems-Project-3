# Development Log

## 2025-12-12 03:10
### Thoughts
Starting Project 3 for Operating Systems Concepts. I know this is late but I had finals all week so I figured I'd take the late points. I worked on it before finals week but not throughly. l I will initialize git today, create this devlog, obviously, and test and commit the skeleton setup. 

### My Plan
- Initialize Git
- Create devlog.md
- Commit initial setup

## 2025-12-12 05:00
### Thoughts
Got caught up with another task but back on. Going to implement the create command and header file handling

### My Plan
- implement create
- initialize index file w/ header

## 2025-12-12 05:30
### Thoughts
Create command implementation is done! In a good workflow now. Going to add the first part of real BTree functionality now. Shouldn't take too long.

### My Plan
- set up BTree node structure & placeholder for insert command

## 2025-12-12 06:15
### Thoughts
I'm pretty happy with the progress so far. For my next session, I am thinking to implement the insert command for one key/value in a new leaf node.

### My Plan
- read the header to get nextBlockID and create a new BTreeNOde
- add the key/value to the node & write it to disk & update the headers' nextBlockId
- confirmation/testing/printing



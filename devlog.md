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

## 2025-12-12 07:00
### Thoughts
Okay nice! I think I could have been faster but I am glad with where I'm at right now. Now I will implement the full insert command & start doing proper work on BTreeNode to implement constructors for a new node etc

### My Plan
- implement the full insert command for the 1st key/value
- BTreeNode work for support for minimal degree 10 w/keys, values & child pointers
- serialzise the node to 512 bytes & write it to disk using BTreeFile
- update headers nextBlockID and rootBlockID if needed
- finally test all this

## 2025-12-12 10:00
### Thoughts
Okay it's looking good! Implemented working insert command, header is now read from disk and updated correctly, basically end to end file I/O and node serialization is done for BTree index. It's a working index file! I want to do leaf node splitting & multiple inserts now, will be a preetty big commit.

### My Plan
- allow multiple keys per leaf & split when the capacity is exceeded
-define node capacity, detect overflow, spilit leaves, update parent/root pointers accordingly

 

